package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	final static Form<User> userForm = Form.form(User.class);
	
	public static Result index() {
		return ok(views.html.index.render(userForm));
	}

	public static Result anzeigen() {
		return ok(views.html.unterseite1.render());
	}

	public static Result anzeigen2() {
		return ok(views.html.unterseite2.render());
	}

	public static Result nextPage() {
		String[] postAction = request().body().asFormUrlEncoded().get("action");
		String action = postAction[0];
		if ("next".equals(action)) {
			
			return anzeigen2();
		}
		return badRequest("Nope");
	}
	
	public static Result submit(){
//		Harris Benedict Formel fuer Grundumsatz
//		66,47 + (13,7 * Körpergewicht [kg]) + (5 * Körpergröße [cm]) – (6,8 * Alter [Jahre])
		Form<User> filledForm = userForm.bindFromRequest();
		User created = filledForm.get();
		created.grundUmsatz = (float) (66.47 + (13.7 * created.gewicht) + (5 * created.groesse) - (6.8 * created.alter));
		return ok(views.html.submit.render(created));
	}

	
}
