package org.searchengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static Scanner sc;

    public static void main(String[] args) throws FileNotFoundException {
        sc = new Scanner(System.in);
        Person person = new Person();

        // Read filename from command line
        String fileName = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {                    //created a flag at args[0]
                fileName = args[i + 1];                        // taking file input from user args[1]
            }
        }

        person.loadFromFile(fileName);                         // loading file to the person class
        person.buildIndex();                                   //generating index

        while (true) {
            //printMenu from method
            printMenu();
            int option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 1:
                    System.out.println("Select a matching strategy: ALL, ANY, NONE");
                    String strategy = sc.nextLine();
                    strategy = strategy.toUpperCase();

                    System.out.println("\nEnter a name or email to search all suitable people.");
                    String query = sc.nextLine();

                    // still prints for user
                    person.search(query, strategy);
                    break;

                case 2:
                    person.printAll();
                    break;

                case 0:
                    System.out.println("Bye!");
                    return;

                default:
                    System.out.println("Incorrect option! Try again.");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all persons");
        System.out.println("0. Exit");
    }
}


class Person {

    ArrayList<String> list = new ArrayList<>();
    Map<String, Set<Integer>> index = new HashMap<>();

    // Load file
    public void loadFromFile(String fileName) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(fileName));
        while (fileScanner.hasNextLine()) {
            list.add(fileScanner.nextLine());
        }
        fileScanner.close();
    }

    // Build inverted index
    public void buildIndex() {
        for (int i = 0; i < list.size(); i++) {
            String[] words = list.get(i).toLowerCase().split("\\s+");
            for (String word : words) {
                index.putIfAbsent(word, new HashSet<>());
                index.get(word).add(i);
            }
        }
    }

    // Console method (unchanged for users)
    public void search(String query, String strategy) {
        Set<String> result = searchResult(query, strategy);

        if (result.isEmpty()) {
            System.out.println("No matching people found.");
        } else {
            System.out.println(result.size() + " persons found:");
            for (String person : result) {
                System.out.println(person);
            }
        }
    }

    // ✅ Testable method (this is what JUnit uses)
    public Set<String> searchResult(String query, String strategy) {

        /**
         * query.toLowerCase().split("\\s+"); method splits the lowercase string into an array of substrings.
         * \\s+   :    regex character class that matches a single whitespace character, which includes spaces, tabs (\t), and newlines (\n).
         *    +   :    greedy quantifier that matches one or more occurrences of the preceding element,  one or more whitespace characters  */


        String[] words = query.toLowerCase().split("\\s+");
        Set<Integer> result;

        switch (strategy) {
            case "ALL":
                result = searchAll(words);
                break;
            case "ANY":
                result = searchAny(words);
                break;
            case "NONE":
                result = searchNone(words);
                break;
            default:
                return Set.of();
        }

        Set<String> output = new HashSet<>();
        for (int i : result) {
            output.add(list.get(i));
        }
        return output;
    }

    // ALL words must be present
    private Set<Integer> searchAll(String[] words) {
        Set<Integer> result = new HashSet<>(index.getOrDefault(words[0], Set.of()));
        for (int i = 1; i < words.length; i++) {
            result.retainAll(index.getOrDefault(words[i], Set.of()));
        }
        return result;
    }

    // At least one word present
    private Set<Integer> searchAny(String[] words) {
        Set<Integer> result = new HashSet<>();
        for (String word : words) {
            result.addAll(index.getOrDefault(word, Set.of()));
        }
        return result;
    }

    // No words present
    private Set<Integer> searchNone(String[] words) {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            result.add(i);
        }
        for (String word : words) {
            result.removeAll(index.getOrDefault(word, Set.of()));
        }
        return result;
    }

    public void printAll() {
        System.out.println("\n=== List of people ===");
        for (String person : list) {
            System.out.println(person);
        }
    }
}
