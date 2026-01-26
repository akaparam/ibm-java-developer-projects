package com.example.petcare;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class App {

    private static final String DATA_FILE = "pets.dat";

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Pet> pets = new ArrayList<>();

    public static void main(String[] args) {

        loadData();

        while (true) {
            printMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerPet();
                    break;
                case "2":
                    scheduleAppointment();
                    break;
                case "3":
                    displayRecords();
                    break;
                case "4":
                    storeData();
                    break;
                case "5":
                    generateReports();
                    break;
                case "99":
                    storeData();
                    System.out.println("Exiting application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void registerPet() {

        System.out.print("Pet ID: ");
        String petId = scanner.nextLine();

        if (findPetById(petId) != null) {
            System.out.println("Pet ID already exists.");
            return;
        }

        System.out.print("Pet Name: ");
        String name = scanner.nextLine();

        System.out.print("Species/Breed: ");
        String species = scanner.nextLine();

        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Owner Name: ");
        String owner = scanner.nextLine();

        System.out.print("Contact Info: ");
        String contact = scanner.nextLine();

        Pet pet = new Pet(
                petId,
                name,
                species,
                age,
                owner,
                contact,
                LocalDate.now()
        );

        pets.add(pet);
        System.out.println("Pet registered successfully.");
    }

    private static void scheduleAppointment() {

        System.out.print("Enter Pet ID: ");
        String petId = scanner.nextLine();

        Pet pet = findPetById(petId);
        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }

        System.out.print("Appointment Type (vet/vaccination/grooming): ");
        String type = scanner.nextLine().toLowerCase();

        if (!List.of("vet", "vaccination", "grooming").contains(type)) {
            System.out.println("Invalid appointment type.");
            return;
        }

        System.out.print("Appointment Date-Time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine());

        if (dateTime.isBefore(LocalDateTime.now())) {
            System.out.println("Appointment must be in the future.");
            return;
        }

        System.out.print("Notes (optional): ");
        String notes = scanner.nextLine();

        Appointment appointment = new Appointment(type, dateTime, notes);
        pet.getAppointments().add(appointment);

        System.out.println("Appointment scheduled.");
    }

    private static void displayRecords() {

        System.out.println("""
            1. All registered pets
            2. Appointments for a pet
            3. Upcoming appointments
            4. Past appointments
            """);

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                pets.forEach(System.out::println);
                break;

            case "2":
                System.out.print("Enter Pet ID: ");
                Pet pet = findPetById(scanner.nextLine());
                if (pet != null) {
                    pet.getAppointments().forEach(System.out::println);
                }
                break;

            case "3":
                for (Pet p : pets) {
                    for (Appointment a : p.getAppointments()) {
                        if (a.getDateTime().isAfter(LocalDateTime.now())) {
                            System.out.println(p.getName() + " → " + a);
                        }
                    }
                }
                break;

            case "4":
                for (Pet p : pets) {
                    for (Appointment a : p.getAppointments()) {
                        if (a.getDateTime().isBefore(LocalDateTime.now())) {
                            System.out.println(p.getName() + " → " + a);
                        }
                    }
                }
                break;
        }
    }

    private static void generateReports() {

        System.out.println("1. Upcoming appointments (next 7 days)");
        System.out.println("2. Overdue vet visits");

        String choice = scanner.nextLine();

        switch (choice) {

            case "1":
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime weekLater = now.plusDays(7);

                for (Pet p : pets) {
                    for (Appointment a : p.getAppointments()) {
                        if (!a.getDateTime().isBefore(now)
                                && a.getDateTime().isBefore(weekLater)) {
                            System.out.println(p.getName() + " → " + a);
                        }
                    }
                }
                break;

            case "2":
                for (Pet p : pets) {
                    boolean overdue = true;

                    for (Appointment a : p.getAppointments()) {
                        if (a.getType().equalsIgnoreCase("vet")) {
                            long months = ChronoUnit.MONTHS.between(
                                    a.getDateTime().toLocalDate(),
                                    LocalDate.now()
                            );
                            if (months < 6) {
                                overdue = false;
                            }
                        }
                    }

                    if (overdue) {
                        System.out.println(p.getName() + " is overdue for vet visit.");
                    }
                }
                break;
        }
    }

    private static void storeData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(pets);
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    private static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            List<Pet> loadedPets = (List<Pet>) ois.readObject();
            pets.addAll(loadedPets);
            System.out.println("Data loaded.");
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }

    private static Pet findPetById(String petId) {
        for (Pet p : pets) {
            if (p.getPetId().equals(petId)) {
                return p;
            }
        }
        return null;
    }

    private static void printMenu() {
        System.out.println("""
            ===== PetCare Scheduler =====
            1. Register Pet
            2. Schedule Appointment
            3. Display Records
            4. Store Data
            5. Generate Reports
            99. Exit
            """);
    }
}
