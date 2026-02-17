import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Thought {
    private int id;
    private String text;
    private String timeStamp;

    public Thought(int id, String text) {
        this.id = id;
        this.text = text;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.timeStamp = now.format(fmt);
    }

    public Thought(int id, String text, String timeStamp) {
        this.id = id;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getTimeStamp() { return timeStamp; }

    @Override
    public String toString() {
        return "ID: " + id + " | Thought: " + text +
               " | Added On: " + timeStamp;
    }
}

public class Main {

    private static final String FILE_NAME = "thoughts.txt";
    private static Queue<Thought> thoughtQueue = new ArrayDeque<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        loadFromFile();

        int choice;
        do {
            System.out.println("\n========= THOUGHTQUEUE =========");
            System.out.println("1. Add Thought");
            System.out.println("2. View All Thoughts");
            System.out.println("3. View Next Thought");
            System.out.println("4. Complete/Delete Next Thought");
            System.out.println("5. Save & Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> addThought(sc);
                    case 2 -> viewAllThoughts();
                    case 3 -> viewNextThought();
                    case 4 -> completeNextThought();
                    case 5 -> {
                        saveToFile();
                        System.out.println("Thoughts saved successfully. Exiting...");
                    }
                    default -> System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter numbers only.");
                choice = 0;
            }

        } while (choice != 5);

        sc.close();
    }

    private static void addThought(Scanner sc) {
        try {
            System.out.print("Enter Thought ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Thought Text: ");
            String text = sc.nextLine();

            if (text.trim().isEmpty()) {
                System.out.println("Thought cannot be empty!");
                return;
            }

            thoughtQueue.add(new Thought(id, text));
            System.out.println("Thought added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID! Please enter a number.");
        }
    }

    private static void viewAllThoughts() {
        if (thoughtQueue.isEmpty()) {
            System.out.println("No thoughts available.");
            return;
        }

        System.out.println("\n---- Pending Thoughts (FIFO Order) ----");
        int count = 1;
        for (Thought t : thoughtQueue) {
            System.out.println(count++ + ") " + t);
        }
    }

    private static void viewNextThought() {
        if (thoughtQueue.isEmpty()) {
            System.out.println("No thoughts available.");
            return;
        }
        System.out.println("Next Thought:");
        System.out.println(thoughtQueue.peek());
    }

    private static void completeNextThought() {
        if (thoughtQueue.isEmpty()) {
            System.out.println("No thoughts available.");
            return;
        }
        Thought removed = thoughtQueue.poll();
        System.out.println("Completed & Removed Thought:");
        System.out.println(removed);
    }

    private static void saveToFile() {
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Thought t : thoughtQueue) {
                bw.write(t.getId() + "|" +
                         t.getText() + "|" +
                         t.getTimeStamp());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error while saving.");
        }
    }

    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    thoughtQueue.add(
                            new Thought(
                                    Integer.parseInt(parts[0]),
                                    parts[1],
                                    parts[2]
                            )
                    );
                }
            }

        } catch (Exception ignored) {}
    }
}
