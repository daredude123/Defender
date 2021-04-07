package com.andynordevelop.defender;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall {
    Body cannonBallBody;
    BodyDef cannonBallBodyDef;
    final BodyDef.BodyType cannonBallBodyType = BodyDef.BodyType.DynamicBody;
    Vector2 cannonBallPosition;
    World world;
    float radius;
    private Vector2 collisionDirection;

    public void initbody(World world, float startPosX, float startPosY, float radius, float density) {
        this.world = world;
        cannonBallPosition = new Vector2(startPosX, startPosY);
        cannonBallBodyDef = new BodyDef();
        cannonBallBodyDef.type = cannonBallBodyType;
        cannonBallBody = world.createBody(cannonBallBodyDef);
        this.radius = radius;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        cannonBallBody.createFixture(circleShape, density);
        cannonBallBody.setTransform(startPosX, startPosY, 0);
        cannonBallPosition = cannonBallBody.getPosition();
        circleShape.dispose();
    }

    public void destroyCannonBall() {
        world.destroyBody(cannonBallBody);
    }

    Vector2 getPosition() {
        return cannonBallPosition;
    }

    public void setPosition(Vector2 newPos) {
        cannonBallPosition = newPos;
    }

    public Body getCannonBallBody() {
        return cannonBallBody;
    }

    public void setCannonBallBody(Body cannonBallBody) {
        this.cannonBallBody = cannonBallBody;
    }

    public BodyDef getCannonBallBodyDef() {
        return cannonBallBodyDef;
    }

    public void setCannonBallBodyDef(BodyDef cannonBallBodyDef) {
        this.cannonBallBodyDef = cannonBallBodyDef;
    }

    public BodyDef.BodyType getCannonBallBodyType() {
        return cannonBallBodyType;
    }

    public Vector2 getCannonBallPosition() {
        return cannonBallPosition;
    }

    public void setCannonBallPosition(Vector2 cannonBallPosition) {
        this.cannonBallPosition = cannonBallPosition;
    }

    public void shoot(float forcex, float forcey, float x, float y, boolean b) {
        cannonBallBody.applyForce(forcex ,forcey ,x , y, b);
    }

    public void setCollisionDirection(Vector2 vecDirection) {
        this.collisionDirection = vecDirection;                
    }

    public Vector2 getCollisionDirection() {
        return collisionDirection;
    }
}
