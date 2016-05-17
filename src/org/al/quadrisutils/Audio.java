package org.al.quadrisutils;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Audio {
    public static synchronized void playNotification() throws IOException {
        InputStream in = new FileInputStream(System.getProperty("user.dir") + "/sounds/n.wav");

        AudioStream as = new AudioStream(in);

        AudioPlayer.player.start(as);

//        AudioPlayer.player.stop(as);
    }
}
