package helpers;

import static org.lwjgl.opencl.CL10.CL_SUCCESS;

import java.nio.IntBuffer;

public class OpenCLHelpers {
	
	public static void checkCLError(IntBuffer errcode) {
        checkCLError(errcode.get(errcode.position()));
    }

    public static void checkCLError(int errcode) {
        if (errcode != CL_SUCCESS) {
            throw new RuntimeException(String.format("OpenCL error [%d]", errcode));
        }
    }
}
