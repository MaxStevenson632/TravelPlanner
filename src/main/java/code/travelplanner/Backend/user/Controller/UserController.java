package code.travelplanner.Backend.user.Controller;

import code.travelplanner.Backend.user.Dto.UserLoginDto;
import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travelplanner")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDto newUserData) {

        userService.registerNewUser(newUserData);
        // Return a 201 Created status code
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Registration successful!\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginUserData) {

        userService.loginUser(loginUserData);

        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Login successful!\"}");
    }
}
