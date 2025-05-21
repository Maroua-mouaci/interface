package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class AccueilController {

    @FXML
    private void goToUserType(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/ui/UserType.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load UserType.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAcheterTitre(ActionEvent event) {
        try {
            System.out.println("Navigating to purchase screen...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/ui/AcheterTitre.fxml"));
            Parent root = loader.load();

            // Get controller and refresh user list
            PurchaseFareController controller = loader.getController();
            controller.refreshUserList();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            System.err.println("Failed to load purchase screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void goToListeTitre(ActionEvent event) throws IOException {
        loadScreen("/transport/ui/ListeTitre.fxml", event);
    }

    @FXML
    private void goToValiderTitre(ActionEvent event) throws IOException {
        loadScreen("/transport/ui/ValiderTitre.fxml", event);
    }

    @FXML
    private void goToAjouterReclamation(ActionEvent event) throws IOException {
        loadScreen("/transport/ui/AjouterReclamation.fxml", event);
    }

    @FXML
    private void goToRecordedComplains(ActionEvent event) throws IOException {
        loadScreen("/transport/ui/RecordedComplains.fxml", event);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Fermer l'application
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Méthode utilitaire
    private void loadScreen(String fxmlPath, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
        stage.show(); // Add this line
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleViewFareMedia() {
        try {
            Parent fareView = FXMLLoader.load(getClass().getResource("/transport/ui/ViewFareMedia.fxml"));
            Stage stage = new Stage();
            stage.setTitle("View Fare Media");
            stage.setScene(new Scene(fareView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
