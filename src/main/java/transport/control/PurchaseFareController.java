package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.*;

import java.io.IOException;
import java.util.List;

public class PurchaseFareController {

    @FXML private ComboBox<Object> userComboBox;
    @FXML private RadioButton radioTicket, radioPersonalCard;
    @FXML private ComboBox<String> paymentComboBox;
    private ToggleGroup fareTypeGroup;

    @FXML
    public void initialize() {
        // Initialize toggle group for fare type
        fareTypeGroup = new ToggleGroup();
        radioTicket.setToggleGroup(fareTypeGroup);
        radioPersonalCard.setToggleGroup(fareTypeGroup);
        radioTicket.setSelected(true);

        // Populate payment methods
        paymentComboBox.getItems().addAll("CASH", "DAHABIA", "BARIDI_MOB");

        // Load and display users
        refreshUserList();
    }

    public void refreshUserList() {
        ObservableList<Object> items = FXCollections.observableArrayList();
        List<Personne> users = Data.getUtilisateurs();

        items.add("Employee");
        users.stream().filter(u -> u instanceof Employe).forEach(items::add);

        items.add("Usager");
        users.stream().filter(u -> u instanceof Usager).forEach(items::add);

        userComboBox.setItems(items);

        // Cell factory for displaying user info
        userComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setDisable(false);
                } else if (item instanceof String) {
                    setText((String) item);
                    setStyle("-fx-font-weight: bold; -fx-background-color: #f0f0f0;");
                    setDisable(true);
                } else if (item instanceof Employe emp) {
                    setText(emp.getNom() + " " + emp.getPrenom() + " (Matricule: " + emp.getMatricule() + ")");
                    setStyle("");
                    setDisable(false);
                } else if (item instanceof Usager usr) {
                    setText(usr.getNom() + " " + usr.getPrenom() + " (ID: " + usr.getIdUsager() + ")");
                    setStyle("");
                    setDisable(false);
                }
            }
        });

        // Button cell for showing selected user
        userComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item instanceof String) {
                    setText(null);
                } else if (item instanceof Employe emp) {
                    setText(emp.getNom() + " " + emp.getPrenom() + " (Matricule: " + emp.getMatricule() + ")");
                } else if (item instanceof Usager usr) {
                    setText(usr.getNom() + " " + usr.getPrenom() + " (ID: " + usr.getIdUsager() + ")");
                }
            }
        });
    }

    @FXML
    private void handlePurchaseAction() {
        Object selectedUser = userComboBox.getValue();
        String selectedPayment = paymentComboBox.getValue();

        if (!(selectedUser instanceof Personne) || selectedPayment == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid user and payment method.");
            return;
        }

        Personne user = (Personne) selectedUser;
        TypePaiement paiement;

        try {
            paiement = TypePaiement.valueOf(selectedPayment);
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid payment method.");
            return;
        }

        try {
            TitreTransport fare;

            if (radioTicket.isSelected()) {
                fare = new Ticket(paiement);
                showAlert(Alert.AlertType.INFORMATION, "Ticket Purchased", formatFareMessage(fare));
            } else {
                fare = new CartePersonnelle(user, paiement);
                showAlert(Alert.AlertType.INFORMATION, "Card Purchased", formatFareMessage(fare));
            }

            // Add to Data and save through Persistance
            Data.ajouterTitre(fare);
            Persistance.sauvegarderFares(fare);

        } catch (ReductionImpossibleException e) {
            showAlert(Alert.AlertType.ERROR, "Card Error", "❌ No reduction applicable for this user.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error", "❌ Could not save the fare to file.");
        }

        // Return to Accueil.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/view/Accueil.fxml"));
            Parent accueilRoot = loader.load();
            Scene scene = new Scene(accueilRoot);
            Stage stage = (Stage) userComboBox.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load Accueil.fxml.");
        }
    }

    private String formatFareMessage(TitreTransport fare) {
        return switch (fare) {
            case Ticket t -> String.format("""
                    ✅ Ticket ID: %d
                    📅 Date: %s
                    🕒 Time: %s
                    💰 Price: %.2f DA""",
                    t.getId(), t.getDateAchat().toLocalDate(),
                    t.getDateAchat().toLocalTime().withNano(0),
                    t.getPrix());
            case CartePersonnelle c -> String.format("""
                    💳 Card Created
                    ✅ Card ID: %d
                    📅 Date: %s
                    🕒 Time: %s
                    💰 Price: %.2f DA
                    🎖️ Type: %s""",
                    c.getId(),
                    c.getDateAchat().toLocalDate(),
                    c.getDateAchat().toLocalTime().withNano(0),
                    c.getPrix(), c.getType());
            default -> "Fare details unavailable.";
        };
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
