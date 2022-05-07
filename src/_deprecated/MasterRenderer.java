package _deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import display.DisplayManager;
import entities.Camera;
import utils.MathUtils;

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final Vector3f SKY_COLOR = new Vector3f(0.745f, 0.886f, 0.988f);
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private WaterShader waterShader = new WaterShader();
	private WaterRenderer waterRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Water> waters = new ArrayList<Water>();
	
	public MasterRenderer() {
		enableCulling();
		projectionMatrix = MathUtils.createPerspectiveProjectionMatrix(DisplayManager.getWidth(), DisplayManager.getHeight(), FOV, NEAR_PLANE, FAR_PLANE);
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		waterRenderer = new WaterRenderer(waterShader, projectionMatrix);
	}
	
	public static void enableCulling() {
		GL30.glEnable(GL30.GL_CULL_FACE);
		GL30.glCullFace(GL30.GL_BACK);
	}
	
	public static void disableCulling() {
		GL30.glDisable(GL30.GL_CULL_FACE);
	}
	
	public void prepare() {
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		GL30.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1.0f);
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Light sun, Camera camera) {
		prepare();
		
		Matrix4f viewMatrix = camera.getViewMatrix();
		
		shader.start();
		shader.loadSkyColor(SKY_COLOR);
		shader.loadLight(sun);
		shader.loadViewMatrix(viewMatrix);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColor(SKY_COLOR);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(viewMatrix);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		disableCulling();
		waterShader.start();
		//waterShader.loadSkyColor(SKY_COLOR);
		//waterShader.loadLight(sun);
		waterShader.loadViewMatrix(viewMatrix);
		waterRenderer.render(waters);
		waterShader.stop();
		
		entities.clear();
		terrains.clear();
		waters.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processWater(Water water) {
		waters.add(water);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		waterShader.cleanUp();
	}
}
