package transport.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class CartePersonnelle extends TitreTransport {
    private Personne proprietaire;
    private TypeCarte type;

    public CartePersonnelle(Personne personne, TypePaiement methodePaiement)
            throws ReductionImpossibleException {
        super(5000); // Base price
        this.proprietaire = personne;
        this.type = calculerTypeCarte();
        this.setMethodePaiement(methodePaiement);
        this.id = Persistance.getNextId();  // Get next ID from shared counter
        this.dateAchat = LocalDateTime.now();  // Set purchase timestamp

        if (this.type == null)
            throw new ReductionImpossibleException(" : aucune réduction applicable.");

        switch (type) {
            case JUNIOR: this.prix *= 0.7; break;
            case SENIOR: this.prix *= 0.75; break;
            case SOLIDARITE: this.prix *= 0.5; break;
            case PARTENAIRE: this.prix *= 0.6; break;
        }
    }
    private TypeCarte calculerTypeCarte() {
        LocalDate auj = LocalDate.now();
        int age = Period.between(proprietaire.getDateNaissance(), auj).getYears();

        boolean isEmploye = proprietaire instanceof Employe;
        boolean isHandicap = proprietaire instanceof Usager && ((Usager)proprietaire).isHandicape();
        boolean isJunior = age < 25;
        boolean isSenior = age > 65;

        if (isEmploye)
            return TypeCarte.PARTENAIRE;
        else if (isHandicap)
            return TypeCarte.SOLIDARITE;
        else if (isJunior)
            return TypeCarte.JUNIOR;
        else if (isSenior)
            return TypeCarte.SENIOR;

        return null;
    }

    public TypeCarte getType() { return this.type; }

    public Personne getProprietaire() { return proprietaire; }

    public void setProprietaire(Personne proprietaire) { this.proprietaire = proprietaire; }

    @Override
    public boolean estValide(LocalDate date) throws TitreNonValideException {
        if (date.isBefore(dateAchat.toLocalDate().plusYears(1))) {
            return true;
        } else {
            throw new TitreNonValideException("Carte expirée.");
        }
    }
}
