package code;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameConfig {
    public static final int NUMBER_OF_LIVES;

    public static final String WIN_SOUND;
    public static final float WIN_VOLUME;

    public static final String LOSE_SOUND;
    public static final float LOSE_VOLUME;

    public static final String CORRECT_SOUND;
    public static final float CORRECT_VOLUME;

    public static final String WRONG_SOUND;
    public static final float WRONG_VOLUME;

    public static final String BACKGROUND_SOUND;
    public static final float BACKGROUND_VOLUME;

    static {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("assets/config/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("⚠ Ошибка загрузки config.properties. Используем значения по умолчанию.");
        }

        // Чтение количества жизней и проверка на минимальное значение 6
        int lives = Integer.parseInt(properties.getProperty("max_attempts", "6"));
        NUMBER_OF_LIVES = Math.max(lives, 6); // Устанавливаем минимум 6 жизней

        WIN_SOUND = properties.getProperty("win_sound", "win.wav");
        WIN_VOLUME = clampVolume(properties.getProperty("win_volume", "1.0"));

        LOSE_SOUND = properties.getProperty("lose_sound", "lose.wav");
        LOSE_VOLUME = clampVolume(properties.getProperty("lose_volume", "1.0"));

        CORRECT_SOUND = properties.getProperty("correct_sound", "correct.wav");
        CORRECT_VOLUME = clampVolume(properties.getProperty("correct_volume", "1.0"));

        WRONG_SOUND = properties.getProperty("wrong_sound", "wrong.wav");
        WRONG_VOLUME = clampVolume(properties.getProperty("wrong_volume", "1.0"));

        BACKGROUND_SOUND = properties.getProperty("background_sound", "background.wav");
        BACKGROUND_VOLUME = clampVolume(properties.getProperty("background_volume", "1.0"));
    }

    // Метод ограничивает громкость в диапазоне 0.0 - 1.0
    private static float clampVolume(String value) {
        try {
            float volume = Float.parseFloat(value);
            return Math.max(0.0f, Math.min(1.0f, volume)); // Ограничиваем в пределах [0.0, 1.0]
        } catch (NumberFormatException e) {
            System.err.println("⚠ Некорректное значение громкости: " + value + ". Устанавливаем 1.0");
            return 1.0f; // Если ошибка в файле, ставим максимум
        }
    }
}
