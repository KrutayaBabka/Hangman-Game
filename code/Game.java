package code;

import java.util.List;
import java.util.Random;

public class Game {
    private String wordToGuess;  // Слово, которое нужно угадать
    private StringBuilder currentState;  // Текущее состояние слова (с подчеркиваниями для неугаданных букв)
    private int attemptsLeft;  // Количество оставшихся попыток
    private StringBuilder usedLetters;  // Хранит все использованные буквы
    private List<String> words;  // Список всех слов

    // Конструктор игры
    public Game(List<String> words) {
        this.words = words;  // Присваиваем список слов
        this.wordToGuess = getRandomWord();  // Получаем случайное слово
        this.currentState = new StringBuilder("_".repeat(wordToGuess.length()));  // Инициализируем состояние слова
        this.attemptsLeft = 6;  // Можно настроить количество попыток
        this.usedLetters = new StringBuilder();  // Инициализируем список использованных букв
    }

    // Метод для получения случайного слова из списка
    public String getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(words.size());  // Генерируем случайный индекс
        return words.get(index);  // Возвращаем слово по индексу
    }

    // Публичный метод для назначения нового случайного слова
    public void setNewWord() {
        this.wordToGuess = getRandomWord();  // Назначаем новое случайное слово
        this.currentState = new StringBuilder("_".repeat(wordToGuess.length()));  // Обновляем состояние слова
        this.attemptsLeft = 6;  // Сбрасываем количество попыток
        this.usedLetters = new StringBuilder();  // Сбрасываем список использованных букв
    }

    // Метод для угадывания буквы или целого слова
    public boolean guessLetter(String input) {
        if (input.length() == 1) {
            // Проверка, была ли буква уже использована
            char letter = input.charAt(0);
            if (usedLetters.indexOf(String.valueOf(letter)) != -1) {
                System.out.println("You have already guessed that letter.");
                return false;  // Если буква уже использована, не меняем попытки
            }

            usedLetters.append(letter);  // Добавляем букву в список использованных
            boolean found = false;

            // Ищем букву в слове
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    currentState.setCharAt(i, letter);  // Заменяем символ на угаданную букву
                    found = true;
                }
            }

            // Если буква не найдена, уменьшаем количество попыток
            if (!found) {
                attemptsLeft--;
                System.out.println("Wrong guess!");  // Сообщение о неправильном ответе
            } else {
                System.out.println("Good guess!");  // Сообщение о правильном ответе
            }

            return found;
        } else {
            // Если введено слово целиком, проверяем его
            if (input.equals(wordToGuess)) {
                currentState = new StringBuilder(wordToGuess);  // Открываем все буквы
                System.out.println("Good guess! You've guessed the word.");
                return true;
            } else {
                attemptsLeft--;  // Если слово не совпало, уменьшаем попытки
                System.out.println("Wrong guess!");  // Сообщение о неправильном ответе
                return false;
            }
        }
    }

    // Метод для отображения текущего состояния игры
    public void displayGameState() {
        System.out.println("Word to guess: " + currentState);
        System.out.println("Attempts left: " + attemptsLeft);
        System.out.println("Used letters: " + usedLetters);
    }

    // Метод для проверки окончания игры
    public boolean isGameOver() {
        return attemptsLeft <= 0 || currentState.toString().equals(wordToGuess);
    }

    // Геттеры для получения информации
    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }
}
