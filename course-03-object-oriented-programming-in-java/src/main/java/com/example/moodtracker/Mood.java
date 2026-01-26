package com.example.moodtracker;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Mood {
	String name;

	LocalDate date;

	LocalTime time;

	String notes;

	public Mood(String name)
	{
		this.name = name;
		this.time = LocalTime.MIDNIGHT;
	}
	public Mood(String name, LocalDate date) {
		this.date = date;
		this(name);
	}
	public Mood(String name, LocalDate date, LocalTime time) {
		this(name, date);
		this.time = time;
	}
	public Mood(String name, String notes) {
		this(name);
		this.notes = notes;
	}
	public Mood(String name, LocalDate date, String notes) {
		this(name, date);
		this.notes = notes;
	}
	public Mood(String name, LocalDate date, LocalTime time, String notes) {
		this(name, date, time);
		this.notes = notes;
	}
}