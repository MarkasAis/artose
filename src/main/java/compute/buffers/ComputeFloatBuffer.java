package compute.buffers;

import java.nio.FloatBuffer;

import org.lwjgl.opencl.CL22;
import org.lwjgl.BufferUtils;

import compute.ComputeManager;
import compute.ComputeQueue;

public class ComputeFloatBuffer extends ComputeBuffer {
	private FloatBuffer buffer;
	
	private ComputeFloatBuffer(long bufferId, FloatBuffer buffer) {
		super(bufferId);
		this.buffer = buffer;
	}
	
	public void load(float[] array, ComputeQueue queue) {
		buffer.clear();
		buffer.put(array);
		buffer.flip();
		
		CL22.clEnqueueWriteBuffer(queue.getId(), bufferId, true, 0, buffer, null, null);
	}
	
	public void loadInstant(float[] array) {
		load(array, ComputeManager.SHARED_QUEUE);
		ComputeManager.SHARED_QUEUE.finish();
	}
	
	public static ComputeFloatBuffer create(int size, int accessMode) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size);
		long id = CL22.clCreateBuffer(ComputeManager.getContext(), accessMode | CL22.CL_MEM_COPY_HOST_PTR, buffer, null);
		
		return new ComputeFloatBuffer(id, buffer);
	}
}
