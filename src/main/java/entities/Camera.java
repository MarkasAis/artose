package entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import display.DisplayManager;
import utils.MathUtils;

public class Camera {
	
	protected Vector3f position;
	protected Vector3f rotation;
	
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	
	public Camera(Vector3f position, Vector3f rotation, float fov, float zNear, float zFar) {
		this.position = position;
		this.rotation = rotation;
		
		projectionMatrix = MathUtils.createPerspectiveProjectionMatrix(DisplayManager.getWidth(), DisplayManager.getHeight(), fov, zNear, zFar);
		updateViewMatrix();
	}
	
	public void update() {
		updateViewMatrix();
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getRotation() {
		return rotation;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	private void updateViewMatrix() {
		viewMatrix = MathUtils.createViewMatrix(position, rotation);
	}
}
