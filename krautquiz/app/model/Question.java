package model;

public class Question {
	
	public String ID;
	public String questionText;
	public Integer voteScore;
	public String userID;
	
	public Question(String inputID, String inputQuestionText, Integer inputVoteScore, String inputUserID){
		this.ID = inputID;
		this.questionText = inputQuestionText;
		this.voteScore = inputVoteScore;
		this.userID = inputUserID;
	}
	
	public String toString(){
		return "ID = " + ID + " questionText = " + questionText + " voteScore = " + voteScore + " userID = " + userID;
	}

}
