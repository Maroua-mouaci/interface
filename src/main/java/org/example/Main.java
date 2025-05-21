package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transport.core.Data;
import transport.core.Persistance;
import transport.core.Personne;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Persistance.restaurerDernierId();
            // Proceed with application initialization
        } catch (IOException e) {
            System.err.println("Failed to restore last ID: " + e.getMessage());
        }

        try {
            // Load existing users
            List<Personne> utilisateurs = Persistance.restaurerUtilisateurs();
            Data.setUtilisateurs(utilisateurs);

            // Load the main UI
            Parent root = FXMLLoader.load(getClass().getResource("/transport/ui/Acceuil.fxml"));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("ESI-RUN Station Agent Portal");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); //allow resizing
            primaryStage.show();

            System.out.println("Application started successfully!");
        } catch (Exception e) {
            System.err.println("FAILED TO START APPLICATION");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching application...");
        launch(args);
    }
}
<<<<<<< HEAD
=======
/*
@Override
public void start(Stage stage) {
    // 1. AUTO-LOAD when app starts
    try {
        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("data/app_state.dat"));

        // Restore ALL data
        Data.setUsers((List<Personne>) in.readObject());
        Data.setTitresVendus((List<TitreTransport>) in.readObject());
        in.close();
    } catch (Exception e) {
        System.out.println("(No existing data found)");
    }

    // ... proceed with normal app startup ...
}

@Override
public void stop() { // 2. AUTO-SAVE when app closes
    try {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("data/app_state.dat"));

        // Save ALL data
        out.writeObject(Data.getUsers());
        out.writeObject(Data.getTitresVendus());
        out.close();
    } catch (IOException e) {
        System.err.println("⚠ Failed to save data!");
    }
}*/
>>>>>>> 8b306ef72eac9bc0181e554256839715b1efdcd1
