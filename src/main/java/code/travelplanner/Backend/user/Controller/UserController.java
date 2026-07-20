package code.travelplanner.Backend.user.Controller;

import code.travelplanner.Backend.user.Dto.LoginResponseTokenDto;
import code.travelplanner.Backend.user.Dto.UserLoginDto;
import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/travelplanner")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto newUserData) {
        try {
            userService.registerNewUser(newUserData);

            // Return a 201 Created status code
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Registration successful!\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginUserData) {

        try {
            // Delegate all authentication logic to the service layer
            String token = userService.authenticateUser(loginUserData);
            return ResponseEntity.ok(new LoginResponseTokenDto(token));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message",
                    "Please verify your account via email before logging in."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message",
                    "Invalid email or password."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",
                    e.getMessage()));
        }
    }
}
