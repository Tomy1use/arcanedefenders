package com.gemserk.games.arcanedefenders.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.values.FloatValue;
import com.gemserk.componentsengine.properties.Property;

public class MovementComponent extends Component {

	private final Property<Vector2> velocity;

	private final Property<FloatValue> angularVelocity;

	public Vector2 getVelocity() {
		return velocity.get();
	}

	public float getAngularVelocity() {
		return angularVelocity.get().value;
	}

	public void setAngularVelocity(float value) {
		angularVelocity.get().value = value;
	}

	public MovementComponent(Property<Vector2> velocity, Property<FloatValue> angularVelocity) {
		this.velocity = velocity;
		this.angularVelocity = angularVelocity;
	}

}
