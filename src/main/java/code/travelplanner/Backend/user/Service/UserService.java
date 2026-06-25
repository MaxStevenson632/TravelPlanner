package code.travelplanner.Backend.user.Service;

import code.travelplanner.Backend.user.Dto.UserLoginDto;
import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerNewUser(UserRegisterDto userRegistrationData) {

        // If email already exists
        if (userRepository.findByEmail(userRegistrationData.getEmail()).isPresent()) {
            return false;
        }

        // New row in DB
        UserEntity user = new UserEntity();
        user.setName(userRegistrationData.getUsername());

        // Encrypt the password using Bcrypt hashing
        user.setPassword(passwordEncoder.encode(userRegistrationData.getPassword()));

        user.setEmail(userRegistrationData.getEmail());
        userRepository.save(user);
        return true;

    }
}
