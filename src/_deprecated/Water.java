package _deprecated;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Water {
	private static final float SIZE = 100;
	private static final float MAP_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 255 * 3;
	
	private float x;
	private float z;
	
	private RawModel model;
	//private ModelTexture texture;
	
	public Water(int gridX, int gridZ, Loader loader) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		//this.texture = texture;
		this.model = generateWater(loader);
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	/*public ModelTexture getTexture() {
		return texture;
	}*/

	private RawModel generateWater(Loader loader) {
		int VERTEX_COUNT = 1000;
		
		final int TOTAL_VERTICES = VERTEX_COUNT*VERTEX_COUNT;
		
		float[] vertices = new float[TOTAL_VERTICES * 3];
		//float[] normals = new float[TOTAL_VERTICES * 3];
		float[] texCoords = new float[TOTAL_VERTICES * 2];
		int[] indices = new int[(VERTEX_COUNT-1)*(VERTEX_COUNT-1) * 6];
		
		for (int z = 0; z < VERTEX_COUNT; z ++) {
			for (int x = 0; x < VERTEX_COUNT; x++) {
				final int INDEX = (z*VERTEX_COUNT+x);
				final float X_PERCENTAGE = (float)x / (float)(VERTEX_COUNT-1);
				final float Z_PERCENTAGE = (float)z / (float)(VERTEX_COUNT-1);
				
				vertices[INDEX*3] = X_PERCENTAGE * SIZE;
				vertices[INDEX*3 + 1] = 0;
				vertices[INDEX*3 + 2] = Z_PERCENTAGE * SIZE;
				
				//Vector3f normal = calculateNormal(x, z, heightmap);
				
				//normals[INDEX*3] = normal.x;
				//normals[INDEX*3 + 1] = normal.y;
				//normals[INDEX*3 + 2] = normal.z ;
				
				texCoords[INDEX*2] = X_PERCENTAGE;
				texCoords[INDEX*2 + 1] = Z_PERCENTAGE;
			}
		}
		
		int pointer = 0;
		for (int gZ = 0; gZ < VERTEX_COUNT-1; gZ++) {
			for (int gX = 0; gX < VERTEX_COUNT-1; gX++) {
				int tl = gZ * VERTEX_COUNT + gX;
				int tr = tl + 1;
				int bl = tl + VERTEX_COUNT;
				int br = bl + 1;
				
				indices[pointer++] = tl;
				indices[pointer++] = bl;
				indices[pointer++] = tr;
				
				indices[pointer++] = tr;
				indices[pointer++] = bl;
				indices[pointer++] = br;
				
			}
		}
		
		return loader.loadToVAO(vertices, indices);
	}
	
	/*private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		//float heightT = getHeight(x, z+1, image);
		//float heightR = getHeight(x+1, z, image);
		//float heightB = getHeight(x, z-1, image);
		//float heightL = getHeight(x-1, z, image);
		
		//Vector3f normal = new Vector3f(heightL-heightR, 2, heightB-heightT);
		//normal.normalize();
		
		return normal;
	}*/
}
