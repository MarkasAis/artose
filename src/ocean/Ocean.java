package ocean;

import org.joml.Vector3f;

import entities.Entity;
import ocean.shaders.OceanShader;
import textures.Texture;
import utils.MathUtils;

public class Ocean {
	protected static final float GRAVITY = -9.81f;
	
	protected static final int TEXTURE_RESOLUTION = 512;
	protected static final int LOD_COUNT = 7;
	protected static final int MESH_DENSITY = 10;
	
	private float[] texelSizes;
	private float[] lodScales;
	
	private Entity parent;
	private Entity[][] chunks;
	
	private WaveDisplacementGenerator waveDisplacements;
	private OceanChunkGenerator chunkGenerator;
	
	private WaveFrequencies spectrum;
	private OceanShader shader;
	
	private float scale;
	
	public Ocean(WaveFrequencies spectrum, OceanShader shader) {
		this.spectrum = spectrum;
		this.shader = shader;
		
		waveDisplacements = new WaveDisplacementGenerator(this);
		chunkGenerator = new OceanChunkGenerator(this);
		
		texelSizes = new float[LOD_COUNT];
		lodScales = new float[LOD_COUNT];
		
		chunkGenerator.generate();
	}
	
	public void update() {
		updateScale();
		updateLODs();
		waveDisplacements.update();
	}
	
	public WaveFrequencies getSpectrum() {
		return spectrum;
	}
	
	public void setSpectrum(WaveFrequencies spectrum) {
		this.spectrum = spectrum;
	}
	
	public OceanShader getShader() {
		return shader;
	}
	
	public void setShader(OceanShader shader) {
		this.shader = shader;
	}
	
	protected float[] getTexelSizes() {
		return texelSizes;
	}
	
	protected float[] getLODScales() {
		return lodScales;
	}
	
	protected Entity[][] getChunks() {
		return chunks;
	}
	
	protected void setChunks(Entity[][] chunks) {
		this.chunks = chunks;
	}
	
	protected Entity getParent() {
		return parent;
	}
	
	protected void setParent(Entity parent) {
		this.parent = parent;
	}
	
	protected Texture getWaveDisplacements() {
		return waveDisplacements.getWaveDisplacements();
	}
	
	protected float getLODScale(int lodIndex) {
		return (float) (scale * Math.pow(2, lodIndex));
	}
	
	protected float getMinWavelength(int lodIndex) {
		float maxDiameter = 4f * getLODScale(lodIndex);
		float maxTexelSize = maxDiameter / TEXTURE_RESOLUTION;
		
		float minWavelength = maxTexelSize * 3f; // TODO: is hard-coded
		
		return minWavelength;
	}
	
	private void updateScale() {
		float minScale = 8f;
		float maxScale = 256f;
		
		float level = 0;
		level = Math.max(minScale, Math.min(level, 1.99f * maxScale));
		
		float levelLog = (float) Math.floor(MathUtils.log2(level));
		scale = (float) Math.pow(2, levelLog);
		
		parent.setLocalScale(new Vector3f(scale, 1, scale));
	}
	
	private void updateLODs() {
		for (int i = 0; i < LOD_COUNT; i++) {
			float lodScale = getLODScale(i);
			lodScales[i] = lodScale;
			texelSizes[i] = 4f * lodScale / TEXTURE_RESOLUTION;
		}
	}
	
	public void cleanUp() {
		waveDisplacements.cleanUp();
		chunkGenerator.cleanUp();
	}
}
