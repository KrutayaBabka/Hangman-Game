package code;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // System.out.println("Current working directory: " + System.getProperty("user.dir"));
        // Загрузка слов из файла
        String filename = "words.txt";
        List<String> words = WordLoader.loadWordsFromFile(filename);

        // Если слов нет, завершаем игру
        if (words.isEmpty()) {
            System.out.println("No words found in the file.");
            return;
        }

        // Создаём игру
        Game game = new Game(words);
        
        playGame(game);
    }

    // Метод для игры
    public static void playGame(Game game) {
        try(Scanner scanner = new Scanner(System.in)) {
            do { 
                // Игровой цикл
                while (!game.isGameOver()) {
                    game.displayGameState();  // Показываем текущее состояние игры
                    System.out.print("Enter a letter: ");
                    String guess = scanner.nextLine().toLowerCase();  // Получаем строку
        
                    // Проверка, была ли угадана буква
                    game.guessLetter(guess);
                }
        
                // Завершаем игру
                if (game.getAttemptsLeft() > 0) {
                    System.out.println("Congratulations! You've guessed the word: " + game.getWordToGuess());
                } else {
                    System.out.println("Game over! The word was: " + game.getWordToGuess());
                }
    
                // Want to continue
                if(askIfPlayAgain(scanner)) {
                    game.setNewWord();
                } else {
                    break;
                }
            } while (true);
        }
    }

    // Запрос на продолжение игры
    public static boolean askIfPlayAgain(Scanner scanner) {
        System.out.print("Do you want to play again? (y/n): ");
        String answer = scanner.nextLine();
        return answer.toLowerCase().equals("y"); // Если нет ввода, по умолчанию не продолжаем
    }

}
