package model;

/*
 * Simple model class for a question
 */
public class Question {
	
	public String questionID;
	public String questionText;
	public Integer voteScore;
	public String userID;
	
	// Constructor
	public Question(String inputID, String inputQuestionText, Integer inputVoteScore, String inputUserID){
		this.questionID = inputID;
		this.questionText = inputQuestionText;
		this.voteScore = inputVoteScore;
		this.userID = inputUserID;
	}
	
	// Default Constructor is needed for the form, else the play framework breaks!
	public Question(){
		
	}
	
	// Mostly for debugging
	public String toString(){
		return "questionID = " + questionID + " questionText = " + questionText + " voteScore = " + voteScore + " userID = " + userID;
	}
}
