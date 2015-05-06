package controllers;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;

/*
 * This controller class will probably get deleted soon, as most of the methods can better be dealt with by the application controller class
 */

public class Forum extends Controller {

	// Returns all questions and answers
	public static List<Forum> listAll() {
		return new ArrayList<Forum>();
	}
	
	// TODO Give Answer
	public static Result giveAnswer(){
		return TODO;
	}
	
	// TODO Delete question or answer
	public static Result delete(){
		return TODO;
	}
}
