package skybox;

import org.lwjgl.opengl.GL30;

import entities.Camera;
import renderEngine.VertexArrayObject;

public class SkyboxRenderer {
	private SkyboxShader shader = new SkyboxShader();
	
	public void render(Skybox skybox, Camera camera) {
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera.getViewMatrix());
		
		VertexArrayObject vao = skybox.getModel().getVAO();
		vao.bind(0);
		
		skybox.getTexture().bind(0);
		
		GL30.glDrawElements(GL30.GL_TRIANGLES, vao.getElementCount(), GL30.GL_UNSIGNED_INT, 0);
		
		vao.unbind(0);

		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
