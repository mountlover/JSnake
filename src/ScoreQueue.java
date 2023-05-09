/**
 * <b>ScoreQueue</b> - A simple max heap for storing high scores and names
 * @author Yama H
 * @version 1.0
 */
public class ScoreQueue
{
	/**
	 * Default value for the initial capacity of the queue
	 */
    public static final int DEFAULT_INIT_CAPACITY = 16;
	private int capacity; // current capacity
	private int index; // index of first free element
	private Score[] scoreArray;
	/**
	 * Constructor for objects of class ScoreQueue
	 * @param initCapacity initial capacity of array
	 */
	public ScoreQueue(int initCapacity)
	{
		scoreArray = new Score[initCapacity];
		capacity = initCapacity;
		index = 0;
	}
	private ScoreQueue(Score[] scores, int capacity, int index)
	{
		scoreArray = scores;
		this.capacity = capacity;
		this.index = index;
	}
	/**
	 * Constructor for objects of class ScoreQueue
	 * Initial capacity defaults to 16
	 */
	public ScoreQueue()
	{
		scoreArray = new Score[DEFAULT_INIT_CAPACITY];
		capacity = DEFAULT_INIT_CAPACITY;
		index = 0;
	}
	private void grow()
	{
		Score[] tmp = new Score[2*capacity];
		for(int i=0; i<capacity; i++)
		{
			tmp[i] = scoreArray[i];
		}
		scoreArray = tmp;
		tmp = null;
		capacity *= 2;
	}
	/**
	 * Adds scores to the list and sorts them in decreasing order
	 * @param s the score to add
	 */
	public void add(Score s)
	{
		if(index >= capacity-1)
			grow();
		if(isEmpty())
			scoreArray[index++] = s;
		else
		{
			scoreArray[index] = s;
			trickleUp(index++);
		}
	}
	private void trickleUp(int k)
	{
		if(k != 0)
		{
			if(scoreArray[k].getScoreInt() > (scoreArray[parent(k)].getScoreInt()))
			{
				Score tmp = scoreArray[k];
				scoreArray[k] = scoreArray[parent(k)];
				scoreArray[parent(k)] = tmp;
				trickleUp(parent(k));
			}
		}
	}
	protected int parent(int k)
	{ 
		return (k - 1) / 2;  
	}
	protected int left(int k)
	{ 
		return 2 * k + 1; 
	}
	protected int right(int k)
	{ 
		return 2 * (k + 1); 
	}
	/**
	 * Removes and retrieves the largest element (the score with the highest integer value) in the heap
	 * @return the highest score
	 */
	public Score poll()
	{
		if(isEmpty())
			throw new java.util.NoSuchElementException();
		else if(index == 1)
		{
			Score ret = scoreArray[0];
			scoreArray[0] = null;
			index = 0;
			return ret;
		}
		else
		{
			Score ret = scoreArray[0];
			scoreArray[0] = scoreArray[--index];
			scoreArray[index] = null;
			trickleDown(0);
			return ret;
		}
	}
	/**
	 * Retrieves but does not return the largest element in the heap
	 * @return the highest score
	 */
	public Score peek()
	{
		return scoreArray[0];
	}
	private void trickleDown(int k)
	{
		if(left(k) < index && right(k) < index)
		{
			if(scoreArray[left(k)].getScoreInt() >= scoreArray[right(k)].getScoreInt())
			{
				if(scoreArray[left(k)].getScoreInt() > scoreArray[k].getScoreInt())
				{
					Score tmp = scoreArray[k];
					scoreArray[k] = scoreArray[left(k)];
					scoreArray[left(k)] = tmp;
					trickleDown(left(k));
				}
			}
			else if(scoreArray[right(k)].getScoreInt() > scoreArray[left(k)].getScoreInt())
			{
				if(scoreArray[right(k)].getScoreInt() > scoreArray[k].getScoreInt())
				{
					Score tmp = scoreArray[k];
					scoreArray[k] = scoreArray[right(k)];
					scoreArray[right(k)] = tmp;
					trickleDown(right(k));
				}
			}
		}
		else if(left(k) < index)
		{
			if(scoreArray[left(k)].getScoreInt() > scoreArray[k].getScoreInt())
			{
				Score tmp = scoreArray[k];
				scoreArray[k] = scoreArray[left(k)];
				scoreArray[left(k)] = tmp;
				trickleDown(left(k));
			}
		}
	}
	/**
	 * Returns the size of the heap expressed as an integer
	 * @return the size of the heap
	 */
	public int size()
	{
		return index;
	}
	/**
	 * Determines whether or not the heap is empty
	 * @return whether or not the heap is empty
	 */
	public boolean isEmpty()
	{
		if(scoreArray[0] == null && index == 0)
		{
			return true;
		}
		else 
		if(scoreArray[0] == null || index == 0)
		{
			throw new InternalError();
		}
		else return false;
	}
	/**
	 * Converts this heap to an array
	 * @return an array depiction of the heap
	 */
	public Score[] toArray()
	{
		Score[] ret = new Score[capacity];
		for(int i=0; i<index; i++)
		{
			ret[i] = scoreArray[i];
		}
		return ret;
	}
	/**
	 * Duplicates the ScoreQueue
	 * @return a duplicate of the ScoreQueue
	 */
	public ScoreQueue duplicate()
	{
		return new ScoreQueue(toArray(), capacity, index);
	}
}