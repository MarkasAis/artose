package ocean;

import java.util.Random;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import helpers.OpenGLHelpers;
import models.Mesh;
import models.PrimitiveGenerator;
import ocean.shaders.GerstnerBatchShader;
import renderEngine.VertexArrayObject;

public class GerstnerBatchGenerator {
	private static final int BATCH_SIZE = 32;
	private static final int COMPONENT_PER_OCTAVE = 8;
	
	public class GerstnerBatch {
		private GerstnerBatchShader shader = new GerstnerBatchShader();
		
		private float wavelength;
		private boolean enabled = false;
		
		public void render(float weight) {
			// TODO: carry this to OpenGLUtils
			OpenGLHelpers.enableAdditiveBlending();
			
			shader.start();
			
			VertexArrayObject vao = rasterMesh.getVAO();
			vao.bind(0, 1);
			
			GL30.glDrawElements(GL30.GL_TRIANGLES, vao.getElementCount(), GL30.GL_UNSIGNED_INT, 0);
			
			vao.unbind(0, 1);
			
			shader.stop();
			OpenGLHelpers.disableBlending();
		}
		
		public void cleanUp() {
			shader.cleanUp();
		}
		
		public float getWavelength() {
			return wavelength;
		}
		
		public boolean isEnabled() {
			return enabled;
		}
	}
	
	private GerstnerBatch[] batches;
	
	private final class GerstnerUpdateData {
		public float[] twoPiOverWavelengths = new float[BATCH_SIZE];
		public float[] amplitudes = new float[BATCH_SIZE];
		public float[] chopAmplitudes = new float[BATCH_SIZE];
		public float[] waveDirectionsX = new float[BATCH_SIZE];
		public float[] waveDirectionsZ = new float[BATCH_SIZE];
		public float[] phases = new float[BATCH_SIZE];
	} 
	
	private final GerstnerUpdateData GERSTNER_UPDATE_DATA = new GerstnerUpdateData();
	
	private Ocean ocean;
	private Mesh rasterMesh;
	
	private float windDirectionAngle = 0f;
	private float weight = 1f;
	
	private int seed = 0;
	private Random prng = new Random(seed);
	
	private float[] wavelengths;
	private float[] amplitudes;
	private float[] angles;
	private float[] phases;
	
	public GerstnerBatchGenerator(Ocean ocean) {
		this.ocean = ocean;
		rasterMesh = Mesh.load(PrimitiveGenerator.generatePlane(2f));
		
		batches = new GerstnerBatch[15]; // TODO: is hard-coded
		for (int i = 0; i < batches.length; i++)
			batches[i] = new GerstnerBatch();
	}
	
	public GerstnerBatch[] getBatches() {
		return batches;
	}
	
	public void update() {
		updateWaveData();
		updateBatches();
	}
	
	private void initPhases() {
		prng.setSeed(seed);
		
		int totalComponents = COMPONENT_PER_OCTAVE * ocean.getSpectrum().getOctaveCount();
		phases = new float[totalComponents];
		
		for (int octave = 0; octave < ocean.getSpectrum().getOctaveCount(); octave++) {
			for (int component = 0; component < COMPONENT_PER_OCTAVE; component++) {
				int index = octave * COMPONENT_PER_OCTAVE + component;
				float random = (component + prng.nextFloat()) / COMPONENT_PER_OCTAVE;
				phases[index] = (float) (random * 2 * Math.PI);
			}
		}
	}
	
	private void updateWaveData() {
		prng.setSeed(seed);
		
		int totalComponents = COMPONENT_PER_OCTAVE * ocean.getSpectrum().getOctaveCount();
		
		if (wavelengths == null || wavelengths.length != totalComponents) wavelengths = new float[totalComponents];
		if (angles == null || angles.length != totalComponents) angles = new float[totalComponents];
		if (amplitudes == null || amplitudes.length != totalComponents) amplitudes = new float[totalComponents];
		
		ocean.getSpectrum().generateWavelengths(COMPONENT_PER_OCTAVE, wavelengths, prng);
		ocean.getSpectrum().generateWaveAngles(COMPONENT_PER_OCTAVE, angles, prng);
		ocean.getSpectrum().generateWaveAmlitudes(COMPONENT_PER_OCTAVE, amplitudes, wavelengths, weight, prng);
		
		if (phases == null || phases.length != totalComponents) initPhases();
	}
	
	public void updateBatches() {
		float minWavelength = ocean.getMinWavelength(0);
		
		int componentIndex = 0;
		while (componentIndex < wavelengths.length && wavelengths[componentIndex] < minWavelength)
			componentIndex++;
		
		for (GerstnerBatch b : batches) b.enabled = false;
		
		int batchIndex = 0;
		
		while (componentIndex < wavelengths.length) {
			if (batchIndex >= batches.length) {
				System.out.println("Out of Gerstner batches.");
				break;
			}
			
			int startComponentIndex = componentIndex;
			while (componentIndex < wavelengths.length && wavelengths[componentIndex] < minWavelength * 2f)
				componentIndex++;
			
			if (componentIndex > startComponentIndex) {
				int lodIndex = Math.min(batchIndex, 6);
				updateBatch(lodIndex, startComponentIndex, componentIndex, batches[batchIndex]);
				batches[batchIndex].wavelength = minWavelength;
			}
			
			batchIndex++;
			minWavelength *= 2f;
		}
	}
	
	private void updateBatch(int lodIndex, int fromComponent, int toComponent, GerstnerBatch batch) {
		int componentsInBatch = 0;
		int dropped = 0;
		
		for (int component = fromComponent; component < toComponent; component++) {
			float wavelength = wavelengths[component];
			float amplitude = amplitudes[component];
			
			if (amplitude > 0.001f) {
				if (componentsInBatch < BATCH_SIZE) {
					float twoPiOverWavelength = (float) (2f * Math.PI / wavelength);
					GERSTNER_UPDATE_DATA.twoPiOverWavelengths[componentsInBatch] = twoPiOverWavelength;
					GERSTNER_UPDATE_DATA.amplitudes[componentsInBatch] = amplitude;
					GERSTNER_UPDATE_DATA.chopAmplitudes[componentsInBatch] = -ocean.getSpectrum().getChop() * amplitude;
					
					float angle = (float) Math.toRadians((windDirectionAngle + angles[component]));
					GERSTNER_UPDATE_DATA.waveDirectionsX[componentsInBatch] = (float) Math.cos(angle);
					GERSTNER_UPDATE_DATA.waveDirectionsZ[componentsInBatch] = (float) Math.sin(angle);
					
                    float phaseSpeed = (float) Math.sqrt(wavelength * Math.abs(Ocean.GRAVITY) / (2f * Math.PI));
                    GERSTNER_UPDATE_DATA.phases[componentsInBatch] = (float) ((phases[component] + twoPiOverWavelength * phaseSpeed * GLFW.glfwGetTime()) % (2f * Math.PI));
					
                    componentsInBatch++;
				} else dropped++;
			}
		}
		
		if (dropped > 0) System.out.println(dropped + " wavelengths dropped. Ran out of space inside the Gerstner batch.");
		
		for (int component = componentsInBatch; component < BATCH_SIZE; component++) {
			GERSTNER_UPDATE_DATA.twoPiOverWavelengths[component] = 1;
			GERSTNER_UPDATE_DATA.amplitudes[component] = 0;
			GERSTNER_UPDATE_DATA.chopAmplitudes[component] = 0;
			GERSTNER_UPDATE_DATA.waveDirectionsX[component] = 0;
			GERSTNER_UPDATE_DATA.waveDirectionsZ[component] = 0;
			GERSTNER_UPDATE_DATA.phases[component] = 0;
		}
		
		GerstnerBatchShader shader = batch.shader;
		shader.start();
		shader.loadTwoPiOverWavelengths(GERSTNER_UPDATE_DATA.twoPiOverWavelengths);
		shader.loadAmplitudes(GERSTNER_UPDATE_DATA.amplitudes);
		shader.loadChopAmplitudes(GERSTNER_UPDATE_DATA.chopAmplitudes);
		shader.loadWaveDirectionsX(GERSTNER_UPDATE_DATA.waveDirectionsX);
		shader.loadWaveDirectionsZ(GERSTNER_UPDATE_DATA.waveDirectionsZ);
		shader.loadPhases(GERSTNER_UPDATE_DATA.phases);
		
		shader.loadLODParams(Ocean.TEXTURE_RESOLUTION, ocean.getTexelSizes());
		shader.loadLODIndex(lodIndex);
		shader.stop();
		
		batch.enabled = true;
	}
	
	public void cleanUp() {
		rasterMesh.delete();
		
		for (GerstnerBatch batch : batches)
			batch.cleanUp();
	}
}

