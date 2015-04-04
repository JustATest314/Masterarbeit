package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import play.Logger;

public class Application extends Controller {

	final static Form<User> userForm = Form.form(User.class);

	public static Result index() {
		return ok(views.html.index.render(userForm));
	}

	public static Result submit() {
		// Harris Benedict Formel fuer Grundumsatz (Mann)
		// 66,47 + (13,7 * Körpergewicht [kg]) + (5 * Körpergröße [cm]) – (6,8 *
		// Alter [Jahre])

		// Formel fuer Frauen
		// 655,1 + (9,6 * Körpergewicht [kg]) + (1,8 * Körpergröße [cm]) – (4,7
		// * Alter [Jahre])
		Form<User> filledForm = userForm.bindFromRequest();
		User created = filledForm.get();

		// For debug
		 Logger.info(created.toString());

		if (created.geschlecht.equals("Mann")) {
			created.grundUmsatz = (float) (66.47 + (13.7 * created.gewicht)
					+ (5 * created.groesse) - (6.8 * created.alter));
		}

		else if (created.geschlecht.equals("Frau")) {
			created.grundUmsatz = (float) (655.1 + (9.6 * created.gewicht)
					+ (1.8 * created.groesse) - (4.7 * created.alter));
		}
		return ok(views.html.submit.render(created));
		// For debug only
		// return status(488, "Strange response type");
	}

}
