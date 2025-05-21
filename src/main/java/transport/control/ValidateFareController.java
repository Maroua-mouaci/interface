package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.core.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateFareController {

    @FXML private TextField idField;
    @FXML private Button validateButton;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]");

    @FXML
    public void initialize() {
        validateButton.setOnAction(event -> validateFare());
    }

    private void validateFare() {
        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            showAlert("Error", "Please enter a fare medium ID", Alert.AlertType.ERROR);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            FareRecord fare = findFareById(id);

            if (fare == null) {
                showAlert("Not Found", "No fare medium found with ID: " + id, Alert.AlertType.ERROR);
                return;
            }

            LocalDate today = LocalDate.now();
            String validationResult = validateFare(fare, today);
            showAlert("Validation Result", validationResult, Alert.AlertType.INFORMATION);


            if (validationResult.startsWith("✅")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/acceuil.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) validateButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }


        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID format. Please enter a number", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Validation failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String validateFare(FareRecord fare, LocalDate date) {
        try {
            if ("TICKET".equals(fare.type)) {
                LocalDate purchaseDate = fare.date.toLocalDate();
                if (date.equals(purchaseDate)) {
                    return String.format("""
                        ✅ Ticket #%d is valid
                        Purchased: %s
                        Price: %.2f DA
                        Payment: %s""",
                            fare.id, fare.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                            Double.parseDouble(fare.price), fare.payment);
                } else {
                    throw new TitreNonValideException("Ticket expired (valid only on purchase date: "
                            + purchaseDate + ")");
                }
            } else if ("CARD".equals(fare.type)) {
                LocalDate expiryDate = fare.date.toLocalDate().plusYears(1);
                if (date.isBefore(expiryDate)) {
                    return String.format("""
                        ✅ Card #%d is valid
                        Issued: %s
                        Expires: %s
                        Price: %.2f DA
                        Payment: %s""",
                            fare.id, fare.date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            expiryDate, Double.parseDouble(fare.price), fare.payment);
                } else {
                    throw new TitreNonValideException("Card expired on " + expiryDate);
                }
            }
            return "Unknown fare type";
        } catch (TitreNonValideException e) {
            return "❌ " + e.getMessage();
        }
    }

    private FareRecord findFareById(int id) throws IOException {
        File file = new File("media.txt");
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    int currentId = Integer.parseInt(parts[0].trim());
                    if (currentId == id) {
                        return new FareRecord(
                                currentId,
                                parts[1].trim(),
                                parts[2].trim(),
                                LocalDateTime.parse(parts[3].trim(), DATE_TIME_FORMATTER),
                                parts[4].trim()
                        );
                    }
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new IOException("Error parsing fare data", e);
        }
        return null;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static class FareRecord {
        final int id;
        final String type;
        final String price;
        final LocalDateTime date;
        final String payment;

        FareRecord(int id, String type, String price, LocalDateTime date, String payment) {
            this.id = id;
            this.type = type;
            this.price = price;
            this.date = date;
            this.payment = payment;
        }
    }
}
