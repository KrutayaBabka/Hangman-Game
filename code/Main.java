package code;

import java.util.List;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Загружаем слова из файла
        String filename = "words.txt";
        List<String> words = WordLoader.loadWordsFromFile(filename);

        // Если слов нет, завершаем программу
        if (words.isEmpty()) {
            System.out.println("No words found in the file.");
            return;
        }

        // Запускаем графический интерфейс
        SwingUtilities.invokeLater(() -> new HangmanGUI(words));
    }
}
