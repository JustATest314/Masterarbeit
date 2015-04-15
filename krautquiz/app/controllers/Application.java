package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    // TODO Quiz erzeugen lassen
    public static Result quizGenerieren(){
    	return TODO;
    }
    
    public static Result zeigeEinstellungen(){
    	return ok(views.html.einstellungen.render());
    }
    
}
