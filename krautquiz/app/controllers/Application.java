package controllers;

import java.util.ArrayList;
import java.util.List;

import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	static List<String> listOfQuestions = new ArrayList<String>();
	static List<String> listOfAnswers = new ArrayList<String>();
	
	// Hilfsmethode, um Daten während der Entwicklung zu mocken
	public static void QAinitialisieren(){
		
		// Listen löschen, da die Elemente angehängt werden, gäbe sonst doppelte Einträge
		listOfQuestions.clear();
		listOfAnswers.clear();
		
    	//Fragen
    	listOfQuestions.add("Was passiert, wenn man ein 'break' verwendet?");
    	listOfQuestions.add("Wieviele Fälle darf ein switch-case haben?");
    	listOfQuestions.add("Geht das auch mit liste.clear()?");
    	
    	//Antworten
    	listOfAnswers.add("Die Schleife springt einfach durch");
    	listOfAnswers.add("Das Programm hängt sich auf");
    	listOfAnswers.add("Ich weiss es selbst nicht...");
    }

	public static Result index() {
    	QAinitialisieren();
        return ok(index.render(listOfQuestions, listOfAnswers));
    }

    public static Result quizStarten(){
    	QAinitialisieren();
    	return ok(views.html.quiz.render(listOfQuestions, listOfAnswers));
    }
    
    public static Result zeigeEinstellungen(){
    	return ok(views.html.einstellungen.render());
    }
}
