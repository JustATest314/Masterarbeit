package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

//	static List<String> listOfQuestions = new ArrayList<String>();
//	static List<String> listOfAnswers = new ArrayList<String>();
	
	static List<String> questionList = new ArrayList<String>();
	static List<String> answerList = new ArrayList<String>();
	
	  private static void log(Object aObject){
		    System.out.println( String.valueOf(aObject) );
		  }
	
	// helper-method, only for mocking data while developing

	// TODOH Should look like this:
	// questionMap.put(ID, List<String>) with List<String> = qText, votes, uid
	// but how can I get the values then? I.e. how to get the qText and put it into a list, so the index-page can show them?
	public static void QAinitialisieren() {
		
		Multimap<String, String> questionMap = ArrayListMultimap.create();
		Multimap<String, String> answerMap = ArrayListMultimap.create();
		
		// FIXME add field "type" (question | answer) and make the ID-field the first one 
		// Question in the format: Questiontext / ID / votes / UID
		// Question 1
		questionMap.put("What happens if I use a break here?", "e77dccbc-fd8d-4641-b9ca-17528e5d56b2");
		questionMap.put("What happens if I use a break here?", "150");
		questionMap.put("What happens if I use a break here?", "Marcus");
		
		// Answer in the format: Answertext / ID / Question-ID (Answer needs to be linked to a question) / votes / UID
		// Answer 1.1
		answerMap.put("The loop will just fall through!", "b8756ff5-ff8a-4a17-9517-811b91639fdf");
		answerMap.put("The loop will just fall through!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b2");
		answerMap.put("The loop will just fall through!", "46");
		answerMap.put("The loop will just fall through!", "Lothar");
	
		// Question 2
		questionMap.put("How many cases must a switchcase have?", "e77dccbc-fd8d-4641-b9ca-17528e5d56b3");
		questionMap.put("How many cases must a switchcase have?", "34");
		questionMap.put("How many cases must a switchcase have?", "Frank");
		
		// Answer in the format: Answertext / ID / Question-ID (Answer needs to be linked to a question) / votes / UID
		// Answer 2.1
		answerMap.put("As many as you want!", "b8756ff5-ff8a-4a17-9517-811b91639fdg");
		answerMap.put("As many as you want!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b3");
		answerMap.put("As many as you want!", "12");
		answerMap.put("As many as you want!", "Lothar");
		
		// Answer 2.2
		answerMap.put("None!", "b8756ff5-ff8a-4a17-9517-811b91639fdh");
		answerMap.put("None!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b3");
		answerMap.put("None!", "9");
		answerMap.put("None!", "Steffi");
		
		// Question 3
		questionMap.put("Will this work with list.clear()?", "e77dccbc-fd8d-4641-b9ca-17528e5d56b4");
		questionMap.put("Will this work with list.clear()?", "92");
		questionMap.put("Will this work with list.clear()?", "Stefan");
		
		// Answer in the format: Answertext / ID / Question-ID (Answer needs to be linked to a question) / votes / UID
		// Answer 3.1
		answerMap.put("Sure!", "b8756ff5-ff8a-4a17-9517-811b91639fdi");
		answerMap.put("Sure!", "e77dccbc-fd8d-4641-b9ca-17528e5d56b4");
		answerMap.put("Sure!", "2");
		answerMap.put("Sure!", "Maria");
		
		ListMultimap<String,String> myMultimap = ArrayListMultimap.create();
		 
		myMultimap.put("Fruits", "Bannana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		myMultimap.put("Vegetables", "Carrot");
		
		List<String> myValues = myMultimap.get("Fruits");
//		System.out.println("MyValues: " + myValues.toString());
		
//		Logger.info("KeySet: " + myMultimap.keySet().toString());
//		Logger.info("Keys: " + myMultimap.keys().toString());
		
//		System.out.println("answermap.keySet(): ");
//		Logger.info(answerMap.keySet().toString());
		
//		System.out.println("for over answerMap.values(): ");
//		for(String key : answerMap.keys()) {
//			Logger.info(key + answerMap.values() + "\n");
//		}
		
		// both lists contain only the keySets (questions / answers at the moment)
		questionList.addAll(questionMap.keySet());
		answerList.addAll(answerMap.keySet());
		

		
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
		
		

	}

	public static Result index() {
		QAinitialisieren();
		return ok(index.render(questionList, answerList));
//		return ok(index.render(listOfQuestions, listOfAnswers));
	}

	public static Result quizStarten() {
		QAinitialisieren();
		return ok(views.html.quiz.render(questionList, answerList));
//		return ok(views.html.quiz.render(listOfQuestions, listOfAnswers));
	}

	public static Result zeigeEinstellungen() {
		return ok(views.html.einstellungen.render());
	}
}
