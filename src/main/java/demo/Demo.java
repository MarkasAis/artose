package demo;

import java.util.Arrays;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import entities.TurningCamera;
import input.Input;
import ocean.Ocean;

public class Demo {
	private Scenario[] scenarios;
	private Scenario currentScenario;
	
	private Ocean ocean;
	private Camera camera;
	private ScenarioRenderer renderer = new ScenarioRenderer();
	
	public Demo(Scenario[] scenarios) {
		if (scenarios.length == 0) {
			System.err.println("At least one scenario must be passed.");
			return;
		} else if (scenarios.length > 10) {
			System.out.println("More than 10 scenarios provided, some scenarios got ignored.");
			scenarios = Arrays.copyOfRange(scenarios, 0, 10);
		}
		
		this.scenarios = scenarios;
		currentScenario = scenarios[0];
		
		ocean = new Ocean(currentScenario.getSpectrum(), currentScenario.getShader());
		camera = new TurningCamera(new Vector3f(0, 10, 0), new Vector3f(0, 0, 0), 70, 0.1f, 10000);
	}
	
	public void update() {
		handleSwitching();
		
		ocean.update();
		camera.update();
	}
	
	public void render() {
		long start = System.nanoTime();
		
//		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
		renderer.render(currentScenario, ocean, camera);
//		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
		
		long delta = System.nanoTime() - start;
		System.out.println(delta/1000);
	}
	
	private void loadScenario(Scenario scenario) {
		ocean.setSpectrum(scenario.getSpectrum());
		ocean.setShader(scenario.getShader());
		
		currentScenario = scenario;
	}
	
	private void loadScenario(int index) {
		if (index < 0 || index >= scenarios.length) return;
		
		loadScenario(scenarios[index]);
	}
	
	private void handleSwitching() {
		for (int i = 0; i < scenarios.length; i++) {
			int key = GLFW.GLFW_KEY_0 + (i+1) % 10;
			
			if (Input.getKeyDownThisFrame(key)) {
				loadScenario(i);
				return;
			}
		}
	}
	
	public void cleanUp() {
		ocean.cleanUp();
	}
}
