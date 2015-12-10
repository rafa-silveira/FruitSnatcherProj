package com.dasilveira.fruitsnatcher.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.dasilveira.fruitsnatcher.model.Food;

public class WorldRenderer implements Disposable {
    private WorldController controller;
    private float flashDuration;
    private int textureIndex;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont fontScore;

    public WorldRenderer(WorldController worldController) {
        this.controller = worldController;
        batch = new SpriteBatch();
        camera = controller.camera;
        fontScore = controller.font;
        fontScore.setColor(Color.CORAL);
        fontScore.getData().setScale(1.2f);
    }

    public void drawScene(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.disableBlending();
        batch.draw(controller.bkTexture, 0, 0);
        batch.enableBlending();

        for (Sprite sprite : controller.treeSprites) {
            sprite.draw(batch);
        }

        fontScore.draw(batch, String.valueOf(controller.getScore()), 10.0f, camera.viewportHeight - 10.0f);

        for (int i = controller.getLivesLeft(); i > 0; i--) {
            batch.draw(controller.livesTexture, camera.viewportWidth / (1 + (i / 10.0f)),
                    camera.viewportHeight / 1.1f, 0.7f * Gdx.graphics.getPpcX(), 0.7f * Gdx.graphics.getPpcY());
        }
        drawFood();

        if (controller.flash) {
            controller.shape.begin(ShapeRenderer.ShapeType.Filled);
            controller.shape.setColor(Color.WHITE);
            controller.shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            controller.shape.end();
            flashDuration += delta;
            if (flashDuration > 0.10f) {
                controller.flash = false;
            }
        }

        for (ParticleEffectPool.PooledEffect effectTemp : controller.effects) {
            effectTemp.setEmittersCleanUpBlendFunction(false);
            effectTemp.draw(batch, delta);
            if (effectTemp.isComplete()) {
                effectTemp.free();
                controller.effects.removeValue(effectTemp, true);
            }
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.end();
    }

    private void drawFood() {
        for (Food f : controller.foodHandler.getList()) {
            if (f.getIndex() == -1) {
                textureIndex = (int) (Math.random() * controller.goodTexture.length);
                f.setIndex(textureIndex);

            }
            switch (f.getState()) {
                case GOOD:
                case GOOD_UNCOVERED:
                    batch.draw(controller.goodTexture[f.getIndex()],
                            f.getBounds().x, f.getBounds().y,
                            f.getBounds().width, f.getBounds().height
                    );
                    break;

                case BAD:
                    batch.draw(controller.badTexture[f.getIndex()],
                            f.getBounds().x, f.getBounds().y,
                            f.getBounds().width, f.getBounds().height
                    );
                    break;

                case COVERED:
                    batch.draw(controller.coveredTexture,
                            f.getBounds().x, f.getBounds().y,
                            f.getBounds().width, f.getBounds().height
                    );
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
