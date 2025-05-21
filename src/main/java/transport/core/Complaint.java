package transport.core;

import java.util.Date;

public class Complaint implements Comparable<Complaint> {
    private static int compteur = 1;
    private int numero;
    private Date date;
    private TypeReclamation type;
    private Suspendable cible;
    private String description;
    private Personne personne;

    public Complaint(Date date, TypeReclamation type, Suspendable cible, String description, Personne personne) {
        this.numero = compteur++;
        this.date = date;
        this.type = type;
        this.cible = cible;
        this.description = description;
        this.personne = personne;
    }

    public int getNumero() {
        return numero;
    }

    public Date getDate() {
        return date;
    }

    public TypeReclamation getType() {
        return type;
    }

    public Suspendable getCible() {
        return cible;
    }

    public String getDescription() {
        return description;
    }

    public Personne getPersonne() {
        return personne;
    }

    @Override
    public int compareTo(Complaint other) {
        return Integer.compare(this.numero, other.numero);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Complaint)) return false;
        Complaint other = (Complaint) obj;
        return this.numero == other.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }
}
