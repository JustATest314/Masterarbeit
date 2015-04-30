package model;

public class Answer {

	public String ID;
	public String questionID;
	public String answerText;
	public Integer voteScore;
	public String userID;
	
	public Answer(String inputID, String inputQuestionID, String inputAnswerText, Integer inputVoteScore, String inputUserID){
		this.ID = inputID;
		this.questionID = inputQuestionID;
		this.answerText = inputAnswerText;
		this.voteScore = inputVoteScore;
		this.userID = inputUserID;
	}
	
	public String toString(){
		return "ID = " + ID + " questionID = " + questionID + " answerText = " + answerText + " voteScore = " + voteScore + " userID = " + userID;
	}
	
}
