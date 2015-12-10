package com.dasilveira.fruitsnatcher.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.dasilveira.fruitsnatcher.FruitSnatcher;
import com.dasilveira.fruitsnatcher.model.Food;
import com.dasilveira.fruitsnatcher.model.FoodHandler;

public class WorldController {
    TextureRegion coveredTexture;
    TextureRegion[] goodTexture, badTexture;
    Array<ParticleEffectPool.PooledEffect> effects;
    ShapeRenderer shape;
    TextureRegion livesTexture;
    BitmapFont font;
    Sprite[] treeSprites;
    OrthographicCamera camera;
    TextureRegion bkTexture;
    boolean flash;
    FoodHandler foodHandler;
    private int score, livesLeft;
    private FruitSnatcher game;
    private Vector3 touchedArea;
    private float timerGood, timerBad, timerCovered;
    private Array<Food> foodTempList;

    public WorldController(FruitSnatcher game) {
        this.game = game;
        camera = game.camera;
        foodHandler = FoodHandler.getInstance();
        foodHandler.clearList();
        touchedArea = new Vector3();
        foodTempList = new Array<Food>();
        livesLeft = 3;

        loadItems();
    }

    private void loadItems() {
        bkTexture = game.bkTexture;
        treeSprites = game.treeSprites;
        font = game.font;
        livesTexture = game.livesTexture;
        shape = game.shape;
        effects = game.effects;
        goodTexture = game.goodTexture;
        badTexture = game.badTexture;
        coveredTexture = game.coveredTexture;
    }

    public void render(float delta) {
        updateScene(delta);
    }

    private void updateScene(float delta) {
        timerGood += delta;
        timerBad += delta;
        timerCovered += delta;

        if (timerGood > 1.0f) {
            foodHandler.addFood(new Vector2((int) (Math.random() * camera.viewportWidth),
                    (int) (Math.random() * camera.viewportHeight)), Food.FoodState.GOOD);
            timerGood = 0.0f;
        }
        if (timerBad > 2.0f) {
            foodHandler.addFood(new Vector2((int) (Math.random() * camera.viewportWidth),
                    (int) (Math.random() * camera.viewportHeight)), Food.FoodState.BAD);
            timerBad = 0.0f;
        }
        if (timerCovered > 3.5f) {
            foodHandler.addFood(new Vector2((int) (Math.random() * camera.viewportWidth),
                    (int) (Math.random() * camera.viewportHeight)), Food.FoodState.COVERED);
            timerCovered = 0.0f;
        }

        foodHandler.letItGrow(delta * 30);

        for (Food food : foodHandler.getList()) {
            if (food.getState() == Food.FoodState.COVERED)
                if (food.getBounds().width > 1.0f * Gdx.graphics.getPpcX()) {
                    int rand = (int) (Math.random() * 2);
                    if (rand % 2 == 0)
                        food.setState(Food.FoodState.GOOD_UNCOVERED);
                    else
                        food.setState(Food.FoodState.BAD);
                }
        }

        foodTempList = foodHandler.getList();
        for (int i = 0; i < foodTempList.size; i++) {
            Food tempFood = foodTempList.get(i);
            if (touchedArea.x >= tempFood.getBounds().x &&
                    touchedArea.x <= tempFood.getBounds().x + tempFood.getBounds().width &&
                    touchedArea.y >= tempFood.getBounds().y &&
                    touchedArea.y <= tempFood.getBounds().y + tempFood.getBounds().height) {

                ParticleEffectPool.PooledEffect smokeTemp = game.smokePool.obtain();

                switch (tempFood.getState()) {
                    case GOOD:
                        smokeTemp.setPosition(tempFood.getBounds().x + 25f, tempFood.getBounds().y);
                        game.effects.add(smokeTemp);
                        smokeTemp.reset();
                        game.popSound.play();

                        if (tempFood.getBounds().width > 0.9f * Gdx.graphics.getPpcX())
                            score += 3;
                        else
                            score += 1;
                        foodTempList.removeValue(tempFood, true);
                        i--;
                        break;

                    case BAD:
                        smokeTemp.setPosition(tempFood.getBounds().x + 25f, tempFood.getBounds().y);
                        game.effects.add(smokeTemp);
                        smokeTemp.reset();
                        game.deathSound.play(0.5f);
                        flash = true;

                        livesLeft--;
                        Gdx.input.vibrate(300);
                        foodTempList.removeValue(tempFood, true);
                        i--;
                        break;

                    case GOOD_UNCOVERED:
                        smokeTemp.setPosition(tempFood.getBounds().x + 25f, tempFood.getBounds().y);
                        game.effects.add(smokeTemp);
                        smokeTemp.reset();
                        game.popSound.play();

                        score += 9;
                        foodTempList.removeValue(tempFood, true);
                        i--;
                        break;

                    default:
                        break;
                }
            }
        }
        foodHandler.setList(foodTempList);
    }

    public void setTouchedArea(Vector3 touchedArea) {
        this.touchedArea = touchedArea;
    }


    public int getScore() {
        return score;
    }

    public int getLivesLeft() {
        return livesLeft;
    }
}

