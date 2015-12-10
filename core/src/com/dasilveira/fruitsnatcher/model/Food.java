package com.dasilveira.fruitsnatcher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Food implements Pool.Poolable {
    private static final float SIZE = .0f;

    private FoodState state;
    private int textureIndex;
    private Rectangle bounds;
    private boolean couldFall;

    public Food(Vector2 position, FoodState state) {
        textureIndex = -1;

        bounds = new Rectangle();
        bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = (SIZE * Gdx.graphics.getPpcX());
        bounds.height = (SIZE * Gdx.graphics.getPpcY());

        this.state = state;
        couldFall = MathUtils.randomBoolean();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public FoodState getState() {
        return state;
    }

    public void setState(FoodState state) {
        this.state = state;
    }

    public int getIndex() {
        return textureIndex;
    }

    public void setIndex(int index) {
        this.textureIndex = index;
    }

    @Override
    public void reset() {
        textureIndex = -1;

        bounds.width = (SIZE * Gdx.graphics.getPpcX());
        bounds.height = (SIZE * Gdx.graphics.getPpcY());
    }

    public boolean isCouldFall() {
        return couldFall;
    }

    public enum FoodState {
        GOOD,
        BAD,
        COVERED,
        GOOD_UNCOVERED,
    }

}
