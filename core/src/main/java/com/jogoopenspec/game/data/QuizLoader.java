package com.jogoopenspec.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class QuizLoader {

    public static Map<String, QuizData> load() {
        FileHandle file = Gdx.files.internal("data/quizzes.json");
        if (!file.exists()) return new HashMap<>();

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        Map<String, QuizData> result = new HashMap<>();
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            QuizData quiz = new QuizData();
            quiz.question = entry.getString("question");
            JsonValue choicesArr = entry.get("choices");
            quiz.choices = new String[choicesArr.size];
            for (int i = 0; i < choicesArr.size; i++) {
                quiz.choices[i] = choicesArr.getString(i);
            }
            quiz.correct = entry.getInt("correct");
            result.put(entry.name, quiz);
        }
        return result;
    }
}
