package code.travelplanner.Backend.trip.Service;

import code.travelplanner.Backend.trip.Entity.TripPlacesEntity;
import code.travelplanner.Backend.trip.Repository.TripPlacesRepository;
import org.springframework.stereotype.Service;

@Service
public class TripPlacesService {

    private final TripPlacesRepository tripPlacesRepository;

    public TripPlacesService(TripPlacesRepository tripPlacesRepository) {
        this.tripPlacesRepository = tripPlacesRepository;
    }

    public void linkWaypointToTrip(long tripId, long waypointId, int visitOrder) {

        tripPlacesRepository.save(new TripPlacesEntity(tripId, waypointId, visitOrder));
    }
}
