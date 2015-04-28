package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	static List<String> listOfQuestions = new ArrayList<String>();
	static List<String> listOfAnswers = new ArrayList<String>();

	
	  private static void log(Object aObject){
		    System.out.println( String.valueOf(aObject) );
		  }
	
	// Hilfsmethode, um Daten während der Entwicklung zu mocken

	// TODOH Müsste umgestellt werden auf Array? Irgendwas, wo man Fragen, Scores
	// und dazu gehörende Antworten mit Scores reintun kann
	public static void QAinitialisieren() {
		
		// Multimap von Guava Google eingefügt, wird vermutlich die neue Datenstruktur
		Multimap<String,String> myMultimap = ArrayListMultimap.create();

		myMultimap.put("Frage 1", "id = sd");
		myMultimap.put("Frage 1", "qtext = dsf");
		myMultimap.put("Frage 1", "vote = 100");
		myMultimap.put("Frage 2", "id = ds");
		
		System.out.println("-------------");
		
		for(String key : myMultimap.keys()) {
			Logger.info(key + myMultimap.values());
		}
		
		myMultimap.remove("Frage 1", "vote = 100");
		myMultimap.put("Frage 1", "vote = 200");
		
		System.out.println("-------------");
		
		for(String key : myMultimap.keys()) {
			Logger.info(key + myMultimap.values());
		}
		
		List<String> test = new ArrayList<String>();
		Logger.info(myMultimap.get("Frage 1").toString());
		test.addAll(myMultimap.get("Frage 1"));
		Logger.info("Testliste: " + test.toString());
		
		if(myMultimap.containsValue("vote = 200")){
			Logger.info("Contains is true");
		}
		
		// So generiert man eindeutige IDs, können für die QA-IDs benutzt werden
		UUID idOne = UUID.randomUUID();
	    UUID idTwo = UUID.randomUUID();
	    log("UUID One: " + idOne);
	    log("UUID Two: " + idTwo);
		
		
		// Listen löschen, da die Elemente angehängt werden, gäbe sonst doppelte
		// Einträge
		listOfQuestions.clear();
		listOfAnswers.clear();

		// Fragen
		listOfQuestions.add("Was passiert, wenn man ein 'break' verwendet?");
		listOfQuestions.add("Wieviele Fälle darf ein switch-case haben?");
		listOfQuestions.add("Geht das auch mit liste.clear()?");

		// Antworten
		listOfAnswers.add("Die Schleife springt einfach durch");
		listOfAnswers.add("Das Programm hängt sich auf");
		listOfAnswers.add("Ich weiss es selbst nicht...");
	}

	public static Result index() {
		QAinitialisieren();
		return ok(index.render(listOfQuestions, listOfAnswers));
	}

	public static Result quizStarten() {
		QAinitialisieren();
		return ok(views.html.quiz.render(listOfQuestions, listOfAnswers));
	}

	public static Result zeigeEinstellungen() {
		return ok(views.html.einstellungen.render());
	}
}
