package transport.core;
import java.time.*;
public class TransportService {


    abstract class TitreTransport {
        protected int id;
        protected LocalDateTime dateAchat;
        protected double prix;
    }
}
