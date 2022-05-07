package demo;

import ocean.WaveFrequencies;
import ocean.shaders.OceanShader;
import skybox.Skybox;

public class Scenario {
	private Skybox skybox;
	private WaveFrequencies spectrum;
	private OceanShader shader;
	
	public Scenario(Skybox skybox, WaveFrequencies spectrum, OceanShader shader) {
		this.skybox = skybox;
		this.spectrum = spectrum;
		this.shader = shader;
	}
	
	public Skybox getSkybox() {
		return skybox;
	}
	
	public WaveFrequencies getSpectrum() {
		return spectrum;
	}
	
	public OceanShader getShader() {
		return shader;
	}
}
