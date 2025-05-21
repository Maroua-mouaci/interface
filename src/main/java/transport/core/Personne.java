package transport.core;

import java.time.*;
public abstract class Personne {
    protected String nom;
    protected String prenom;
    protected LocalDate dateNaissance;
    protected boolean handicap;

    public Personne(String nom, String prenom, LocalDate dateNaissance,boolean handicap) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.handicap = handicap;
    }
    public boolean isHandicape(){return handicap;}
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
}

