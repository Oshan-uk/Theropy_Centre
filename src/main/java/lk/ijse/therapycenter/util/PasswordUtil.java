package lk.ijse.therapycenter.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int WORK_FACTOR = 12;

    private PasswordUtil() {}


    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    public static boolean verifyPassword(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}