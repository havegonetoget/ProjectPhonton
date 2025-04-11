import javax.sound.sampled.*;
import java.io.File;
import java.util.*;

public class PlayMusic {
    private static List<File> playlist = new ArrayList<>();
    private static Random rand = new Random();
    private static File lastPlayed = null;

    public static void startShuffledPlayback(String folderPath) {
        File musicFolder = new File(folderPath);
        File[] files = musicFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

        if (files == null || files.length == 0) {
            System.out.println("No WAV files found in " + folderPath);
            return;
        }

        playlist = new ArrayList<>(Arrays.asList(files));
        playNextTrack();
    }

    private static void playNextTrack() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }

        File nextTrack;
        do {
            nextTrack = playlist.get(rand.nextInt(playlist.size()));
        } while (playlist.size() > 1 && nextTrack.equals(lastPlayed));

        lastPlayed = nextTrack;

        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(nextTrack);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    playNextTrack();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
