package compute;

import static helpers.OpenCLHelpers.checkCLError;

import org.lwjgl.opencl.CL22;

public class ComputeQueue {
	
	private final long queueId;
	
	public ComputeQueue() {
		queueId = CL22.clCreateCommandQueue(ComputeManager.getContext(), ComputeManager.getDevice(), 0, ComputeManager.SHARED_ERROR_BUFFER);
        checkCLError(ComputeManager.SHARED_ERROR_BUFFER);
	}
	
	public long getId() {
		return queueId;
	}
	
	public void finish() {
		CL22.clFinish(queueId);
	}
	
	public void delete() {
		CL22.clReleaseCommandQueue(queueId);
	}
}
