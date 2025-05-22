package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Complaint implements Comparable<Complaint>, Serializable {
    private static final long serialVersionUID = 1L;
    private static int compteur = 1;

    private final int numero;
    private final LocalDate date;
    private final TypeReclamation type;
    private final Suspendable cible;
    private final String description;
    private final Personne personne;

    // Constructor with current date
    public Complaint(TypeReclamation type, Suspendable cible, String description, Personne personne) {
        this(LocalDate.now(), type, cible, description, personne);
    }

    // Constructor with specific date
    public Complaint(LocalDate date, TypeReclamation type, Suspendable cible, String description, Personne personne) {
        this.numero = compteur++;
        this.date = Objects.requireNonNull(date);
        this.type = Objects.requireNonNull(type);
        this.cible = Objects.requireNonNull(cible);
        this.description = Objects.requireNonNull(description);
        this.personne = Objects.requireNonNull(personne);
    }

    // Getters
    public int getNumero() {
        return numero;
    }

    public LocalDate getDate() {
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

    // Comparable: compare by numero
    @Override
    public int compareTo(Complaint other) {
        return Integer.compare(this.numero, other.numero);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Complaint)) return false;
        Complaint other = (Complaint) obj;
        return this.numero == other.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }

    @Override
    public String toString() {
        return "[" + type + "] " + cible + " - " + date;
    }
}
