package compute.buffers;

import org.lwjgl.opencl.CL12GL;

import compute.ComputeManager;
import textures.Texture;

public class ComputeTexture extends ComputeBuffer {
	private Texture texture;
	
	private ComputeTexture(long bufferId, Texture texture) {
		super(bufferId);
		this.texture = texture;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public static ComputeTexture create(Texture texture, int accessMode) {
		long bufferId = CL12GL.clCreateFromGLTexture(ComputeManager.getContext(), accessMode, texture.getTextureType(), 0, texture.getId(), ComputeManager.SHARED_ERROR_BUFFER);
		CL12GL.clEnqueueAcquireGLObjects(ComputeManager.SHARED_QUEUE.getId(), bufferId, null, null);
		
		ComputeManager.SHARED_QUEUE.finish();
		
		return new ComputeTexture(bufferId, texture);
	}
}
