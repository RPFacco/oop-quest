package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InputHandler {

    private final Viewport viewport;
    private final Vector3 touchPos = new Vector3();

    public InputHandler(Viewport viewport) {
        this.viewport = viewport;
    }

    public Vector3 handleTouch() {
        if (!Gdx.input.justTouched()) return null;
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPos);
        return touchPos.cpy();
    }

    public boolean isEscPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    public boolean isEPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.E);
    }
}
