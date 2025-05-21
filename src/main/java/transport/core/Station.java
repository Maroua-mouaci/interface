package transport.core;

public class Station implements Suspendable {
    private String nom;
    private boolean suspendu;
    private int nombreReclamations;

    public Station(String nom) {
        this.nom = nom;
        this.suspendu = false; // la station est active par défaut
        this.nombreReclamations = 0;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public void suspendre() {
        suspendu = true;
    }

    @Override
    public void reactiver() {
        suspendu = false;
    }

    @Override
    public boolean estSuspendu() {
        return suspendu;
    }

    @Override
    public String getEtat() {
        return suspendu ? "suspendu" : "actif";
    }

    @Override
    public String toString() {
        return "Station de " + nom;
    }

    // Gestion des réclamations

    @Override
    public void incrementerReclamations() {
        nombreReclamations++;
        if (nombreReclamations > 3) {
            suspendre();
        }
    }

    @Override
    public int getNombreReclamations() {
        return nombreReclamations;
    }
}
