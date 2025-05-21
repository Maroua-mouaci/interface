package transport.core;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Persistance {
    private static final String USERS_FILE = "users.txt";
    private static final String ID_FILE = "last_id.dat";
    private static final String MEDIA_FILE = "media.txt";
    private static int lastId = 0; // Single ID counter for all fare media
    static {
        initializeIdCounter(); // Runs when Persistance class loads
    }
    // ----------- Utilisateurs ------------

    public static void sauvegarderUtilisateurs(List<Personne> utilisateurs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Personne user : utilisateurs) {
                writer.write(userToString(user));
                writer.newLine();
            }
        }
        sauvegarderDernierId();
    }

    public static List<Personne> restaurerUtilisateurs() throws IOException {
        List<Personne> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        int maxId = 1000;

        if (!file.exists()) {
            Usager.setDernierId(maxId);
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Personne user = stringToUser(line);
                if (user != null) {
                    users.add(user);
                    if (user instanceof Usager) {
                        String idStr = ((Usager) user).getIdUsager();
                        try {
                            int idNum = Integer.parseInt(idStr.replaceAll("[^0-9]", ""));
                            if (idNum > maxId) {
                                maxId = idNum;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }

        Usager.setDernierId(maxId);
        return users;
    }

    private static String userToString(Personne user) {
        if (user instanceof Employe) {
            Employe emp = (Employe) user;
            return String.join(",",
                    "EMPLOYEE",
                    emp.getMatricule(),
                    emp.getNom(),
                    emp.getPrenom(),
                    emp.getDateNaissance().toString() ,
                    String.valueOf(emp.isHandicape()),
                    emp.getFonction().name()
            );
        } else if (user instanceof Usager) {
            Usager usr = (Usager) user;
            return String.join(",",
                    "USAGER",
                    usr.getIdUsager(),
                    usr.getNom(),
                    usr.getPrenom(),
                    usr.getDateNaissance().toString() ,
                    String.valueOf(usr.isHandicape())
            );
        }
        return "";
    }

    private static Personne stringToUser(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) return null;

        try {
            String type = parts[0];
            String id = parts[1];
            String nom = parts[2];
            String prenom = parts[3];
            String dateStr = parts[4];
            boolean handicape = Boolean.parseBoolean(parts[5]);

            LocalDate dateNaissance = dateStr.isEmpty() ? null : LocalDate.parse(dateStr);

            if ("EMPLOYEE".equals(type) && parts.length >= 7) {
                return new Employe(
                        nom, prenom, dateNaissance, handicape,
                        id, // matricule
                        TypeFonction.valueOf(parts[6])
                );
            } else if ("USAGER".equals(type)) {
                return new Usager(id, nom, prenom, dateNaissance, handicape);
            }
        } catch (Exception e) {
            System.err.println("Error parsing user: " + e.getMessage());
        }
        return null;
    }

    private static void sauvegarderDernierId() throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(ID_FILE))) {
            out.writeInt(Usager.getDernierId());
        }
    }

    public static void restaurerDernierId() throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(ID_FILE))) {
            Usager.setDernierId(in.readInt());
        } catch (FileNotFoundException e) {
            Usager.setDernierId(1000);
        }
    }

    // ----------- Titres de transport (Fares) ------------
    private static void initializeIdCounter() {
        File file = new File(ID_FILE);
        if (file.exists()) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
                lastId = dis.readInt();
            } catch (IOException e) {
                System.err.println("Error loading ID counter: " + e.getMessage());
            }
        }
    }


    public static synchronized int getNextId() {
        lastId++;
        saveLastId();
        return lastId;
    }

    private static void saveLastId() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(ID_FILE))) {
            dos.writeInt(lastId);
        } catch (IOException e) {
            System.err.println("Error saving ID counter: " + e.getMessage());
        }
    }
    public static void sauvegarderFares(TitreTransport newFare) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEDIA_FILE, true))) {
            writer.write(mediaToString(newFare));
            writer.newLine();
        }
    }

    private static String mediaToString(TitreTransport media) {
        if (media instanceof Ticket) {
            return String.join(";",
                    String.valueOf(media.getId()),
                    "TICKET",
                    String.valueOf(media.getPrix()),
                    media.getDateAchat().toString(),
                    media.getMethodePaiement().name()
            );
        } else if (media instanceof CartePersonnelle) {
            CartePersonnelle carte = (CartePersonnelle) media;
            return String.join(";",
                    String.valueOf(carte.getId()),  // Now includes ID for cards
                    "CARD",
                    String.valueOf(carte.getPrix()),
                    carte.getDateAchat().toString(),
                    carte.getMethodePaiement().name()
            );
        }
        return "";
    }
    // Restaure la liste complète des titres de transport (pas juste les prix)
    public static List<TitreTransport> restaurerFares() throws IOException {
        List<TitreTransport> titres = new ArrayList<>();
        File file = new File(MEDIA_FILE);

        if (!file.exists()) return titres;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                TitreTransport titre = stringToMedia(line);
                if (titre != null) {
                    titres.add(titre);
                } else {
                    System.err.println("Entrée titre invalide ignorée : " + line);
                }
            }
        }
        return titres;
    }

    private static TitreTransport stringToMedia(String line) {
        // Normalize the line by replacing : with ; for consistent splitting
        line = line.replace(":", ";");
        String[] parts = line.split(";");

        try {
            String id = parts[0].trim();
            String type = parts[1].trim();
            double price = Double.parseDouble(parts[2].trim());
            LocalDateTime date = LocalDateTime.parse(parts[3].trim());
            TypePaiement payment = TypePaiement.valueOf(parts[4].trim());

            if ("TICKET".equals(type)) {
                Ticket ticket = new Ticket(payment);
                ticket.setId(Integer.parseInt(id));
                ticket.setPrix(price);
                ticket.setDateAchat(date);
                return ticket;
            } else if ("CARD".equals(type)) {
                CartePersonnelle card = new CartePersonnelle(null, payment);
                card.setPrix(price);
                card.setDateAchat(date);
                return card;
            }
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
        }
        return null;
    }
    }

