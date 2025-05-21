package transport.core;
import java.time.*;

public class Ticket extends TitreTransport {

    public Ticket(TypePaiement methodePaiement) {
        super(50);  // Fixed price: 50 DA
        this.setMethodePaiement(methodePaiement);
        this.id = Persistance.getNextId();  // Get next  ID
        this.dateAchat = LocalDateTime.now();  // Set purchase timestamp
    }
    // Ajout getter/setter pour dateAchat (utile si besoin)
    public LocalDateTime getDateAchat() {
        return this.dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }

    @Override
    public boolean estValide(LocalDate date) throws TitreNonValideException {
        LocalDate dateAchatTicket = dateAchat.toLocalDate();
        if (date.equals(dateAchatTicket)) {
            return true;
        } else {
            throw new TitreNonValideException("Ticket non valide à cette date.");
        }
    }

    public void setId(int id) {
        this.id = id;
    }

}
