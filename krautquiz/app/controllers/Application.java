package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import models.Answer;
import models.Nutzer;
import models.Question;
import models.Quiz;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/*
 * This is the application controller. It contains the most of the logic at the moment. 
 * TODO The class should be split into several smaller controllers as it has become quite long now.
 */

// TODOL Votescores are Integer, make sure they are not bigger than MAX_INT!
public class Application extends Controller {

	// ArrayList might not be performant when there are many add / delete operations.
	// If need be -> use LinkedList!
	public static List<Question> questionListAll = new ArrayList<Question>();
	public static List<Answer> answerListAll = new ArrayList<Answer>();
	
	public static final Form<Question> newQuestionForm = Form.form(Question.class);
	public static final Form<Answer> newAnswerForm = Form.form(Answer.class);
	
	// Construct a randomized list of questions, depending on if the user has already seen the question in the quiz before
	public static List<Answer> answerList = new ArrayList<Answer>();
	public static List<Question> randomQuestionList = new ArrayList<Question>();
	
	static Form<Answer> answerForm = Form.form(Answer.class);
	
	public static boolean usersCreatedForLogin = false;
	
	public static Integer currentPage = 1;
	
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
	public static Result index(Integer page) {
		questionListAll.clear();
		answerListAll.clear();
		
		// Get all questions from DB
		// TODO Collect only the questions / answers by the page they are on. Should be easy by using "currentPage"
		for (Question questionItem : Question.find.all()) {
			if(questionItem.page.equals(page)){
				questionListAll.add(questionItem);
			}
		}
		
		// Get all answers from DB
		for (Answer answerItem : Answer.find.all()) {
			answerListAll.add(answerItem);
		}
		Collections.sort(questionListAll, Collections.reverseOrder());
		Collections.sort(answerListAll, Collections.reverseOrder());
		
		return ok(views.html.index.render(questionListAll, answerListAll, page));
	}

	// Settings
	// TODO Give the user some choices for things like colors etc. At the moment this only shows some answered questions for the evaluation
	@Security.Authenticated(Secured.class)
	public static Result showSettings() {
		questionListAll.clear();
		for (Quiz quizItem : Quiz.find.where().like("user_id", request().username()).like("interval", "5000").findList()) {
			Question questionEntry = Question.find.where().like("question_id", quizItem.questionID).findUnique();
			questionListAll.add(questionEntry);
		}
		return ok(views.html.einstellungen.render(questionListAll, answerListAll));
	}
	
	// Go to the ask question page
	@Security.Authenticated(Secured.class)
	public static Result askQuestion(){
		Nutzer formUser = Nutzer.find.byId(request().username());
		Question questionToAsk = new Question(generateFakeID(), null, 1, formUser.email, 1);
		Form<Question> preFilledQuestion = newQuestionForm.fill(questionToAsk);
		return ok(views.html.frageAntwort.render(preFilledQuestion));
	}
	
	// Send the question to the indexpage
	// TODOL Use Bootstraps "Modal" to smoothly glide into the view
	// http://getbootstrap.com/javascript/#modals
	@Security.Authenticated(Secured.class)
	public static Result sendQuestion(){
		// Create new question-form and fill it with the values from the other page
		Form<Question> boundQuestion = newQuestionForm.bindFromRequest();
		Question newQuestion = boundQuestion.get();
		Question.create(newQuestion);
//		Quiz.createQuestion(newQuestion);
		System.out.println("BoundQuestion: " + boundQuestion.toString());
		questionListAll.add(newQuestion);
		Collections.sort(questionListAll, Collections.reverseOrder());
//		return ok(views.html.index.render(questionListAll, answerListAll));
		 return redirect(routes.Application.index(1));
	}
	
	// Write an answer, goto answerpage
	@Security.Authenticated(Secured.class)
	public static Result writeAnswer(String questionIDInput){
		Nutzer formUser = Nutzer.find.byId(request().username());
		Answer answerWithQuestionID = new Answer(generateFakeID(), questionIDInput, null, 1, formUser.email, 1);
		Form<Answer> preFilledAnswer = newAnswerForm.fill(answerWithQuestionID);
		Question questionToAnswer = Question.find.byId(questionIDInput);
		return ok(views.html.antwortGeben.render(preFilledAnswer, questionToAnswer));
	}
	
	// Send answer to indexpage
	@Security.Authenticated(Secured.class)
	public static Result sendAnswer(){
		Form<Answer> boundAnswer = newAnswerForm.bindFromRequest();
		System.out.println("boundAnswer: " + boundAnswer.toString());
//		String testString = newAnswerForm.data().get("testValueAnswerID");
//		System.out.println("testString: " + testString);
		Answer newAnswer = boundAnswer.get();
		Answer.create(newAnswer);
		answerListAll.add(newAnswer);
		
		Collections.sort(answerListAll, Collections.reverseOrder());
		
		// Redirect to the index-page because else you land on /Antwort and not /index
		// Also its recommended to use PRG: http://en.wikipedia.org/wiki/Post/Redirect/Get
		return redirect(routes.Application.index(1));
	}
	
	// TODOL Show the users page, will need an ID for a routing paramter
	public static Result showUsers(){
		return TODO;
	}
	
	// TODO Remove in production, only for development. Populates the DB with fake entries
	@Security.Authenticated(Secured.class)
	public static Result initDB(){
		initialize();
		return redirect(routes.Application.index(1));
	}
	
	// Quizpage
	@Security.Authenticated(Secured.class)
	public static Result startQuiz() {
		Nutzer currentUser = Nutzer.find.byId(request().username());
		randomQuestionList.clear();
		answerList.clear();

		// If Quiz-Table empty, get the first question and make an entry for the random question list
		if(Quiz.find.all().isEmpty()){
			
			// Go through all questions and put them into a list
			List<Question> allQuestionsList = new ArrayList<Question>();
			for (Question question : Question.find.all()) {
				allQuestionsList.add(question);
			}
			// Get a random item from the question list
			// This is the first Question
			Question randomFirstQuestion = allQuestionsList.get(new Random().nextInt(allQuestionsList.size())); 
			randomQuestionList.add(randomFirstQuestion);
			allQuestionsList.clear();
		}
		
		// Quiz table NOT empty
		else {
			// If there are still open questions in the quiz table -> ask them
			// Are there still quizQuestions for current user?
			List<Quiz> quizList1 = Quiz.find.where().like("user_id", currentUser.email).like("interval", "0").findList(); 
			
			if(quizList1.size() > 0){
				for (Quiz quizItem : quizList1) {
					Question findUniqueQuestion = Question.find.where().like("question_ID", quizItem.questionID).findUnique();
					// There can only be one question that matches the questionID
					randomQuestionList.add(findUniqueQuestion);
				}
				quizList1.clear();
			}
			
			// TODO If question answered wrongly, it gets asked immediately again -> clear any of the lists?
			// If there are no open questions in the quiz table, 
			// but unasked question from the question table
			if(quizList1.size() == 0){
				for (Question questionItem : Question.find.all()) {
					if(Quiz.find.where().like("question_id", questionItem.questionID).like("user_id", currentUser.email).findUnique() == null){
						randomQuestionList.add(questionItem);	
					}
				}
				quizList1.clear();
			}
			quizList1.clear();
		}
			
		if(randomQuestionList.size() > 0){
			// Take a random question from the list, clear list, put the left behind item in it
			Question randomQuestion = randomQuestionList.get(new Random().nextInt(randomQuestionList.size())); 
			randomQuestionList.clear();
			randomQuestionList.add(randomQuestion);	

			for (Answer answerItem : Answer.find.all()) {
				if(answerItem.questionID == randomQuestion.questionID){
					answerList.add(answerItem);
				}
			}
		}
		
		// Shuffle the answers, so the correct answer is not always on top
		Collections.shuffle(answerList);
		return ok(views.html.quiz.render(randomQuestionList, answerList, answerForm));
	}
	
	// The user has answered a question on the quiz page and wants to get the next question
	// The answered question gets updated whether it was correctly or wrongly answered
	@Security.Authenticated(Secured.class)
	public static Result nextQuizPage(){
		Nutzer currentUser = Nutzer.find.byId(request().username());
		// Get the answerID from the form
		Form<Answer> filledForm = answerForm.bindFromRequest();
		String answerIDfromForm = filledForm.data().get("answer");
		
		// If button for next page gets clicked without any question to ask, redirect to quiz page
		if(filledForm.data().get("answer") == null){
			return redirect(routes.Application.startQuiz());
		}
		// Another Quizquestion is available
		else{
			// With the clicked answer get the matching Question
			Answer clickedRadioAnswer = Answer.find.where().like("answer_ID", answerIDfromForm).findUnique();
			Question matchingQuestionRadio = Question.find.where().like("question_ID", clickedRadioAnswer.questionID).findUnique();
			
			// Find the best voted answer for the question
			List<Answer> highestAnswerList = Answer.find.where().like("question_id", clickedRadioAnswer.questionID).findList();
			// Works, as compareTo() in Answer.java has been overridden and sorts for voteScore
			Answer bestAnswer = Collections.max(highestAnswerList);
			
			// Quizquestion already answered by current user
			List<Quiz> tempQuizList = Quiz.find.where().like("question_ID", clickedRadioAnswer.questionID).like("user_id", currentUser.email).findList();
			
			// If at least one entry in quiz-table matches the question that has been asked
			if( tempQuizList.size() > 0 ){
				
				for (Quiz quizItem : tempQuizList) {
					// TODOL Is this if necessary? tempQuizList already checks user
					if((quizItem.userID).equals(currentUser.email)){
						if(clickedRadioAnswer.answerID.equals(bestAnswer.answerID)){
							Quiz.updateAnswer(clickedRadioAnswer.questionID, currentUser.email, 5000);
						} 
						if(!clickedRadioAnswer.answerID.equals(bestAnswer.answerID)){
							Quiz.updateAnswer(clickedRadioAnswer.questionID, currentUser.email, 0);
						}
					}
				}
			}
			
			// Quiz is empty or user has no quizquestion open
			if(tempQuizList.size() == 0){
				if(clickedRadioAnswer.answerID.equals(bestAnswer.answerID)){
					Quiz.createAnswer(clickedRadioAnswer, currentUser.email, 5000);
				} 
				else{
					Quiz.createAnswer(clickedRadioAnswer, currentUser.email, 0);
				}
			}
			tempQuizList.clear();
			randomQuestionList.clear();
		}
		
		return redirect(routes.Application.startQuiz());
	}
	
	// Method for voting on questions / answers, gets a map from an AJAX POST in the view class
	// The parameters are Q/A ID and voteScore, new score gets saved in DB
	// TODOL Duplicate code in voteUp() and voteDown() -> extract!
	@Security.Authenticated(Secured.class)
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
		return ok(views.html.index.render(questionListAll, answerListAll, currentPage));
	}
	
	// Method for voting on questions / answers, gets a map from an AJAX POST in the view class
	// The parameters are Q/A ID and voteScore, new score gets saved in DB
	// TODOL Duplicate code in voteUp() and voteDown() -> extract!
	@Security.Authenticated(Secured.class)
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
			return ok(views.html.index.render(questionListAll, answerListAll, currentPage));
		}
		
	// Gets an ID from the form in the view class, finds the matching question and updates it
	@Security.Authenticated(Secured.class)
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
	@Security.Authenticated(Secured.class)
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
		return redirect(routes.Application.index(1));
	}

	// Similar to editQuestion()
	@Security.Authenticated(Secured.class)
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
	@Security.Authenticated(Secured.class)
	public static Result sendEditedAnswer(){
		Form<Answer> boundAnswer = newAnswerForm.bindFromRequest();
		Answer newAnswer = boundAnswer.get();
		Answer.find.byId(newAnswer.answerID).delete();
		Answer.create(newAnswer);
		answerListAll.add(newAnswer);
		Collections.sort(questionListAll, Collections.reverseOrder());
		return redirect(routes.Application.index(1));
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

	// Inner class for registering new users
	public static class Register{
		public String name;
		public String email;
		public String password;
	}

	// TODOL Remove, just for development
	public static void createAndRetrieveUser() {
		usersCreatedForLogin = true;
		new Nutzer("bob@mail.com", "Bob", "secret").save();
		new Nutzer("marcus@mail.com", "Marcus", "secret").save();
		new Nutzer("admin@mail.com", "Admin", "secret").save();
		
		new Nutzer("susanne@mail.com", "Susanne", "sodifu").save();
		new Nutzer("friedrich@mail.com", "Friedrich", "coiwucu").save();
		new Nutzer("hendrik@mail.com", "Hendrik", "cpsoiwp").save();
		new Nutzer("sebastian@mail.com", "Sebastian", "sdmnfbd").save();
		new Nutzer("steffen@mail.com", "Steffen", "sidfzz").save();
	}

	public static Result login() {
		// TODOL Remove, just for development
		if(!usersCreatedForLogin){
			createAndRetrieveUser();
		}
		return ok(views.html.login.render(Form.form(Login.class)));
	}

	public static Result logout() {
		usersCreatedForLogin = true;
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.login());
	}

	// Checks, if the user may enter the webapplication
	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(views.html.login.render(loginForm));
		} else {
			session().clear();
			session("email", loginForm.get().email);
			return redirect(routes.Application.index(1));
		}
	}

	public static Result register(){
		return ok(views.html.register.render(Form.form(Register.class)));
	}

	public static Result registerUser(){
		Form<Register> registerForm = Form.form(Register.class).bindFromRequest();
		Nutzer formNutzer = new Nutzer(registerForm.get().email, registerForm.get().name, registerForm.get().password);
		formNutzer.save();
		return redirect(routes.Application.index(1));
	}
}
