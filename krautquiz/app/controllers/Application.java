package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

import akka.io.Tcp.Register;
import models.Answer;
import models.Nutzer;
import models.Question;
import models.Quiz;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import scala.collection.immutable.HashMap;
import controllers.Application.Login;

/*
 * This is the application controller. It contains the most of the logic at the moment. Splitting it into several other classes did not
 * help to make it simpler, therefore this will be the main page for everything that needs to be processed
 */

// TODOL Votescores are Integer, make sure they are not bigger than MAX_INT!
public class Application extends Controller {

	// ArrayList might not be performant when there are many add / delete operations.
	// If need be -> use LinkedList!
	public static List<Question> questionListAll = new ArrayList<Question>();
	public static List<Answer> answerListAll = new ArrayList<Answer>();
	
	public static final Form<Question> newQuestionForm = Form.form(Question.class);
	public static final Form<Answer> newAnswerForm = Form.form(Answer.class);
	
//	public static List<Question> highestRankedQuestionList = new ArrayList<Question>();
//	public static List<Answer> highestRankedAnswerList = new ArrayList<Answer>();
	
	public static String generateFakeID(){
		String fakeID = UUID.randomUUID().toString();
		return fakeID;
	}
	
	// Helper-method for initializing the index page
	// TODO Remove in production, populate DB with fake entries
	public static void initialize() {
		questionListAll.clear();
		answerListAll.clear();
		
		// Question format: ID / questionText / voteScore / userID
		// Answer format: ID / questionID (answer linked to question) / answerText / voteScore / userID
		
		// Fake IDs are needed while developing, so there are no duplicates
		String questionFakeID1 = generateFakeID();
		
		Question question1 = new Question(questionFakeID1, "Do Androids dream?", 76, "marcus@mail.de", 1);
		Answer answer11 = new Answer(generateFakeID(), questionFakeID1, "Only of electric sheep!", 70, "tibor@mail.de", 1);
		Answer answer12 = new Answer(generateFakeID(), questionFakeID1, "No, they dont!", 10, "sarah@mail.de", 1);
		
		String questionFakeID2 = generateFakeID();

		Question question2 = new Question(questionFakeID2, "Why is the sky blue?", 124, "frank@mail.de", 1);
		Answer answer21 = new Answer(generateFakeID(), questionFakeID2, "Frequency filtered sunlight!", 45, "oliver@mail.de", 1);
		Answer answer22 = new Answer(generateFakeID(), questionFakeID2, "Light reflects from the blue sea water!", 3, "tom@mail.de", 1);
		
		String questionFakeID3 = generateFakeID();
		
		Question question3 = new Question(questionFakeID3, "How tall is tall?", 34, "tim@mail.de", 1);
		Answer answer31 = new Answer(generateFakeID(), questionFakeID3, "Depends on your definition!", 12, "oliver@mail.de", 1);
		Answer answer32 = new Answer(generateFakeID(), questionFakeID3, "Very!", 1, "marcus@mail.de", 1);
		Answer answer33 = new Answer(generateFakeID(), questionFakeID3, "Not much!", 1, "frank@mail.de", 1);
		
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
	@Security.Authenticated(Secured.class)
	public static Result index() {
		questionListAll.clear();
		answerListAll.clear();
		
		// Get all questions from DB
		for (Question questionItem : Question.find.all()) {
			questionListAll.add(questionItem);
		}
		
		// Get all answers from DB
		for (Answer answerItem : Answer.find.all()) {
			answerListAll.add(answerItem);
		}
		
		Collections.sort(questionListAll, Collections.reverseOrder());
		Collections.sort(answerListAll, Collections.reverseOrder());
		
		return ok(views.html.index.render(questionListAll, answerListAll));
	}

	// Settings
	public static Result showSettings() {
		return ok(views.html.einstellungen.render(questionListAll, answerListAll));
	}
	
	// Go to the ask question page
	@Security.Authenticated(Secured.class)
	public static Result askQuestion(){
		Nutzer formUser = Nutzer.find.byId(request().username());
		Question answerWithQuestionID = new Question(generateFakeID(), null, null, formUser.email, 1);
		Form<Question> preFilledQuestion = newQuestionForm.fill(answerWithQuestionID);
		return ok(views.html.frageAntwort.render(preFilledQuestion));
	}
	
	// Send the question to the indexpage
	// TODOL Use Bootstraps "Modal" to smoothly glide into the view
	// http://getbootstrap.com/javascript/#modals
	public static Result sendQuestion(){
		// Create new question-form and fill it with the values from the other page
		Form<Question> boundQuestion = newQuestionForm.bindFromRequest();
		Question newQuestion = boundQuestion.get();
		Question.create(newQuestion);
//		Quiz.createQuestion(newQuestion);
		
		System.out.println("Quizquestion created!");
		
		questionListAll.add(newQuestion);
		Collections.sort(questionListAll, Collections.reverseOrder());
//		return ok(views.html.index.render(questionListAll, answerListAll));
		 return redirect(routes.Application.index());
	}
	
	// Write an answer, goto answerpage
	@Security.Authenticated(Secured.class)
	public static Result writeAnswer(String questionIDInput){
		Nutzer formUser = Nutzer.find.byId(request().username());
		System.out.println("Name FormUser: " + formUser.name);
		
		Answer answerWithQuestionID = new Answer(generateFakeID(), questionIDInput, null, null, formUser.email, 1);
		Form<Answer> preFilledAnswer = newAnswerForm.fill(answerWithQuestionID);
		
		System.out.println("formAnswer: " + preFilledAnswer.toString());
		
		return ok(views.html.antwortGeben.render(preFilledAnswer));
	}
	
	// Send answer to indexpage
	public static Result sendAnswer(){
		Form<Answer> boundAnswer = newAnswerForm.bindFromRequest();
		Answer newAnswer = boundAnswer.get();
		Answer.create(newAnswer);
		answerListAll.add(newAnswer);
		
		Collections.sort(answerListAll, Collections.reverseOrder());
		
		// Redirect to the index-page because else you land on /Antwort and not /index
		// Also its recommended to use PRG: http://en.wikipedia.org/wiki/Post/Redirect/Get
		return redirect(routes.Application.index());
	}
	
	// TODOL Show the users page, will need an ID for a routing paramter
	public static Result showUsers(){
		return TODO;
	}
	
	// TODO Remove in production, only for development. Populates the DB with fake entries
	public static Result initDB(){
		initialize();
		System.out.println("DB initialized");
		return redirect(routes.Application.index());
	}
	
	// Quizpage
	@Security.Authenticated(Secured.class)
	public static Result startQuiz() {
		/**
		 * What do I need for a quiz?
		 * Interval - only in fixed time periods can a new quiz be generated, else the old one will be shown
		 * Should I keep already given answers for new versions?
		 * Threshold - Quiz collects questions based on voting, but not lower than 0?
		 * get positive Q/As from DB, if user has not answered yet -> show
		 * 
		 * --------
		 * Quiz geht durch alle Fragen
		 * - wählt random eine aus, deren letzte Stellzeit - aktuelle Zeit < Interval ist
		 * - wenn Nutzer richtig antwortet Interval erhöhen
		 * - wenn Nutzer falsch antwortet, Interval auf 0 setzen (also sofort wieder)
		 * 
		 */
		// Construct a randomized list of questions, depending on if the user has already seen the question in the quiz before
		List<Answer> answerList = new ArrayList<Answer>();
		List<Question> randomQuestionList = new ArrayList<Question>();

		// Find all questions, put them into list
		for (Question questionItem : Question.find.all()) {
			randomQuestionList.add(questionItem);
		}
		

		
		// Take a random question from the list, clear list, put the left behind item in it
		Question randomQuestion = randomQuestionList.get(new Random().nextInt(randomQuestionList.size())); 
		randomQuestionList.clear();
		randomQuestionList.add(randomQuestion);
		
		
		for (Answer answerItem : Answer.find.all()) {
			if(answerItem.questionID == randomQuestion.questionID){
				answerList.add(answerItem);
			}
		}
		
		// Shuffle the answers, so the correct answer is not always on top
		Collections.shuffle(answerList);
		
		
		return ok(views.html.quiz.render(randomQuestionList, answerList));
	}
	
	public static Result nextQuizPage(){
		/**
		 * if user has answered correct
		 * - save interval into quiz db
		 * - goto next question
		 */
		
		
		return redirect(routes.Application.startQuiz());
	}
	
	// Method for voting on questions / answers, gets a map from an AJAX POST in the view class
	// The parameters are Q/A ID and voteScore, new score gets saved in DB
	// TODOL Duplicate code in voteUp() and voteDown() -> extract!
	public static Result voteUp(){
		Map<String, String[]> parameters = request().body().asFormUrlEncoded();
		String questionIDInput = parameters.get("questionID")[0];
		String voteScoreInput = parameters.get("score")[0];
		String buttonType = parameters.get("type")[0];
		
		if(buttonType.equals("questionButton")){
			Question changeQuestion = Question.find.byId(questionIDInput);
			// voteScore needs to get +1 as the score gets AJAXed after the click
			changeQuestion.voteScore = Integer.parseInt(voteScoreInput) + 1;
			changeQuestion.save();
		}
		
		else if(buttonType.equals("answerButton")){
			Answer changeAnswer = Answer.find.byId(questionIDInput);
			// voteScore needs to get +1 as the score gets AJAXed after the click
			changeAnswer.voteScore = Integer.parseInt(voteScoreInput) + 1;
			changeAnswer.save();
		}
		return ok(views.html.index.render(questionListAll, answerListAll));
	}
	
	// Method for voting on questions / answers, gets a map from an AJAX POST in the view class
		// The parameters are Q/A ID and voteScore, new score gets saved in DB
	// TODOL Duplicate code in voteUp() and voteDown() -> extract!
		public static Result voteDown(){
			Map<String, String[]> parameters = request().body().asFormUrlEncoded();
			String questionIDInput = parameters.get("questionID")[0];
			String voteScoreInput = parameters.get("score")[0];
			String buttonType = parameters.get("type")[0];
			
			if(buttonType.equals("questionButton")){
				Question changeQuestion = Question.find.byId(questionIDInput);
				// voteScore needs to get +1 as the score gets AJAXed after the click
				changeQuestion.voteScore = Integer.parseInt(voteScoreInput) - 1;
				changeQuestion.save();
			}
			
			else if(buttonType.equals("answerButton")){
				Answer changeAnswer = Answer.find.byId(questionIDInput);
				// voteScore needs to get +1 as the score gets AJAXed after the click
				changeAnswer.voteScore = Integer.parseInt(voteScoreInput) - 1;
				changeAnswer.save();
			}
			return ok(views.html.index.render(questionListAll, answerListAll));
		}
		
		// Gets an ID from the form in the view class, finds the matching question and updates it
		public static Result editQuestion(String questionIDInput){
			List<Question> questionHelper = new ArrayList<Question>();
			for (Question questionItem : Question.find.all()) {
				questionHelper.add(questionItem);
			}
			Question helper = Question.find.byId(questionIDInput);
			// Fill a form with the last entered values
			Form<Question> preFilledQuestion = newQuestionForm.fill(helper);
			return ok(views.html.editQuestion.render(preFilledQuestion, questionHelper));
		}
		
		// Sends an edited Question
		public static Result sendEditedQuestion(){
			// Get the input text by a request
			Form<Question> boundQuestion = newQuestionForm.bindFromRequest();
			Question newQuestion = boundQuestion.get();
			// Delete the old question, create a new with the entered input
			// TODOL Should the question really be deleted and created? No method for update()?
			// Ebean.update(QUESTION); might work?
			Question.find.byId(newQuestion.questionID).delete();
			Question.create(newQuestion);
			
			questionListAll.add(newQuestion);
			// Sort the list, so best rated goes to the top
			Collections.sort(questionListAll, Collections.reverseOrder());
			return redirect(routes.Application.index());
		}
		
		// Similar to editQuestion()
		public static Result editAnswer(String answerIDInput){
			List<Answer> answerHelper = new ArrayList<Answer>();
			for (Answer answerItem : Answer.find.all()) {
				answerHelper.add(answerItem);
			}
			Answer helper = Answer.find.byId(answerIDInput);
			Form<Answer> preFilledAnswer = newAnswerForm.fill(helper);
			return ok(views.html.editAnswer.render(preFilledAnswer, answerHelper));
		}
		
		// Similar to sendEditedQuestion()
		public static Result sendEditedAnswer(){
			Form<Answer> boundAnswer = newAnswerForm.bindFromRequest();
			Answer newAnswer = boundAnswer.get();
			Answer.find.byId(newAnswer.answerID).delete();
			Answer.create(newAnswer);
			answerListAll.add(newAnswer);
			Collections.sort(questionListAll, Collections.reverseOrder());
			return redirect(routes.Application.index());
		}
		
		// Inner class to handle login
		public static class Login {
		    public String email;
		    public String password;
    
		    public String validate() {
			    if (Nutzer.authenticate(email, password) == null) {
			    	System.out.println("validate() has been called");
			      return "Invalid user or password";
			    }
			    return null;
			}
		}
		
		public static class Register{
			public String name;
			public String email;
			public String password;
		}
		
		// TODOL Remove, just for development
		public static void createAndRetrieveUser() {
	        new Nutzer("bob@mail.com", "Bob", "secret").save();
	        new Nutzer("marcus@mail.com", "Marcus", "12345").save();
	        new Nutzer("tim@mail.com", "Tim", "12345").save();
	    }
		
		public static Result login() {
			// TODOL Remove, just for development
//			createAndRetrieveUser();
			return ok(views.html.login.render(Form.form(Login.class)));
		}
		
		public static Result logout() {
		    session().clear();
		    flash("success", "You've been logged out");
		    return redirect(routes.Application.login());
		}
		
		public static Result authenticate() {
		    Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		    if (loginForm.hasErrors()) {
		        return badRequest(views.html.login.render(loginForm));
		    } else {
		        session().clear();
		        session("email", loginForm.get().email);
		        return redirect(routes.Application.index());
		    }
		}
		
		public static Result register(){
			return ok(views.html.register.render(Form.form(Register.class)));
		}
		
		public static Result registerUser(){
			Form<Register> registerForm = Form.form(Register.class).bindFromRequest();
			Nutzer formNutzer = new Nutzer(registerForm.get().email, registerForm.get().name, registerForm.get().password);
			formNutzer.save();
			return redirect(routes.Application.index());
		}
}
