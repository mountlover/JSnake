/**
 * <b>Score</b> - A simple class designed for storing high scores
 * @author Yama H
 * @version 1.0
 */
public class Score
{
	private double score;
	private String name;
	/**
	 * Constructor for objects of class Score
	 * @param score the score expressed as a String
	 * @param name the name of the high scorer expressed as a String
	 */
	public Score(String score, String name)
	{
		this.score = Integer.parseInt(score);
		this.name = name;
	}
	/**
	 * Constructor for objects of class Score
	 * @param score the score expressed as a double
	 * @param name the name of the high scorer expressed as a String
	 */
	public Score(double score, String name)
	{
		this.score = score;
		this.name = name;
	}
	/**
	 * Constructor for objects of class Score
	 * @param score the score expressed as a double
	 * @param name the name of the high scorer expressed as a String
	 */
	public Score(int score, String name)
	{
		this.score = score;
		this.name = name;
	}
	/**
	 * Returns the high score expressed as an Integer
	 * @return the score as an int
	 */
	public int getScoreInt()
	{
		return (int)score;
	}
	/**
	 * Returns the high score expressed in decimal notation
	 * @return the score as an Double
	 */
	public double getScoreDub()
	{
		return score;
	}
	/**
	 * Returns the name of the high scorer
	 * @return the name expressed as a String
	 */
	public String getName()
	{
		return name;
	}
}