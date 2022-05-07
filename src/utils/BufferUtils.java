package utils;

import static org.lwjgl.BufferUtils.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {
	public static FloatBuffer storeInBuffer(float[] array) {
		FloatBuffer buffer = createFloatBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer storeInBuffer(int[] array) {
		IntBuffer buffer = createIntBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		
		return buffer;
	}
}
