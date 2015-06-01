package models;

import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Nutzer extends Model{
	
	private static final long serialVersionUID = 1L;

	// FIXME Dont really save the password as String...
	
	@Id
	public String userID;
	public String email;
	public String name;
	public String password;


	public static Nutzer authenticate(String email, String password) {
        return find.where().eq("email", email).eq("password", password).findUnique();
    }
	
	public Nutzer(String userID, String email, String name, String password) {
		this.userID = userID;
		this.email = email;
	    this.name = name;
	    this.password = password;
	}

	public static Finder<String,Nutzer> find = new Finder<String,Nutzer>(
	        String.class, Nutzer.class
	); 
	
}
