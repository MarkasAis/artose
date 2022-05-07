package compute;

import static org.lwjgl.opencl.CL10.CL_CONTEXT_PLATFORM;
import static org.lwjgl.opencl.CL10.CL_DEVICE_TYPE_CPU;
import static org.lwjgl.opencl.CL10.CL_DEVICE_TYPE_GPU;
import static org.lwjgl.opencl.CL10.CL_PLATFORM_VENDOR;
import static org.lwjgl.opencl.CL10.clCreateContext;
import static org.lwjgl.opencl.CL10.clGetDeviceIDs;
import static org.lwjgl.opencl.CL10.clGetPlatformInfo;
import static org.lwjgl.opengl.CGL.CGLGetCurrentContext;
import static org.lwjgl.opengl.CGL.CGLGetShareGroup;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;
import static helpers.OpenCLHelpers.checkCLError;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

public class ComputeManager {
	public static IntBuffer SHARED_ERROR_BUFFER;
	public static ComputeQueue SHARED_QUEUE;
	
	private static long platform;
	private static long device;
	
	private static CLCapabilities platformCaps;
	private static CLCapabilities deviceCaps; // TODO: use it to print device specifications
	
	private static CLContextCallback contextCallback;
	private static long context;

    public static void createContext(long window) {
		SHARED_ERROR_BUFFER = BufferUtils.createIntBuffer(1);
		
		List<Long> platforms = getPlatforms();
		
		if (platforms.isEmpty()) {
            throw new IllegalStateException("No OpenCL platform found that supports OpenGL context sharing.");
        }
		
		sortPlatformsByGPU(platforms);
		
		platform = platforms.get(0);
        platformCaps = CL.createPlatformCapabilities(platform);
        
        device = getDevicePrioritiseGPU(platform, platformCaps);
        
        if (device == NULL) {
            throw new RuntimeException("No OpenCL devices found with OpenGL sharing support.");
        }

        deviceCaps = CL.createDeviceCapabilities(device, platformCaps);
        
        PointerBuffer ctxProps = BufferUtils.createPointerBuffer(7);
        switch (Platform.get()) {
            case MACOSX:
                ctxProps
                    .put(APPLEGLSharing.CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE)
                    .put(CGLGetShareGroup(CGLGetCurrentContext()));
                break;
			case LINUX:
				throw new RuntimeException("Linux operating system is not currently supported.");
			case WINDOWS:
                throw new RuntimeException("Windows operating system is not currently supported.");
//                ctxProps
//                    .put(KHRGLSharing.CL_GL_CONTEXT_KHR)
//                    .put(org.lwjgl.glfw.GLFWNativeWGL.glfwGetWGLContext(window))
//                    .put(KHRGLSharing.CL_WGL_HDC_KHR)
//                    .put(org.lwjgl.opengl.WGL.wglGetCurrentDC());
//				break;
			default:
				throw new RuntimeException("Current operating system is not currently supported.");
        }
        
        ctxProps
	        .put(CL_CONTEXT_PLATFORM)
	        .put(platform)
	        .put(NULL)
	        .flip();
        
        createContextCallback();
        
        context = clCreateContext(ctxProps, device, contextCallback, NULL, SHARED_ERROR_BUFFER);    
        checkCLError(SHARED_ERROR_BUFFER);
        
        SHARED_QUEUE = new ComputeQueue();
	}
	
	public static long getContext() {
		return context;
	}
	
	public static long getDevice() {
		return device;
	}
	
	public static void cleanUp() {
		CL22.clReleaseContext(context);
        CL.destroy();
	}
	
	private static void createContextCallback() {
		contextCallback = CLContextCallback.create((errinfo, private_info, cb, user_data) -> 
			System.err.println("cl_context_callback\n\tInfo: " + memUTF8(errinfo))
		);
	}
	
	private static void sortPlatformsByGPU(List<Long> platforms) {
		platforms.sort((p1, p2) -> {
            boolean gpu1 = !getDevices(p1, CL_DEVICE_TYPE_GPU).isEmpty();
            boolean gpu2 = !getDevices(p2, CL_DEVICE_TYPE_GPU).isEmpty();
            int cmp  = (gpu1 == gpu2) ? 0 : (gpu1 ? -1 : 1);
            
            if (cmp != 0) return cmp;

            return getPlatformInfoStringUTF8(p1, CL_PLATFORM_VENDOR).compareTo(getPlatformInfoStringUTF8(p2, CL_PLATFORM_VENDOR));
        });
	}
	
	private static long getDevicePrioritiseGPU(long platform, CLCapabilities platformCaps) {
		long device = getDevice(platform, platformCaps, CL_DEVICE_TYPE_GPU);
		
        if (device == NULL)
            device = getDevice(platform, platformCaps, CL_DEVICE_TYPE_CPU);
        
        return device;
	}
	
	private static List<Long> getPlatforms() {
		List<Long> platforms;
		
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            
            PointerBuffer test = stack.mallocPointer(10);
            
            CL22.clGetPlatformIDs(test, pi);
            
            if (pi.get(0) == 0)
                throw new IllegalStateException("No OpenCL platforms found.");

            PointerBuffer platformIDs = stack.mallocPointer(pi.get(0));
            checkCLError(CL22.clGetPlatformIDs(platformIDs, (IntBuffer)null));
            
            platforms = new ArrayList<>(platformIDs.capacity());

            for (int i = 0; i < platformIDs.capacity(); i++) {
                long platform = platformIDs.get(i);
                CLCapabilities caps = CL.createPlatformCapabilities(platform);
                if (caps.cl_khr_gl_sharing || caps.cl_APPLE_gl_sharing) {
                    platforms.add(platform);
                }
            }
        }
        
        return platforms;
	}
	
	private static List<Long> getDevices(long platform, int deviceType) {
        List<Long> devices;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            int errcode = CL22.clGetDeviceIDs(platform, deviceType, null, pi);
            if (errcode == CL22.CL_DEVICE_NOT_FOUND) {
                devices = Collections.emptyList();
            } else {
                PointerBuffer deviceIDs = stack.mallocPointer(pi.get(0));
                CL22.clGetDeviceIDs(platform, deviceType, deviceIDs, (IntBuffer) null);

                devices = new ArrayList<>(deviceIDs.capacity());

                for (int i = 0; i < deviceIDs.capacity(); i++)
                	devices.add(deviceIDs.get(i));
            }
        }

        return devices;
    }
	
	private static long getDevice(long platform, CLCapabilities platformCaps, int deviceType) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            checkCLError(clGetDeviceIDs(platform, deviceType, null, pi));

            PointerBuffer devices = stack.mallocPointer(pi.get(0));
            checkCLError(clGetDeviceIDs(platform, deviceType, devices, (IntBuffer)null));

            for (int i = 0; i < devices.capacity(); i++) {
                long device = devices.get(i);

                CLCapabilities caps = CL.createDeviceCapabilities(device, platformCaps);
                if (!(caps.cl_khr_gl_sharing || caps.cl_APPLE_gl_sharing)) continue;

                return device;
            }
        }

        return NULL;
    }
	
	private static String getPlatformInfoStringUTF8(long cl_platform_id, int param_name) {
        try (MemoryStack stack = stackPush()) {
            PointerBuffer pp = stack.mallocPointer(1);
            checkCLError(clGetPlatformInfo(cl_platform_id, param_name, (ByteBuffer)null, pp));
            int bytes = (int)pp.get(0);

            ByteBuffer buffer = stack.malloc(bytes);
            checkCLError(clGetPlatformInfo(cl_platform_id, param_name, buffer, null));

            return memUTF8(buffer, bytes - 1);
        }
    }
}
