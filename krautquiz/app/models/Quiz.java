package models;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Quiz extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public String entryID;
	public String userID;
	public String questionID;
	public String answerID;
	public long time;
	public long interval;
	
	public Quiz(String inputEntryID, String inputUserID, String inputQuestionID, String inputAnswerID, long inputTime, long inputInterval){
		this.entryID = inputEntryID;
		this.userID = inputUserID;
		this.questionID = inputQuestionID;
		this.answerID = inputAnswerID;
		this.time = inputTime;
		this.interval = inputInterval;
	}
	
	public static Finder<String,Quiz> find = new Finder<String, Quiz>(
		    String.class, Quiz.class
		  );
	
	public static List<Quiz> all() {
		return find.all();
		}

	
	public static String generateFakeID(){
		String fakeID = UUID.randomUUID().toString();
		return fakeID;
	}
	
	// TODOL Not necessary? The quiz only creates answer-entries in the quiz-table, but never question-entries
	public static void createQuestion(Question question, String userID) {
		Quiz entry = new Quiz(generateFakeID(), userID, question.questionID, null, System.currentTimeMillis(), 5000);
		entry.save();
	}
	
	public static void createAnswer(Answer answer, String userID) {
		Quiz entry = new Quiz(generateFakeID(), userID, answer.questionID, null, System.currentTimeMillis(), 5000);
		entry.save();
	}

	public void delete(String id) {
		find.ref(id).delete();
	}
	
	// Mostly for debugging
	public String toString(){
		return "EntryID: " + entryID + " userID: " + userID + " questionID: " + questionID + " answerID: " + answerID + " time: " + time;
	}
}
