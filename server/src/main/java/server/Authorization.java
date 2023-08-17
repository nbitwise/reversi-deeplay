package server;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentMap;

public class Authorization {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    public static boolean checkAuth(User user, ConcurrentMap<String, User> onlineUsers){
        String userToken = user.token;
        if(onlineUsers.get(userToken).getName().equals(user.getName())){
            return true;
        }
        return false;
    }
}
