package com.andynordevelop.defender;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Shrapnel {

    long aliveTime;
    BodyDef shrapnelBodyDef;
    BodyDef.BodyType shrapnelBodyType = BodyDef.BodyType.DynamicBody;
    Body shrapnelBody;
    float width, height;
    World world;


    //REMEMBER: setasbox skal ha halv vidde og halv høyde
    public void initbody(World world, float startPosX, float startPosY, float width, float height, float density) {
        aliveTime = System.currentTimeMillis();
        this.world = world;
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);

        this.width = width;
        this.height = height;

        shrapnelBodyDef = new BodyDef();
        shrapnelBodyDef.type = shrapnelBodyType;
        shrapnelBody = world.createBody(shrapnelBodyDef);
        shrapnelBody.createFixture(poly, density);
        shrapnelBody.setTransform(startPosX, startPosY, 0);

        poly.dispose();

    }

    public long checkAliveTime() {
        return System.currentTimeMillis() - aliveTime;
    }

    public BodyDef getShrapnelBodyDef() {
        return shrapnelBodyDef;
    }

    public void setShrapnelBodyDef(BodyDef shrapnelBodyDef) {
        this.shrapnelBodyDef = shrapnelBodyDef;
    }

    public BodyDef.BodyType getShrapnelBodyType() {
        return shrapnelBodyType;
    }

    public void setShrapnelBodyType(BodyDef.BodyType shrapnelBodyType) {
        this.shrapnelBodyType = shrapnelBodyType;
    }

    public Body getShrapnelBody() {
        return shrapnelBody;
    }

    public void setShrapnelBody(Body shrapnelBody) {
        this.shrapnelBody = shrapnelBody;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
