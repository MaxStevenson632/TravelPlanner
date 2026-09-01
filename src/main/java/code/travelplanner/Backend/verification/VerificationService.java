package code.travelplanner.Backend.verification;

import code.travelplanner.Backend.Exception.UserNotFoundException;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

    private final UserRepository userRepository;
    private RedisTemplate<String, Long> redisTemplate;

    public VerificationService(UserRepository userRepository,
                               RedisTemplate<String, Long> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(UserEntity user) {

        // Generates a random 36-character long string - Used for verification
        String token = UUID.randomUUID().toString();

        // Add the token, userId to cache with 20 minute expiration time
        redisTemplate.opsForValue().set(token, user.getUserId(), 20, TimeUnit.MINUTES);
        return token;
    }

    @Transactional
    public void verifyToken(String token) {

        // Get userId from token
        Long userId = redisTemplate.opsForValue().get(token);

        // Token expired or invalid
        if (userId == null) {
            throw new RuntimeException("Token has expired");
        }

        // Check user exists
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Enable user's account
        user.setAccountEnabled(true);
        userRepository.save(user);

        // Delete token from cache
        redisTemplate.delete(token);
    }
}
