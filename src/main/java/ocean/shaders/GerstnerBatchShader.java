package ocean.shaders;

import renderEngine.ShaderProgram;

public class GerstnerBatchShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/gerstnerBatchVertex.txt";
	private static final String FRAGMENT_FILE = "shaders/gerstnerBatchFragment.txt";
	
	private int location_twoPiOverWavelengths;
	private int location_amplitudes;
	private int location_waveDirectionsX;
	private int location_waveDirectionsZ;
	private int location_phases;
	private int location_chopAmplitudes;
	
	private int location_textureResolution;
	private int location_texelSizes;
	private int location_lodIndex;
	
	public GerstnerBatchShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_positionOS");
		super.bindAttribute(1, "in_uv");
	}

	@Override
	protected void getUniformLocations() {
		location_twoPiOverWavelengths = super.getUniformLocation("u_twoPiOverWavelengths");
		location_amplitudes = super.getUniformLocation("u_amplitudes");
		location_waveDirectionsX = super.getUniformLocation("u_waveDirX");
		location_waveDirectionsZ = super.getUniformLocation("u_waveDirZ");
		location_phases = super.getUniformLocation("u_phases");
		location_chopAmplitudes = super.getUniformLocation("u_chopAmplitudes");
		
		location_textureResolution = super.getUniformLocation("u_textureResolution");
		location_texelSizes = super.getUniformLocation("u_texelSizes");
		location_lodIndex = super.getUniformLocation("u_lodIndex");
	}
	
	public void loadTwoPiOverWavelengths(float[] twoPiOverWavelengths) {
		super.loadVec4fArray(location_twoPiOverWavelengths, twoPiOverWavelengths);
	}
	
	public void loadAmplitudes(float[] amplitudes) {
		super.loadVec4fArray(location_amplitudes, amplitudes);
	}
	
	public void loadWaveDirectionsX(float[] waveDirectionsX) {
		super.loadVec4fArray(location_waveDirectionsX, waveDirectionsX);
	}
	
	public void loadWaveDirectionsZ(float[] waveDirectionsZ) {
		super.loadVec4fArray(location_waveDirectionsZ, waveDirectionsZ);
	}
	
	public void loadPhases(float[] phases) {
		super.loadVec4fArray(location_phases, phases);
	}
	
	public void loadChopAmplitudes(float[] chopAmplitudes) {
		super.loadVec4fArray(location_chopAmplitudes, chopAmplitudes);
	}
	
	public void loadLODParams(float textureResolution, float[] texelSizes) {
		super.loadFloat(location_textureResolution, textureResolution);
		super.loadFloatArray(location_texelSizes, texelSizes);
	}
	
	public void loadLODIndex(int lodIndex) {
		super.loadInt(location_lodIndex, lodIndex);
	}
}
