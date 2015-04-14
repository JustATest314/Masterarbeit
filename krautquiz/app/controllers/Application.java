package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    // TODO PDF Anzeigen lassen
    public static Result pdfAnzeigen(){
    	return TODO;
    }
    
    // TODO Quiz erzeugen lassen
    public static Result quizGenerieren(){
    	return TODO;
    }
    
}
