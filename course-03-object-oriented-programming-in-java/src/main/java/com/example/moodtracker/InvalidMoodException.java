package com.example.moodtracker;

import lombok.Getter;

@Getter
public class InvalidMoodException extends RuntimeException {

	String message;

	public InvalidMoodException(String message) {
		System.out.println(message);
	}
}