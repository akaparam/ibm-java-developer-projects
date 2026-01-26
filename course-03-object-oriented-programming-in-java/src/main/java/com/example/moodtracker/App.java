package com.example.moodtracker;

/**
 * Hello world!
 *
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import com.example.moodtracker.Mood;

public class App 
{

    private static final Scanner sc = new Scanner(System.in);

    private static List<Mood> moods = new ArrayList<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        while(true) {
    
            availableCommands();
    
            System.out.print("Enter input: ");
            String input = sc.nextLine().trim();
    
            System.out.println("--------------------");
            switch(input) {
            case "1":
                addMood();
                break;
            case "2":
                deleteMood();
                break;
            case "3":
                editMood();
                break;
            case "4":
                searchMood();
                break;
            case "5":
                getAllMoods();
                break;
            case "6":
                export();
                break;
            case "99":
            case "exit":
                System.out.println("Exiting! BYE!");
                System.exit(0);
                break;
            default:
                System.out.println("Wrong input! Please be mindful.");
                break;
            }
        }
    }


    static void export() {
        
        try {
            Files.writeString(Path.of("moodtracker.txt"), moods.toString());
        } catch (IOException ex) {
            System.out.println("Error while exporting!");
        }

        System.out.println("Successfully! Exported to moodtracker.txt");
    }
    static void getAllMoods() {
        System.out.println(moods.toString());
    }

    static void searchMood() {
        System.out.print("Enter the search term to search moods: ");
        String searchTerm = sc.nextLine();

        boolean found = false;
        for (Mood m : moods) {
            if (m.name.toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println(m);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Nothing was found!");
        }
    }
    static void editMood() {
        System.out.print("Enter the name of mood to add notes to: ");

        String name = sc.nextLine();

        for (int i = 0; i < moods.size(); i++) {
            if (name.equals(moods.get(i).name)) {
                System.out.print("Mood found! Enter the note: ");
                String notes = sc.nextLine();

                moods.get(i).setNotes(notes);

                System.out.println("Mood updated successfully!");
            }
        }
    }
    static void deleteMood() {
        System.out.println("1. Delete moods by date.");
        System.out.println("2. Delete mood by name.");
        System.out.print("Enter your choice: ");
        String input = sc.nextLine();

        if (input.trim().equals("1")) {
            System.out.println("enter date to delete all moods [YYYY-MM-DD]: ");
            LocalDate date = LocalDate.parse(sc.nextLine(), DATE_FORMATTER);

            for (int i = 0; i < moods.size(); i++) {
                if (moods.get(i).date.compareTo(date) == 0) {
                    moods.remove(i);
                }
            }
        }

        else if (input.trim().equals("2")) {
            System.out.println("Enter name to delete mood: ");
            String name = sc.nextLine();

            for (int i = 0; i < moods.size(); i++) {
                if (moods.get(i).name.equals(name)) {
                    moods.remove(i);
                }
            }
        }

        else {
            System.out.println("Invalid input");
        }
    }
    static void addMood() {
        System.out.print("Add name for the mood: ");
        String name = sc.nextLine();

        System.out.print("Date [YYYY-MM-DD] (enter to skip): ");
        String _date = sc.nextLine();

        System.out.print("Time [HH:mm] (enter to skip): ");
        String _time = sc.nextLine();

        LocalDate date = _date.isBlank() ? LocalDate.now() : LocalDate.parse(_date, DATE_FORMATTER);
        LocalTime time = _time.isBlank() ? LocalTime.now().withSecond(0).withNano(0) : LocalTime.parse(_time, TIME_FORMATTER);

        try {

            validateNewMood(date, time);
        } catch (InvalidMoodException ex) {
            return;
        }

        Mood mood = new Mood(name, date, time);

        moods.add(mood);
        System.out.println("Mood added successfully!");

    }

    static void validateNewMood(LocalDate date, LocalTime time) {

        for (int i = 0; i < moods.size(); i++) {
            if (moods.get(i).date.compareTo(date) == 0 && moods.get(i).time.compareTo(time) == 0) {
                throw new InvalidMoodException("Mood at date & time mentiond already exists");
            }
        }
    }
    static void availableCommands() {
        System.out.println("1. Add a mood");
        System.out.println("2. Delete a mood");
        System.out.println("3. Edit a specific mood");
        System.out.println("4. Search moods");
        System.out.println("5. Get all moods");
        System.out.println("6. Export");
        System.out.println("99. exit");
    }
}
