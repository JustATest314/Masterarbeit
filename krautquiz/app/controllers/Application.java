package controllers;

import java.util.ArrayList;
import java.util.List;

import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result quizStarten(){
    	
    	// Fragen
    	List<String> listOfQuestions = new ArrayList<String>();
    	listOfQuestions.add("Was passiert, wenn man ein 'break' verwendet?");

    	// Antworten
    	List<String> listOfAnswers = new ArrayList<String>();
    	listOfAnswers.add("Die Schleife springt einfach durch");
    	listOfAnswers.add("Das Programm hängt sich auf");
    	listOfAnswers.add("Ich weiss es auch nicht...");
    	
    	return ok(views.html.quiz.render(listOfQuestions, listOfAnswers));
    }
    
    public static Result zeigeEinstellungen(){
    	return ok(views.html.einstellungen.render());
    }
    
}
