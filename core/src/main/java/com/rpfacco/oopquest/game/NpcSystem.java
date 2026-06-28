package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.NpcEntity;
import com.rpfacco.oopquest.game.data.QuizData;
import com.rpfacco.oopquest.game.data.QuizLoader;

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
            if (gameState.completedQuizzes.contains(npc.quizId)) continue;

            float triggerWidth = npc.width * 2.0f;
            float triggerHeight = npc.height * 2.0f;
            float triggerX = npc.x - (triggerWidth - npc.width) / 2f;
            float triggerY = npc.y - (triggerHeight - npc.height) / 2f;
            npcTriggerRect.set(triggerX, triggerY, triggerWidth, triggerHeight);

            if (playerRect.overlaps(npcTriggerRect)) {
                QuizData quiz = quizzes.get(npc.quizId);
                if (quiz != null) {
                    handler.onQuizTrigger(npc.quizId, quiz);
                } else {
                    Gdx.app.log("NpcSystem", "Quiz ID " + npc.quizId + " not found for NPC at (" + npc.x + ", " + npc.y + ")");
                }
                return;
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer, GameState gameState) {
        if (npcs == null) return;
        for (NpcEntity npc : npcs) {
            if (gameState.completedQuizzes.contains(npc.quizId)) {
                shapeRenderer.setColor(128f / 255, 128f / 255, 128f / 255, 1);
            } else {
                shapeRenderer.setColor(1, 165f / 255, 0, 1);
            }
            shapeRenderer.rect(npc.x, npc.y, npc.width, npc.height);
        }
    }
}
