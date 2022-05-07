package skybox;

import org.joml.Vector3f;

import models.Mesh;
import models.PrimitiveGenerator;
import textures.Texture;
import textures.TextureUtils;

public class Skybox {
	private static final float DEFAULT_SIZE = 1000f;
	
	private Mesh cube;
	private Texture texture;
	
	private Vector3f skyColor;
	private Vector3f lightDirection;
	
	public Skybox(Texture texture, float size, Vector3f skyColor, Vector3f lightDirection) {
		this.cube = Mesh.load(PrimitiveGenerator.generateCube(size));
		this.texture = texture;
		this.skyColor = skyColor;
		this.lightDirection = lightDirection;
	}
	
	public Mesh getModel() {
		return cube;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Vector3f getSkyColor() {
		return skyColor;
	}
	
	public Vector3f getLightDirection() {
		return lightDirection;
	}
	
	public void delete() {
		cube.delete();
		texture.delete();
	}
	
	public static Skybox load(String[] files, float size, Vector3f skyColor, Vector3f lightDirection) {
		return new Skybox(TextureUtils.loadCubemap(files), size, skyColor, lightDirection);
	}
	
	public static Skybox load(String directory, float size, Vector3f skyColor, Vector3f lightDirection) {
		return new Skybox(TextureUtils.loadCubemap(directory), size, skyColor, lightDirection);
	}
	
	public static Skybox load(String[] files, Vector3f skyColor, Vector3f lightDirection) {
		return load(files, DEFAULT_SIZE, skyColor, lightDirection);
	}
	
	public static Skybox load(String directory, Vector3f skyColor, Vector3f lightDirection) {
		return load(directory, DEFAULT_SIZE, skyColor, lightDirection);
	}
}
