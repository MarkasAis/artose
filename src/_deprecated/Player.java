package _deprecated;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.joml.Vector3f;

import display.DisplayManager;

public class Player extends Entity {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 2;
	private static final float GRAVITY = -50;
	private static final float JUMP_FORCE = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, position, rotation, scale);
	}

	public void move() {
		checkInputs();
		super.rotate(new Vector3f(0, currentTurnSpeed * DisplayManager.getDeltaTime(), 0));
		
		float distance = currentSpeed * DisplayManager.getDeltaTime();
		float dX = (float)Math.sin(super.getRotation().y) * distance;
		float dY = (float)Math.cos(super.getRotation().y) * distance;
		super.translate(new Vector3f(dX, 0, dY));
		
		upwardsSpeed += GRAVITY * DisplayManager.getDeltaTime();
		super.translate(new Vector3f(0, upwardsSpeed * DisplayManager.getDeltaTime(), 0));
		
		if (super.getPosition().y < TERRAIN_HEIGHT) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = TERRAIN_HEIGHT;
		}
	}
	
	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_FORCE;
			isInAir = true;
		}
	}
	
	private void checkInputs() {
		if(DisplayManager.getKey(GLFW_KEY_W))
			currentSpeed = RUN_SPEED;
		else if(DisplayManager.getKey(GLFW_KEY_S))
			currentSpeed = -RUN_SPEED;
		else
			currentSpeed = 0;
		
		if(DisplayManager.getKey(GLFW_KEY_D))
			currentTurnSpeed = -TURN_SPEED;
		else if(DisplayManager.getKey(GLFW_KEY_A))
			currentTurnSpeed = TURN_SPEED;
		else
			currentTurnSpeed = 0;
		
		if (DisplayManager.getKey(GLFW_KEY_SPACE)) {
			jump();
		}
	}
}
