package utils;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MathUtils {

	public static Matrix4f createTransformationMatrix(Vector3f translation, Quaternionf rotation, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		storeTransformationMatrix(translation, rotation, scale, matrix);
		
		return matrix;
	}
	
	public static void storeTransformationMatrix(Vector3f translation, Quaternionf rotation, Vector3f scale, Matrix4f matrix) {
		matrix.identity();
		
		matrix.translate(translation);
		matrix.rotate(rotation);
		matrix.scale(scale);
	} 
	
	public static Matrix4f createViewMatrix(Vector3f position, Vector3f rotation) {
		Matrix4f matrix = new Matrix4f();
		
		matrix.translate(position);
		matrix.rotateXYZ(rotation);
		matrix.invert();
		
		return matrix;
	}
	
	public static Matrix4f createPerspectiveProjectionMatrix(float width, float height, float fov, float zNear, float zFar) {
		Matrix4f matrix = new Matrix4f();
		
		float aspectRatio = width / height;
		matrix.perspective((float) Math.toRadians(fov), aspectRatio, zNear, zFar);
		
		return matrix;
	}
	
	public static Matrix4f createOrthogonalProjectionMatrix(float width, float height, float zNear, float zFar) {
		Matrix4f matrix = new Matrix4f();
		
		matrix.orthoSymmetric(width, height, zNear, zFar);
		
		return matrix;
	}
	
	public static Vector3f rotationToDirection(Vector3f rotation) {
		Quaternionf q = new Quaternionf();
		q.rotateXYZ(rotation.x, rotation.y, rotation.z);
		
		Vector3f direction = new Vector3f(0, 0, 1);
		q.transform(direction);
		
		return direction;
	}
	
	public static float lerp(float a, float b, float t) {
		return (b-a) * t + a;
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(value, max));
	}
	
	public static float logn(float value, float n) {
		return (float) (Math.log10(value) / Math.log10(n));
	}
	
	public static float log2(float value) {
		return logn(value, 2);
	}
}
