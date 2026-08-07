package code.travelplanner.Backend.Exception;

public class TripNotFoundException extends RuntimeException{

    public  TripNotFoundException(String message) {
        super(message);
    }
}
