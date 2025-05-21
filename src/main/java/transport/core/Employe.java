package transport.core;

import java.io.Serializable;
import java.time.*;
public class Employe extends Personne implements Serializable {
    private String matricule;
    private TypeFonction fonction;

    public Employe(String nom, String prenom, LocalDate dateNaissance, boolean handicape,
                   String matricule, TypeFonction fonction) {
        super(nom, prenom, dateNaissance != null ? dateNaissance : LocalDate.now(), handicape);//remove that ?
        this.matricule = matricule;
        this.fonction = fonction;
    }

    public String getMatricule() { return matricule; }
    public TypeFonction getFonction() { return fonction; }
}
