package server;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class Authorization {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static boolean checkAuth(UUID uuid, String name, ConcurrentMap<UUID, String> onlineUsers) {
        if (onlineUsers.get(uuid).equals(name)) {
            return true;
        }
        return false;
    }
}
