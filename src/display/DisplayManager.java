package display;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {
	
	private static final int INITIAL_WIDTH = 1200;
	private static final int INITIAL_HEIGHT = 750;
	private static final int FPS_CAP = 120; //TODO: do fps syncing
	
	private static final String TITLE = "ARTOSE - A Real-Time Ocean Simulation Engine";
	
	private static float lastFrameTime;
	private static float deltaTime;
	
	private static long window;
	
	private static int frameBufferWidth;
	private static int frameBufferHeight;;
	
	public static void createDisplay() {
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

	    // Enables sRGB color space
	    //glfwWindowHint(GLFW_SRGB_CAPABLE, GLFW_TRUE);
	    
		window = glfwCreateWindow(INITIAL_WIDTH, INITIAL_HEIGHT, TITLE, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
		});

		try ( MemoryStack stack = stackPush() ) {
			IntBuffer windowWidth = stack.mallocInt(1);
			IntBuffer windowHeight = stack.mallocInt(1);
			IntBuffer fbWidth = stack.mallocInt(1);
			IntBuffer fbHeight = stack.mallocInt(1);

			glfwGetWindowSize(window, windowWidth, windowHeight);
			glfwGetFramebufferSize(window, fbWidth, fbHeight);

			frameBufferWidth = fbWidth.get(0);
			frameBufferHeight = fbHeight.get(0);
			
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			glfwSetWindowPos(
				window,
				(vidmode.width() - windowWidth.get(0)) / 2,
				(vidmode.height() - windowHeight.get(0)) / 2
			);
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		GL.createCapabilities();
		
		lastFrameTime = (float)GLFW.glfwGetTime();
	}
	
	public static void updateDisplay() {
		glfwSwapBuffers(window);
		glfwPollEvents();
		
		float currentTime = (float)GLFW.glfwGetTime();
		deltaTime = (currentTime - lastFrameTime);
		lastFrameTime = currentTime;
	}
	
	public static void closeDisplay() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
	}
	
	public static void setKeyCallback(GLFWKeyCallback callback) {
		glfwSetKeyCallback(window, callback);
	}
	
	// TODO: put this in a seperate Input class
	public static boolean getKey(int key) {
		return glfwGetKey(window, key) == 1;
	}
	
	public static int getWidth() {
		return frameBufferWidth;
	}
	
	public static int getHeight() {
		return frameBufferHeight;
	}
	
	public static float getDeltaTime() {
		return deltaTime;
	}
}
