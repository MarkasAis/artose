package _deprecated;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader {

	public static RawModel loadObjModel(String file, Loader loader) {
		FileReader fr = null;
		
		try {
			fr = new FileReader(new File("res/" + file + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray;
		float[] texCoordsArray;
		float[] normalsArray;
		int[] indicesArray;
		
		try {
			String line;
			String[] lineSplit;
			
			loop: while (true) {
				line = reader.readLine();
				lineSplit = line.split(" ");
				
				switch(lineSplit[0]) {
				case "v":
					Vector3f vertex = new Vector3f(
						Float.parseFloat(lineSplit[1]),
						Float.parseFloat(lineSplit[2]),
						Float.parseFloat(lineSplit[3]));
					
					vertices.add(vertex);
					break;
					
				case "vt":
					Vector2f texCoord = new Vector2f(
						Float.parseFloat(lineSplit[1]),
						Float.parseFloat(lineSplit[2]));
					
					texCoords.add(texCoord);
					break;
					
				case "vn":
					Vector3f normal = new Vector3f(
						Float.parseFloat(lineSplit[1]),
						Float.parseFloat(lineSplit[2]),
						Float.parseFloat(lineSplit[3]));
					
					normals.add(normal);
					break;
					
				case "f":
					texCoordsArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break loop;
				}
			}
			
			while(line != null) {
				lineSplit = line.split(" ");
			
				if (!lineSplit[0].equals("f")) {
					line = reader.readLine();
					continue;
				}
				
				String[] v1 = lineSplit[1].split("/");
				String[] v2 = lineSplit[2].split("/");
				String[] v3 = lineSplit[3].split("/");
				
				processVertex(v1, indices, texCoords, normals, texCoordsArray, normalsArray);
				processVertex(v2, indices, texCoords, normals, texCoordsArray, normalsArray);
				processVertex(v3, indices, texCoords, normals, texCoordsArray, normalsArray);
				
				line = reader.readLine();
			}
			
			reader.close();
			
			verticesArray = new float[vertices.size()*3];
			indicesArray = new int[indices.size()];
			
			int vertexPointer = 0;
			for (Vector3f vertex : vertices) {
				verticesArray[vertexPointer++] = vertex.x;
				verticesArray[vertexPointer++] = vertex.y;
				verticesArray[vertexPointer++] = vertex.z;
			}
			
			for (int i = 0; i < indices.size(); i++) {
				indicesArray[i] = indices.get(i);
			}
			
			return loader.loadToVAO(verticesArray, texCoordsArray, normalsArray, indicesArray);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void processVertex(
		String[] vertexData, List<Integer> indices,
		List<Vector2f> texCoords, List<Vector3f> normals,
		float[] texCoordsArray, float[] normalsArray) {
		
		int curVertexIndex = Integer.parseInt(vertexData[0]) - 1;
		indices.add(curVertexIndex);
		
		Vector2f curTexCoord = texCoords.get(Integer.parseInt(vertexData[1]) - 1);
		texCoordsArray[curVertexIndex*2] = curTexCoord.x;
		texCoordsArray[curVertexIndex*2 + 1] = 1 - curTexCoord.y;
		
		Vector3f curNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[curVertexIndex*3] = curNormal.x;
		normalsArray[curVertexIndex*3 + 1] = curNormal.y;
		normalsArray[curVertexIndex*3 + 2] = curNormal.z;
		
	}
}
