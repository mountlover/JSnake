/**
 * <b>ScoreKeeper</b> - saves scores
 * @author Yama H
 * @version 1.0
 */
public class ScoreKeeper
{
	private ScoreQueue scores = null;
	/**
	 * Constructor for objects of class ScoreKeeper
	 */
	public ScoreKeeper(){}
	/**
	 * Constructor for objects of class ScoreKeeper
	 * @param sq a list of scores
	 */
	public ScoreKeeper(ScoreQueue sq)
	{
		scores = sq;
	}
	/**
	 * Indicates whether or not any scores have been saved to the score keeper as of yet
	 * @return false if scores have been set, true if no scores have been set
	 */
	public boolean notSet()
	{
		if(scores == null)
			return true;
		return false;
	}
	/**
	 * Retrieves the list of scores
	 * @return the scores
	 */
	public ScoreQueue getScores()
	{
		return scores;
	}
	/**
	 * Saves the list of scores
	 * @param sq scores to be saved
	 */
	public void saveScores(ScoreQueue sq)
	{
		scores = sq;
	}
}