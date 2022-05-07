package renderEngine;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import display.DisplayManager;
import textures.Texture;
import textures.TextureUtils;

public class FrameBufferObject {
	private int fboId;
	private Texture texture;
	
	private int width;
	private int height;
	
	public FrameBufferObject(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		
		fboId = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
		
		texture = TextureUtils.createTexture2DArray(GL30.GL_FLOAT, width, height, depth);
		int textureId = texture.getId();
		
		for (int i = 0; i < depth; i++)
			GL32.glFramebufferTextureLayer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + i, textureId, 0, i);
		
		
		unbind();
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void bind(int layer) {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
        GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 + layer);
        GL30.glViewport(0, 0, width, height);
	}
	
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	    GL30.glViewport(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
	}
	
	public void delete() {
		GL30.glDeleteFramebuffers(fboId);
		texture.delete();
	}
}
