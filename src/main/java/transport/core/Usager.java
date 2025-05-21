package transport.core;

import java.time.LocalDate;

public class Usager extends Personne {
    private static final long serialVersionUID = 1L;
    private static int dernierId = 1000; // Starting sequence

    private String idUsager;  // Auto-generated ID

    public Usager(String nom, String prenom, LocalDate dateNaissance, boolean handicape) {
        super(nom, prenom, dateNaissance, handicape);
        this.idUsager = generateId();
    }

    // Constructor for loading existing users
    public Usager(String idUsager, String nom, String prenom, LocalDate dateNaissance, boolean handicape) {
        super(nom, prenom, dateNaissance, handicape);
        this.idUsager = idUsager;
    }

    // Auto-generate USR-XXXX ID (thread-safe)
    private static synchronized String generateId() {
        return "USR-" + (++dernierId);
    }

    // Getters
    public String getIdUsager() { return idUsager; }

    // Static methods for ID management
    public static int getDernierId() { return dernierId; }
    public static void setDernierId(int id) { dernierId = id; }
}
