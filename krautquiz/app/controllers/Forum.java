package controllers;

import java.util.*;
import play.mvc.*;

public class Forum extends Controller {

	public String frage;
	public String antwort;
	
	public Integer rating;
	
	public Long id;
	
	// Gibt alle Fragen / Antworten zurueck
	public static List<Forum> auflisten() {
		return new ArrayList<Forum>();
	}
	
	// TODO Frage stellen
	public static Result frageStellen(){
		return ok(views.html.frageAntwort.render());	
	}
	
	// TODO Antwort geben
	public static Result antwortGeben(){
		return TODO;
	}
	
	// TODO Frage oder Antwort bewerten
	public static Result bewerten(){
		return TODO;
	}
	
	// TODO Frage / Antwort loeschen
	public static Result loeschen(){
		return TODO;
	}
}
