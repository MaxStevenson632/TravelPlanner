package code.travelplanner;

import code.travelplanner.Backend.user.Dto.UserRegisterDto;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import code.travelplanner.Backend.user.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void registerNewUser_ShouldHashPasswordAndSaveUserSuccessfully() {

        // Set up fake data to use
        UserRegisterDto incomingDto = new UserRegisterDto();
        incomingDto.setUsername("Test Backpacker");
        incomingDto.setEmail("backpacker@travelbound.com");
        incomingDto.setPassword("RawSecretPassword123");

        // Tell the fake password encoder what to return when called
        when(passwordEncoder.encode("RawSecretPassword123")).thenReturn("$$BcryptHashedPassword$$");

        // Save new user to dummy DB
        userService.registerNewUser(incomingDto);

        // Verify that the encoder was actually called once with the correct raw password
        verify(passwordEncoder, times(1)).encode("RawSecretPassword123");

        // Capture the exact UserEntity object that was passed into userRepository.save()
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();

        // Assert that the fields were mapped correctly and the password was securely hashed
        assertEquals("Test Backpacker", savedUser.getName());
        assertEquals("backpacker@travelbound.com", savedUser.getEmail());
        assertEquals("$$BcryptHashedPassword$$", savedUser.getPassword());
    }
}
