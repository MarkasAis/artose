package textures;

import java.nio.ByteBuffer;

public class TextureData {
	private int textureType;
	private int pixelType;
	
	private ByteBuffer[] buffers;
	
	private int width;
	private int height;
	private int depth;
	
	public TextureData(int textureType, int pixelType, ByteBuffer buffer, int width, int height, int depth) {
		this.textureType = textureType;
		this.pixelType = pixelType;
		this.buffers = new ByteBuffer[1];
		this.buffers[0] = buffer;
		
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public TextureData(int textureType, int pixelType, ByteBuffer[] buffers, int width, int height, int depth) {
		this.textureType = textureType;
		this.pixelType = pixelType;
		this.buffers = buffers;
		
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public TextureData(int textureType, int pixelType, ByteBuffer buffer, int width, int height) {
		this(textureType, pixelType, buffer, width, height, 1);
	}
	
	public TextureData(int textureType, int pixelType, ByteBuffer[] buffers, int width, int height) {
		this(textureType, pixelType, buffers, width, height, 1);
	}
	
	public ByteBuffer[] getBuffers() {
		return buffers;
	}
	
	public int getTextureType() {
		return textureType;
	}
	
	public int getPixelType() {
		return pixelType;
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
}
