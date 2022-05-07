package helpers;

import org.lwjgl.opengl.GL30;

public class OpenGLHelpers {

	public static void enableAdditiveBlending() {
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE);
	}
	
	public static void disableBlending() {
		GL30.glDisable(GL30.GL_BLEND);
	}
}
