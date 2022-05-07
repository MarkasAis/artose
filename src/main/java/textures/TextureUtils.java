package textures;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class TextureUtils {
	private static class ImageBuffer { public ByteBuffer buffer; int width; int height; }
	
	private static final String[] CUBEMAP_FILES = new String[] { "right.png", "left.png", "up.png", "down.png", "back.png", "front.png" };
	
	private static ImageBuffer readImageFromFile(String file) {
		MemoryStack stack = stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		
		ImageBuffer image = new ImageBuffer();
		
		image.buffer = STBImage.stbi_load("src/main/resources/" + file, w, h, comp, 4);
		if (image.buffer == null) {
		    throw new RuntimeException("Failed to load a texture file!"
		            + System.lineSeparator() + STBImage.stbi_failure_reason());
		}

		image.width = w.get();
		image.height = h.get();
		
		return image;
	}
	
	public static TextureData readTexture(String file) {
		ImageBuffer image = readImageFromFile(file);
		
		return new TextureData(GL30.GL_TEXTURE_2D, GL30.GL_UNSIGNED_BYTE, image.buffer, image.width, image.height);
	}
	
	public static TextureData readCubemap(String[] files) {
		if (files.length != 6) {
			System.err.println("Failed to load a cubemap texture. Make sure 6 images are provided.");
			return null;
		}
		
		ByteBuffer[] buffers = new ByteBuffer[6];
		int width = 0;
		int height = 0;
		
		for (int i = 0; i < 6; i++) {
			ImageBuffer image = readImageFromFile(files[i]);
			buffers[i] = image.buffer;
			
			if (i == 0 || width == image.width && height == image.height) {
				width = image.width;
				height = image.height;
			} else {
				System.err.println("Cubemap textures are different dimensions.");
			}
		}
		
		return new TextureData(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_UNSIGNED_BYTE, buffers, width, height);
	}
	
	private static Texture loadTexture2D(TextureData data) {
		int textureId = GL30.glGenTextures();
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);
		
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, data.getWidth(), data.getHeight(), 0, GL30.GL_RGBA, data.getPixelType(), data.getBuffers()[0]);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
		
		return new Texture(GL30.GL_TEXTURE_2D, textureId, data.getWidth(), data.getHeight());
	}
	
	private static Texture loadCubemap(TextureData data) {
		int textureId = GL30.glGenTextures();
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, textureId);
		
		ByteBuffer[] buffers = data.getBuffers();
		
		for (int i = 0; i < 6; i++)
			GL30.glTexImage2D(GL30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL30.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL30.GL_RGBA, data.getPixelType(), buffers[i]);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_CUBE_MAP, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
		
		GL30.glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, 0);
		
		return new Texture (GL30.GL_TEXTURE_CUBE_MAP, textureId, data.getWidth(), data.getHeight());
	}
	
	public static Texture createTexture2D(int pixelType, int width, int height) {
		return loadTexture2D(new TextureData(GL30.GL_TEXTURE_2D, pixelType, (ByteBuffer) null, width, height));
	}
	
	public static Texture loadTexture2DArray(TextureData data) {
		int textureId = GL30.glGenTextures();
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, textureId);
		GL30.glTexImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, GL30.GL_RGBA, data.getWidth(), data.getHeight(), data.getDepth(), 0, GL30.GL_RGBA, data.getPixelType(), data.getBuffers()[0]);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
		
		return new Texture(GL30.GL_TEXTURE_2D_ARRAY, textureId, data.getWidth(), data.getHeight(), data.getDepth());
	}
	
	public static Texture createTexture2DArray(int pixelType, int width, int height, int depth) {
		return loadTexture2DArray(new TextureData(GL30.GL_TEXTURE_2D_ARRAY, pixelType, (ByteBuffer) null, width, height, depth));
	}
	
	public static Texture loadCubemap(String[] files) {
		return loadCubemap(readCubemap(files));
	}
	
	public static Texture loadCubemap(String directory) {
		String[] files = new String[6];
		
		for (int i = 0; i < files.length; i++) {
			files[i] = directory + "/" + CUBEMAP_FILES[i];
		}
		
		return loadCubemap(files);
	}
	
	public static Texture loadTexture(TextureData data) {
		int type = data.getTextureType();
		
		switch (type) {
			case GL30.GL_TEXTURE_2D:
				return loadTexture2D(data);
			case GL30.GL_TEXTURE_2D_ARRAY:
				return null;
			case GL30.GL_TEXTURE_CUBE_MAP:
				return loadCubemap(data);
		}
		
		return null;
	}
}
