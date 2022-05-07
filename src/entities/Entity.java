package entities;

import java.util.HashSet;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import models.Model;
import utils.MathUtils;

public class Entity {
	private Entity parent;
	private Set<Entity> children = new HashSet<Entity>();
	
	private Model model;
	
	private Vector3f localPosition;
	private Quaternionf localRotation;
	private Vector3f localScale;
	
	private Vector3f worldPosition;
	private Quaternionf worldRotation;
	private Vector3f worldScale;
	
	private boolean recalculateTransformation = true;
	private Matrix4f transformationMatrix = new Matrix4f();
	
	public Entity(Model model, Vector3f position, Quaternionf rotation, Vector3f scale) {
		this.model = model;
		this.localPosition = position;
		this.localRotation = rotation;
		this.localScale = scale;
		
		matchLocalWorld();
	}
	
	public Entity() {
		this(null, new Vector3f(0f, 0f, 0f), new Quaternionf(0f, 0f, 0f, 1f), new Vector3f(1f, 1f, 1f));
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	private void matchLocalWorld() {
		worldPosition = localPosition;
		worldRotation = localRotation;
		worldScale = localScale;
		
		recalculateTransformation = true;
	}
	
	protected void recalculateWorldPosition() {
		if (parent != null)
			worldPosition = parent.getWorldRotation().transform(new Vector3f(localPosition)).mul(parent.getWorldScale()).add(parent.getWorldPosition());
		for (Entity child : children) {
			child.recalculateWorldPosition();
		}
		
		recalculateTransformation = true;
	}
	
	protected void recalculateWorldRotation() {
		if (parent != null)
			worldRotation = new Quaternionf(parent.getWorldRotation()).mul(localRotation);
	
		for (Entity child : children) {
			child.recalculateWorldPosition();
			child.recalculateWorldRotation();
		}
		
		recalculateTransformation = true;
	}
	
	protected void recalculateWorldScale() {
		if (parent != null)
			worldScale = new Vector3f(localScale).mul(parent.getWorldScale());
	
		for (Entity child : children) {
			child.recalculateWorldPosition();
			child.recalculateWorldScale();
		}
		
		recalculateTransformation = true;
	}
	
	public Vector3f getLocalPosition() {
		return localPosition;
	}

	public Vector3f getWorldPosition() {
		return worldPosition;
	}
	
	public void setLocalPosition(Vector3f position) {
		if (localPosition.equals(position)) return;
		
		localPosition.set(position);
		
		recalculateWorldPosition();
	}

	public Quaternionf getLocalRotation() {
		return localRotation;
	}

	public Quaternionf getWorldRotation() {
		return worldRotation;
	}
	
	public void setLocalRotation(Quaternionf rotation) {
		if (localRotation.equals(rotation)) return;
		
		localRotation.set(rotation);
		
		recalculateWorldRotation();
	}

	public Vector3f getLocalScale() {
		return localScale;
	}

	public Vector3f getWorldScale() {
		return worldScale;
	}
	
	public void setLocalScale(Vector3f scale) {
		if (localScale.equals(scale)) return;
		
		localScale.set(scale);
		
		recalculateWorldScale();
	}
	
	protected void addChild(Entity child) {
		children.add(child);
	}
	
	protected void removeChild(Entity child) {
		children.remove(child);
	}
	
	public void setParent(Entity parent) {
		if (parent == null) {
			matchLocalWorld();
			this.parent.removeChild(this);
			this.parent = parent;
		} else {
			this.parent = parent;
			this.parent.addChild(this);
			recalculateWorldPosition();
			recalculateWorldRotation();
			recalculateWorldScale();
		}
	}
	
	public Matrix4f getTransformationMatrix() {
		if (recalculateTransformation) {
			MathUtils.storeTransformationMatrix(worldPosition, worldRotation, worldScale, transformationMatrix);
			recalculateTransformation = false;
		}
		
		return transformationMatrix;
	}
}
