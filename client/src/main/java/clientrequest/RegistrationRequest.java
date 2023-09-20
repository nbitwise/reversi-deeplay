package clientrequest;
public class RegistrationRequest implements Request {
    protected final String command = "REGISTRATION";
    protected String nickname;

    public RegistrationRequest(String nickname) {
        this.nickname = nickname;
    }
}
