package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.Question;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/*
 * This is the application controller. It contains the most of the logic at the moment. Splitting it into several other classes did not
 * help to make it simpler, therefore this will be the main page for everything that needs to be processed
 */

public class Application extends Controller {

	// HashMap for q/a, answers need to be linked somehow to questions, therefore List is used
	// TODOL HashMap does no sorting i.e. of the voteScore -> Linked HashMap?
	static Map<Question, List<Answer>> myMap = new HashMap<Question, List<Answer>>();
	
	private static final Form<Question> newQuestion = Form.form(Question.class);

	// Helper-method for initializing the index page
	public static void initialize() {
		// Question format: ID / questionText / voteScore / userID
		// Answer format: ID / questionID (answer linked to question) / answerText / voteScore / userID
		Question question1 = new Question("xyz", "Do Androids dream?", 127, "Marcus");
		Answer answer11 = new Answer("zab", "xyz", "Only of electric sheep!", 70, "Tibor");
		Answer answer12 = new Answer("qwert", "xyz", "No, they dont!", 10, "Sarah");

		Question question2 = new Question("fgh", "Why is the sky blue?", 76, "Frank");
		Answer answer21 = new Answer("xcv", "fgh", "Frequency filtered sunlight!", 45, "Oliver");
		Answer answer22 = new Answer("tzu", "fgh", "Light reflects from the blue sea water!", 3, "Tom");
		
		Question question3 = new Question("ertw", "How tall is tall?", 34, "Tim");
		Answer answer31 = new Answer("werw", "ertw", "Depends on your definition!", 12, "Oliver");
		Answer answer32 = new Answer("tzsdfu", "ertw", "Very!", 1, "Marcus");
		Answer answer33 = new Answer("wcfewf", "ertw", "Not much!", 1, "Frank");
		
		// Put answers into a list and later into the HashMap
		List<Answer> answerList1 = new ArrayList<Answer>();
		answerList1.add(answer11);
		answerList1.add(answer12);
		
		List<Answer> answerList2 = new ArrayList<Answer>();
		answerList2.add(answer21);
		answerList2.add(answer22);
		
		List<Answer> answerList3 = new ArrayList<Answer>();
		answerList3.add(answer31);
		answerList3.add(answer32);
		answerList3.add(answer33);
		

		// clear(), else everytime the index page is called the q/as get added again
//		myMap.clear();
		myMap.put(question1, answerList1);
		myMap.put(question2, answerList2);
		myMap.put(question3, answerList3);
		}

	// Frontpage
	public static Result index() {
		initialize();
		return ok(views.html.index.render(myMap));
	}

	// Quizpage
	public static Result startQuiz() {
		initialize();
		return ok(views.html.quiz.render(myMap));
	}

	// Settings
	public static Result showSettings() {
		return ok(views.html.einstellungen.render());
	}
	
	// Until now a new page gets generated where the user can enter a new question
	// TODOL Use Bootstraps "Modal" to smoothly glide into the view
	// http://getbootstrap.com/javascript/#modals
	public static Result askQuestion(){
//		return ok(views.html.frageAntwort.render(myMap));
		Form<Question> boundQuestion = newQuestion.bindFromRequest();
		Question testQuestion = boundQuestion.get();
		
		System.out.println("boundQuestion: " + boundQuestion.toString());
		
		Answer answer51 = new Answer("adsf", "ewr", "SendMap Answer Form1!", 6, "Kittie");	
		List<Answer> answerList5 = new ArrayList<Answer>();
		answerList5.add(answer51);
		
		myMap.put(testQuestion, answerList5);
		return ok(views.html.frageAntwort.render(newQuestion));
	}
	
	// In the frageAntwort-view a question gets asked, then the user gets transferred back to the index-page, where
	// a fake question gets added
	public static Result sendMap(){
		Question question4 = new Question("ertw", "Sendmap Question1", 34, "Tim");
		Answer answer41 = new Answer("werw", "ertw", "SendMap Answer 1!", 12, "Oliver");
		Answer answer42 = new Answer("tzsdfu", "ertw", "SendMap Answer 2!", 1, "Marcus");
		
		List<Answer> answerList4 = new ArrayList<Answer>();
		answerList4.add(answer41);
		answerList4.add(answer42);
		
		myMap.put(question4, answerList4);
		return ok(views.html.index.render(myMap));
	}
}
