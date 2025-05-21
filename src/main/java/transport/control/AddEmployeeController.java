package transport.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import transport.core.Data;
import transport.core.Employe;
import transport.core.Persistance;
import transport.core.TypeFonction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddEmployeeController extends BaseController {

    @FXML private TextField txtNom, txtPrenom, txtMatricule;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox chkHandicape;
    @FXML private ComboBox<String> comboFunction;

    @FXML
    private void initialize() {
        comboFunction.getItems().addAll("AGENT", "CONDUCTEUR");
    }

    @FXML
    private void handleSaveEmployee() {
        List<String> errors = new ArrayList<>();

        // Validate all fields
        if (txtNom.getText().trim().isEmpty()) errors.add("First name is required");
        if (txtPrenom.getText().trim().isEmpty()) errors.add("Last name is required");
        if (txtMatricule.getText().trim().isEmpty()) errors.add("Employee ID is required");
        if (datePicker.getValue() == null) {
            errors.add("Birth date is required");
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            errors.add("Birth date cannot be in the future");
        }
        if (comboFunction.getValue() == null) errors.add("Function must be selected");

        // Show all errors if any exist
        if (!errors.isEmpty()) {
            showErrorAlert("\n• " + String.join("\n• ", errors));
            return;
        }

        try {
            Employe employee = new Employe(
                    txtNom.getText().trim(),
                    txtPrenom.getText().trim(),
                    datePicker.getValue(),
                    chkHandicape.isSelected(),
                    txtMatricule.getText().trim(),
                    TypeFonction.valueOf(comboFunction.getValue())
            );

            Data.ajouterUtilisateur(employee);
            Persistance.sauvegarderUtilisateurs(Data.getUtilisateurs());
            showSuccessAlert("Employee saved successfully!");
            clearFields();

            // Navigate to Acceuil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/acceuil.fxml")); // Fix path
            Parent root = loader.load();
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (Exception e) {
            showErrorAlert("System error: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        txtMatricule.clear();
        datePicker.setValue(null);
        chkHandicape.setSelected(false);
        comboFunction.setValue(null);
    }
<<<<<<< HEAD
}
=======

}
>>>>>>> 8b306ef72eac9bc0181e554256839715b1efdcd1
