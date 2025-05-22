package transport.control;

import java.time.LocalDate;
import java.time.Period;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transport.core.*;

public class AjouterReclamationController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private CheckBox handicapeCheck;

    @FXML private RadioButton rbTechnique;
    @FXML private RadioButton rbService;
    @FXML private RadioButton rbPayment;
    @FXML private ToggleGroup complaintTypeGroup;

    @FXML private ComboBox<Suspendable> targetComboBox;

    @FXML private TextArea descriptionField;  // Champ description

    @FXML private Label msgLabel;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    public void initialize() {
        ObservableList<Suspendable> targets = FXCollections.observableArrayList(
                new MoyenTransport("Bus-101"),
                new MoyenTransport("Metro-A3"),
                new Station("Khelifa Boukhalfa"),
                new Station("Tafourah Grande Poste")
        );
        targetComboBox.setItems(targets);
    }

    @FXML
    private void handleSubmit() {
        msgLabel.setText("");

        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        boolean handicape = handicapeCheck.isSelected();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null) {
            showError("Tous les champs personnels sont requis.");
            return;
        }

        if (Period.between(dateNaissance, LocalDate.now()).getYears() < 18) {
            showError("Vous devez avoir au moins 18 ans.");
            return;
        }

        RadioButton selectedRadio = (RadioButton) complaintTypeGroup.getSelectedToggle();
        if (selectedRadio == null) {
            showError("Veuillez choisir un type de réclamation.");
            return;
        }

        TypeReclamation typeReclamation;
        switch (selectedRadio.getText()) {
            case "Technical": typeReclamation = TypeReclamation.TECHNIQUE; break;
            case "Service":   typeReclamation = TypeReclamation.SERVICE;   break;
            case "Payment":   typeReclamation = TypeReclamation.PAIEMENT;  break;
            default:
                showError("Type de réclamation inconnu.");
                return;
        }

        Suspendable cible = targetComboBox.getValue();
        if (cible == null) {
            showError("Veuillez sélectionner une cible.");
            return;
        }

        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            showError("Veuillez écrire la description de la réclamation.");
            return;
        }

        // Créer l'usager
        Usager usager = new Usager(nom, prenom, dateNaissance, handicape);

        // Créer la réclamation
        Complaint complaint = new Complaint(typeReclamation, cible, description, usager);

        // Ajouter la réclamation via service
        serviceReclamation.ajouterReclamation(complaint);

        if (cible.estSuspendu()) {
            showMessage("Réclamation enregistrée. La cible est suspendue suite à trop de réclamations !", true);
        } else {
            showMessage("Réclamation enregistrée avec succès.", false);
        }

        clearForm();
    }

    private void showError(String message) {
        msgLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        msgLabel.setText(message);
    }

    private void showMessage(String message, boolean danger) {
        String color = danger ? "red" : "green";
        msgLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
        msgLabel.setText(message);
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        handicapeCheck.setSelected(false);
        complaintTypeGroup.selectToggle(null);
        targetComboBox.getSelectionModel().clearSelection();
        descriptionField.clear();
    }
}
