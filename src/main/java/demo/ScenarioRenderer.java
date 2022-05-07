package demo;

import org.lwjgl.opengl.GL30;

import entities.Camera;
import ocean.Ocean;
import ocean.OceanRenderer;
import skybox.SkyboxRenderer;

public class ScenarioRenderer {
	
	private OceanRenderer oceanRenderer = new OceanRenderer();
	private SkyboxRenderer skyboxRenderer = new SkyboxRenderer();
	
	public void render(Scenario scenario, Ocean ocean, Camera camera) {
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		GL30.glClearColor(0f, 0f, 0f, 1f);
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		
		oceanRenderer.render(ocean, scenario.getSkybox().getTexture(), camera);
		skyboxRenderer.render(scenario.getSkybox(), camera);
	}
}
