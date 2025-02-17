package code;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;

public class HangmanGUI extends JFrame {
    private Game game;
    private JLabel wordLabel;       // Поле с загаданным словом
    private JLabel attemptsLabel;   // Количество попыток
    private JLabel usedLettersLabel;// Использованные буквы
    private JTextField inputField;  // Поле ввода
    private JButton guessButton;    // Кнопка "Угадать"
    private JButton exitButton;     // Кнопка выхода
    private JLabel imageLabel;      // Поле для картинки

    public HangmanGUI(List<String> words) {
        game = new Game(words);
        initUI();

        // Запуск фоновой музыки с громкостью 100%
        SoundManager.playBackgroundMusic("background.wav", 1.0f);
    }

    private void initUI() {
        setTitle("Hangman Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Полноэкранный режим
        setUndecorated(true); // Убирает рамку (если нужно)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Используем BorderLayout

        // Панель для размещения кнопки "Exit" в правом верхнем углу с отступами
        JPanel topPanel = new JPanel(new BorderLayout());
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setPreferredSize(new Dimension(100, 50)); // Устанавливаем размер кнопки
        exitButton.addActionListener(e -> exitGame());
        
        // Устанавливаем отступы от краёв экрана для панели, содержащей кнопку
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Отступы сверху, слева, снизу и справа

        topPanel.add(exitButton, BorderLayout.EAST); // Размещаем кнопку на востоке (справа)
        add(topPanel, BorderLayout.NORTH); // Добавляем панель в верхнюю часть окна

        // Панель для остальной части UI
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Поле для изображения
        imageLabel = new JLabel();
        updateImage();
        mainPanel.add(imageLabel, gbc);

        // Текущее слово
        wordLabel = new JLabel(formatMaskedWord(game.getMaskedWord()));
        wordLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(wordLabel, gbc);

        // Отображение количества попыток
        attemptsLabel = new JLabel("Attempts left: " + game.getAttemptsLeft());
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(attemptsLabel, gbc);

        // Отображение использованных букв
        usedLettersLabel = new JLabel("Used letters: " + game.getUsedLetters());
        usedLettersLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(usedLettersLabel, gbc);

        // Панель ввода и кнопка
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputField = new JTextField(5);
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Arial", Font.BOLD, 16));

        inputPanel.add(inputField);
        inputPanel.add(guessButton);
        mainPanel.add(inputPanel, gbc);

        // Обработчик кнопки "Угадать"
        guessButton.addActionListener(e -> handleGuess());

        // Обработчик нажатия Enter
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleGuess();
                }
            }
        });

        add(mainPanel, BorderLayout.CENTER); // Добавляем основную панель в центральную часть

        setVisible(true);
    }

    private void handleGuess() {
        String guess = inputField.getText().trim().toLowerCase();
        inputField.setText("");

        if (guess.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a letter or word.");
            return;
        }

        int attemptsBefore = game.getAttemptsLeft();
        game.guessLetter(guess);
        updateUI();

        // Звук при угаданной букве
        if (game.getAttemptsLeft() == attemptsBefore) {
            SoundManager.playSound("correct.wav", 1.0f); // 100% громкость
        } else {
            SoundManager.playSound("wrong.wav", 1.0f);   // 100% громкость
        }

        // Проверяем конец игры
        if (game.isGameOver()) {
            if (game.getAttemptsLeft() > 0) {
                SoundManager.playSound("win.wav", 1.0f); // 100% громкость
                JOptionPane.showMessageDialog(this, "Congratulations! You guessed the word: " + game.getWordToGuess());
            } else {
                SoundManager.playSound("lose.wav", 1.0f); // 100% громкость
                JOptionPane.showMessageDialog(this, "Game Over! The word was: " + game.getWordToGuess());
            }
            askToPlayAgain();
        }
    }

    private void updateUI() {
        wordLabel.setText(formatMaskedWord(game.getMaskedWord()));
        attemptsLabel.setText("Attempts left: " + game.getAttemptsLeft());
        usedLettersLabel.setText("Used letters: " + game.getUsedLetters());
        updateImage();
    }

    private void askToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            game.setNewWord();
            updateUI();
        } else {
            SoundManager.stopBackgroundMusic(); // Остановить музыку перед выходом
            System.exit(0);
        }
    }

    // Метод для обновления изображения в зависимости от количества оставшихся жизней
    private void updateImage() {
        int index = 7 - game.getAttemptsLeft();
        if (index < 1) index = 1;
        if (index > 7) index = 7;

        String imagePath = "assets/images/" + index + ".png";
        ImageIcon icon = new ImageIcon(imagePath);
        imageLabel.setIcon(icon);
    }

    // Метод для форматирования слова с пробелами между "_"
    private String formatMaskedWord(String maskedWord) {
        return String.join(" ", maskedWord.split(""));
    }

    // Метод для выхода из игры
    private void exitGame() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            SoundManager.stopBackgroundMusic(); // Остановить музыку перед выходом
            System.exit(0);
        }
    }
}
