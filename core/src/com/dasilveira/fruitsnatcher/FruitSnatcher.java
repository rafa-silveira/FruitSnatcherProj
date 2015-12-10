package com.dasilveira.fruitsnatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasilveira.fruitsnatcher.loads.LoadScreen;

/**
 * Created by Rafael Silveira on 11/12/2015.
 */

public class FruitSnatcher extends Game {
    private static final int SCREEN_WIDTH;
    private static final int SCREEN_HEIGHT;

    static {
        SCREEN_WIDTH = 800;
        SCREEN_HEIGHT = 480;
    }

    public AssetManager manager;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BitmapFont font;
    public Skin skin;
    public Pixmap pixmap;
    public Drawable backGDraw;
    public TextureAtlas atlas;
    public ParticleEffect smoke;
    public ParticleEffectPool smokePool;
    public Array<ParticleEffectPool.PooledEffect> effects;
    public ShapeRenderer shape;

    public Viewport viewport;
    public float soundVolume;
    public boolean soundEnabled;
    public Music music;
    public Sound popSound, deathSound;
    public Preferences prefs;
    public TextureRegion[] goodTexture, badTexture;
    public TextureRegion bkTexture, coveredTexture, livesTexture;
    public InputAdapter principalInput;
    public Sprite[] treeSprites;

    @Override
    public void create() {
        manager = new AssetManager();
        batch = new SpriteBatch();
        prefs = Gdx.app.getPreferences("fruitPrefs");
        camera = new OrthographicCamera();
        camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        effects = new Array<ParticleEffectPool.PooledEffect>();
        goodTexture = new TextureRegion[5];
        badTexture = new TextureRegion[5];

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.CORAL);
        pixmap.fill();
        backGDraw = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        treeSprites = new Sprite[170];

        soundVolume = prefs.getFloat("sound vol", 1.0f);
        soundEnabled = prefs.getBoolean("sound stat", true);
        shape = new ShapeRenderer();

// atualizar sem o logger

        this.setScreen(new LoadScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        font.dispose();
        skin.dispose();
        pixmap.dispose();
        atlas.dispose();
        smoke.dispose();
        smokePool.clear();
        effects.clear();
        shape.dispose();
        music.dispose();
        popSound.dispose();
        deathSound.dispose();
        super.dispose();
    }
}

