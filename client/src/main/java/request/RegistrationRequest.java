package request;

public class RegistrationRequest implements Request {
    public final String command = "registration";
    public String nickname;

    public RegistrationRequest(String nickname) {
        this.nickname = nickname;
    }
}
