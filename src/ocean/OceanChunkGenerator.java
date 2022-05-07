package ocean;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Entity;
import models.Mesh;
import models.MeshData;
import models.Model;
import models.PrimitiveGenerator;

public class OceanChunkGenerator {
	private static final Vector2f[] CENTER_CHUNK_OFFSETS = {
		new Vector2f(-1.5f,  1.5f), new Vector2f(-0.5f,  1.5f), new Vector2f(0.5f,  1.5f), new Vector2f(1.5f,  1.5f),
        new Vector2f(-1.5f,  0.5f), new Vector2f(-0.5f,  0.5f), new Vector2f(0.5f,  0.5f), new Vector2f(1.5f,  0.5f),
        new Vector2f(-1.5f, -0.5f), new Vector2f(-0.5f, -0.5f), new Vector2f(0.5f, -0.5f), new Vector2f(1.5f, -0.5f),
        new Vector2f(-1.5f, -1.5f), new Vector2f(-0.5f, -1.5f), new Vector2f(0.5f, -1.5f), new Vector2f(1.5f, -1.5f)
	};
	
	private static final Vector2f[] SURROUNDING_CHUNK_OFFSETS = {
		new Vector2f(-1.5f,  1.5f), new Vector2f(-0.5f,  1.5f), new Vector2f(0.5f,  1.5f), new Vector2f(1.5f,  1.5f),
        new Vector2f(-1.5f,  0.5f),                                                        new Vector2f(1.5f,  0.5f),
        new Vector2f(-1.5f, -0.5f),                                                        new Vector2f(1.5f, -0.5f),
        new Vector2f(-1.5f, -1.5f), new Vector2f(-0.5f, -1.5f), new Vector2f(0.5f, -1.5f), new Vector2f(1.5f, -1.5f)
	};
	
	private Ocean ocean;
	
	private Model[] chunkModels;
	
	public OceanChunkGenerator(Ocean ocean) {
		this.ocean = ocean;
	}
	
	private void createLOD(int lodIndex) {
		float scale = (float) Math.pow(2, lodIndex);
		
		Vector2f[] offsets = lodIndex == 0 ? CENTER_CHUNK_OFFSETS : SURROUNDING_CHUNK_OFFSETS;
		
		Entity[][] chunks = ocean.getChunks();
		Entity parent = ocean.getParent();
		
		chunks[lodIndex] = new Entity[offsets.length];
		
		for (int i = 0; i < offsets.length; i++) {
			Vector3f pos = new Vector3f(offsets[i].x, 0, offsets[i].y).mul(scale);
			
			Entity chunk = new Entity(chunkModels[lodIndex], pos, new Quaternionf(0, 0, 0, 1), new Vector3f(scale, 1, scale));
			chunk.setParent(parent);
			
			chunks[lodIndex][i] = chunk;
		}
	}
	
	public void generate() {
		cleanUp();
		
		chunkModels = new Model[Ocean.LOD_COUNT];
		ocean.setChunks(new Entity[Ocean.LOD_COUNT][]);
		ocean.setParent(new Entity());
		
		int density = Ocean.MESH_DENSITY;
		float scale = 1f + 2f / density;
		
		for (int i = 0; i < Ocean.LOD_COUNT; i++) {
			MeshData meshData = PrimitiveGenerator.createTriangleGrid(scale, density+2);
			Mesh mesh = Mesh.load(meshData);
			chunkModels[i] = new Model(mesh, null);
		}
		
		for (int i = 0; i < Ocean.LOD_COUNT; i++) {
			createLOD(i);
		}
	}
	
	public void cleanUp() {
		if (chunkModels != null)
			for (Model model : chunkModels)
				model.getMesh().delete();
	}
}
