package models;

import renderEngine.VertexArrayObject;

public class Mesh {
	private final VertexArrayObject vao;
	
	public Mesh(VertexArrayObject vao) {
		this.vao = vao;
	}
	
	public VertexArrayObject getVAO() {
		return vao;
	}
	
	public void delete() {
		vao.delete();
	}
	
	public static Mesh load(MeshData data) {
		VertexArrayObject vao = new VertexArrayObject();
		int[] indices = data.getIndices();
		
		if (indices != null)
			vao.storeData(data.getVertexCount(), indices, data.getAttributes());
		else
			vao.storeData(data.getVertexCount(), data.getAttributes());
		
		return new Mesh(vao);
	}
}
