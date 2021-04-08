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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

public class Defender extends ApplicationAdapter {
	World world;
	OrthographicCamera camera;
	Box2DDebugRenderer debugRenderer;
	Player player;
	ArrayList<Enemy> enemyList;
	ArrayList<CannonBall> cannonBallListToRemove;
	ArrayList<Enemy> enemyListToRemove;
	int worldWidth = 50;
	int worldHeight = 25;
	ArrayList<CannonBall> cannonBallList;
	ArrayList<Shrapnel> shrapnelListToRemove;
	ArrayList<Shrapnel> shrapnelList;
	private float timer;

	@Override
	public void create () {
		world = new World(new Vector2(0, -10), true);
		camera = new OrthographicCamera(worldWidth, worldHeight);
		//vi har denne på for øyeblikket.
		debugRenderer = new Box2DDebugRenderer();
		enemyList = new ArrayList<>();
		cannonBallListToRemove = new ArrayList<>();
		cannonBallList = new ArrayList<>();
		enemyListToRemove = new ArrayList<>();
		shrapnelList = new ArrayList<>();
		shrapnelListToRemove = new ArrayList<>();
		timer = 0;
		initPlayer();
		spawnEnemy();

		// ground
		createEdge(BodyDef.BodyType.StaticBody, -25, -12.5f, 25, -12.5f, 5);
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
				cannonBallList.add(cannonBall);
				return true;
			}

			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				startTime = System.nanoTime();
				return true;
			}
		});
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.125f, .125f, .125f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		timer += 1*Gdx.graphics.getDeltaTime();
		debugRenderer.render(world, camera.combined);
		world.step(1 / 60f, 10, 2);

		for (CannonBall x : cannonBallList) {
			if (x.cannonBallBody.getPosition().y + x.radius > worldHeight || x.cannonBallBody.getPosition().y - x.radius < -worldHeight) {
				cannonBallListToRemove.add(x);
			} else if (x.cannonBallBody.getPosition().x + x.radius > worldWidth || x.cannonBallBody.getPosition().x - x.radius < -worldHeight) {
				cannonBallListToRemove.add(x);
			}
			for (Enemy enemy : enemyList) {
				if (checkForCannonBallHit(x, enemy)) {
					spawnFragments(x.getCollisionDirection(), enemy.getEnemyBody().getPosition(),0.1f,0.1f );
					enemyListToRemove.add(enemy);
				}
			}
		}

		for (Shrapnel x : shrapnelList) {
			if (x.checkAliveTime() / 1000 > 3) {
				shrapnelListToRemove.add(x);
			} else if (x.getShrapnelBody().getPosition().y > worldHeight || x.getShrapnelBody().getPosition().y < -worldHeight) {
				shrapnelListToRemove.add(x);
			} else if (x.getShrapnelBody().getPosition().x > worldWidth || x.getShrapnelBody().getPosition().x < -worldWidth) {
				shrapnelListToRemove.add(x);
			}
		}

		if (timer >= 1) {
			spawnEnemy();
			timer = 0;
		}

		moveEnemies();
		cleanCannonBalls();
		cleanEnemies();
		cleanShrapnels();
	}

	private void cleanShrapnels() {
		for (Shrapnel x : shrapnelListToRemove) {
			world.destroyBody(x.shrapnelBody);
			shrapnelList.remove(x);
		}
		shrapnelListToRemove.clear();
	}

	private void spawnEnemy() {
		Enemy enemy = new Enemy();
		enemy.initBody(world, worldWidth/2, getRandomSpawnPosition(),0.4f,5);
		enemyList.add(enemy);
	}

	private int getRandomSpawnPosition() {
		int rand = new Random().nextInt(25 + 25) - 25;
		return rand;
	}

	private void initPlayer() {
		Vector2 position = new Vector2(-23f, 0f);
		player = new Player(position);
	}

	//todo: fortsett med denne, ikke helt ferdig 4 april 22:44
	private void spawnFragments(Vector2 collisionDirection, Vector2 position, float halfWidth, float halfHeight) {
		float startposX = position.x - (halfWidth);
		float startposY = position.y - (halfHeight);
		float tmp = startposX;

		for (int i = 0; i < 3; i++) {
			for (int y = 0; y < 3; y++) {
				Shrapnel shrapnel = new Shrapnel();
				shrapnel.initbody(world, tmp, startposY, halfWidth * 2, halfHeight * 2, 0.1f);
				shrapnel.getShrapnelBody().applyForce(collisionDirection.scl(.2f), shrapnel.getShrapnelBody().getPosition(),true);
				shrapnelList.add(shrapnel);
				tmp += halfWidth*2;
			}
			tmp = startposX;
			startposY -= halfHeight*2;
		}
	}

	private boolean checkForCannonBallHit(CannonBall x, Enemy enemy) {
		float xD = x.getCannonBallBody().getPosition().x - enemy.getEnemyBody().getPosition().x;      // delta x
		float yD = x.getCannonBallBody().getPosition().y - enemy.getEnemyBody().getPosition().y;      // delta y
		float sqDist = xD * xD + yD * yD;  // square distance
		boolean collision = sqDist <= (x.radius+enemy.radius) * (x.radius+enemy.radius);
		if (collision) {
			Vector2 vecDirection = enemy.getEnemyBody().getPosition().sub(x.getCannonBallBody().getPosition()).nor();
			x.setCollisionDirection(vecDirection);
		}
		return collision;
	}

	private void cleanEnemies() {
		for (Enemy x : enemyListToRemove) {
			world.destroyBody(x.enemyBody);
			enemyList.remove(x);
		}
		enemyListToRemove.clear();
	}

	private void cleanCannonBalls() {
		for (CannonBall x : cannonBallListToRemove) {
			world.destroyBody(x.cannonBallBody);
			cannonBallList.remove(x);
		}
		cannonBallListToRemove.clear();
	}

	private void moveEnemies() {
		for (Enemy x : enemyList) {
			x.getEnemyBody().setLinearVelocity(-5, 0);
			if (x.getEnemyPosition().x - x.radius < (-worldHeight)) {
				enemyListToRemove.add(x);
			}
		}
	}

	@Override
	public void dispose () {
		world.dispose();
		debugRenderer.dispose();
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

	private Vector2 getMousePosInGameWorld(float x, float y) {
		Vector3 vec3 = camera.unproject(new Vector3(x, y,0));
		return new Vector2(vec3.x,vec3.y);
	}
}
