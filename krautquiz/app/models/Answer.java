package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/*
 * Simple model class for an answer
 */

@Entity
public class Answer extends Model{

	// Auto-generated serial
	private static final long serialVersionUID = 1L;
	
	// The questionID is needed to somehow link the answer to a question, as an answer can not be without one
	@Id
	public String answerID;
	public String questionID;
	public String answerText;
	public Integer voteScore;
	public String userID;
	
	// Contructor
	public Answer(String inputID, String inputQuestionID, String inputAnswerText, Integer inputVoteScore, String inputUserID){
		this.answerID = inputID;
		this.questionID = inputQuestionID;
		this.answerText = inputAnswerText;
		this.voteScore = inputVoteScore;
		this.userID = inputUserID;
	}
	
	public static Finder<String,Answer> find = new Finder<String, Answer>(
		    String.class, Answer.class
		  );
	
	public static List<Answer> all() {
		  return find.all();
		}

		public static void create(Answer answer) {
		  answer.save();
		}

		public void delete(String id) {
		  find.ref(id).delete();
		}
	
	// Mostly for debugging
	public String toString(){
		return "ID = " + answerID + " questionID = " + questionID + " answerText = " + answerText + " voteScore = " + voteScore + " userID = " + userID;
	}
}
