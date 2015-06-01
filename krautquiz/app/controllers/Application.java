package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.Answer;
import models.Nutzer;
import models.Question;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
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
	
	public static List<Question> highestRankedQuestionList = new ArrayList<Question>();
	public static List<Answer> highestRankedAnswerList = new ArrayList<Answer>();
	
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
		
		Question question1 = new Question(questionFakeID1, "Do Androids dream?", 76, "Marcus", 1);
		Answer answer11 = new Answer(generateFakeID(), questionFakeID1, "Only of electric sheep!", 70, "Tibor", 1);
		Answer answer12 = new Answer(generateFakeID(), questionFakeID1, "No, they dont!", 10, "Sarah", 1);
		
		String questionFakeID2 = generateFakeID();

		Question question2 = new Question(questionFakeID2, "Why is the sky blue?", 124, "Frank", 1);
		Answer answer21 = new Answer(generateFakeID(), questionFakeID2, "Frequency filtered sunlight!", 45, "Oliver", 1);
		Answer answer22 = new Answer(generateFakeID(), questionFakeID2, "Light reflects from the blue sea water!", 3, "Tom", 1);
		
		String questionFakeID3 = generateFakeID();
		
		Question question3 = new Question(questionFakeID3, "How tall is tall?", 34, "Tim", 1);
		Answer answer31 = new Answer(generateFakeID(), questionFakeID3, "Depends on your definition!", 12, "Oliver", 1);
		Answer answer32 = new Answer(generateFakeID(), questionFakeID3, "Very!", 1, "Marcus", 1);
		Answer answer33 = new Answer(generateFakeID(), questionFakeID3, "Not much!", 1, "Frank", 1);
		
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
	public static Result askQuestion(){
		// TODO questionHelper not needed? Check and remove!
		List<Question> questionHelper = new ArrayList<Question>();
		for (Question questionItem : Question.find.all()) {
			questionHelper.add(questionItem);
		}
		
		Question answerWithQuestionID = new Question(generateFakeID(), null, null, null, 1);
		Form<Question> preFilledQuestion = newQuestionForm.fill(answerWithQuestionID);
		
		return ok(views.html.frageAntwort.render(preFilledQuestion, questionHelper));
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
		Collections.sort(questionListAll, Collections.reverseOrder());
		return ok(views.html.index.render(questionListAll, answerListAll));
		// TODOL Not working, why not?
		// return redirect(routes.Application.index());
	}
	
	// Write an answer, goto answerpage
	@Security.Authenticated(Secured.class)
	public static Result writeAnswer(String questionIDInput){
		// TODO answerHelper not needed in Production, remove
		List<Answer> answerHelper = new ArrayList<Answer>();
		
		for (Answer answerItem : Answer.find.all()) {
			answerHelper.add(answerItem);
		}
	
		Nutzer formUser = Nutzer.find.byId(request().username());
		System.out.println("Name FormUser: " + formUser.name);
		Answer answerWithQuestionID = new Answer(generateFakeID(), questionIDInput, null, null, request().username(), 1);
		Form<Answer> preFilledAnswer = newAnswerForm.fill(answerWithQuestionID);
		
		System.out.println("writeAnswer() username: " + request().username());
		
		return ok(views.html.antwortGeben.render(preFilledAnswer, answerHelper));
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
	// TODO If 2 questions have the exact same score, always the first one will get taken and never the next one
	// Maybe add some randomness?
	public static Result startQuiz() {
		highestRankedQuestionList.clear();
		highestRankedAnswerList.clear();
		Integer highestVotescore = 0;
		Question highestRankedQuestion = new Question();
		
		// Gets all entries from the database and finds the highest ranked Question
		for (Question questionItem : Question.find.all()) {
			if (questionItem.voteScore > highestVotescore){
				highestRankedQuestion = questionItem;
				highestVotescore = questionItem.voteScore;
			}
		}	
		highestRankedQuestionList.add(highestRankedQuestion);
		highestVotescore = 0;
		
		// List will only have 1 item - the highest ranked question!
		for (Answer answerItem : Answer.find.all()) {
			if (answerItem.questionID == highestRankedQuestion.questionID){
				highestRankedAnswerList.add(answerItem);
			}
		}
		return ok(views.html.quiz.render(highestRankedQuestionList, highestRankedAnswerList));
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
		
		// TODOL Remove, just for development
		public static void createAndRetrieveUser() {
	        new Nutzer(generateFakeID(), "bob@mail.com", "Bob", "secret").save();
	        new Nutzer(generateFakeID(), "marcus@mail.com", "Marcus", "12345").save();
	        new Nutzer(generateFakeID(), "tim@mail.com", "Tim", "12345").save();
	    }
		
		public static Result login() {
			// TODOL Remove, just for development
			createAndRetrieveUser();
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
		        System.out.println(loginForm.get().email);
		        
		        for (Nutzer nutzer : Nutzer.find.all()) {
		        	System.out.println("UserIDs: " + nutzer.userID);
		        }
		        return redirect(routes.Application.index());
		    }
		}
		
		
		
		
}
