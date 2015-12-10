package com.dasilveira.fruitsnatcher.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.dasilveira.fruitsnatcher.FruitSnatcher;
import com.dasilveira.fruitsnatcher.view.GameWorld;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class MenuScene extends ScreenAdapter {

    private Image title, title2, howToPlayImage;
    private Table table, options, rankTbl, howToTbl;
    private TextButton playButton, optionsButton, exitButton, backButton, rankBtn,
            backRank, howToBtn, backhowTo;
    private CheckBox muteCheckBox;
    private Slider volumeSlider;
    private Label soundTitle, volumTitle, rankTtl, scoreRank, tutoDia;
    private Drawable backG;
    private Stage stageMe;
    private OrthographicCamera camera;
    private Skin skin;
    private FruitSnatcher game;
    private SpriteBatch batch;


    public MenuScene(FruitSnatcher game) {
        this.game = game;
        backG = game.backGDraw;
        stageMe = new Stage(game.viewport, game.batch);
        camera = game.camera;
        skin = game.skin;

        batch = new SpriteBatch();

        title = new Image(game.atlas.findRegion("logoApple"));
        title.setPosition(600, -600);
        title2 = new Image(game.atlas.findRegion("logoSnatcher"));
        title2.setPosition(camera.viewportWidth / 2 - title2.getWidth() / 2,
                camera.viewportHeight - 100 - title2.getHeight());

        table = new Table();
        playButton = new TextButton("PLAY GAME", skin);
        playButton.pad(10).setColor(Color.CORAL);
        table.add(playButton).pad(10).colspan(3).row();
        howToBtn = new TextButton("HOW TO PLAY", skin);
        howToBtn.setColor(Color.CORAL);
        table.add(howToBtn).padBottom(10).colspan(3).row();

        rankBtn = new TextButton("HIGHSCORE", skin);
        rankBtn.setColor(Color.CORAL);
        table.add(rankBtn).padBottom(10);
        optionsButton = new TextButton("SOUND", skin);
        optionsButton.setColor(Color.CORAL);
        table.add(optionsButton).padBottom(10).colspan(2).row();

        exitButton = new TextButton("EXIT GAME", skin);
        exitButton.setColor(Color.CORAL);
        table.add(exitButton).colspan(3);
        table.setPosition(300, -600);

        options = new Table();
        soundTitle = new Label("SOUND AND SAFE", skin);
        volumTitle = new Label("VOLUME", skin);
        soundTitle.setColor(Color.GOLD);
        options.add(soundTitle).pad(30).colspan(2).row();
        options.add(volumTitle).pad(10);
        volumeSlider = new Slider(0, 2, 0.2f, false, skin);
        options.add(volumeSlider).pad(10).growX().row();
        muteCheckBox = new CheckBox(" MUTE ALL", skin);
        muteCheckBox.getImageCell().size(19);
        options.add(muteCheckBox).padBottom(10).colspan(2).row();
        backButton = new TextButton("BACK", skin);
        options.add(backButton).colspan(2).pad(6);
        options.setBackground(backG);
        options.pack();
        options.setPosition(400, -600);
        muteCheckBox.setChecked(!game.soundEnabled);
        volumeSlider.setValue(game.soundVolume);

        rankTbl = new Table();
        rankTtl = new Label("TOP 6", skin);
        rankTtl.setColor(Color.GOLD);
        backRank = new TextButton("BACK", skin);
        rankTbl.add(rankTtl).padBottom(10).colspan(2).row();
        showScore();
        rankTbl.add(backRank).colspan(2).pad(6);
        rankTbl.setBackground(backG);
        rankTbl.pack();
        rankTbl.setPosition(400, -600);

        howToTbl = new Table();

        tutoDia = new Label("Make sure to snatch only fruits. \n the grown ones are better ", skin);
        howToPlayImage = new Image(game.atlas.findRegion("howToPlay"));
        backhowTo = new TextButton("BACK", skin);
        howToTbl.add(howToPlayImage).pad(5).row();
        howToTbl.add(tutoDia).pad(2).row();
        howToTbl.add(backhowTo).colspan(5).pad(6);
        howToTbl.setBackground(backG);
        howToTbl.pack();
        howToTbl.setPosition(400, -600);

        stageMe.addActor(title);
        stageMe.addActor(title2);
        stageMe.addActor(table);
        stageMe.addActor(options);
        stageMe.addActor(rankTbl);
        stageMe.addActor(howToTbl);
    }


    private void showScore() {
        for (int i = 1; i <= 6; i++) {
            scoreRank = new Label(game.prefs.getInteger("scorP" + i, 0) + "- "
                    + game.prefs.getString("scorN" + i, "RBS"), skin);
            rankTbl.add(scoreRank).pad(0, 39, 16, 39).row();
        }
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setInputProcessor(stageMe);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stageMe.addAction(sequence(fadeOut(0.7f, Interpolation.bounceIn), run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameWorld(game));
                    }
                })));
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu(options, table);
            }
        });

        rankBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu(rankTbl, table);
            }
        });

        howToBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu(howToTbl, table);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundVolume = volumeSlider.getValue();
            }
        });

        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundEnabled = !muteCheckBox.isChecked();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu(table, options);
                game.prefs.putFloat("sound vol", game.soundVolume)
                        .putBoolean("sound stat", game.soundEnabled).flush();
            }
        });

        backRank.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu(table, rankTbl);
            }
        });

        backhowTo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu(table, howToTbl);
            }
        });

        stageMe.addAction(sequence(alpha(0), fadeIn(0.3f)));
        title.addAction(moveTo(camera.viewportWidth / 3 - title.getWidth() / 2,
                camera.viewportHeight - 12 - title.getHeight(), 5, Interpolation.elasticOut));
        title2.addAction(sequence(alpha(0), delay(1), fadeIn(2.5f)));

        showMenu(table, options);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.disableBlending();
        batch.draw(game.bkTexture, 0, 0);
        batch.enableBlending();
        for (Sprite sprite : game.treeSprites) {
            sprite.draw(batch);
        }
        batch.end();

        stageMe.act(delta);
        stageMe.draw();
    }

    private void showMenu(Table tab1, Table tab2) {
        tab1.addAction(moveTo(camera.viewportWidth / 2 - tab1.getWidth() / 2,
                tab1.getHeight() < camera.viewportHeight / 2 ? camera.viewportHeight / 3 :
                        camera.viewportHeight / 2 - tab1.getHeight() / 2, 1.5f, Interpolation.swing));
        tab2.addAction(moveTo(camera.viewportWidth / 2, -600, 1, Interpolation.swingIn));
    }

    @Override
    public void dispose() {
        stageMe.dispose();
        super.dispose();
    }
}
