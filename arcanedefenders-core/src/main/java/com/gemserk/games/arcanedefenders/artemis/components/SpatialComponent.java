package com.gemserk.games.arcanedefenders.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.values.FloatValue;
import com.gemserk.componentsengine.properties.Property;

public class SpatialComponent extends Component {
	
	private final Property<Vector2> position;
	
	private final Property<Vector2> size;

	private final Property<FloatValue> angle;
	
	public Vector2 getPosition() {
		return position.get();
	}
	
	public Vector2 getSize() {
		return size.get();
	}
	
	public float getAngle() {
		return angle.get().value;
	}
	
	public void setAngle(float angle) {
		this.angle.get().value = angle;
	}
	
	public SpatialComponent(Property<Vector2> position, Property<Vector2> size, Property<FloatValue> angle) {
		this.position = position;
		this.size = size;
		this.angle = angle;
	}

}
