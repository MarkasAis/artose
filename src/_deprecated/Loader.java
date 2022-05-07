package _deprecated;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import textures.TextureData;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		unbindVAO();
		
		return new RawModel(vaoID, positions.length);
	}
	
	/*public TextureData old_readTexture(String file) {
		MemoryStack stack = stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		
		ByteBuffer image = STBImage.stbi_load("res/" + file + ".png", w, h, comp, 4);
		if (image == null) {
		    throw new RuntimeException("Failed to load a texture file!"
		            + System.lineSeparator() + STBImage.stbi_failure_reason());
		}

		int width = w.get();
		int height = h.get();
		
		return new TextureData(GL30.GL_UNSIGNED_INT, image, width, height);
	}
	
	public int loadCubemap(String[] files) {
		if (files.length != 6) {
			System.err.println("Failed to load cubemap texture, 6 filenames must be provided.");
			return -1;
		}
		
		int textureID = GL30.glGenTextures();
		textures.add(textureID);
		GL30.glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, textureID);
		
		for (int i = 0; i < 6; i++) {
			TextureData data = old_readTexture(files[i]);
			GL30.glTexImage2D(GL30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL30.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
		
		GL30.glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, 0);
		return textureID;
	}
	
	public int loadTexture(String file) {
		int textureID = GL30.glGenTextures();
		textures.add(textureID);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureID);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_LOD_BIAS, -0.4f);
		
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
		
		TextureData data = old_readTexture(file);
		
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, data.getWidth(), data.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, data.getBuffer());
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
		return textureID;
	}*/
	
	public void cleanUp() {
		for (int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		
		for (int vbo : vbos)
			GL30.glDeleteBuffers(vbo);
		
		for (int texture : textures)
			GL30.glDeleteTextures(texture);
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeID, int attributeSize, float[] data) {
		int vboID = GL30.glGenBuffers();
		vbos.add(vboID);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(attributeID, attributeSize, GL30.GL_FLOAT, false, 0, 0);
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL30.glGenBuffers();
		vbos.add(vboID);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
}
