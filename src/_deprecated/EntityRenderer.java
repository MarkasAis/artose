package _deprecated;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import ocean.shaders.GerstnerBatchShader;
import utils.MathUtils;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL30.glDrawElements(GL30.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel texturedModel) {
		RawModel model = texturedModel.getRawModel();
		ModelTexture texture = texturedModel.getTexture();
		
		GL30.glBindVertexArray(model.getVaoID());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		
		if (texture.getHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		
		shader.loadReflectivity(texture.getShineDamper(), texture.getReflectivity());
		shader.loadFakeLighting(texture.getUseFakeLighting());
		
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texturedModel.getTexture().getID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
