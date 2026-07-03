package code.travelplanner.Backend.user.Service;

import code.travelplanner.Backend.user.Dto.UserLoginDto;
import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import code.travelplanner.Backend.verification.VerificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationService verificationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService,
                       VerificationService verificationService,  AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public boolean registerNewUser(UserRegisterDto userRegistrationData) {

        // If email already exists, refuse registration
        if (userRepository.findByEmail(userRegistrationData.getEmail()).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        // New row in DB
        UserEntity user = new UserEntity();
        user.setName(userRegistrationData.getUsername());

        // Encrypt the password using Bcrypt hashing
        user.setPassword(passwordEncoder.encode(userRegistrationData.getPassword()));
        user.setEmail(userRegistrationData.getEmail());

        // Save user to database
        UserEntity savedUser = userRepository.save(user);

        // Generate token and link it to saved user
        String token = verificationService.generateToken(savedUser);

        // Send email
        try {
            emailService.sendVerificationEmail(userRegistrationData.getEmail(), token);
        } catch (Exception e) {
            System.out.println("Email sending failed");
        }
        return true;
    }

    public void authenticateUser (UserLoginDto loginUserData) {

        // If email doesn't exist throw error, findByEmail requires Optional
        UserEntity user = userRepository.findByEmail(loginUserData.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        // If user hasn't verified, don't allow authentication
        if (user.getAccountEnabled() == false) {
            throw new DisabledException("User needs to verify");
        }

        // User verified and email exists
        // Wrap the raw credentials into a standard Spring token
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginUserData.getEmail(), loginUserData.getPassword());

        // Hand the token to the authentication manager
        // This will send it to the DaoAuthenticationProvider in SecurityConfiguration
        Authentication authentication = authenticationManager.authenticate(token);

        // Store authenticated user sessions in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
