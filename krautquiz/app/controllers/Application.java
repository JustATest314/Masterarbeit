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

public class Application extends Controller {

//	static List<String> listOfQuestions = new ArrayList<String>();
//	static List<String> listOfAnswers = new ArrayList<String>();
	
//	static List<String> questionList = new ArrayList<String>();
//	static List<String> answerList = new ArrayList<String>();
	
	static Map<Question, List<Answer>> myMap = new HashMap<Question, List<Answer>>();
	
//	  private static void log(Object aObject){
//		    System.out.println( String.valueOf(aObject) );
//		  }
	
	// helper-method, only for mocking data while developing

	// TODOH Should look like this:
	// questionMap.put(ID, List<String>) with List<String> = qText, votes, uid
	// but how can I get the values then? I.e. how to get the qText and put it into a list, so the index-page can show them?
	public static void initialize() {
		
//		Multimap<String, String> questionMap = ArrayListMultimap.create();
//		Multimap<String, String> answerMap = ArrayListMultimap.create();
//		
//		// FIXME add field "type" (question | answer) and make the ID-field the first one 
//		// Question in the format: Questiontext / ID / votes / UID
//		// Question 1
//		questionMap.put("What happens if I use a break here?", "e77dccbc-fd8d-4641-b9ca-17528e5d56b2");
//		questionMap.put("What happens if I use a break here?", "150");
//		questionMap.put("What happens if I use a break here?", "Marcus");
//		
//		// Answer in the format: Answertext / ID / Question-ID (Answer needs to be linked to a question) / votes / UID
//		// Answer 1.1
//		answerMap.put("The loop will just fall through!", "b8756ff5-ff8a-4a17-9517-811b91639fdf");
//		answerMap.put("The loop will just fall through!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b2");
//		answerMap.put("The loop will just fall through!", "46");
//		answerMap.put("The loop will just fall through!", "Lothar");
//	
//		// Question 2
//		questionMap.put("How many cases must a switchcase have?", "e77dccbc-fd8d-4641-b9ca-17528e5d56b3");
//		questionMap.put("How many cases must a switchcase have?", "34");
//		questionMap.put("How many cases must a switchcase have?", "Frank");
//		
//		// Answer in the format: Answertext / ID / Question-ID (Answer needs to be linked to a question) / votes / UID
//		// Answer 2.1
//		answerMap.put("As many as you want!", "b8756ff5-ff8a-4a17-9517-811b91639fdg");
//		answerMap.put("As many as you want!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b3");
//		answerMap.put("As many as you want!", "12");
//		answerMap.put("As many as you want!", "Lothar");
//		
//		// Answer 2.2
//		answerMap.put("None!", "b8756ff5-ff8a-4a17-9517-811b91639fdh");
//		answerMap.put("None!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b3");
//		answerMap.put("None!", "9");
//		answerMap.put("None!", "Steffi");
//		
//		// Question 3
//		questionMap.put("Will this work with list.clear()?", "e77dccbc-fd8d-4641-b9ca-17528e5d56b4");
//		questionMap.put("Will this work with list.clear()?", "92");
//		questionMap.put("Will this work with list.clear()?", "Stefan");
//		
//		// Answer in the format: Answertext / ID / Question-ID (Answer needs to be linked to a question) / votes / UID
//		// Answer 3.1
//		answerMap.put("Sure!", "b8756ff5-ff8a-4a17-9517-811b91639fdi");
//		answerMap.put("Sure!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b4");
//		answerMap.put("Sure!", "2");
//		answerMap.put("Sure!", "Maria");
		
//		ListMultimap<String,String> myMultimap = ArrayListMultimap.create();
//		 
//		myMultimap.put("Fruits", "Bannana");
//		myMultimap.put("Fruits", "Apple");
//		myMultimap.put("Fruits", "Pear");
//		myMultimap.put("Vegetables", "Carrot");
//		
//		List<String> myValues = myMultimap.get("Fruits");
//		System.out.println("MyValues: " + myValues.toString());
		
//		Logger.info("KeySet: " + myMultimap.keySet().toString());
//		Logger.info("Keys: " + myMultimap.keys().toString());
		
//		System.out.println("answermap.keySet(): ");
//		Logger.info(answerMap.keySet().toString());
		
//		System.out.println("for over answerMap.values(): ");
//		for(String key : answerMap.keys()) {
//			Logger.info(key + answerMap.values() + "\n");
//		}
		
//		// both lists contain only the keySets (questions / answers at the moment)
//		questionList.addAll(questionMap.keySet());
//		answerList.addAll(answerMap.keySet());
		

		
		// Logger.info(questionMap.keys().toString());
		
//		// Clear lists, as the elements get appended. Else there would be duplicates
//		listOfQuestions.clear();
//		listOfAnswers.clear();
//
//		// Questions
//		listOfQuestions.add("Was passiert, wenn man ein 'break' verwendet?");
//		listOfQuestions.add("Wieviele Fälle darf ein switch-case haben?");
//		listOfQuestions.add("Geht das auch mit liste.clear()?");
//
//		// Answers
//		listOfAnswers.add("Die Schleife springt einfach durch");
//		listOfAnswers.add("Das Programm hängt sich auf");
//		listOfAnswers.add("Ich weiss es selbst nicht...");
		
//		System.out.println("--- questionMap ---");
//		Logger.info(questionMap.toString());
//		System.out.println("--- answerMap ---");
//		Logger.info(answerMap.toString());
		
//		// Multimap von Guava Google eingefügt, wird vermutlich die neue Datenstruktur
//		Multimap<String,String> myMultimap = ArrayListMultimap.create();
//
//		myMultimap.put("Frage 1", "id = sd");
//		myMultimap.put("Frage 1", "qtext = dsf");
//		myMultimap.put("Frage 1", "vote = 100");
//		myMultimap.put("Frage 2", "id = ds");
//		myMultimap.putAll("Frage 1", listOfAnswers);
//		
//		Logger.info(myMultimap.toString());
//		
//		System.out.println("-------------");
//		
//		for(String key : myMultimap.keys()) {
//			Logger.info(key + myMultimap.values());
//		}
//		
//		myMultimap.remove("Frage 1", "vote = 100");
//		myMultimap.put("Frage 1", "vote = 200");
//		
//		System.out.println("-------------");
//		
//		for(String key : myMultimap.keys()) {
//			Logger.info(key + myMultimap.values());
//		}
//		
//		List<String> test = new ArrayList<String>();
//		Logger.info(myMultimap.get("Frage 1").toString());
//		test.addAll(myMultimap.get("Frage 1"));
//		Logger.info("Testliste: " + test.toString());
//		
//		if(myMultimap.containsValue("vote = 200")){
//			Logger.info("Contains is true");
//		}
//		
//		// So generiert man eindeutige IDs, können für die QA-IDs benutzt werden
//		UUID idOne = UUID.randomUUID();
//	    UUID idTwo = UUID.randomUUID();
//	    log("UUID One: " + idOne);
//	    log("UUID Two: " + idTwo);
		
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
		
//		Map<Question, List<Answer>> questionHashMap = new HashMap<Question, List<Answer>>();
//		questionHashMap.put(frage1, antwortenListe1);
//		questionHashMap.put(frage2, antwortenListe2);
		
//		for ( Question elem : questionHashMap.keySet() ){
//			  System.out.println("Q-HashMap: " + elem.toString() );
//			  for (List<Answer> elem2 : questionHashMap.values()){
//
//			  }
//		}
		
		myMap.clear();
		myMap.put(question1, answerList1);
		myMap.put(question2, answerList2);
		myMap.put(question3, answerList3);
		
//		for(Question key : myMultimap.keys()) {
//			System.out.println(key + myMultimap.get(key).toString());
//		}
//		
//		System.out.println("===================");
//		System.out.println(myMultimap.toString());
	}

	public static Result index() {
		initialize();
		return ok(views.html.index.render(myMap));
//		return ok(index.render(listOfQuestions, listOfAnswers));
	}

	public static Result startQuiz() {
		initialize();
		return ok(views.html.quiz.render(myMap));
//		return ok(views.html.quiz.render(listOfQuestions, listOfAnswers));
	}

	public static Result showSettings() {
		System.out.println("settings");
		Logger.info("settings"); 
		return ok(views.html.einstellungen.render());
	}
	
	public static Result askQuestion(){
		return ok(views.html.frageAntwort.render(myMap));	
	}
	
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
