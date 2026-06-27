package com.jogoopenspec.game;

import com.badlogic.gdx.Game;

public class JogoOpenSpec extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}