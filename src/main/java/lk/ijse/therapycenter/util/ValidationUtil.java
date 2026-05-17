package lk.ijse.therapycenter.util;

import java.util.regex.Pattern;


public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );


    private static final Pattern SRI_LANKA_PHONE = Pattern.compile(
            "^(0)(7[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9]|6[0-9]|8[0-9]|9[0-9])[0-9]{7}$"
    );

    private static final Pattern PROGRAM_ID_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{4}$");

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,30}$");

    private ValidationUtil() {}

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && SRI_LANKA_PHONE.matcher(phone.trim()).matches();
    }

    public static boolean isValidProgramId(String id) {
        return id != null && PROGRAM_ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}