package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static final String[] types = {"dataType", "sortingType", "inputFile", "outputFile"};

    private  static String getArg(String[] args, String arg) {
        boolean isValid = false;
        for (String type : types) {
            if (arg.equals(type)) {
                isValid = true;
                break;
            }
        }

        if (!isValid)
            return "error";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-" + arg)) {
                try {
                    return args[i + 1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    return "error";
                }
            }
        }

        if(arg.equals("sortingType"))
            return "natural";
        else
            return null;
    }

    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (Objects.equals(getArg(args, "sortingType"), "error")) {
            System.out.println("No sorting type defined!");
            return;
        }

        if (Objects.equals(getArg(args, "dataType"), "error")) {
            System.out.println("No data type defined!");
            return;
        }

        int total = -1;
        int inputs = 0;
        boolean newWord = true;

        List<String> words = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        while (scanner.hasNext()) {
            newWord = true;
            total++;
            String line = scanner.nextLine();
            inputs++;

            lines.add(line);

            for (int i = 0; i < line.length(); i++) {
                if(line.charAt(i) != ' '){
                    if (newWord) {
                        words.add(line.charAt(i) + "");
                        newWord = false;
                    } else {
                        words.set(total, words.get(total) + line.charAt(i) + "");
                    }
                } else if (!newWord) {
                    newWord = true;
                    total++;
                }
            }
        }

        if (Objects.equals(getArg(args, "dataType"), "input.txt")) {
            try {
                Scanner scannerFile = new Scanner(new File("input.txt"));
                while (scannerFile.hasNext()) {
                    words.add(scannerFile.next());
                }
                scannerFile.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        switch (Objects.requireNonNull(getArg(args, "sortingType"))) {
            case "natural" -> {
                System.out.println("Total numbers: " + words.size() + ".");
                words.sort(Comparator.comparingInt(Integer::parseInt));
                System.out.print("Sorted data: ");
                for (String w : words) {
                    System.out.print(w + " ");
                }
            }
            case "byCount" -> {
                TreeMap<String, Integer> wordsMap = new TreeMap<>();
                for (String word : words) {
                    wordsMap.put(word, wordsMap.getOrDefault(word, 0) + 1);
                }

                if (Objects.equals(getArg(args, "dataType"), "word") || Objects.equals(getArg(args, "dataType"), "long"))
                    System.out.println("Total numbers: " + words.size() + ".");
                if (Objects.equals(getArg(args, "dataType"), "line")) {
                    System.out.println("Total lines: " + inputs);
                }

                if(getArg(args, "dataType") != null)
                    switch (getArg(args, "dataType")) {
                        case "long":
                            System.out.println(sort(words, "long"));
                            break;
                        case "word":
                            System.out.println(sort(words, "word"));
                            break;
                        case "line":
                            List<String> liensWithoutDuplicates = new ArrayList<>(new HashSet<>(lines));
                            Collections.sort(liensWithoutDuplicates);
                            for (String l : liensWithoutDuplicates) {
                                System.out.println(l);
                            }
                            break;
                    }

            }
            default -> {
            }
        }

        if (Objects.equals(getArg(args, "outputFile"), "out.txt")) {
            File file = new File("out.txt");
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter("out.txt");
                fileWriter.write(sort(words, "word"));
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static String sort(List<String> words, String type) {
        List<String> wordsWithoutDuplicates = new ArrayList<>(new HashSet<>(words));

        if (type.equals("long"))
            wordsWithoutDuplicates.sort(Comparator.comparingLong(Long::parseLong));
        else
            Collections.sort(wordsWithoutDuplicates);

        TreeMap<String, Integer> resultMap = new TreeMap<>();

        for (String word : wordsWithoutDuplicates) {
            for (String wordCount : words) {
                if(word.equals(wordCount))
                    resultMap.put(word, resultMap.getOrDefault(word, 0) + 1);
            }
        }

        for (int i = 0; i < wordsWithoutDuplicates.size(); i++) {
            if (resultMap.get(wordsWithoutDuplicates.get(i)) > resultMap.get(wordsWithoutDuplicates.get(wordsWithoutDuplicates.size()-1))) {
                wordsWithoutDuplicates.add(wordsWithoutDuplicates.get(i));
                wordsWithoutDuplicates.remove(i);
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < wordsWithoutDuplicates.size(); i++) {
            result.append(wordsWithoutDuplicates.get(i))
                    .append(": " + resultMap.get(wordsWithoutDuplicates.get(i)))
                    .append(" time(s), " + ((resultMap.get(wordsWithoutDuplicates.get(i)) * 100) / words.size()) + "%\n");
        }

        return result.toString();
    }
}
