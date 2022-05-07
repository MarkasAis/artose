package entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import input.Input;
import utils.MathUtils;

public class TurningCamera extends Camera {

	private boolean isTurning = false;
	
	private float maxRotationVelocity = -0.01f;
	private float transitionSpeed = 0.5f;
	
	private float currentRotationVelocity = 0;
	
	public TurningCamera(Vector3f position, Vector3f rotation, float fov, float zNear, float zFar) {
		super(position, rotation, fov, zNear, zFar);
	}

	@Override
	public void update() {
		if (Input.getKeyDownThisFrame(GLFW.GLFW_KEY_SPACE)) isTurning = !isTurning;
		
		currentRotationVelocity = MathUtils.lerp(currentRotationVelocity, isTurning ? maxRotationVelocity : 0f, transitionSpeed);
		rotation.y += currentRotationVelocity;
		super.update();
	}
}
