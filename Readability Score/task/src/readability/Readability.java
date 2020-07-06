package readability;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Readability {
    private final String content;
    private final String[] sentences;
    private final String[][] words;

    private final int wordCount;
    private final int sentenceCount;
    private final int charCount;
    private final int syllableCount;
    private final int polysyllableCount;

    public Readability(String content) {
        this.content = content;
        this.sentences = splitToSentences();
        this.words = splitToWords();
        this.wordCount = countWords();
        this.sentenceCount = countSentences();
        this.charCount = countCharacters();
        this.syllableCount = countTotalSyllables();
        this.polysyllableCount = countTotalPolysyllables();
    }

    private String[] splitToSentences() {
        return content.split("[.?!]\\s+");
    }

    private String[][] splitToWords() {
        String[][] words = new String[sentences.length][];
        for (int i = 0; i < sentences.length; i++) {
            words[i] = sentences[i].split(",\\s|\\.\\s|\\s");
        }
        return words;
    }

    private int countSentences() {
        return sentences.length;
    }

    private int countWords() {
        int wordCount = 0;
        for (String[] word : splitToWords()) {
            for (int j = 0; j < word.length; j++) {
                wordCount++;
            }
        }
        return wordCount;
    }

    private int countSyllablesInWord(String word) {
        Pattern wordPattern = Pattern.compile("[aeiouyAEIOUY][^aeiouyAEIOUY\\s]|[aiouyAIOUY]$");
        Matcher matcher = wordPattern.matcher(word);
        return (int) matcher.results().count();
    }

    private int countTotalSyllables() {
        int syllables = 0;
        for (String[] words : splitToWords()) {
            for (String word : words) {
                int wordSyllables = countSyllablesInWord(word);
                if (wordSyllables > 0) {
                    syllables += wordSyllables;
                } else {
                    syllables++;
                }
            }
        }
        return syllables;
    }

    private int countTotalPolysyllables() {
        int polysyllables = 0;
        for (String[] words : splitToWords()) {
            for (String word : words) {
                if (countSyllablesInWord(word) >= 3) {
                    polysyllables++;
                }
            }
        }
        return polysyllables;
    }

    private int countCharacters() {
        int characterCount = 0;
        for (int i = 0; i < content.length(); i++) {
            if (!String.valueOf(content.charAt(i)).matches("\\s+")) {
                characterCount++;
            }
        }
        return characterCount;
    }

    private double calculateAri() {
        double score = 4.71 * countCharacters() / countWords() + 0.5 * countWords() / countSentences() - 21.43;
        return (double) Math.round(score * 100) / 100;
    }

    private double calculateFK() {
        double score = 0.39 * wordCount / sentenceCount + 11.8 * syllableCount / wordCount - 15.59;
        return (double) Math.round(score * 100) / 100;
    }

    private double calculateSMOG() {
        double score = 1.043 * Math.sqrt((double) polysyllableCount * 30 / sentenceCount) + 3.1291;
        return (double) Math.round(score * 100) / 100;
    }

    private double calculateCL() {
        double score = 0.0588 * charCount / wordCount * 100 - 0.296 * sentenceCount / wordCount * 100 - 15.8;
        return (double) Math.round(score * 100) / 100;
    }

    private String getAge(double formulaScore) {
        double score = Math.round(formulaScore);
        if (score >= 14) {
            return "25";
        } else if (score >= 13) {
            return "24";
        } else if (score >= 12) {
            return "18";
        } else if (score >= 11) {
            return "17";
        } else if (score >= 10) {
            return "16";
        } else if (score >= 9) {
            return "15";
        } else if (score >= 8) {
            return "14";
        } else if (score >= 7) {
            return "13";
        } else if (score >= 6) {
            return "12";
        } else if (score >= 5) {
            return "11";
        } else if (score >= 4) {
            return "10";
        } else if (score >= 3) {
            return "9";
        } else if (score >= 2) {
            return "7";
        } else if (score >= 1) {
            return "6";
        }
        return "0";
    }

    private int getIntAge(double score) {
        return Integer.parseInt(getAge(score));
    }

    private double calculateAverageAge() {
        return (getIntAge(calculateAri()) + getIntAge(calculateFK()) + getIntAge(calculateSMOG()) + getIntAge(calculateCL())) / 4.0;
    }

    public void print() {
        System.out.println("The text is:");
        System.out.println(content);
        System.out.println();
        System.out.println("Words: " + wordCount);
        System.out.println("Sentences: " + sentenceCount);
        System.out.println("Characters: " + charCount);
        System.out.println("Syllables: " + syllableCount);
        System.out.println("Polysyllables: " + polysyllableCount);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String input = new Scanner(System.in).next();
        System.out.println();
        switch (input) {
            case "ARI":
                System.out.println("Automated Readability Index: " + calculateAri());
                break;
            case "FK":
                System.out.println("Flesch–Kincaid readability tests: " + calculateFK());
                break;
            case "SMOG":
                System.out.println("Simple Measure of Gobbledygook: " + calculateSMOG());
                break;
            case "CL":
                System.out.println("Coleman–Liau index: " + calculateCL());
                break;
            case "all":
                System.out.println("Automated Readability Index: " + calculateAri() + " (about " + getAge(calculateAri()) + " year olds).");
                System.out.println("Flesch–Kincaid readability tests: " + calculateFK() + " (about " + getAge(calculateFK()) + " year olds).");
                System.out.println("Simple Measure of Gobbledygook: " + calculateSMOG() + " (about " + getAge(calculateSMOG()) + " year olds).");
                System.out.println("Coleman–Liau index: " + calculateCL() + " (about " + getAge(calculateCL()) + " year olds).");
                System.out.println();
                System.out.println("This text should be understood in average by " + calculateAverageAge() + " year olds.");
        }
    }
}
