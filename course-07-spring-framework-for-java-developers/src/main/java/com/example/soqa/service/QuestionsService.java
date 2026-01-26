package com.example.soqa.service;


import java.util.List;
import com.example.soqa.model.Question;
import com.example.soqa.model.QuizResult;
import java.util.Map;
import java.lang.Integer;

public interface QuestionsService {

	List<Question> loadQuizzes();

	void addQuiz(Question question);

	void editQuiz(int id, Question question);

	void deleteQuiz(int id);

	QuizResult submitQuiz(Map<Integer, String> questionIdWithAnswers);

}