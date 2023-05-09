import javax.swing.JOptionPane;

/**
 * <b>ReplayThread</b> - Thread that allows a game of snake to start itself over from
 * the beginning.
 * @author Yama H
 * @version 1.0
 */
public class ReplayThread extends Thread
{
    private Arena arena;
    /**
     * Custom start method with a parameter that enables us to get arena's variables.
     * @param arena arena from which the thread is called.
     */
    public void start(Arena arena)
    {
        this.arena = arena;
		//arena.stop();
		//arena.destroy();
        start();
    }
    public void run()
    {
        try
        {
            arena.frameThread.join();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(arena.cont, "Error waiting for thread to return: \n" + e.toString(), 
                    "Unexpected Exception", JOptionPane.ERROR_MESSAGE);
        }
        arena.init();
        return;
    }
}