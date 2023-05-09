import javax.swing.JOptionPane;

/**
 * <b>FrameThread</b> - Enables the snake to move by deciding how long to hold 
 * each frame.
 * @author Yama H
 * @version 1.1
 */
public class FrameThread extends Thread
{
    private Arena arena;
    /**
     * Custom start method with a parameter that enables us to get arena's variables.
     * @param arena arena from which the thread is called.
     */
    public void start(Arena arena)
    {
        this.arena = arena;
        start();
    }
    public void run() 
    {
        while(!arena.gameOver)
        {
            try
            {
                if(!arena.pause && !arena.gameOver)
                    arena.frame();
                else if(arena.gameOver)
                {
                    arena.sc.saveScores(arena.hiScores);
                    return;
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(arena.cont, "Frame Error: \n" + e.toString(),
                        "Unexpected Exception", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!arena.gameOver) pause(arena.pausetime);
        }
        arena.sc.saveScores(arena.hiScores);
        return;
    }
    /**
     * Pauses the thread temporarily. Makes several more attempts if pausing fails.
     * @param time time to pause for
     */
    public void pause(long time)
    {
        recursivePause(time, Arena.RECURSIVE_LIMIT);
    }
    private void recursivePause(long time, int tries) 
    {
        if(tries != 0)
        {
            try 
            { 
                sleep(time);
            }
            catch(Exception ignored) 
            {
                recursivePause(time, tries-1);
            }
        }
    }
}