package com.andynordevelop.defender;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy {
    Body enemyBody;
    BodyDef enemyBodyDef;
    BodyDef.BodyType enemyBodyType = BodyDef.BodyType.DynamicBody;
    Vector2 enemyPosition;
    World world;
    float radius;

    public void initBody(World world, float startX, float startY, float radius, float density) {
        this.world = world;
        this.radius = radius;
        enemyPosition = new Vector2(startX, startY);
        enemyBodyDef = new BodyDef();
        enemyBodyDef.type = enemyBodyType;
        enemyBody = world.createBody(enemyBodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        enemyBody.createFixture(circleShape, density);
        enemyBody.setTransform(startX, startY, 0);
        enemyPosition = enemyBody.getPosition();
        circleShape.dispose();

    }

    public void destroyEnemy() {
        world.destroyBody(enemyBody);

    }

    public Body getEnemyBody() {
        return enemyBody;
    }

    public void setEnemyBody(Body enemyBody) {
        this.enemyBody = enemyBody;
    }

    public BodyDef getEnemyBodyDef() {
        return enemyBodyDef;
    }

    public void setEnemyBodyDef(BodyDef enemyBodyDef) {
        this.enemyBodyDef = enemyBodyDef;
    }

    public BodyDef.BodyType getEnemyBodyType() {
        return enemyBodyType;
    }

    public void setEnemyBodyType(BodyDef.BodyType enemyBodyType) {
        this.enemyBodyType = enemyBodyType;
    }

    public Vector2 getEnemyPosition() {
        return enemyPosition;
    }

    public void setEnemyPosition(Vector2 enemyPosition) {
        this.enemyPosition = enemyPosition;
    }
}
