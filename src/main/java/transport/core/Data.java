package transport.core;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Data {
    // In-memory storage
    private static final List<TitreTransport> titresVendus = new ArrayList<>();
    private static final List<Personne> utilisateurs = new ArrayList<>();

    // Load data when class is first accessed
    static {
        try {
            List<Personne> loadedUsers = Persistance.restaurerUtilisateurs();
            if (loadedUsers != null) {
                utilisateurs.addAll(loadedUsers);
                System.out.println("Loaded " + loadedUsers.size() + " users from file");
            }
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    // Fare media operations
    public static void ajouterTitre(TitreTransport titre) {
        titresVendus.add(0, titre);
    }

    public static List<TitreTransport> getTitresVendus() {
        return Collections.unmodifiableList(titresVendus);
    }

    // User operations
    public static void ajouterUtilisateur(Personne personne) {
        utilisateurs.add(personne);
    }

    public static List<Personne> getUtilisateurs() {
        return new ArrayList<>(utilisateurs); // Return a copy, not unmodifiable list
    }

    public static void setUtilisateurs(List<Personne> liste) {
        utilisateurs.clear();
        utilisateurs.addAll(liste);
    }



}
