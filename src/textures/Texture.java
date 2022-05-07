package textures;

import org.lwjgl.opengl.GL30;

public class Texture {
	private int textureType;
	private int textureId;
	
	private int width;
	private int height;
	private int depth;
	
	public Texture(int textureType, int textureId, int width, int height) {
		this.textureType = textureType;
		this.textureId = textureId;
		
		this.width = width;
		this.height = height;
		this.depth = 1;
	}
	
	public Texture(int textureType, int textureId, int width, int height, int depth) {
		this.textureType = textureType;
		this.textureId = textureId;
		
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public int getTextureType() {
		return textureType;
	}
	
	public int getId() {
		return textureId;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void bind(int unit) {
		GL30.glActiveTexture(GL30.GL_TEXTURE0 + unit);
		GL30.glBindTexture(textureType, textureId);
	}
	
	public void delete() {
		GL30.glDeleteTextures(textureId);
	}
}
