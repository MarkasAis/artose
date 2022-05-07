package renderEngine;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import utils.FileUtils;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer arrayBuffer;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL30.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL30.GL_FRAGMENT_SHADER);
		
		programID = GL30.glCreateProgram();
		GL30.glAttachShader(programID, vertexShaderID);
		GL30.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GL30.glLinkProgram(programID);
		GL30.glValidateProgram(programID);
		
		GL30.glDetachShader(programID, vertexShaderID);
		GL30.glDetachShader(programID, fragmentShaderID);
		GL30.glDeleteShader(vertexShaderID);
		GL30.glDeleteShader(fragmentShaderID);
		
		getUniformLocations();
	}
	
	protected abstract void getUniformLocations();
	
	protected int getUniformLocation(String uniformName) {
		return GL30.glGetUniformLocation(programID, uniformName);
	}
	
	public void start() {
		GL30.glUseProgram(programID);
	}
	
	public void stop() {
		GL30.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL30.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		GL30.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadInt(int location, int value) {
		GL30.glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value) {
		GL30.glUniform1f(location, value);
	}
	
	protected void loadFloatArray(int location, float[] values) {
		GL30.glUniform1fv(location, values);
	}
	
	protected void loadVec3f(int location, Vector3f vector) {
		GL30.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVec4fArray(int location, Vector4f[] vectorArray) {
		int totalElements = vectorArray.length * 4;
		
		if (arrayBuffer == null || arrayBuffer.capacity() != totalElements)
			arrayBuffer = BufferUtils.createFloatBuffer(totalElements);
		else
			arrayBuffer.clear();
		
		// TODO: use buffer utils instead
		
		for (Vector4f vec : vectorArray)
			for(int i = 0; i < 4; i++)
				arrayBuffer.put(vec.get(i));
		
		arrayBuffer.flip();
		GL30.glUniform4fv(location, arrayBuffer);
	}
	
	protected void loadVec4fArray(int location, float[] array) {
		GL30.glUniform4fv(location, array);
	}
	
	protected void loadBoolean(int location, boolean value) {
		GL30.glUniform1i(location, value ? 1 : 0);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(matrixBuffer);
		GL30.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	private static int loadShader(String file, int type){
        String shaderSource = FileUtils.readFile(file);
        
        int shaderID = GL30.glCreateShader(type);
        GL30.glShaderSource(shaderID, shaderSource);
        GL30.glCompileShader(shaderID);
        
        if(GL30.glGetShaderi(shaderID, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            System.out.println(GL30.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1); // TODO: whole engine doesn't have to exit, maybe use a default shader
        }
        
        return shaderID;
    }
}
