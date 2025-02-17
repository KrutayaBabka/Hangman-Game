package code;

import java.util.List;
import java.util.Random;

public final class Game {
    private String wordToGuess;
    private StringBuilder currentState;
    private int attemptsLeft;
    private StringBuilder usedLetters;
    private final List<String> words;

    public Game(List<String> words) {
        this.words = words;
        setNewWord();
    }

    public void setNewWord() {
        this.wordToGuess = getRandomWord();
        this.currentState = new StringBuilder("_".repeat(wordToGuess.length()));
        this.attemptsLeft = 6;
        this.usedLetters = new StringBuilder();
    }

    private String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public String guessLetter(String input) {
        if (input.length() == 1) {
            char letter = input.charAt(0);

            if (usedLetters.indexOf(String.valueOf(letter)) != -1) {
                return "You have already guessed that letter.";
            }

            usedLetters.append(letter);
            boolean found = false;

            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    currentState.setCharAt(i, letter);
                    found = true;
                }
            }

            if (!found) {
                attemptsLeft--;
                return "Wrong guess!";
            }
            return "Good guess!";
        } else {
            if (input.equalsIgnoreCase(wordToGuess)) {
                currentState = new StringBuilder(wordToGuess);
                return "Congratulations! You've guessed the word.";
            } else {
                attemptsLeft--;
                return "Wrong guess!";
            }
        }
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0 || currentState.toString().equals(wordToGuess);
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public String getCurrentState() {
        return currentState.toString();
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    // Метод для получения текущего состояния слова (с _ для скрытых букв)
public String getMaskedWord() {
    return currentState.toString();
}

// Метод для получения использованных букв
    public String getUsedLetters() {
        return usedLetters.toString();
    }

}
