package code.travelplanner.Backend.user.Dto;

public class UserRegisterDto {

    private String username;
    private String password;
    private String email;

    public String getUsername() { return username; }
    public void setUsername(String newUsername) { username = newUsername; }

    public String getPassword() { return password; }
    public void setPassword(String newPassword) { password = newPassword; }

    public String getEmail() { return email; }
    public void setEmail(String newEmail) { email = newEmail; }

}
