package gui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip musicClip;
    private FloatControl volumeControl;

    public MusicPlayer(String filePath) {
        try {
            // Открываем аудиофайл
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            // Получаем Clip
            musicClip = AudioSystem.getClip();
            // Открываем Clip и устанавливаем повторное воспроизведение
            musicClip.open(audioInputStream);

            // Получаем управление громкостью
            if (musicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (musicClip != null && !musicClip.isRunning()) {
            musicClip.start();
        }
    }

    public void pause() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }

    public void stop() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            volumeControl.setValue(volume);
        }
    }
}