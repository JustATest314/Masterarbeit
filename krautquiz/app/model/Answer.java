package model;

/*
 * Simple model class for an answer
 */

public class Answer {

	// The questionID is needed to somehow link the answer to a question, as an answer can not be without one
	public String ID;
	public String questionID;
	public String answerText;
	public Integer voteScore;
	public String userID;
	
	// Contructor
	public Answer(String inputID, String inputQuestionID, String inputAnswerText, Integer inputVoteScore, String inputUserID){
		this.ID = inputID;
		this.questionID = inputQuestionID;
		this.answerText = inputAnswerText;
		this.voteScore = inputVoteScore;
		this.userID = inputUserID;
	}
	
	// Mostly for debugging
	public String toString(){
		return "ID = " + ID + " questionID = " + questionID + " answerText = " + answerText + " voteScore = " + voteScore + " userID = " + userID;
	}
}
