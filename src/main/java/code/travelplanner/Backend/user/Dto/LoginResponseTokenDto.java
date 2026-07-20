package code.travelplanner.Backend.user.Dto;

public class LoginResponseTokenDto {

    private String token;

    public LoginResponseTokenDto(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
