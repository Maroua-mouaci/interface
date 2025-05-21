package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.Data;
import transport.core.Employe;
import transport.core.Persistance;
import transport.core.TypeFonction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddEmployeeController extends BaseController {

    @FXML private TextField txtNom, txtPrenom, txtMatricule;
    @FXML private ComboBox<String> comboFunction;
    @FXML private Button btnSave;

    @FXML
    private void initialize() {
        comboFunction.getItems().addAll("AGENT", "CONDUCTEUR");
    }

    @FXML
    private void handleSaveEmployee() {
        List<String> errors = new ArrayList<>();

        if (txtNom.getText().trim().isEmpty()) errors.add("First name is required");
        if (txtPrenom.getText().trim().isEmpty()) errors.add("Last name is required");
        if (txtMatricule.getText().trim().isEmpty()) errors.add("Employee ID is required");
        if (comboFunction.getValue() == null) errors.add("Function must be selected");

        if (!errors.isEmpty()) {
            showErrorAlert(errors.stream().map(e -> "• " + e).reduce("", (a, b) -> a + "\n" + b));
            return;
        }

        try {
            Employe employee = new Employe(
                    txtNom.getText().trim(),
                    txtPrenom.getText().trim(),
                    null,
                    false,
                    txtMatricule.getText().trim(),
                    TypeFonction.valueOf(comboFunction.getValue())
            );

            Data.ajouterUtilisateur(employee);
            Persistance.sauvegarderUtilisateurs(Data.getUtilisateurs());
            showSuccessAlert("Employee saved successfully!");
            clearFields();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/acceuil.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showErrorAlert("Failed to load the main screen: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showErrorAlert("Invalid function selected.");
        } catch (Exception e) {
            showErrorAlert("System error: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        txtMatricule.clear();
        comboFunction.setValue(null);
    }
}
