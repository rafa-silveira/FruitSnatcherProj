package com.dasilveira.fruitsnatcher.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.dasilveira.fruitsnatcher.FruitSnatcher;
import com.dasilveira.fruitsnatcher.screen.BaseScene;
import com.dasilveira.fruitsnatcher.screen.WorldController;
import com.dasilveira.fruitsnatcher.screen.WorldRenderer;

public class GameWorld extends BaseScene {
    private FruitSnatcher game;
    private Vector3 touchPos;

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    public GameWorld(FruitSnatcher game) {
        super(game);
        this.game = game;
        touchPos = new Vector3();
        loadItems();

        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        worldController.setTouchedArea(new Vector3(-300.0f, -300.0f, -300.0f));

        gameState = GameState.ACTION;

    }

    private void loadItems() {
        game.principalInput = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPos.set(screenX, screenY, 0);
                game.camera.unproject(touchPos);
                worldController.setTouchedArea(touchPos);

                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                worldController.setTouchedArea(new Vector3(-300.0f, -300.0f, -300.0f));

                return true;
            }
        };

        Gdx.input.setInputProcessor(game.principalInput);
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRenderer.drawScene(delta);
        super.render(delta);

        if (gameState == GameState.PAUSE || gameState == GameState.GAME_OVER)
            return;

        worldController.render(delta);
        if (worldController.getLivesLeft() == 0) {
            gameState = GameState.GAME_OVER;
            checkStoreScore(worldController.getScore());
        }
    }

    public void dispose() {
        worldRenderer.dispose();
        super.dispose();
    }

    @Override
    protected void handleBackPress() {
        if (gameState == GameState.ACTION) {
            pause();
            return;
        }
        if (gameState == GameState.PAUSE) {
            resume();
        }
    }

}