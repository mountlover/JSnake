import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
/**
 * <b>Arena</b> - The first (and perhaps the only) stage for Snake.
 * @author Yama H
 * @version 1.0
 */
public class Arena extends JFrame implements KeyListener
{
    /**
     * Constant for declaring that the image has loaded successfully.
     */
	public static final int IMAGE_LOAD = 39;
	/**
     * Number of grids in panel.
     */
	public static final int SIZE = 30;
	/**
	 * Starting time to pause between frames. Correlates to game speed and difficulty.
	 */
	public static final long INIT_PAUSETIME = 150;
	/**
	 * Minimum amount of time in between frames.
	 */
	public static final long MIN_PAUSETIME = 60;
	/**
	 * How much to ramp up the game speed by at a time
	 */
	public static final long PAUSETIME_INCREMENT = 10;
	/**
	 * Minimum amount of time in between frames.
	 */
	public static long pausetime;
	/**
	 * Number of times to recurse certain operations before giving up and failing.
	 */
	public static final int RECURSIVE_LIMIT = 10;
	private static Coord[] choices; // array of Coords
	private static JLabel[][] arena; // double-array of JLabels
	protected static ScoreQueue hiScores; // heap-implemented priority queue of Scores
	protected static Snake david; // protected in case I feel like making a level 2 Arena
	private static int chIndex = 0, topScore = 0;
	protected static boolean gameOver, sound;
	private static ImageIcon blankIcon, snakeIcon, heartIcon, titleIcon;
	protected static Container cont;
	private static String player = null;
	protected static Clip collect = null;
	private int x,y;
	protected boolean pressToStart, pause;
	protected static boolean audioClipLoaded;
	private Image blank = null, snake = null, heart = null, title = null;
	private JPanel arenaPanel;
	private Scanner scan;
	private Toolkit toolkit;
	protected FrameThread frameThread;
	private ReplayThread replayThread;
	protected ScoreKeeper sc = new ScoreKeeper();
	public static void main(String[] args)
	{
		Arena arena = new Arena("JSnake");
	}
	public Arena(String title)
	{
		super(title);
		init();
	}
	/**
	 * init() called automatically by constructor
	 */
	public void init()
	{
		pausetime = INIT_PAUSETIME;
	    if(collect == null)
	    	new AudioClipThread().start(this); // loads audio clip
	    gameOver = false;
		System.out.println("Press any key to begin, F1 to pause, F2 to restart, " +
				"F3 to view Wall of Fame, F4 to toggle sound");
		hiScores = new ScoreQueue();
		if(player == null)
			player = JOptionPane.showInputDialog("Please enter your name:");
		scan = new Scanner(player);
		if(scan.hasNext())
		{
			player = scan.next();
		}
		else
		{
			player = "Anonymous";
		}
		if(!sc.notSet()) // if the scores have already been saved
			hiScores = sc.getScores();
		else // add in built-in top scores
		{
			hiScores.add(new Score(60, "Birdo"));
			hiScores.add(new Score(1000, "Luigi"));
			hiScores.add(new Score(2000, "Toad"));
			hiScores.add(new Score(3000, "Peach"));
			hiScores.add(new Score(4000, "Mario"));
			hiScores.add(new Score(5000, "Jack"));
			hiScores.add(new Score(6000, "David"));
			hiScores.add(new Score(7000, "Queen"));
			hiScores.add(new Score(8000, "King"));
			hiScores.add(new Score(10000, "Ace"));
			sc.saveScores(hiScores);
		}
		chIndex = 0;
		frameThread = new FrameThread();
		x = SIZE/2;
		y = SIZE/2;
		setFocusable(true);
		addKeyListener(this);
		cont = getContentPane();
		cont.setFocusable(true);
		cont.addKeyListener(this);
		cont.setLayout(new BorderLayout());
		arenaPanel = new JPanel(new GridLayout(SIZE,SIZE));
		arena = new JLabel[SIZE][SIZE];
		choices = new Coord[SIZE*SIZE];
		toolkit = getToolkit();
		if(title == null)
			title = toolkit.getImage(getClass().getResource("title.jpeg"));
		if(blank == null)
			blank = toolkit.getImage(getClass().getResource("blank.gif"));
		if(snake == null)
			snake = toolkit.getImage(getClass().getResource("snake.gif"));
		if(heart == null)
			heart = toolkit.getImage(getClass().getResource("heart.gif"));
		toolkit.prepareImage(title, -1, -1, this);
		toolkit.prepareImage(blank, -1, -1, this);
		toolkit.prepareImage(snake, -1, -1, this);
		toolkit.prepareImage(heart, -1, -1, this);
		// make sure the images load properly before doing anything else
		while(!(toolkit.checkImage(title, -1, -1, this) == IMAGE_LOAD &&
				toolkit.checkImage(blank, -1, -1, this) == IMAGE_LOAD &&
				toolkit.checkImage(snake, -1, -1, this) == IMAGE_LOAD &&
				toolkit.checkImage(heart, -1, -1, this) == IMAGE_LOAD))
		{
			pause(MIN_PAUSETIME);
		}
		blankIcon = new ImageIcon(blank);
		snakeIcon = new ImageIcon(snake);
		heartIcon = new ImageIcon(heart);
		titleIcon = new ImageIcon(title);
		for(int i=0; i<SIZE; i++)
		{
			for(int j=0; j<SIZE; j++)
			{
				arena[j][i] = new JLabel(blankIcon);
				arenaPanel.add(arena[j][i]);
				choices[chIndex++] = new Coord(j,i);
			}
		}
		setRandomHeart();
		cont.add(new JLabel(titleIcon), BorderLayout.WEST);
		cont.add(arenaPanel, BorderLayout.EAST);
		david = new Snake(x, y, snakeIcon, heartIcon, SIZE);
		david.move();
		cont.validate();
		cont.repaint();
		setBounds(0,0,800,639);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		repaint();
		setVisible(true);
		requestFocus();
		pause(MIN_PAUSETIME); // give it some time...
		sound = audioClipLoaded;
		pressToStart = false;
	}
	/**
	 * called for every cycle of the game
	 */
	public void frame()
	{
		if(pressToStart) david.move();
		arenaPanel.repaint();
		if(pressToStart) System.out.println("Top Score: " + topScore
				+ ", Current Score: " + david.score());
	}
	/**
	 * erases a tile and adds it to the list of blank tiles
	 * @param c coordinates of tile
	 */
	public static void eraseTile(Coord c)
	{
		arena[c.getX()][c.getY()].setIcon(blankIcon);
		choices[chIndex++] = c;
	}
	/**
	 * sets a "tile" to the given image and removes it from list of blank tiles
	 * (O(n)| n = no. of free tiles)
	 * @param icon Image to set to this tile
	 * @param a X coordinate of tile
	 * @param b Y coordinate of tile
	 */
	public static void setTile(ImageIcon icon, int a, int b)
	{
		// if a snakeIcon wants to be drawn off the screen or on top of another snake icon
		if(a < 0 || a >= SIZE || b < 0 || b >= SIZE || (icon.equals(snakeIcon) &&
		                arena[a][b].getIcon().equals(icon)))
		{
			gameOver(false);
		}
		else
		{
			// if a snakeIcon wants to be drawn on a heartIcon
			if(icon.equals(snakeIcon) && arena[a][b].getIcon().equals(heartIcon))
			{
				if(sound)
				{
					collect.stop();
					collect.setFramePosition(0);
					collect.start();
				}
				david.incScore();
				// Slightly speed up the game for the first few hearts
				if(pausetime > MIN_PAUSETIME)
				{
					pausetime -= PAUSETIME_INCREMENT;
					if(pausetime < MIN_PAUSETIME) pausetime = MIN_PAUSETIME;
				}
				setRandomHeart();
			}
			arena[a][b].setIcon(icon);
			for(int i=0; i<chIndex; i++)
			{
				if(choices[i].getX() == a && choices[i].getY() == b)
				{
					choices[i] = choices[chIndex-1];
					choices[--chIndex] = null;
					return;
				}
			}
		}
	}
	/**
	 * Not used
	 */
	public void keyTyped(KeyEvent key){}
	/**
	 * listens to pressed keys and tells the snake to move accordingly
	 * @param key the key pressed
	 */
	public void keyPressed(KeyEvent key)
	{
		switch(key.getKeyCode())
		{
		case KeyEvent.VK_UP:
			david.changeDir("UP");
			break;
		case KeyEvent.VK_LEFT:
			david.changeDir("LEFT");
			break;
		case KeyEvent.VK_RIGHT:
			david.changeDir("RIGHT");
			break;
		case KeyEvent.VK_DOWN:
			david.changeDir("DOWN");
			break;
		default:
			break;
		}
	}
	/**
	 * listens to released keys and uses them as options
	 * @param key the key released
	 */
	public void keyReleased(KeyEvent key)
	{
		if (!pressToStart)
		{
			pressToStart = true;
			frameThread.start(this);
		}
		else if(key.getKeyCode() == KeyEvent.VK_F1) // pause
		{
			if(frameThread.isAlive())
			{
				pause = true;
				JOptionPane.showMessageDialog(cont, "PAUSED");
				cont.requestFocus();
				pause = false;
			}
		}
		else if(key.getKeyCode() == KeyEvent.VK_F2) // restart
		{
			gameOver = true;
			cont.removeAll();
			cont.removeKeyListener(this);
			removeKeyListener(this);
			replayThread = new ReplayThread();
			replayThread.start(this);
		}
		else if(key.getKeyCode() == KeyEvent.VK_F3) // wall of fame
		{
			pause = true;
			String message = "";
			ScoreQueue tmp = hiScores.duplicate();
			int place = 1;
			while(!tmp.isEmpty() && place <= 10)
			{
				message += place + ": " + tmp.peek().getName() + " - " +
				    tmp.poll().getScoreInt() + "\n";
				place++;
			}
			JOptionPane.showMessageDialog(cont, message, "The Wall of Fame",
					JOptionPane.PLAIN_MESSAGE);
			cont.requestFocus();
			pause = false;
		}
		else if(key.getKeyCode() == KeyEvent.VK_F4 && audioClipLoaded) // toggle sound
		{
			System.out.println("Sound: " + !sound);
			if(sound) sound = false;
			else sound = true;
		}
		else if(key.getKeyCode() == KeyEvent.VK_F5) // credits
		{
			pause = true;
			JOptionPane.showMessageDialog(cont, "Snake v1.0\n\nMade by Yama D. Habib",
					"Credits", JOptionPane.PLAIN_MESSAGE);
			cont.requestFocus();
			pause = false;
		}
	}
	/**
	 * assigns a heart to a tile at random and removes the tile from the list of blank tiles
	 */
	public static void setRandomHeart()
	{
		if(chIndex <= 0)
		{
			gameOver(true);
		}
		else
		{
			int i = (int)(Math.random()*chIndex);
			int a = choices[i].getX();
			int b = choices[i].getY();
			choices[i] = choices[chIndex-1];
			choices[--chIndex] = null;
			arena[a][b].setIcon(heartIcon);
		}
	}
	/**
	 * ends the game
	 * @param win true if the game was won (unlikely), and false if not
	 */
	public static void gameOver(boolean win)
	{
		gameOver = true;
		if(win)
		{
			JOptionPane.showMessageDialog(cont, "Congratulations! You win! \nYour Score: " +
			    david.score(), "Wow...", JOptionPane.PLAIN_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(cont, "Game over! \nYour Score: " + david.score(),
					"Too bad...", JOptionPane.PLAIN_MESSAGE);
		}
		if(david.score() > hiScores.peek().getScoreInt())
		{
			JOptionPane.showMessageDialog(cont, "Whoa!! A new record!!!",
				"Awesome Stuff!", JOptionPane.PLAIN_MESSAGE);
		}
		if(david.score() > topScore)
		{
			topScore = david.score();
		}
		hiScores.add(new Score(david.score(), player));
		JOptionPane.showMessageDialog(cont, "Press F2 to play again, F3 to view " +
				"high scores", "You know you want to...",
				JOptionPane.INFORMATION_MESSAGE);
		cont.requestFocus();
	}
	/**
	 * Pauses the thread temporarily. Makes several more attempts if pausing fails.
	 * @param time time to pause for
	 * @return whether or not the Thread paused successfully
	 */
	public boolean pause(long time)
	{
		return recursivePause(time, RECURSIVE_LIMIT);
	}
    private boolean recursivePause(long time, int tries)
    {
    	if(tries != 0)
    	{
	    	try
	    	{
	    		Thread.sleep(time);
	    		return true;
	    	}
	    	catch(Exception ignored)
	    	{
	    		JOptionPane.showMessageDialog(cont, ignored.toString());
	    		recursivePause(time, tries-1);
	    	}
    	}
    	return false;
    }
}