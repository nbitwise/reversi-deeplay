package serverresponses;

import java.io.Serializable;

public class ResponseRegistration implements Response  {

    public String status;
    public String message;
    public ResponseRegistration(final String status,final String message) {
        this.status = status;
        this.message = message;
    }
}
