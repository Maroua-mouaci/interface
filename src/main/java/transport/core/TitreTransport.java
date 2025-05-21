package transport.core;

import java.io.Serializable;
import java.time.*;

public abstract class TitreTransport implements Serializable {
    protected int id;
    protected LocalDateTime dateAchat;
    protected double prix;
    protected TypePaiement methodePaiement;

    private static int compteur = 0;

    public TitreTransport(double prix) {
        this.id = ++compteur;
        this.dateAchat = LocalDateTime.now();
        this.prix = prix;
    }

    public TypePaiement getMethodePaiement() { return methodePaiement; }
    public void setMethodePaiement(TypePaiement methode) { this.methodePaiement = methode; }

    public int getId() { return id; }

    public LocalDateTime getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDateTime dateAchat) { this.dateAchat = dateAchat; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public abstract boolean estValide(LocalDate date) throws TitreNonValideException;
}
