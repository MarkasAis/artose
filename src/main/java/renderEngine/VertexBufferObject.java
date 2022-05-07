package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class VertexBufferObject {
	private final int id;
	private final int type;
	
	public VertexBufferObject(int type) {
		this.id = GL30.glGenBuffers();
		this.type = type;
	}
	
	public void bind() {
		GL30.glBindBuffer(type, id);
	}
	
	public void unbind() {
		GL30.glBindBuffer(type, 0);
	}
	
	public void storeData(FloatBuffer data) {
		GL30.glBufferData(type, data, GL30.GL_STATIC_DRAW);
	}
	
	public void storeData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		storeData(buffer);
	}
	
	public void storeData(IntBuffer data) {
		GL30.glBufferData(type, data, GL30.GL_STATIC_DRAW);
	}
	
	public void storeData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		storeData(buffer);
	}
	
	public void delete() {
		GL30.glDeleteBuffers(id);
	}
}
