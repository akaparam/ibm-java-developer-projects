package com.example.soqa.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.example.soqa.model.Question;
import com.example.soqa.model.QuizResult;

import com.example.soqa.service.QuestionsService;
import org.springframework.stereotype.Service;



@Service
public class QuestionsServiceImpl implements QuestionsService {

    private static List<Question> questions = new ArrayList<>();

    @Override
    public List<Question> loadQuizzes() {

        return questions;
    }

    @Override
    public void addQuiz(Question question) {
        
        questions.add(question);

    }

    @Override
    public void editQuiz(int id, Question question) {
        
        for (Question q : questions) {
            if (q.getId() == id) {
                q.setQuestionText(question.getQuestionText());
                q.setOptions(question.getOptions());
                q.setCorrectAnswer(question.getCorrectAnswer());
            }
        }
    }

    @Override
    public void deleteQuiz(int id) {
        Question markForDelete = new Question();

        boolean questionNotFound = true;
        for (Question q : questions) {
            if (q.getId() == id) {
                markForDelete = q;
                questionNotFound = false;
                break;
            }
        }

        if (questionNotFound) {
            return;
        }
        else {
            questions.remove(markForDelete);
        }
    }

    @Override
    public QuizResult submitQuiz(Map<Integer, String> questionIdWithAnswers) {

        int correctAnswersTillNow = 0;

        for (Map.Entry<Integer, String> entry : questionIdWithAnswers.entrySet()) {
            Integer key = entry.getKey();
            boolean questionNotFound = true;
            for (Question q : questions) {
                if (q.getId() == key) {
                    if (q.getCorrectAnswer().equals(entry.getValue().toLowerCase().trim())) {
                        correctAnswersTillNow++;
                    }
                }
                questionNotFound = false;
            }
            if (questionNotFound) {
                System.out.println("Question with id " + key + " not found... Moving to next");
            }
        }

        return new QuizResult(questions.size(), correctAnswersTillNow);
    }
}
