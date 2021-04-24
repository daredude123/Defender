package com.andynordevelop.defender;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall {
    Sprite sprite;
    Texture texture;
    Body cannonBallBody;
    BodyDef cannonBallBodyDef;
    final BodyDef.BodyType cannonBallBodyType = BodyDef.BodyType.DynamicBody;
    World world;
    float radius;
    private Vector2 collisionDirection;

    public void initbody(World world, float startPosX, float startPosY, float radius, float density) {
        this.world = world;
        cannonBallBodyDef = new BodyDef();
        cannonBallBodyDef.type = cannonBallBodyType;
        cannonBallBody = world.createBody(cannonBallBodyDef);
        this.radius = radius;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        cannonBallBody.createFixture(circleShape, density).setRestitution(0.5f);
        cannonBallBody.setTransform(startPosX, startPosY, 0);
        circleShape.dispose();

        texture = new Texture("cannonball.png");
        sprite = new Sprite(texture);
        sprite.setPosition(cannonBallBody.getPosition().x-1f,cannonBallBody.getPosition().y-10f);
        sprite.setSize(1.5f,1.5f);
//        sprite.setRotation(20f);
    }
    public void update() {
        sprite.setPosition(getPosition().x-0.75f,getPosition().y-0.75f);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.rotate(cannonBallBody.getAngle() * MathUtils.radiansToDegrees);
    }

    public void destroyCannonBall() {
        world.destroyBody(cannonBallBody);
    }

    Vector2 getPosition() {
        return cannonBallBody.getPosition();
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

    public void shoot(float forcex, float forcey, float x, float y, boolean b) {
        cannonBallBody.applyForce(forcex ,forcey ,x , y, b);
    }

    public void setCollisionDirection(Vector2 vecDirection) {
        this.collisionDirection = vecDirection;                
    }

    public Vector2 getCollisionDirection() {
        return collisionDirection;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
