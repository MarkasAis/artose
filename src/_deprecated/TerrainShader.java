package _deprecated;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;
import renderEngine.ShaderProgram;
import utils.MathUtils;

public class TerrainShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/deprecated/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/deprecated/terrainFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColor");
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
	
	public void loadLight(Light light) {
		super.loadVec3f(location_lightPosition, light.getPosition());
		super.loadVec3f(location_lightColor, light.getColor());
	}
	
	public void loadReflectivity(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadSkyColor(Vector3f color) {
		super.loadVec3f(location_skyColor, color);
	}
}
