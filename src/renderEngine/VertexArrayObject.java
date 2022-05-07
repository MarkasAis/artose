package renderEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

public class VertexArrayObject {
	private final int id;
	
	private List<VertexBufferObject> vbos = new ArrayList<VertexBufferObject>();
	private VertexBufferObject indexVBO;
	
	private int elementCount;
	
	public VertexArrayObject() {
		id = GL30.glGenVertexArrays();
	}
	
	public int getElementCount() {
		return elementCount;
	}
	
	public void bind(int...attributes) {
		GL30.glBindVertexArray(id);
		
		for (int a : attributes)
			GL30.glEnableVertexAttribArray(a);
	}
	
	public void unbind(int... attributes) {
		GL30.glBindVertexArray(0);
		
		for (int a : attributes)
			GL30.glDisableVertexAttribArray(a);
	}
	
	public void delete() {
		for (VertexBufferObject vbo : vbos)
			vbo.delete();
		
		indexVBO.delete();
		
		GL30.glDeleteVertexArrays(id);
	}
	
	public void storeData(int vertexCount, int[] indices, float[]... data) {
		bind();
		storeVertexData(vertexCount, data);
		storeIndices(indices);
		unbind();
		
		elementCount = indices.length;
	}
	
	public void storeData(int vertexCount, float[]... data) {
		bind();
		storeVertexData(vertexCount, data);
		unbind();
		
		elementCount = vertexCount;
	}
	
	private void storeVertexData(int vertexCount, float[]... data) {
		for (int i = 0; i < data.length; i++) {
			storeDataInAttributeList(i, data[i].length / vertexCount, data[i]);
		}
	}
	
	private void storeIndices(int[] indices) {
		indexVBO = new VertexBufferObject(GL30.GL_ELEMENT_ARRAY_BUFFER);
		indexVBO.bind();
		indexVBO.storeData(indices);
		elementCount = indices.length;
	}
	
	private void storeDataInAttributeList(int attributeId, int attributeSize, float[] data) {
		VertexBufferObject vbo = new VertexBufferObject(GL30.GL_ARRAY_BUFFER);
		vbos.add(vbo);
		vbo.bind();
		
		vbo.storeData(data);
		GL30.glVertexAttribPointer(attributeId, attributeSize, GL30.GL_FLOAT, false, 0, 0);
		
		vbo.unbind();
	}
}
