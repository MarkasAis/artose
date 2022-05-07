package ocean.compute;

import compute.ComputeProgram;
import compute.ComputeQueue;
import compute.Kernel;
import compute.buffers.ComputeFloatBuffer;
import compute.buffers.ComputeTexture;
import textures.Texture;

public class CombineCompute extends ComputeProgram {
	private static final String SOURCE_FILE = "src/ocean/compute/combineCompute.txt";
	
	private int size;
	
	private Kernel kernel_combine;
	
	public CombineCompute(int size) {
		super(SOURCE_FILE);
		this.size = size;
	}

	@Override
	protected void createKernels() {
		kernel_combine = super.createKernel("combine", size, size);
	}

	public void loadWaveBuffers(ComputeTexture buffers) {
		Texture texture = buffers.getTexture();
		
		kernel_combine.setWorkSize(texture.getWidth(), texture.getHeight());
		kernel_combine.setArgument(0, buffers);
		kernel_combine.setArgument(1, texture.getWidth());
	}
	
	public void loadTexelSizes(ComputeFloatBuffer texelSizes) {
		kernel_combine.setArgument(2, texelSizes);
	}
	
	public void loadLODIndex(int lodIndex) {
		kernel_combine.setArgument(3, lodIndex);
	}
	
	public void enqueue(ComputeQueue queue) {
		kernel_combine.enqueue(queue);
	}
}
