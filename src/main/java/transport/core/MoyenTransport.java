package transport.core;

public class MoyenTransport implements Suspendable {
    private String identifiant;
    private boolean suspendu;
    private int nombreReclamations;

    public MoyenTransport(String identifiant) {
        this.identifiant = identifiant;
        this.suspendu = false; // Le moyen de transport est actif par défaut
        this.nombreReclamations = 0;
    }

    public String getIdentifiant() {
        return identifiant;
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
        return identifiant;
    }

    // Méthodes de gestion des réclamations
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
