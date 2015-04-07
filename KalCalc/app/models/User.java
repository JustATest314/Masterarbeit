package models;

public class User {
	public Integer gewicht;
	public Integer groesse;
	public Integer alter;

	public String geschlecht;

	public Float grundUmsatz;
	
	public User(){
		gewicht = 0;
		groesse = 0;
		alter = 0;
		geschlecht = "Mann";
	}

	public String toString() {
		 return "Gewicht " + this.gewicht + " Groesse " + this.groesse +
		 " Alter " + this.alter + " GU " + this.grundUmsatz + " Geschlecht " +
		 this.geschlecht;
	
	}
}
