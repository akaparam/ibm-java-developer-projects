package com.example.soqa.model;

import java.util.List;
import lombok.*;


@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

	int id;

	String questionText;

	List<String> options;

	String correctAnswer;
}