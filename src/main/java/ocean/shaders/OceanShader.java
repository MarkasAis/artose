package ocean.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import renderEngine.ShaderProgram;

public class OceanShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/oceanVertex.txt";
	private static final String FRAGMENT_FILE = "shaders/oceanFragment.txt";
	
	private int location_transformationMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	
	private int location_textureResolution;
	private int location_texelSizes;
	private int location_lodScales;
	private int location_lodIndex;
	
	private int location_diffuse;
	private int location_diffuseGrazing;
	
	private int location_subsurface;
	private int location_subsurfaceBase;
	private int location_subsurfaceSun;
	private int location_subsurfaceFallOff;
	
	private int location_specularity;
	private int location_fresnel;
	
	private int location_lightDirection;
	
	private int location_fogColor;
	private int location_fogDensity;
	private int location_fogGradient;
	
	private int location_useCustomReflections;
	private int location_customBaseColor;
	private int location_customLightColor;
	private int location_customShadowColor;
	private int location_customLightRadiality;
	
	public OceanShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_positionOS");
	}

	@Override
	protected void getUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("u_transformationMatrix");
		location_viewMatrix = super.getUniformLocation("u_viewMatrix");
		location_projectionMatrix = super.getUniformLocation("u_projectionMatrix");
		
		location_textureResolution = super.getUniformLocation("u_textureResolution");
		location_texelSizes = super.getUniformLocation("u_texelSizes");
		location_lodScales = super.getUniformLocation("u_lodScales");
		location_lodIndex = super.getUniformLocation("u_lodIndex");
		
		location_diffuse = super.getUniformLocation("u_diffuse");
		location_diffuseGrazing = super.getUniformLocation("u_diffuseGrazing");
		
		location_subsurface = super.getUniformLocation("u_subsurfaceColor");
		location_subsurfaceBase = super.getUniformLocation("u_subsurfaceBase");
		location_subsurfaceSun = super.getUniformLocation("u_subsurfaceSun");
		location_subsurfaceFallOff = super.getUniformLocation("u_subsurfaceSunFallOff");
		
		location_specularity = super.getUniformLocation("u_specularity");
		location_fresnel = super.getUniformLocation("u_fresnel");
		
		location_lightDirection = super.getUniformLocation("u_lightDirection");
		
		location_fogColor = super.getUniformLocation("u_fogColor");
		location_fogDensity = super.getUniformLocation("u_fogDensity");
		location_fogGradient = super.getUniformLocation("u_fogGradient");
		
		location_useCustomReflections = super.getUniformLocation("u_useCustomReflections");
		location_customBaseColor = super.getUniformLocation("u_customBaseColor");
		location_customLightColor = super.getUniformLocation("u_customLightColor");
		location_customShadowColor = super.getUniformLocation("u_customShadowColor");
		location_customLightRadiality = super.getUniformLocation("u_customLightRadiality");
		
		// TODO: move this to some method. bindTextures
		start();
		super.loadInt(super.getUniformLocation("u_waveDisplacements"), 0);
		super.loadInt(super.getUniformLocation("u_reflectionCube"), 1);
		stop();
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadLODParams(float textureResolution, float[] texelSizes) {
		super.loadFloat(location_textureResolution, textureResolution);
		super.loadFloatArray(location_texelSizes, texelSizes);
	}
	
	public void loadLODScales(float[] lodScales) {
		super.loadFloatArray(location_lodScales, lodScales);
	}
	
	public void loadLODIndex(int lodIndex) {
		super.loadInt(location_lodIndex, lodIndex);
	}
	
	public void loadDiffuse(Vector3f baseColor, Vector3f grazingColor) {
		super.loadVec3f(location_diffuse, baseColor);
		super.loadVec3f(location_diffuseGrazing, grazingColor);
	}
	
	public void loadSubsurface(Vector3f color, float base, float sun, float falloff) {
		super.loadVec3f(location_subsurface, color);
		super.loadFloat(location_subsurfaceBase, base);
		super.loadFloat(location_subsurfaceSun, sun);
		super.loadFloat(location_subsurfaceFallOff, falloff);
	}
	
	public void loadSpecularity(float specularity, float fresnel) {
		super.loadFloat(location_specularity, specularity);
		super.loadFloat(location_fresnel, fresnel);
	}
	
	public void loadLightDirection(Vector3f direction) {
		super.loadVec3f(location_lightDirection, direction);
	}
	
	public void loadFog(Vector3f color, float density, float gradient) {
		super.loadVec3f(location_fogColor, color);
		super.loadFloat(location_fogDensity, density);
		super.loadFloat(location_fogGradient, gradient);
	}
	
	public void loadCustomReflections(Vector3f baseColor, Vector3f lightColor, Vector3f shadowColor, float radiality) {
		super.loadVec3f(location_customBaseColor, baseColor);
		super.loadVec3f(location_customLightColor, lightColor);
		super.loadVec3f(location_customShadowColor, shadowColor);
		super.loadFloat(location_customLightRadiality, radiality);
		enableCustomReflections();
	}
	
	public void enableCustomReflections() {
		super.loadBoolean(location_useCustomReflections, true);
	}
	
	public void disableCustomReflections() {
		super.loadBoolean(location_useCustomReflections, false);
	}
}
