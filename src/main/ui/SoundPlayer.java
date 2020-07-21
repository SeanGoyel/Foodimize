package ui;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.InputStream;

//Responsible for playing and retrieving sound
public class SoundPlayer {
    private Clip buttonPressClip;
    private AudioInputStream buttonPressAIS;


    public SoundPlayer() {
        retrieveButtonPressAudio();
    }

    //EFFECTS retrieves audio file for a button press sound
    public void retrieveButtonPressAudio() {
        try {
            buttonPressAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("buttonPress.wav"));
            buttonPressClip = AudioSystem.getClip();
            buttonPressClip.open(buttonPressAIS);

        } catch (Exception e) {
            //
        }
    }

    //MODIFIES: this
    //EFFECTS: plays button press sound
    public void playButtonPressAudio() {

        buttonPressClip.setFramePosition(0);
        buttonPressClip.start();

    }
}

