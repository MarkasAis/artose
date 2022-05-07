package compute.buffers;

import org.lwjgl.opencl.CL22;

public abstract class ComputeBuffer {
	protected final long bufferId;
	
	public ComputeBuffer(long bufferId) {
		this.bufferId = bufferId;
	}
	
	public long getId() {
		return bufferId;
	}
	
	public void delete() {
		CL22.clReleaseMemObject(bufferId);
	}
}
