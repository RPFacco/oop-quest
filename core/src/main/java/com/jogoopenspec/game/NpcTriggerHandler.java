package com.jogoopenspec.game;

import com.jogoopenspec.game.data.QuizData;

@FunctionalInterface
public interface NpcTriggerHandler {
    void onQuizTrigger(String quizId, QuizData quiz);
}
