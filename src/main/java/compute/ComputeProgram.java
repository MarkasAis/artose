package compute;

import static helpers.OpenCLHelpers.checkCLError;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opencl.CL22;

import utils.FileUtils;

public abstract class ComputeProgram {
	
	private final long context;
	private final long device;
	
	private final long program;
	
	private List<Kernel> kernels = new ArrayList<Kernel>();
	
	public ComputeProgram(String file) {
		context = ComputeManager.getContext();
		device = ComputeManager.getDevice();
		
		String computeSource = FileUtils.readFile(file);
		program = CL22.clCreateProgramWithSource(context, computeSource, ComputeManager.SHARED_ERROR_BUFFER);
		checkCLError(ComputeManager.SHARED_ERROR_BUFFER);
		
		CL22.clBuildProgram(program, device, "", null, 0);
		
		createKernels();
	}
	
	protected long getId() {
		return program;
	}
	
	protected abstract void createKernels();
	
	protected Kernel createKernel(String name, int... worksize) {
		Kernel kernel = new Kernel(this, name);
		kernels.add(kernel);
		
		return kernel;
	}
	
	public void cleanUp() {
		for (Kernel kernel : kernels)
			kernel.cleanUp();
		
		CL22.clReleaseProgram(program);
	}
}
