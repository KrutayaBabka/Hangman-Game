package code;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class SoundManager {
    private static Clip backgroundMusic;
    private static FloatControl bgVolumeControl; // Контроль громкости фоновой музыки

    // Метод для воспроизведения звуковых эффектов с настройкой громкости
    public static void playSound(String soundFile, float volume) {
        try {
            File file = new File("assets/sounds/" + soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Настраиваем громкость
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(convertVolume(volume));

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // e.printStackTrace();
        }
    }

    // Метод для запуска фоновой музыки с настраиваемой громкостью
    public static void playBackgroundMusic(String musicFile, float volume) {
        try {
            File file = new File("assets/sounds/" + musicFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            // Настраиваем громкость фоновой музыки
            bgVolumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            bgVolumeControl.setValue(convertVolume(volume));

            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // e.printStackTrace();
        }
    }

    // Метод для установки громкости фоновой музыки во время игры
    public static void setBackgroundMusicVolume(float volume) {
        if (bgVolumeControl != null) {
            bgVolumeControl.setValue(convertVolume(volume));
        }
    }

    // Метод для остановки фоновой музыки
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    // Конвертирует громкость от 0.0 до 1.0 в децибелы (диапазон FloatControl)
    private static float convertVolume(float volume) {
        float min = -80.0f; // Минимальный уровень в децибелах (практически без звука)
        float max = 6.0f;   // Максимальный уровень
        return min + (max - min) * volume;
    }
}
