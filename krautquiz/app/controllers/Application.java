package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.avaje.ebean.ExpressionList;

import models.Answer;
import models.Question;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/*
 * This is the application controller. It contains the most of the logic at the moment. Splitting it into several other classes did not
 * help to make it simpler, therefore this will be the main page for everything that needs to be processed
 */

public class Application extends Controller {

	// ArrayList might not be performant when there are many add / delete operations.
	// If need be -> use LinkedList!
	// TODOH Write methods for sorting the lists by voteScore
	static List<Question> questionListAll = new ArrayList<Question>();
	static List<Answer> answerListAll = new ArrayList<Answer>();
	
	private static final Form<Question> newQuestionForm = Form.form(Question.class);
	private static final Form<Answer> newAnswerForm = Form.form(Answer.class);
	
	public static String generateFakeID(){
		String fakeID = UUID.randomUUID().toString();
		return fakeID;
	}
	
	// Helper-method for initializing the index page
	// TODOH Put this into a database!
	public static void initialize() {
		
		// Question format: ID / questionText / voteScore / userID
		// Answer format: ID / questionID (answer linked to question) / answerText / voteScore / userID
		
		// Fake IDs are needed while developing, so there are no duplicates
		// TODO Remove in production
		String questionFakeID1 = generateFakeID();
		
		Question question1 = new Question(questionFakeID1, "Do Androids dream?", 127, "Marcus");
		Answer answer11 = new Answer(generateFakeID(), questionFakeID1, "Only of electric sheep!", 70, "Tibor");
		Answer answer12 = new Answer(generateFakeID(), questionFakeID1, "No, they dont!", 10, "Sarah");
		
		String questionFakeID2 = generateFakeID();

		Question question2 = new Question(questionFakeID2, "Why is the sky blue?", 76, "Frank");
		Answer answer21 = new Answer(generateFakeID(), questionFakeID2, "Frequency filtered sunlight!", 45, "Oliver");
		Answer answer22 = new Answer(generateFakeID(), questionFakeID2, "Light reflects from the blue sea water!", 3, "Tom");
		
		String questionFakeID3 = generateFakeID();
		
		Question question3 = new Question(questionFakeID3, "How tall is tall?", 34, "Tim");
		Answer answer31 = new Answer(generateFakeID(), questionFakeID3, "Depends on your definition!", 12, "Oliver");
		Answer answer32 = new Answer(generateFakeID(), questionFakeID3, "Very!", 1, "Marcus");
		Answer answer33 = new Answer(generateFakeID(), questionFakeID3, "Not much!", 1, "Frank");
		
		questionListAll.clear();
		answerListAll.clear();
		
		questionListAll.add(question1);
		questionListAll.add(question2);
		questionListAll.add(question3);
		
		answerListAll.add(answer11);
		answerListAll.add(answer12);
		answerListAll.add(answer21);
		answerListAll.add(answer22);
		answerListAll.add(answer31);
		answerListAll.add(answer32);
		answerListAll.add(answer33);
		
		Question.create(question1);
		Question.create(question2);
		Question.create(question3);
		
		Answer.create(answer11);
		Answer.create(answer12);
		Answer.create(answer21);
		Answer.create(answer22);
		Answer.create(answer31);
		Answer.create(answer32);
		Answer.create(answer33);
	}

	// Frontpage
	public static Result index() {
//		questionListAll.clear();
//		answerListAll.clear();
		// TODOH Get questions / answer from the DB, not manually
		return ok(views.html.index.render(questionListAll, answerListAll));
	}

	// Quizpage
	public static Result startQuiz() {
		initialize();
		return ok(views.html.quiz.render(questionListAll, answerListAll));
	}

	// Settings
	public static Result showSettings() {
		return ok(views.html.einstellungen.render(questionListAll, answerListAll));
	}
	
	// Go to the ask question page
	public static Result askQuestion(){
		List<Question> questionHelper = new ArrayList<Question>();
		for (Question questionItem : Question.find.all()) {
			questionHelper.add(questionItem);
		}
		return ok(views.html.frageAntwort.render(newQuestionForm, questionHelper));
	}
	
	// Send the question to the indexpage
	// TODOL Use Bootstraps "Modal" to smoothly glide into the view
	// http://getbootstrap.com/javascript/#modals
	public static Result sendQuestion(){
		// Create new question-form and fill it with the values from the other page
		Form<Question> boundQuestion = newQuestionForm.bindFromRequest();
		Question newQuestion = boundQuestion.get();
		Question.create(newQuestion);
		questionListAll.add(newQuestion);
		return ok(views.html.index.render(questionListAll, answerListAll));
		// FIXME Not working, why not?
//		return redirect(routes.Application.index());
	}
	
	// Write an answer, goto answerpage
	public static Result writeAnswer(){
		List<Answer> answerHelper = new ArrayList<Answer>();
		
		for (Answer answerItem : Answer.find.all()) {
			answerHelper.add(answerItem);
		}
		return ok(views.html.antwortGeben.render(newAnswerForm, answerHelper));
	}
	
	// Send answer to indexpage
	public static Result sendAnswer(){
		Form<Answer> boundAnswer = newAnswerForm.bindFromRequest();
		Answer newAnswer = boundAnswer.get();
		Answer.create(newAnswer);
		answerListAll.add(newAnswer);
//		return ok(views.html.index.render(questionListAll, answerListAll));
		// Redirect to the index-page because else you land on /Antwort and not /index
		// Also its recommended to use PRG: http://en.wikipedia.org/wiki/Post/Redirect/Get
		return redirect(routes.Application.index());
	}
	
	// TODOL Show the users page, will need an ID for a routing paramter
	public static Result showUsers(){
		return TODO;
	}
	
	public static Result initDB(){
		initialize();
		System.out.println("DB initialized");
		return redirect(routes.Application.index());
	}
}
