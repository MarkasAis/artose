package _deprecated;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import utils.MathUtils;

public class WaterRenderer {

	private WaterShader shader;
	
	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Water> waters) {
		for (Water water : waters) {
			prepareWater(water);
			loadModelMatrix(water);
			GL30.glDrawElements(GL30.GL_TRIANGLES, water.getModel().getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
	private void prepareWater(Water water) {
		RawModel model = water.getModel();
		//ModelTexture texture = water.getTexture();
		
		GL30.glBindVertexArray(model.getVaoID());
		GL30.glEnableVertexAttribArray(0);
		//GL30.glEnableVertexAttribArray(1);
		//GL30.glEnableVertexAttribArray(2);
		
		//shader.loadReflectivity(water.getShineDamper(), water.getReflectivity());
		
		//GL30.glActiveTexture(GL30.GL_TEXTURE0);
		//GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTexturedModel() {
		GL30.glDisableVertexAttribArray(0);
		//GL30.glDisableVertexAttribArray(1);
		//GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Water water) {
		Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(new Vector3f(water.getX(), 0, water.getZ()), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
