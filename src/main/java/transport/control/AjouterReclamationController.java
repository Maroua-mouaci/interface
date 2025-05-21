package transport.control;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AjouterReclamationController {

    @FXML
    private TextField userIdField;

    @FXML
    private RadioButton rbTechnique;

    @FXML
    private RadioButton rbService;

    @FXML
    private RadioButton rbPayment;

    @FXML
    private VBox complaintDetailsBox;

    @FXML
    private Button submitButton;

    @FXML
    private Label msgLabel;

    @FXML
    private ListView<String> reclamationsListView;

    // Toggle groups
    @FXML
    private ToggleGroup complaintTypeGroup;
    private ToggleGroup techServiceTargetGroup;

    // Sample data for stations and transport modes
    private final String[] stations = {"Station A", "Station B", "Station C", "Station D", "Station E"};
    private final String[] transportModes = {"Bus", "Tram", "Metro"};

    // Payment methods from enum example (replace by your enum values)
    private final String[] paymentMethods = {"Cash", "Dahabia", "BaridiMob"};

    // Store submitted reclamations
    private final List<Reclamation> reclamations = new ArrayList<>();

    public void initialize() {
        // Complaint type group
        complaintTypeGroup = new ToggleGroup();
        rbTechnique.setToggleGroup(complaintTypeGroup);
        rbService.setToggleGroup(complaintTypeGroup);
        rbPayment.setToggleGroup(complaintTypeGroup);

        // Listen for complaint type changes
        complaintTypeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT != null) {
                String selected = ((RadioButton) newT).getText();
                showComplaintDetails(selected);
                msgLabel.setText("");
            } else {
                complaintDetailsBox.getChildren().clear();
            }
        });

        // Initialize empty list view
        reclamationsListView.setItems(FXCollections.observableArrayList());
    }

    private void showComplaintDetails(String complaintType) {
        complaintDetailsBox.getChildren().clear();

        switch (complaintType.toLowerCase()) {
            case "service", "technical":
                showTechOrServiceDetails(complaintType.toUpperCase());
                break;
            case "payment":
                showPaymentDetails();
                break;
            default:
                break;
        }
    }

    private void showTechOrServiceDetails(String type) {
        Label targetLabel = new Label("Select Target:");
        targetLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold; -fx-font-size: 14px;");

        RadioButton rbStation = new RadioButton("Station");
        RadioButton rbTransport = new RadioButton("Transport Mode");
        rbStation.setStyle("-fx-text-fill: #7ea8be;");
        rbTransport.setStyle("-fx-text-fill: #7ea8be;");

        techServiceTargetGroup = new ToggleGroup();
        rbStation.setToggleGroup(techServiceTargetGroup);
        rbTransport.setToggleGroup(techServiceTargetGroup);

        VBox targetBox = new VBox(8, targetLabel, rbStation, rbTransport);
        targetBox.setStyle("-fx-padding: 10 0 10 0;");

        complaintDetailsBox.getChildren().add(targetBox);

        VBox dynamicFields = new VBox(10);
        complaintDetailsBox.getChildren().add(dynamicFields);

        // Default selection and listener to show fields immediately
        rbStation.setSelected(true);
        techServiceTargetGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            dynamicFields.getChildren().clear();
            if (newT != null) {
                RadioButton selected = (RadioButton) newT;
                if (selected.getText().equals("Station")) {
                    addStationFields(dynamicFields, type);
                } else {
                    addTransportFields(dynamicFields, type);
                }
            }
        });
        techServiceTargetGroup.selectToggle(rbStation);
    }

    private void addStationFields(VBox container, String type) {
        Label stationLabel = new Label("Choose Station:");
        stationLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");

        ComboBox<String> stationComboBox = new ComboBox<>(FXCollections.observableArrayList(stations));
        stationComboBox.setPromptText("Select station");
        stationComboBox.setStyle("-fx-border-color:  #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe the problem...");
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-border-color:  #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        container.getChildren().addAll(stationLabel, stationComboBox, descLabel, descArea);
    }

    private void addTransportFields(VBox container, String type) {
        Label modeLabel = new Label("Choose Transport Mode:");
        modeLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");

        ComboBox<String> modeComboBox = new ComboBox<>(FXCollections.observableArrayList(transportModes));
        modeComboBox.setPromptText("Select mode");
        modeComboBox.setStyle("-fx-border-color:  #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe the problem...");
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-border-color:  #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        container.getChildren().addAll(modeLabel, modeComboBox, descLabel, descArea);
    }

    private void showPaymentDetails() {
        Label stationLabel = new Label("Choose Station:");
        stationLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");

        ComboBox<String> stationComboBox = new ComboBox<>(FXCollections.observableArrayList(stations));
        stationComboBox.setPromptText("Select station");
        stationComboBox.setStyle("-fx-border-color: #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");

        ComboBox<String> paymentComboBox = new ComboBox<>(FXCollections.observableArrayList(paymentMethods));
        paymentComboBox.setPromptText("Select payment method");
        paymentComboBox.setStyle("-fx-border-color: #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-text-fill: #7ea8be; -fx-font-weight: bold;");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe the problem...");
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-border-color: #28536b; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");

        complaintDetailsBox.getChildren().addAll(
                stationLabel, stationComboBox,
                paymentLabel, paymentComboBox,
                descLabel, descArea
        );
    }

    @FXML
    private void handleSubmit() {
        if (complaintTypeGroup.getSelectedToggle() == null) {
            setMessage("Please select a complaint type.", false);
            return;
        }

        String userId = userIdField.getText().trim();
        if (userId.isEmpty()) {
            setMessage("Please enter your User ID.", false);
            return;
        }

        String complaintType = ((RadioButton) complaintTypeGroup.getSelectedToggle()).getText();
        String description = null;
        String entity = null;

        // Extract dynamic fields depending on complaint type
        if (complaintType.equalsIgnoreCase("Technical") || complaintType.equalsIgnoreCase("Service")) {
            if (techServiceTargetGroup == null || techServiceTargetGroup.getSelectedToggle() == null) {
                setMessage("Please select a target (Station or Transport Mode).", false);
                return;
            }

            RadioButton targetRb = (RadioButton) techServiceTargetGroup.getSelectedToggle();
            entity = targetRb.getText();

            VBox dynamicFields = (VBox) complaintDetailsBox.getChildren().get(1); // second VBox with fields
            if (dynamicFields.getChildren().size() < 4) {
                setMessage("Please fill all required fields.", false);
                return;
            }

            if (entity.equals("Station")) {
                ComboBox<?> stationCombo = (ComboBox<?>) dynamicFields.getChildren().get(1);
                TextArea descArea = (TextArea) dynamicFields.getChildren().get(3);

                if (stationCombo.getValue() == null || descArea.getText().trim().isEmpty()) {
                    setMessage("Please select a station and enter description.", false);
                    return;
                }

                entity = (String) stationCombo.getValue();
                description = descArea.getText().trim();

            } else { // Transport Mode
                ComboBox<?> modeCombo = (ComboBox<?>) dynamicFields.getChildren().get(1);
                TextArea descArea = (TextArea) dynamicFields.getChildren().get(3);

                if (modeCombo.getValue() == null || descArea.getText().trim().isEmpty()) {
                    setMessage("Please select a transport mode and enter description.", false);
                    return;
                }

                entity = (String) modeCombo.getValue();
                description = descArea.getText().trim();
            }
        } else if (complaintType.equalsIgnoreCase("Payment")) {
            ComboBox<?> stationCombo = (ComboBox<?>) complaintDetailsBox.getChildren().get(1);
            ComboBox<?> paymentCombo = (ComboBox<?>) complaintDetailsBox.getChildren().get(3);
            TextArea descArea = (TextArea) complaintDetailsBox.getChildren().get(5);

            if (stationCombo.getValue() == null || paymentCombo.getValue() == null || descArea.getText().trim().isEmpty()) {
                setMessage("Please fill all payment complaint fields.", false);
                return;
            }

            entity = stationCombo.getValue() + " - " + paymentCombo.getValue();
            description = descArea.getText().trim();
        }

        // Create and add new reclamation
        Reclamation rec = new Reclamation(userId, complaintType, entity, description);
        reclamations.add(rec);

        updateReclamationsList();

        // Check suspension condition (>3 complaints)
        if (countUserComplaints(userId) > 3) {
            msgLabel.setText("Your account is suspended due to multiple complaints.");
            msgLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            submitButton.setDisable(true);
        } else {
            setMessage("Complaint submitted successfully!", true);
        }

        clearForm();
    }

    private void updateReclamationsList() {
        List<String> displayList = new ArrayList<>();
        for (Reclamation r : reclamations) {
            displayList.add("[" + r.getType() + "] " + r.getEntity() + " (User: " + r.getUserId() + ")");
        }
        reclamationsListView.setItems(FXCollections.observableArrayList(displayList));
    }

    private int countUserComplaints(String userId) {
        int count = 0;
        for (Reclamation r : reclamations) {
            if (r.getUserId().equals(userId)) count++;
        }
        return count;
    }

    private void setMessage(String msg, boolean success) {
        msgLabel.setText(msg);
        if (success) {
            msgLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            msgLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    private void clearForm() {
        userIdField.clear();
        complaintTypeGroup.selectToggle(null);
        complaintDetailsBox.getChildren().clear();
    }

    // Inner class representing a complaint
    public static class Reclamation {
        private final String userId;
        private final String type;
        private final String entity;
        private final String description;

        public Reclamation(String userId, String type, String entity, String description) {
            this.userId = userId;
            this.type = type;
            this.entity = entity;
            this.description = description;
        }

        public String getUserId() {
            return userId;
        }

        public String getType() {
            return type;
        }

        public String getEntity() {
            return entity;
        }

        public String getDescription() {
            return description;
        }
    }
}
