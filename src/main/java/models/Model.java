package models;

import renderEngine.ShaderProgram;

public class Model {
	private Mesh mesh;
	private ShaderProgram shader;
	
	public Model(Mesh mesh, ShaderProgram shader) {
		this.mesh = mesh;
		this.shader = shader;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public ShaderProgram getShader() {
		return shader;
	}
}
