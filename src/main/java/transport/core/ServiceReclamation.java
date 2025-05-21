package transport.core;

import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation {
    private final List<Complaint> reclamations = new ArrayList<>();

    public void ajouterReclamation(Complaint c) {
        reclamations.add(c);
        Suspendable cible = c.getCible();
        cible.incrementerReclamations();

        if (cible.getNombreReclamations() > 3 && !cible.estSuspendu()) {
            cible.suspendre();
        }
    }

    public List<Complaint> getListeReclamations() {
        return reclamations;
    }

    public void clearReclamations() {
        reclamations.clear();
    }

    public int getNombreReclamationsPourCible(Suspendable cible) {
        int count = 0;
        for (Complaint c : reclamations) {
            if (c.getCible().equals(cible)) {
                count++;
            }
        }
        return count;
    }
}
