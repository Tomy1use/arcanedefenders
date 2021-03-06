package com.gemserk.games.arcanedefenders;

import java.util.Random;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.entities.EntityFactory;
import com.gemserk.commons.artemis.systems.HierarchySystem;
import com.gemserk.commons.artemis.systems.MovementSystem;
import com.gemserk.commons.artemis.systems.SpriteRendererSystem;
import com.gemserk.commons.artemis.systems.SpriteUpdateSystem;
import com.gemserk.commons.artemis.systems.TextRendererSystem;
import com.gemserk.commons.gdx.ScreenAdapter;
import com.gemserk.componentsengine.properties.SimpleProperty;
import com.gemserk.games.arcanedefenders.artemis.entities.ArcaneDefendersEntityFactory;
import com.gemserk.games.arcanedefenders.artemis.entities.EntityTemplate;
import com.gemserk.games.arcanedefenders.artemis.systems.AllGameLogicSystem;
import com.gemserk.games.arcanedefenders.artemis.systems.SpawnerSystem;

public class GameScreen extends ScreenAdapter {

	private final Game game;

	private BitmapFont font;

	private com.artemis.World world;

	private TextRendererSystem textRendererSystem;

	private SpriteRendererSystem spriteRendererSystem;

	private MovementSystem movementSystem;

	private SpawnerSystem spawnerSystem;

	private AllGameLogicSystem allGameLogicSystem;

	private HierarchySystem hierarchySystem;

	private SpriteUpdateSystem spriteUpdateSystem;

	public GameScreen(Game game) {
		this.game = game;

		final Texture texture = new Texture(Gdx.files.internal("data/mage-512x512.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		final Texture rockTexture = new Texture(Gdx.files.internal("data/rock-512x512.png"));
		rockTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture fontTexture = new Texture(Gdx.files.internal("data/font.png"));
		fontTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Sprite fontSprite = new Sprite(fontTexture);
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), fontSprite, false);

		textRendererSystem = new TextRendererSystem();
		spriteRendererSystem = new SpriteRendererSystem();
		movementSystem = new MovementSystem();
		spawnerSystem = new SpawnerSystem();
		allGameLogicSystem = new AllGameLogicSystem();
		hierarchySystem = new HierarchySystem();
		spriteUpdateSystem = new SpriteUpdateSystem();

		world = new World();
		
		world.getSystemManager().setSystem(textRendererSystem);
		world.getSystemManager().setSystem(movementSystem);
		world.getSystemManager().setSystem(spawnerSystem);
		world.getSystemManager().setSystem(allGameLogicSystem);
		world.getSystemManager().setSystem(hierarchySystem);

		world.getSystemManager().setSystem(spriteUpdateSystem);
		world.getSystemManager().setSystem(spriteRendererSystem);
		
		world.getSystemManager().initializeAll();

		EntityFactory comonsEntityFactory = new EntityFactory(world);
		comonsEntityFactory.fpsEntity(new SimpleProperty<BitmapFont>(font));

		final ArcaneDefendersEntityFactory arcaneDefendersEntityFactory = new ArcaneDefendersEntityFactory(world);

		int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		int n = 3;

		int part = width / n;
		int midPart = part / 2;

		int xStart = part - midPart;

		final Random random = new Random();

		for (int i = 0; i < n; i++) {

			final int x = xStart;
			final int y = 74;
			
			// builds the defenders
			{
				Sprite sprite = new Sprite(texture);
				sprite.setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);

				final Vector2 position = new Vector2(x, y);
				Vector2 size = new Vector2(128, 128);
				ElementType type = ElementType.Paper;

				SimpleProperty<ElementType> elementType = new SimpleProperty<ElementType>(type);

				Entity defender = arcaneDefendersEntityFactory.defender(position, size, sprite, elementType);
				
				SimpleProperty<Entity> owner = new SimpleProperty<Entity>(defender);
				
				arcaneDefendersEntityFactory.typeEntity(position, elementType, owner, new SimpleProperty<BitmapFont>(font));
			}

			// build the falling elements spawners
			{
				arcaneDefendersEntityFactory.spawner(new EntityTemplate() {
					@Override
					public Entity build() {

						Sprite sprite = new Sprite(rockTexture);
						sprite.setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);

						Vector2 size = new Vector2(64, 64);

						ElementType type = randomElementType();

						Vector2 position = new Vector2(x, height + y);
						
						SimpleProperty<ElementType> elementType = new SimpleProperty<ElementType>(type);

						Entity fallingElement = arcaneDefendersEntityFactory.fallingElementEntity(position, size, sprite, new Vector2(0f, -50f), elementType);
						
						SimpleProperty<Entity> owner = new SimpleProperty<Entity>(fallingElement);
						
						arcaneDefendersEntityFactory.typeEntity(position, elementType, owner, new SimpleProperty<BitmapFont>(font));

						return super.build();
					}

					public ElementType randomElementType() {
						ElementType[] values = ElementType.values();
						return values[random.nextInt(values.length)];
					}

				});
			}

			xStart += part;

		}

	}

	@Override
	public void render(float delta) {

		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		world.loopStart();
		world.setDelta((int) (delta * 1000));

		spriteUpdateSystem.process();

		spriteRendererSystem.process();
		textRendererSystem.process();

		movementSystem.process();
		
		spawnerSystem.process();
		allGameLogicSystem.process();
		hierarchySystem.process();
		
	}

	@Override
	public void show() {

	}

	@Override
	public void dispose() {

	}

}