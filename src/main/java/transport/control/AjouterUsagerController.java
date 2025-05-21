package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.Data;
import transport.core.Persistance;
import transport.core.Usager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AjouterUsagerController extends BaseController {

    @FXML private TextField txtNom, txtPrenom;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox chkHandicape;

    @FXML
    private void handleSaveUsager() {
        List<String> errors = new ArrayList<>();

        // Validation des champs
        if (txtNom.getText().trim().isEmpty()) errors.add("First name is required");
        if (txtPrenom.getText().trim().isEmpty()) errors.add("Last name is required");
        if (datePicker.getValue() == null) {
            errors.add("Birth date is required");
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            errors.add("Birth date cannot be in the future");
        }

        // Affichage des erreurs
        if (!errors.isEmpty()) {
            showErrorAlert("\n• " + String.join("\n• ", errors));
            return;
        }

        try {
            // Création de l'usager
            Usager usager = new Usager(
                    txtNom.getText().trim(),
                    txtPrenom.getText().trim(),
                    datePicker.getValue(),
                    chkHandicape.isSelected()
            );

            // Sauvegarde
            Data.ajouterUtilisateur(usager);
            Persistance.sauvegarderUtilisateurs(Data.getUtilisateurs());

            // Message de succès
            showSuccessAlert("Passenger saved successfully!\nYour ID: " + usager.getIdUsager());

            // Nettoyage des champs
            clearFields();

            // ✅ Redirection vers Accueil.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/view/Accueil.fxml"));
            Parent accueilRoot = loader.load();
            Scene scene = new Scene(accueilRoot);
            Stage stage = (Stage) txtNom.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showErrorAlert("Navigation error: could not load Accueil.fxml.");
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("System error: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        datePicker.setValue(null);
        chkHandicape.setSelected(false);
    }
}
