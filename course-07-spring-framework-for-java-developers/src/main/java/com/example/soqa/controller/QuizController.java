package com.example.soqa.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.soqa.service.*;
import com.example.soqa.model.*;

import java.util.Map;
import java.util.List;
import java.lang.Void;
import java.lang.Integer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(produces = "application/json")
@RequiredArgsConstructor
public class QuizController {

	@Autowired
	private final QuizUserDetailsService us;

	@Autowired
	private final QuestionsService qs;

	// @GetMapping("/login") // Thymeleaf

	// @GetMapping("/register") // Thymeleaf

	// @GetMapping("/addQuiz") // Thymeleaf
	// @GetMapping("/editQuiz") // Thymeleaf


	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {

		us.register(user);

		return new ResponseEntity<>("User successfully created!", HttpStatus.CREATED);
	}

	@PostMapping("/quiz/addQuiz")
	public ResponseEntity<String> addQuiz(@RequestBody Question question) {
		qs.addQuiz(question);

		return new ResponseEntity<>("Question added successfully!", HttpStatus.CREATED);
	}


	@PutMapping("/quiz/editQuiz/{id}")
	public ResponseEntity<Void> editQuiz(@PathVariable int id, @RequestBody Question question) {

		qs.editQuiz(id, question);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/quiz/deleteQuiz/{id}")
	public ResponseEntity<Void> deleteQuiz(@PathVariable int id) {
		qs.deleteQuiz(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/listQuizzes")
	public ResponseEntity<List<Question>> getQuizQuestions() {
		return ResponseEntity.ok(qs.loadQuizzes());
	}

	@PostMapping("/submitAnswers")
	// @GetMapping("/results")
	public ResponseEntity<QuizResult> submit(@RequestBody Map<Integer, String> questionIdWithAnswers) {
		QuizResult result = qs.submitQuiz(questionIdWithAnswers);

		return ResponseEntity.ok(result);
	}
}