package lk.ijse.therapycenter.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

    public static String encrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean check(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

    public static void main(String[] args) {

        System.out.println(
                BCryptUtil.encrypt("1234")
        );
    }
}