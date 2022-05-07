package compute;

import static helpers.OpenCLHelpers.checkCLError;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL22;

import compute.buffers.ComputeBuffer;

public class Kernel {
	
	ComputeProgram program;
	private long kernel;
	
	private int dimensions;
	private PointerBuffer workSizeBuffer;
	
	protected Kernel(ComputeProgram program, String name) {
		this.program = program;
		
		kernel = CL22.clCreateKernel(program.getId(), name, ComputeManager.SHARED_ERROR_BUFFER);
		checkCLError(ComputeManager.SHARED_ERROR_BUFFER);
	}
	
	public void setWorkSize(int... sizes) {
		if (sizes.length == 0)
			System.err.println("Kernel work size was not provided.");
		
		if (workSizeBuffer == null || dimensions != sizes.length) {
			dimensions = sizes.length;
			workSizeBuffer = BufferUtils.createPointerBuffer(dimensions);
		}
			
		for (int i = 0; i < dimensions; i++)
			workSizeBuffer.put(i, sizes[i]);
	}
	
	public void setArgument(int index, ComputeBuffer  buffer) {
		CL22.clSetKernelArg1p(kernel, index, buffer.getId());
	}
	
	public void setArgument(int index, int value) {
		CL22.clSetKernelArg1i(kernel, index, value);
	}
	
	public void setArgument(int index, float value) {
		CL22.clSetKernelArg1f(kernel, index, value);
	}
	
	public void enqueue(ComputeQueue queue) {
		CL22.clEnqueueNDRangeKernel(queue.getId(), kernel, 2, null, workSizeBuffer, null, null, null);
	}
	
	public void cleanUp() {
		CL22.clReleaseKernel(kernel);
	}
}
