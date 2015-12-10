package com.dasilveira.fruitsnatcher.loads;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.dasilveira.fruitsnatcher.FruitSnatcher;
import com.dasilveira.fruitsnatcher.screen.MenuScene;

public class LoadScreen extends ScreenAdapter {

    private Pixmap pixmap, pixmapTree, pixmapTree2;
    private Texture backG, backG2, backG3, logo;
    private AssetManager manager;
    private FruitSnatcher game;
    private float percent, bgCounter, bgCounter2;
    private Sprite[] treeSprites;
    private Sprite spr;

    public LoadScreen(FruitSnatcher game) {
        game.manager.load("background/RafBoSlogo.png", Texture.class);
        game.manager.finishLoading();

        this.game = game;
        manager = game.manager;

        logo = game.manager.get("background/RafBoSlogo.png", Texture.class);

        pixmap = game.pixmap;
        pixmap.setColor(Color.CHARTREUSE);
        pixmap.fill();
        backG = new Texture(pixmap);
        pixmap.setColor(Color.GOLD);
        pixmap.fill();
        backG2 = new Texture(pixmap);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        backG3 = new Texture(pixmap);

        initTreePixmaps();
        manager.load("models/models.pack", TextureAtlas.class);
        manager.load("fonts/storybook.fnt", BitmapFont.class);
        manager.load("ui/uiskin.json", Skin.class);
        manager.load("ui/uiskin.atlas", TextureAtlas.class);
        manager.load("sounds/somnambulism.mp3", Music.class);
        manager.load("sounds/cork.ogg", Sound.class);
        manager.load("sounds/mutantDeath.ogg", Sound.class);
        manager.load("effects/splash.p", ParticleEffect.class);
        manager.finishLoading();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.isKeyPressed(Input.Keys.BACK);

        if (game.manager.update()) {
            game.atlas = manager.get("models/models.pack", TextureAtlas.class);
            game.font = manager.get("fonts/storybook.fnt", BitmapFont.class);
            game.font.setColor(Color.GOLD);
            game.skin = new Skin(manager.get("ui/uiskin.atlas", TextureAtlas.class));
            game.skin = manager.get("ui/uiskin.json", Skin.class);
            game.music = manager.get("sounds/somnambulism.mp3", Music.class);
            game.popSound = manager.get("sounds/cork.ogg", Sound.class);
            game.deathSound = manager.get("sounds/mutantDeath.ogg", Sound.class);

            game.smoke = manager.get("effects/splash.p", ParticleEffect.class);
            game.smokePool = new ParticleEffectPool(game.smoke, 1, 30);

            for (int i = 0; i < 5; i++) {
                game.goodTexture[i] = game.atlas.findRegion("food" + i);
                game.badTexture[i] = game.atlas.findRegion("bfood" + i);
            }
            game.coveredTexture = game.atlas.findRegion("cfood");
            game.livesTexture = game.atlas.findRegion("lives");
            game.bkTexture = game.atlas.findRegion("background");

            if (Gdx.input.isTouched()) {
                game.setScreen(new MenuScene(game));
            }
        }

        percent = Interpolation.linear.apply(percent, game.manager.getProgress() * logo.getWidth()
                + 70, 0.03f);
        bgCounter = Interpolation.linear.apply(bgCounter, percent, 0.03f);
        bgCounter2 = Interpolation.linear.apply(bgCounter2, bgCounter, 0.03f);

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.disableBlending();
        game.batch.draw(backG, 0, 0, 800, percent);
        game.batch.draw(backG2, 0, 0, 800, bgCounter);
        game.batch.draw(backG3, 0, 0, 800, bgCounter2);
        game.batch.enableBlending();
        game.batch.draw(logo, 200, 200, 0, 0, (int) bgCounter2, logo.getHeight());
        if (percent / 4 <= 101)
            game.font.draw(game.batch, (int) percent / 4 + "%", 30, 30);
        game.batch.end();
    }

    private void initTreePixmaps() {
        treeSprites = new Sprite[30];
        int width = 16;
        int height = 16;
        float randomX, randomY;

        for (int i = 0; i < treeSprites.length; i++) {
            if (i < 20) {
                randomX = MathUtils.random(-20, Gdx.graphics.getWidth() - 60);
                randomY = MathUtils.random(-20, Gdx.graphics.getHeight() - 60);
            } else {
                randomX = MathUtils.randomTriangular(-20, Gdx.graphics.getWidth() - 60);
                randomY = MathUtils.randomTriangular(-20, Gdx.graphics.getHeight() - 60);
            }

            pixmapTree = createProceduralLeafs(width, height, randomX);
            pixmapTree.setBlending(Pixmap.Blending.None);
            spr = new Sprite(new Texture(pixmapTree));
            spr.setSize(30, 30);
            spr.setPosition(randomX, randomY);

            treeSprites[i] = spr;
        }
        game.treeSprites = this.treeSprites;
    }

    private Pixmap createProceduralLeafs(int width, int height, float randomX) {
        pixmapTree2 = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        if (MathUtils.randomBoolean())
            pixmapTree2.setColor(0, 0.7f, 0, 0.5f);
        else
            pixmapTree2.setColor(0.7f, 0.7f, 0f, 0.5f);

        pixmapTree2.fillCircle(width / 2, height / 2, 9);

        pixmapTree2.setColor(0, 0.05f, 0, 0.07f);
        if (randomX > Gdx.graphics.getWidth() / 2)
            pixmapTree2.drawLine(width, 0, 0, height);
        else
            pixmapTree2.drawLine(0, 0, width, height);

        return pixmapTree2;
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        backG.dispose();
        backG2.dispose();
        backG3.dispose();
        logo.dispose();
        manager.unload("background/RafBoSlogo.png");
        super.dispose();
    }
}
