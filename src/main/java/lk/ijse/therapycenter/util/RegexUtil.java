package lk.ijse.therapycenter.util;

public class RegexUtil {

    public static boolean emailValidate(String email){

        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean phoneValidate(String phone){

        return phone.matches("^(07)(0|1|2|4|5|6|7|8)\\d{7}$");
    }
}