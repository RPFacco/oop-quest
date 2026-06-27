package com.jogoopenspec.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class QuizLoader {

    private static Map<String, QuizData> cache;

    public static Map<String, QuizData> load() {
        if (cache != null) return cache;

        FileHandle file = Gdx.files.internal("data/quizzes.json");
        if (!file.exists()) {
            cache = new HashMap<>();
            return cache;
        }

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        cache = new HashMap<>();
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            QuizData quiz = new QuizData();
            quiz.question = entry.getString("question");
            JsonValue choicesArr = entry.get("choices");
            quiz.choices = new String[choicesArr.size];
            for (int i = 0; i < choicesArr.size; i++) {
                quiz.choices[i] = choicesArr.getString(i);
            }
            quiz.correct = entry.getInt("correct");
            cache.put(entry.name, quiz);
        }
        return cache;
    }
}
