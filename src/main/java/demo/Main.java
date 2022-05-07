package demo;

import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL30;

import compute.ComputeManager;
import display.DisplayManager;
import input.Input;
import ocean.shaders.OceanShader;
import ocean.WaveFrequencies;
import skybox.Skybox;
import utils.MathUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	private static Demo demo;
	
	public static void main(String[] args) {
		System.out.println("LWJGL Version " + Version.getVersion() + "!");
		long window = DisplayManager.createDisplay();
		ComputeManager.createContext(window);
		Input.initListener();
		System.out.println(GL30.glGetString(GL30.GL_VERSION));


//		FileWriter writer = null;
//		try {
//			writer = new FileWriter(new File("./src/main/resources/skyboepic_pink/test.txt"));
//			writer.write("Test");
//			writer.close();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}



		Skybox[] skyboxes = new Skybox[] {
			Skybox.load("skyboxes/epic_pink", new Vector3f(0.706f, 0.639f, 0.722f), MathUtils.rotationToDirection(new Vector3f(-0.05f, 0f, 0f))),
			Skybox.load("skyboxes/moon_burst", new Vector3f(0.098f, 0.121f, 0.158f), MathUtils.rotationToDirection(new Vector3f(-0.1f, 0f, 0f))),
			Skybox.load("skyboxes/epic_sunset", new Vector3f(0.231f, 0.349f, 0.541f), MathUtils.rotationToDirection(new Vector3f(-0.05f, 0f, 0f))),
			Skybox.load("skyboxes/sunset", new Vector3f(0.29f, 0.161f, 0.161f), MathUtils.rotationToDirection(new Vector3f(-0.5f, -0.1f, 0f))),
			Skybox.load("skyboxes/overcast2", new Vector3f(0.95f, 0.95f, 0.95f), MathUtils.rotationToDirection(new Vector3f(-1.57f, 0f, 0f))),
			Skybox.load("skyboxes/space", new Vector3f(0.098f, 0.121f, 0.158f), MathUtils.rotationToDirection(new Vector3f(-0.05f, 0f, 0f)))
		};
		
		WaveFrequencies[] frequencies = new WaveFrequencies[] {
			new WaveFrequencies(new float[] { 0f, 0.051f, 0.122f, 0.201f, 0.245f, 0.324f, 0.417f, 0.519f, 0.619f, 0.647f, 0.928f, 0.993f, 1f, 1f }, 1f, 0.1f),
			new WaveFrequencies(new float[] { 0.1f, 0.04f, 0.122f, 0.201f, 0.245f, 0.302f, 0.417f, 0.5f, 0.62f, 0.65f, 0.95f, 1f, 1f, 1f }, 1f, 0.5f),
			new WaveFrequencies(new float[] { 0f, 0.041f, 0.122f, 0.201f, 0.235f, 0.312f, 0.4f, 0.519f, 0.619f, 0.65f, 0.928f, 0.993f, 1f, 1f }, 1f, 1f)
		};
		
		OceanShader[] shaders = new OceanShader[] {
			new OceanShader(), new OceanShader(), new OceanShader(), new OceanShader(), new OceanShader(), new OceanShader()
		};
		
		shaders[0].start();
		shaders[0].loadDiffuse(new Vector3f(0.094f, 0.11f, 0.145f), new Vector3f(0.2f, 0.051f, 0.051f));
		shaders[0].loadSubsurface(new Vector3f(0.102f, 0.573f, 0.525f), 0.5f, 2.5f, 19.7f);
		shaders[0].loadSpecularity(0.75f, 1f);
		shaders[0].loadFog(skyboxes[0].getSkyColor(), 0.004f, 1.5f);
		shaders[0].loadLightDirection(skyboxes[0].getLightDirection());
		shaders[0].stop();
		
		shaders[1].start();
		shaders[1].loadDiffuse(new Vector3f(0.05f, 0.05f, 0.145f), new Vector3f(0.15f, 0.031f, 0.031f));
		shaders[1].loadSubsurface(new Vector3f(0f/255, 14f/255, 34f/255), 0.24f, 1.6f, 7.11f);
		shaders[1].loadSpecularity(1f, 5f);
		shaders[1].loadCustomReflections(new Vector3f(28f/255, 30f/255, 40f/255), new Vector3f(0.8f, 0.8f, 0.8f), new Vector3f(33f/255, 43f/255, 45f/255), 0.01f);
		shaders[1].loadFog(skyboxes[1].getSkyColor(), 0.01f, 1.5f);
		shaders[1].loadLightDirection(skyboxes[1].getLightDirection());
		shaders[1].stop();
		
		shaders[2].start();
		shaders[2].loadDiffuse(new Vector3f(0.094f, 0.11f, 0.145f), new Vector3f(0.2f, 0.051f, 0.051f));
		shaders[2].loadSubsurface(new Vector3f(0.102f, 0.573f, 0.525f), 0.5f, 1.5f, 19.7f);
		shaders[2].loadSpecularity(0.75f, 1f);
		shaders[2].loadFog(skyboxes[0].getSkyColor(), 0.002f, 1.5f);
		shaders[2].loadLightDirection(skyboxes[2].getLightDirection());
		shaders[2].stop();
		
		shaders[3].start();
		shaders[3].loadDiffuse(new Vector3f(0.05f, 0.05f, 0.145f), new Vector3f(0.15f, 0.031f, 0.031f));
		shaders[3].loadSubsurface(new Vector3f(200f/255, 80f/255, 70f/255), 0f, 0.75f, 19.7f);
		shaders[3].loadSpecularity(1f, 3.5f);
		shaders[3].loadCustomReflections(new Vector3f(192f/255, 99f/255, 110f/255), new Vector3f(255f/255, 255f/255, 145f/255), new Vector3f(37f/255, 44f/255, 71f/255), 0.01f);
		shaders[3].loadFog(skyboxes[3].getSkyColor(), 0.01f, 1.5f);
		shaders[3].loadLightDirection(skyboxes[3].getLightDirection());
		shaders[3].stop();
		
		shaders[4].start();
		shaders[4].loadDiffuse(new Vector3f(0.9f, 0.9f, 0.9f), new Vector3f(0.7f, 0.7f, 0.7f));
		shaders[4].loadSubsurface(new Vector3f(0.8f, 0.8f, 0.8f), 0.5f, 1.5f, 7f);
		shaders[4].loadSpecularity(0.75f, 1f);
		shaders[4].loadFog(skyboxes[4].getSkyColor(), 0.01f, 1.5f);
		shaders[4].loadLightDirection(skyboxes[4].getLightDirection());
		shaders[4].stop();
		
		shaders[5].start();
		shaders[5].loadDiffuse(new Vector3f(0.05f, 0.05f, 0.145f), new Vector3f(0.15f, 0.031f, 0.031f));
		shaders[5].loadSubsurface(new Vector3f(0f/255, 14f/255, 34f/255), 0.5f, 12.5f, 3.7f);
		shaders[5].loadSpecularity(1f, 5f);
		shaders[5].loadFog(skyboxes[5].getSkyColor(), 0f, 1.5f);
		shaders[5].loadLightDirection(skyboxes[5].getLightDirection());
		shaders[5].stop();
		
		Scenario[] scenarios = new Scenario[] {
			new Scenario(skyboxes[0], frequencies[1], shaders[0]),
			new Scenario(skyboxes[2], frequencies[2], shaders[2]),
			new Scenario(skyboxes[3], frequencies[1], shaders[3]),
			new Scenario(skyboxes[1], frequencies[1], shaders[1]),
			new Scenario(skyboxes[5], frequencies[0], shaders[5]),
			new Scenario(skyboxes[4], frequencies[2], shaders[4])
		};
		
		demo = new Demo(scenarios);
		
		while(!DisplayManager.isCloseRequested()) {
			Input.update();
			demo.update();
			demo.render();
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
		ComputeManager.cleanUp();
	}
}