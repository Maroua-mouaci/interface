package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class UserTypeController {

    @FXML private RadioButton radioUsager;
    @FXML private RadioButton radioEmployee;
    private ToggleGroup userTypeGroup;

    @FXML
    private void initialize() {
        // Create a new ToggleGroup
        userTypeGroup = new ToggleGroup();

        // Assign the ToggleGroup to both RadioButtons
        radioUsager.setToggleGroup(userTypeGroup);
        radioEmployee.setToggleGroup(userTypeGroup);

        // Optional: Set a default selection
        radioUsager.setSelected(true);
    }


    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            String fxmlPath;
            if (radioUsager.isSelected()) {
                fxmlPath = "/transport/ui/AjouterUsager.fxml"; // Path to Usager screen
            } else {
                fxmlPath = "/transport/ui/AddEmployee.fxml";   // Path to Employee screen
            }

            // Debug: Print the selected path
            System.out.println("Loading: " + fxmlPath);

            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the next screen: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}