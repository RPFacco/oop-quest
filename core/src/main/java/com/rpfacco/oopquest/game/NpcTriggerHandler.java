package com.rpfacco.oopquest.game;

import com.rpfacco.oopquest.game.data.model.QuizData;

@FunctionalInterface
public interface NpcTriggerHandler {
    void onQuizTrigger(String quizId, QuizData quiz);
}
