package code.travelplanner.Backend.user.Dto;

public class UserLoginDto {

    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String newEmail) { email = newEmail; }

    public String getPassword() { return password; }
    public void setPassword(String newPassword) { password = newPassword; }

}
