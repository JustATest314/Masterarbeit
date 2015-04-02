package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result anzeigen(){
    	return ok(views.html.unterseite1.render());
    }
    
    public static Result anzeigen2(){
    	return ok(views.html.unterseite2.render());
    }

}
