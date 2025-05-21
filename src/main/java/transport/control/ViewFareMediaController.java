package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewFareMediaController {

    @FXML private TableView<FareRecord> fareTable;
    @FXML private TableColumn<FareRecord, String> idColumn;
    @FXML private TableColumn<FareRecord, String> typeColumn;
    @FXML private TableColumn<FareRecord, String> priceColumn;
    @FXML private TableColumn<FareRecord, String> dateColumn;
    @FXML private TableColumn<FareRecord, String> paymentColumn;
    @FXML private Button refreshButton;

    private final ObservableList<FareRecord> fareData = FXCollections.observableArrayList();

    // Model class for the table data
    public static class FareRecord {
        private final String id;
        private final String type;
        private final String price;
        private final String date;
        private final String payment;

        public FareRecord(String id, String type, String price, String date, String payment) {
            this.id = id;
            this.type = type;
            this.price = price;
            this.date = date;
            this.payment = payment;
        }

        // Getters
        public String getId() { return id; }
        public String getType() { return type; }
        public String getPrice() { return price; }
        public String getDate() { return date; }
        public String getPayment() { return payment; }
    }

    @FXML
    public void initialize() {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        // Load initial data
        loadFileContent();

        // Set up refresh button action
        refreshButton.setOnAction(event -> loadFileContent());
    }

    private void loadFileContent() {
        File file = new File("media.txt");
        if (!file.exists()) {
            fareData.clear();
            return;
        }

        List<FareRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    records.add(new FareRecord(
                            parts[0].trim(),  // ID
                            parts[1].trim(),  // Type
                            parts[2].trim(),  // Price
                            parts[3].trim(),  // Date
                            parts[4].trim()   // Payment
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(records); // Reverse the list here

        fareData.clear();
        fareData.addAll(records);
        fareTable.setItems(fareData);
    }
}