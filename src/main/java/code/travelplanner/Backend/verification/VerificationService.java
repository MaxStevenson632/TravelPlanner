package code.travelplanner.Backend.verification;

import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;

    public VerificationService(VerificationRepository verificationRepository,  UserRepository userRepository) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
    }

    public String generateToken(UserEntity user) {

        VerificationEntity verificationEntry = new VerificationEntity();

        // Generates a random 36-character long string - Used for verification
        String token = UUID.randomUUID().toString();

        verificationEntry.setToken(token);
        verificationEntry.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        verificationEntry.setUser(user);

        verificationRepository.save(verificationEntry);

        return token;
    }

    @Transactional
    public void verifyToken(String token) {

        // Find token row
        VerificationEntity tokenEntry = verificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // Check token hasn't expired
        if (tokenEntry.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        UserEntity user = tokenEntry.getUser();
        user.setAccountEnabled(true);
        userRepository.save(user);

        // Delete token row
        verificationRepository.delete(tokenEntry);
    }

    // Runs every 20 minutes automatically
    @Scheduled(fixedRate = 12000)
    @Transactional
    public void removeExpiredTokens() {

        LocalDateTime now = LocalDateTime.now();

        // Delete all rows where expiration date is less than right now
        verificationRepository.deleteAllByExpiryDateBefore(now);
    }
}
