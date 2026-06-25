package code.travelplanner.Backend.user.Controller;

import code.travelplanner.Backend.user.Dto.UserLoginDto;
import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travelplanner")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDto newUserData) {

        userService.registerNewUser(newUserData);
        // Return a 201 Created status code
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Registration successful!\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginUserData) {

        // Wrap the raw credentials into a standard Spring token
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginUserData.getEmail(), loginUserData.getPassword());

        // Hand the token to the authentication manager
        // This will send it to the DaoAuthenticationProvider in SecurityConfiguration
        Authentication authentication = authenticationManager.authenticate(token);

        // Store authenticated user sessions in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Login successful!\"}");
    }
}
