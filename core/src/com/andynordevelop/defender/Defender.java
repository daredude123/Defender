package com.andynordevelop.defender;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Defender extends ApplicationAdapter {
	World world;
	OrthographicCamera camera;
	Box2DDebugRenderer debugRenderer;
	Player player;

	@Override
	public void create () {
		world = new World(new Vector2(0, -10), true);
		camera = new OrthographicCamera(50, 25);
		//vi har denne på for øyeblikket.
		debugRenderer = new Box2DDebugRenderer();
		initPlayer();
		spawnEnemies();

		// ground
		createEdge(BodyDef.BodyType.StaticBody, -23, -10f, 23, -10f, 5);
		createEdge(BodyDef.BodyType.StaticBody, -25,12.5f,25,12.5f,5);

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				return super.keyUp(keycode);
			}
		});

		Gdx.input.setInputProcessor(new InputAdapter() {
			long startTime = 0;

			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {


				//shoot a cannonball towards point
				CannonBall cannonBall = new CannonBall();
				cannonBall.initbody(world, -23f, 0f, 0.3f, 2);

//				Body body = createCircle(BodyDef.BodyType.DynamicBody, -23f, 0f, 0.3f, 2);

				long currentTime = System.nanoTime();
				long measuredTime = (currentTime - startTime)/10000000;

				Vector2 bodyposition = new Vector2(player.getPosition());

				Vector2 userTouch = getMousePosInGameWorld(x, y);

				float velx = userTouch.x - bodyposition.x;
				float vely = userTouch.y - bodyposition.y;

				if (y < bodyposition.y) {
					vely = 0- y;
				}

				float forcex = velx*measuredTime;
				float forcey = vely*measuredTime;

				cannonBall.shoot(forcex, forcey, player.position.x, player.position.y, true);
				return true;
			}

			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				startTime = System.nanoTime();
				return true;
			}
		});
	}

	private void spawnEnemies() {
		Enemy enemy = new Enemy();

	}

	private void initPlayer() {
		Vector2 position = new Vector2(-23f, 0f);
		player = new Player(position);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.125f, .125f, .125f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		debugRenderer.render(world, camera.combined);
		world.step(1 / 60f, 10, 2);
	}

	@Override
	public void dispose () {
		world.dispose();
		debugRenderer.dispose();
	}

	private Body createBox(BodyDef.BodyType type, float x, float y, float width, float height, float density) {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width, height);

		BodyDef def = new BodyDef();
		def.type = type;
		Body body = world.createBody(def);
		body.createFixture(poly, density);
		body.setTransform(x, y, 0);
		poly.dispose();

		return body;
	}

	private Body createEdge(BodyDef.BodyType type, float x1, float y1, float x2, float y2, float density) {
		EdgeShape poly = new EdgeShape();
		poly.set(new Vector2(0, 0), new Vector2(x2 - x1, y2 - y1));

		BodyDef def = new BodyDef();
		def.type = type;
		Body body = world.createBody(def);
		body.createFixture(poly, density);
		body.setTransform(x1, y1, 0);
		poly.dispose();

		return body;
	}

	private Body createCircle(BodyDef.BodyType type, float x, float y, float radius, float density) {
		CircleShape poly = new CircleShape();
		poly.setRadius(radius);

		BodyDef def = new BodyDef();
		def.type = type;
		Body body = world.createBody(def);
		body.createFixture(poly, density);
		body.setTransform(x, y, 0);
		poly.dispose();

		return body;
	}

	private Vector2 getMousePosInGameWorld(float x, float y) {
		Vector3 vec3 = camera.unproject(new Vector3(x, y,0));
		return new Vector2(vec3.x,vec3.y);
	}
}
