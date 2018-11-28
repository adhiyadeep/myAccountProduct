package skytechhub.myaccounts.component;

/**
 * Created by Asiya A Khatib on 11/25/2015.
 */
public class StringValidation {
    public static boolean validateString(String input) {
        try {
            if (input != null && !input.equals("") && input.length() > 0 && !input.equals("null")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
