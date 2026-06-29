package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.NpcEntity;
import com.rpfacco.oopquest.game.data.model.QuizData;
import com.rpfacco.oopquest.game.data.loader.QuizLoader;

import java.util.Map;

public class NpcSystem {

    private Array<NpcEntity> npcs;
    private Rectangle npcTriggerRect;
    private Map<String, QuizData> quizzes;

    public NpcSystem() {
        npcTriggerRect = new Rectangle();
        quizzes = QuizLoader.load();
    }

    public void setNpcs(Array<NpcEntity> npcs) {
        this.npcs = npcs;
    }

    public void checkProximity(Rectangle playerRect, GameState gameState, NpcTriggerHandler handler) {
        if (npcs == null) return;

        for (NpcEntity npc : npcs) {
            if (gameState.isCompleted(npc.getQuizId())) continue;

            float triggerWidth = npc.getWidth() * 2.0f;
            float triggerHeight = npc.getHeight() * 2.0f;
            float triggerX = npc.getX() - (triggerWidth - npc.getWidth()) / 2f;
            float triggerY = npc.getY() - (triggerHeight - npc.getHeight()) / 2f;
            npcTriggerRect.set(triggerX, triggerY, triggerWidth, triggerHeight);

            if (playerRect.overlaps(npcTriggerRect)) {
                QuizData quiz = quizzes.get(npc.getQuizId());
                if (quiz != null) {
                    handler.onQuizTrigger(npc.getQuizId(), quiz);
                } else {
                    Gdx.app.log("NpcSystem", "Quiz ID " + npc.getQuizId() + " not found for NPC at (" + npc.getX() + ", " + npc.getY() + ")");
                }
                return;
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer, GameState gameState) {
        if (npcs == null) return;
        for (NpcEntity npc : npcs) {
            if (gameState.isCompleted(npc.getQuizId())) {
                shapeRenderer.setColor(128f / 255, 128f / 255, 128f / 255, 1);
            } else {
                shapeRenderer.setColor(1, 165f / 255, 0, 1);
            }
            shapeRenderer.rect(npc.getX(), npc.getY(), npc.getWidth(), npc.getHeight());
        }
    }
}
