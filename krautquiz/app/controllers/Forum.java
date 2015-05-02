package controllers;

import java.util.*;
import play.mvc.*;

public class Forum extends Controller {

	// Returns all questions and answers
	public static List<Forum> listAll() {
		return new ArrayList<Forum>();
	}
	
	// TODO Ask question
	public static Result askQuestion(){
		return ok(views.html.frageAntwort.render());	
	}
	
	// TODO give Answer
	public static Result giveAnswer(){
		return TODO;
	}
	
	// TODO Rate question or answer
	public static Result voteScore(){
		return TODO;
	}
	
	// TODO Delete question or answer
	public static Result delete(){
		return TODO;
	}
	
}
