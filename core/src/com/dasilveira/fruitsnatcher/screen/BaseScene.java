package com.dasilveira.fruitsnatcher.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.dasilveira.fruitsnatcher.FruitSnatcher;
import com.dasilveira.fruitsnatcher.view.GameWorld;

public abstract class BaseScene extends ScreenAdapter {
    protected FruitSnatcher game;
    protected Stage stage;
    protected Skin skin;
    protected Dialog pauseDia, saveScoreDia, gameOverDia;
    protected GameState gameState;
    protected Music music;
    protected String scoredName;
    protected TextField writeField;
    private int finalScore, lowScore;
    private boolean keyHandled;

    public BaseScene(FruitSnatcher veganSnatcher) {
        game = veganSnatcher;
        stage = new Stage(game.viewport, game.batch);
        skin = game.skin;

        music = game.music;
        music.setVolume(game.soundVolume);
        music.setLooping(true);

        writeField = new TextField("RBS", skin);
        scoredName = "RBS";

        keyHandled = false;
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        pauseDia = new Dialog("Pause", skin) {
            {
                getTitleLabel().setAlignment(Align.center);
                button("Continue", "cont");
                button("Menu", "menu");
                button("Exit", "exit");
            }

            @Override
            protected void result(Object object) {
                if (object == "menu") {
                    game.setScreen(new MenuScene(game));
                } else if (object == "exit") {
                    Gdx.app.exit();
                } else {
                    resume();
                }
            }
        };

        saveScoreDia = new Dialog("Save Score", skin) {
            {
                getTitleLabel().setAlignment(Align.center);
                getContentTable().add(writeField).pad(10).row();
                button("Save", "save");
            }

            @Override
            protected void result(Object object) {
                if (object == "save") {
                    scoredName = writeField.getText().length() > 2 ?
                            writeField.getText().substring(0, 3) : writeField.getText();
                    saveScoreDia.remove();
                    upHighScore();
                    gameOverDia.show(stage);
                }
            }
        };

        gameOverDia = new Dialog("Game Over", skin) {
            {
                getTitleLabel().setAlignment(Align.center);
                button("Try Again", "try");
                button("Menu", "menu");
                button("Exit", "exit");
            }

            @Override
            protected void result(Object object) {
                if (object == "menu") {
                    saveScoreDia.remove();
                    game.setScreen(new MenuScene(game));
                } else if (object == "exit") {
                    Gdx.app.exit();
                } else {
                    saveScoreDia.remove();
                    game.setScreen(new GameWorld(game));
                }
            }
        };

    }

    protected void checkStoreScore(int finalScore) {
        this.finalScore = finalScore;
        lowScore = game.prefs.getInteger("scorP6");
        if (finalScore > lowScore) {
            saveScoreDia.show(stage);
            Gdx.input.setInputProcessor(stage);
        } else {
            gameOverDia.show(stage);
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void upHighScore() {
        String finalName;

        int[] scores = new int[6];
        String[] names = new String[6];
        for (int i = 1; i <= 6; i++) {
            scores[i - 1] = game.prefs.getInteger("scorP" + i);
            names[i - 1] = game.prefs.getString("scorN" + i);
        }
        scores[5] = finalScore;
        names[5] = scoredName;
        for (int i = 5; i > 0; i--) {
            if (scores[i] > scores[i - 1]) {
                finalScore = scores[i - 1];
                finalName = names[i - 1];
                scores[i - 1] = scores[i];
                names[i - 1] = names[i];
                scores[i] = finalScore;
                names[i] = finalName;
            } else {
                break;
            }
        }
        for (int i = 1; i <= 6; i++) {
            game.prefs.putInteger("scorP" + i, scores[i - 1]).flush();
            game.prefs.putString("scorN" + i, names[i - 1]).flush();
        }

    }

    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (keyHandled) {
                return;
            }
            handleBackPress();
            keyHandled = true;
        } else {
            keyHandled = false;
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        music.stop();
    }

    @Override
    public void show() {
        music.stop();
        if (game.soundEnabled)
            music.play();
        else
            game.soundVolume = 0f;

    }

    public void pause() {
        gameState = GameState.PAUSE;
        pauseDia.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    public void resume() {
        gameState = GameState.ACTION;
        pauseDia.remove();
        Gdx.input.setInputProcessor(game.principalInput);
    }

    @Override
    public void dispose() {
        stage.dispose();
        game.dispose();
        super.dispose();
    }

    protected abstract void handleBackPress();

    protected enum GameState {
        ACTION, PAUSE, GAME_OVER
    }
}