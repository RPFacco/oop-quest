package com.jogoopenspec.game;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JogoOpenSpec extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}