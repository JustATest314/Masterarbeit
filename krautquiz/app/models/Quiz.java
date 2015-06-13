package models;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;
import scala.reflect.internal.Trees.This;

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
	
	public static Finder<String,Quiz> find = new Finder<String, Quiz>(String.class, Quiz.class);
	
	// TODOL Method used after all?
	public static Question findByQuestionID(String questionID){
		return Ebean.find(Question.class).where().eq("questionID", questionID).findUnique();
	}
	
	public static List<Quiz> all() {
		return find.all();
		}

	
	public void setInterval(long interval){
		if(interval == 0){
			this.interval = 0;
		}
		else{
			this.interval = this.interval + interval;	
		}
	}
	
	public static String generateFakeID(){
		String fakeID = UUID.randomUUID().toString();
		return fakeID;
	}
	
	// TODOL Not necessary? The quiz only creates answer-entries in the quiz-table, but never question-entries
	public static void createQuestion(Question question, String userID, long interval) {
		Quiz entry = new Quiz(generateFakeID(), userID, question.questionID, null, System.currentTimeMillis(), interval);
		entry.save();
	}
	
	public static void createAnswer(Answer answer, String userID, long interval) {
		Quiz entry = new Quiz(generateFakeID(), userID, answer.questionID, null, System.currentTimeMillis(), interval);
		entry.save();
	}
	
	public static void updateAnswer(String questionID, String currentUser, long inputInterval){
		List<Quiz> quizList = Quiz.find.where().like("question_id", questionID).findList();
		// TODOL Is this for loop necessary?
		for (Quiz quizItem : quizList) {
			if(quizItem.userID.equals(currentUser)){
				quizItem.setInterval(inputInterval);
				quizItem.update();
			}
		}
		
//		Quiz entry = Quiz.find.where().like("question_id", questionID).findUnique();
//		entry.setInterval(inputInterval);
//		entry.update();
	}

	public void delete(String id) {
		find.ref(id).delete();
	}
	
	// Mostly for debugging
	public String toString(){
		return "EntryID: " + entryID + " userID: " + userID + " questionID: " + questionID + " answerID: " + answerID + " time: " + time + " interval: " + interval;
	}
}
