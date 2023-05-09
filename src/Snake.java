import java.util.LinkedList;
import java.util.Queue;
import javax.swing.ImageIcon;
/**
 * <b>Snake</b> - A representation of a snake consisting of a queue of coordinates
 * @author Yama H
 * @version 1.0
 */
public class Snake
{
	// no private declarations in case i feel like making a level 2 snake
    /**
     * Default score incremental value
     */
	public static final int SCORE_INC = 9;
	/**
	 * Value for bonuses
	 */
	public static final int BONUS = 50;
	protected boolean acceptInput, heartEaten;
	protected int x, y, arenaSize, length, score;
	protected Queue<Coord> snake; // Linked List-implemented Queue of Coords
	protected ImageIcon food, me;
	/*
	 * DIRECTIONAL VALUES FOR int dir:
	 * UP : 1
	 * LEFT : 2
	 * RIGHT : 3
	 * DOWN : 4
	 */
	protected int dir;
	/**
	 * Constructor for objects of class Snake
	 * @param initX initial X coordinate
	 * @param initY initial Y coordinate
	 * @param snakeIcon Image of snake
	 * @param heartIcon Image of heart
	 * @param arenaSize size of arena (in tiles)
	 */
	public Snake(int initX, int initY, ImageIcon snakeIcon, ImageIcon heartIcon, int arenaSize)
	{
		snake = new LinkedList<Coord>();
		length = 1;
		dir = 1;
		heartEaten = true;
		score = 0;
		x = initX;
		y = initY;
		me = snakeIcon;
		food = heartIcon;
		acceptInput = false;
		this.arenaSize = arenaSize;
		Arena.setTile(me, x, y);
		snake.offer(new Coord(x,y));
	}
	/**
	 * Returns the player's current score
	 * @return the current score
	 */
	public int score()
	{
		return score;
	}
	/**
	 * Increments the current score
	 */
	public void incScore()
	{
		score += SCORE_INC + snake.size();
		if(x == 0 || x == arenaSize-1)
			score += BONUS;
		if(y == 0 || y == arenaSize-1)
			score += BONUS;
		heartEaten = true;
	}
	/**
	 * changes the direction for the snake to move in only if it's turning perpendicularly
	 * @param newDir the direction to move in expressed as one of the following String keywords:
	 * <br><b>UP</b><br><b>up</b><br><b>LEFT</b><br><b>left</b>
	 * <br><b>RIGHT</b><br><b>right</b><br><b>DOWN</b><br><b>down</b>
	 */
	public void changeDir(String newDir)
	{
		if(acceptInput)
		{
			if((newDir == "UP" || newDir == "up") && (dir == 2 || dir == 3))
			{
				dir = 1;
				acceptInput = false;
			}
			else if((newDir == "LEFT" || newDir == "left") && (dir == 1 || dir == 4))
			{
				dir = 2;
				acceptInput = false;
			}
			else if((newDir == "RIGHT" || newDir == "right") && (dir == 1 || dir == 4))
			{
				dir = 3;
				acceptInput = false;
			}
			else if((newDir == "DOWN" || newDir == "down") && (dir == 2 || dir == 3))
			{
				dir = 4;
				acceptInput = false;
			}
		}
	}
	/**
	 * moves the snake one block in the current direction
	 */
	public void move()
	{
		switch(dir)
		{
		case 1:
			acceptInput = true;
			removeTail();
			Arena.setTile(me, x, --y);
			snake.offer(new Coord(x,y));
			break;
		case 2:
			acceptInput = true;
			removeTail();
			Arena.setTile(me, --x, y);
			snake.offer(new Coord(x,y));
			break;
		case 3:
			acceptInput = true;
			removeTail();
			Arena.setTile(me, ++x, y);
			snake.offer(new Coord(x,y));
			break;
		case 4:
			acceptInput = true;
			removeTail();
			Arena.setTile(me, x, ++y);
			snake.offer(new Coord(x,y));
			break;
		default:
			break;
		}
	}
	/**
	 * removes value at end of snake if a heart was not eaten already
	 */
	public void removeTail()
	{
		if(!heartEaten)
		{
			Arena.eraseTile(snake.poll());
		}
		heartEaten = false;
	}
}