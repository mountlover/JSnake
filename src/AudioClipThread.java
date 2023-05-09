import javax.sound.sampled.*;
import java.io.IOException;

/**
 * <b>AudioClipThread</b> - Loads an audio clip without hanging the applet.
 * @author Yama H
 * @version 1.0
 */
public class AudioClipThread extends Thread
{
    private Arena arena;
    private AudioInputStream stream;
    /**
     * Constructor for objects of type AudioClipThread.
     * @param arena Arena using this Thread.
     */
    public void start(Arena arena)
    {
        this.arena = arena;
        start();
    }
    public void run()
    {
        try
        {
            stream = AudioSystem.getAudioInputStream(getClass().getResource("collect.wav"));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(stream);
            arena.collect = audioClip;
            arena.audioClipLoaded = true;
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}