package transport.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import transport.core.Data;
import transport.core.Persistance;
import transport.core.Usager;
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

        // Validate all fields
        if (txtNom.getText().trim().isEmpty()) errors.add("First name is required");
        if (txtPrenom.getText().trim().isEmpty()) errors.add("Last name is required");
        if (datePicker.getValue() == null) {
            errors.add("Birth date is required");
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            errors.add("Birth date cannot be in the future");
        }

        // Show all errors if any exist
        if (!errors.isEmpty()) {
            showErrorAlert("\n• " + String.join("\n• ", errors));
            return;
        }

        try {
            Usager usager = new Usager(
                    txtNom.getText().trim(),
                    txtPrenom.getText().trim(),
                    datePicker.getValue(),
                    chkHandicape.isSelected()
            );

            Data.ajouterUtilisateur(usager);
            Persistance.sauvegarderUtilisateurs(Data.getUtilisateurs());

            // Show success alert with generated ID
            showSuccessAlert("Passenger saved successfully!\nYour ID: " + usager.getIdUsager());

            clearFields();
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