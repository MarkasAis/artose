package skybox;

import org.joml.Matrix4f;

import renderEngine.ShaderProgram;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/skybox/skyboxVertex.txt";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragment.txt";
	
	private int location_viewMatrix;
	private int location_projectionMatrix;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_positionOS");
	}

	@Override
	protected void getUniformLocations() {
		location_viewMatrix = super.getUniformLocation("u_viewMatrix");
		location_projectionMatrix = super.getUniformLocation("u_projectionMatrix");
	}
	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
}
