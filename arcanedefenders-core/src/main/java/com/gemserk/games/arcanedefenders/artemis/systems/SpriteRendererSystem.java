package com.gemserk.games.arcanedefenders.artemis.systems;

import java.util.Comparator;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.gemserk.games.arcanedefenders.artemis.components.SpriteComponent;

public class SpriteRendererSystem extends EntitySystem {

	private final class LayerComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity o1, Entity o2) {
			SpriteComponent spriteComponent1 = o1.getComponent(SpriteComponent.class);
			SpriteComponent spriteComponent2 = o2.getComponent(SpriteComponent.class);
			return spriteComponent1.getLayer() - spriteComponent2.getLayer();
		}
	}

	LayerComparator layerComparator = new LayerComparator();

	private final SpriteBatch spriteBatch;

	@SuppressWarnings("unchecked")
	public SpriteRendererSystem(SpriteBatch spriteBatch) {
		super(SpriteComponent.class);
		this.spriteBatch = spriteBatch;
	}

	Array<Entity> orderedByLayerEntities = new Array<Entity>();

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

		orderedByLayerEntities.clear();

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			orderedByLayerEntities.add(entity);
		}

		orderedByLayerEntities.sort(layerComparator);

		for (int i = 0; i < orderedByLayerEntities.size; i++) {
			Entity entity = orderedByLayerEntities.get(i);
			SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
			Sprite sprite = spriteComponent.getSprite();
			sprite.draw(spriteBatch);
		}

	}

	@Override
	public void initialize() {

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}