package ocean;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import entities.Entity;
import models.Model;
import ocean.shaders.OceanShader;
import renderEngine.VertexArrayObject;
import textures.Texture;

public class OceanRenderer {
	
	public void render(Ocean ocean, Texture reflectionCubemap, Camera camera) {
		GL30.glDisable(GL30.GL_CULL_FACE);
		
		OceanShader shader = ocean.getShader();
		Matrix4f viewMatrix = camera.getViewMatrix();
		Matrix4f projectionMatrix = camera.getProjectionMatrix();
		
		shader.start();
		
		shader.loadViewMatrix(viewMatrix);
		shader.loadProjectionMatrix(projectionMatrix);
		
		shader.loadLODParams(Ocean.TEXTURE_RESOLUTION, ocean.getTexelSizes());
		shader.loadLODScales(ocean.getLODScales());
		
		ocean.getWaveDisplacements().bind(0);
		reflectionCubemap.bind(1);
		
		Entity[][] chunks = ocean.getChunks();
		
		for (int lodIndex = 0; lodIndex < Ocean.LOD_COUNT; lodIndex++) {
			shader.loadLODIndex(lodIndex);
			
			for (Entity chunk : chunks[lodIndex]) {
				shader.loadTransformationMatrix(chunk.getTransformationMatrix());
				
				Model model = chunk.getModel();
				VertexArrayObject vao = model.getMesh().getVAO();
				
				vao.bind(0);
				GL30.glDrawElements(GL30.GL_TRIANGLES, vao.getElementCount(), GL30.GL_UNSIGNED_INT, 0);
				vao.unbind(0);
			}
		}
		
		shader.stop();
	}
}
