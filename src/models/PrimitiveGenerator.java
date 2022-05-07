package models;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import utils.MathUtils;

public class PrimitiveGenerator {
	
	public static MeshData generateCube(float scale) {
		float[] vertices = new float[] { -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f };
		int[] indices = new int[] { 0, 1, 3, 1, 2, 3, 1, 5, 2, 2, 5, 6, 4, 7, 5, 5, 7, 6, 0, 3, 4, 4, 3, 7, 7, 3, 6, 6, 3, 2, 4, 5, 0, 0, 5, 1 };
		
		scaleVertices(vertices, scale);
		
		return new MeshData(vertices, indices);
	}
	
	public static MeshData generatePlane(float scale) {
		float[] vertices = { -0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f, 0.5f, -0.5f, 0f, -0.5f, 0.5f, 0f };	
		float[] uvs = { 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f };
		float[] normals = { 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f };
		int[] indices = { 0, 1, 2, 1, 0, 3 };
		
		scaleVertices(vertices, scale);
		
		return new MeshData(vertices, uvs, normals, indices);
	}
	
	public static MeshData createTriangleGrid(float scale, int density) {
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float diameter = scale / 2f;
		
		for (int iZ = 0; iZ < density+1; iZ++) {
			float z = MathUtils.lerp(-diameter, diameter, (float) iZ / density);
			
			for (int iX = 0; iX < density+1; iX++) {
				float x = MathUtils.lerp(-diameter, diameter, (float) iX / density);
				
				vertices.add(new Vector3f(x, 0, z));
			}
		}
		
		for (int iZ = 0; iZ < density; iZ++) {
			for (int iX = 0; iX < density; iX++) {
				int bottomLeft = iX + iZ * (density+1);
				int bottomRight = bottomLeft + 1;
				int topLeft = bottomLeft + (density+1);
				int topRight = topLeft + 1;
				
				indices.add(topRight);
				indices.add(bottomRight);
				indices.add(bottomLeft);
				
				indices.add(bottomLeft);
				indices.add(topLeft);
				indices.add(topRight);
			}
		}
		
		return new MeshData(vertices, indices);
	}
	
	private static void scaleVertices(float[] vertices, float scale) {
		for (int i = 0; i < vertices.length; i++)
			vertices[i] *= scale;
	}
}
