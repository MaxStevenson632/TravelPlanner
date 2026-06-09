package code.travelplanner.Backend.user.Controller;

import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterDto newUserData) {
        userService.registerNewUser(newUserData);
    }
}
