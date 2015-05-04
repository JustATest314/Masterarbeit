package controllers;

import java.util.*;

import model.Answer;
import model.Question;
import play.mvc.*;

public class Forum extends Controller {

	static Map<Question, List<Answer>> myMap = new HashMap<Question, List<Answer>>();
	
	// Returns all questions and answers
	public static List<Forum> listAll() {
		return new ArrayList<Forum>();
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
