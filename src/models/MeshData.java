package models;

import java.util.List;

import org.joml.Vector3f;

public class MeshData {
	private float[] vertices;
	private float[] uvs;
	private float[] normals;
	private int[] indices;
	
	private int activeAttributeCount = 0;
	
	public MeshData(float[] vertices, float[] uvs, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.uvs = uvs;
		this.normals = normals;
		this.indices = indices;
		
		activeAttributeCount = 3;
	}
	
	public MeshData(float[] vertices, float[] uvs, int[] indices) {
		this.vertices = vertices;
		this.uvs = uvs;
		this.indices = indices;
		
		activeAttributeCount = 2;
	}
	
	public MeshData(float[] vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
		
		activeAttributeCount = 1;
	}
	
	public MeshData(List<Vector3f> vertices, List<Integer> indices) {
		this(Vector3fListToFloatArray(vertices), IntListToIntArray(indices));
	}

	public static int[] IntListToIntArray(List<Integer> list) {
		int[] array = new int[list.size()];
		
		for (int i = 0; i < list.size(); i++)
			array[i] = list.get(i);
		
		return array;
	}
	
	public static float[] Vector3fListToFloatArray(List<Vector3f> list) {
		float[] array = new float[list.size() * 3];
		
		for (int i = 0; i < list.size(); i++)
			for (int j = 0; j < 3; j++)
				array[i*3 + j] = list.get(i).get(j);
		
		return array;
	}
	
	public MeshData(float[] vertices) {
		this.vertices = vertices;
		
		activeAttributeCount = 1;
	}
	
	public int getVertexCount() {
		return vertices.length/3;
	}
	
	public float[] getVertices() {
		return vertices;
	}

	public float[] getUvs() {
		return uvs;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}
	
	public float[][] getAttributes() {
		float[][] attributes = new float[activeAttributeCount][];
		
		int curIndex = 0;
		if (vertices != null) attributes[curIndex++] = vertices;
		if (uvs != null) attributes[curIndex++] = uvs;
		if (normals != null) attributes[curIndex++] = normals;
		
		return attributes;
	}
}
