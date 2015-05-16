package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/*
 * Simple model class for a question
 */

@Entity
public class Question extends Model implements Comparable<Question> {
	
	// Auto-generated serial
	private static final long serialVersionUID = 1L;
	
	@Id
	public String questionID;
	public String questionText;
	public Integer voteScore;
	public String userID;
	public Integer page;
	
	// Constructor
	public Question(String inputID, String inputQuestionText, Integer inputVoteScore, String inputUserID, Integer inputPage){
		this.questionID = inputID;
		this.questionText = inputQuestionText;
		this.voteScore = inputVoteScore;
		this.userID = inputUserID;
		this.page = inputPage;
	}
	
	// Default Constructor is needed for the form, else the play framework breaks!
	public Question(){
		
	}
	
	public static Finder<String,Question> find = new Finder<String, Question>(
		    String.class, Question.class
		  );
	
	public static List<Question> all() {
		  return find.all();
		}

		public static void create(Question question) {
		  question.save();
		}

		public void delete(String id) {
		  find.ref(id).delete();
		}
	
	// Mostly for debugging
	public String toString(){
		return "questionID = " + questionID + " questionText = " + questionText + " voteScore = " + voteScore + " userID = " + userID + " page = " + page;
	}

	@Override
	public int compareTo(Question question) {
		return this.voteScore.compareTo(question.voteScore);
	}
}
