package com.dasilveira.fruitsnatcher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class FoodHandler {
    private static FoodHandler handler = new FoodHandler();

    private Array<Food> foodList;
    private Pool<Food> foodPool;
    private Vector2 position;
    private Food.FoodState state;

    private FoodHandler() {
        foodList = new Array<Food>();
        foodPool = new Pool<Food>(3, 10) {
            @Override
            protected Food newObject() {
                return new Food(position, state);
            }
        };
    }

    public static FoodHandler getInstance() {
        return handler;
    }

    public Array<Food> getList() {
        return foodList;
    }

    public void setList(Array<Food> foodList) {
        this.foodList = foodList;
    }

    public void addFood(Vector2 position, Food.FoodState state) {
        this.position = position;
        this.state = state;
        Food food = foodPool.obtain();
        food.getBounds().x = position.x;
        food.getBounds().y = position.y;
        food.setState(state);
        foodList.add(food);
    }

    public void letItGrow(float delta) {
        for (int i = 0; i < foodList.size; i++) {
            Food food = foodList.get(i);
            if (food.getBounds().width < 1.7f * Gdx.graphics.getPpcX()) {
                food.setBounds(new Rectangle(food.getBounds().x, food.getBounds().y,
                        food.getBounds().width + delta, food.getBounds().height + delta));
            } else if (food.getBounds().y > 0 && food.isCouldFall()) {
                food.setBounds(new Rectangle(food.getBounds().x, food.getBounds().y - delta * 12.0f,
                        food.getBounds().width, food.getBounds().height));
            } else {
                foodList.removeValue(food, true);
                foodPool.free(food);
                i--;
            }
        }
    }

    public void clearList() {
        foodList.clear();
    }

}
