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

    public void initbody(World world, float startPosX, float startPosY, float radius, float density) {
        cannonBallPosition = new Vector2();
        cannonBallBodyDef = new BodyDef();
        cannonBallBodyDef.type = cannonBallBodyType;
        cannonBallBody = world.createBody(cannonBallBodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        cannonBallBody.createFixture(circleShape, density);
        cannonBallBody.setTransform(startPosX, startPosY,0);

        circleShape.dispose();
    }

    Vector2 getPosition() {
        return cannonBallPosition;
    }

    public void setPosition(Vector2 newPos) {
        cannonBallPosition = newPos;
    }
}
