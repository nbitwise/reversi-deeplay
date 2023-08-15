package request;

public class RegistrationRequest implements Request {
    public String command = "registration";
    public String nickname;

    public RegistrationRequest(String nickname) {
        this.nickname = nickname;
    }
}
