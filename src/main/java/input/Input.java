package input;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import display.DisplayManager;

public class Input {
	private static final int KEY_UP = -1;
	
	private static int currentFrame = 0;
	
	// Stores the frame when key was pressed, if it's up than it's set to KEY_UP
	private static Map<Integer, Integer> keyMap = new HashMap<Integer, Integer>();
	
	public static void initListener() {
		GLFWKeyCallback callback = new GLFWKeyCallback() {
			
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW.GLFW_RELEASE) {
					keyMap.put(key, KEY_UP);
				} else if (action == GLFW.GLFW_PRESS) {
					keyMap.put(key, currentFrame+1);
				}
			}
		};
		
		DisplayManager.setKeyCallback(callback);
	}
	
	public static void update() {
		currentFrame++;
	}
	
	public static boolean getKeyDown(int keycode) {
		int state = keyMap.getOrDefault(keycode, KEY_UP);
		
		return state != KEY_UP;
	}
	
	public static boolean getKeyDownThisFrame(int keycode) {
		return keyMap.getOrDefault(keycode, KEY_UP) == currentFrame;
	}
}
