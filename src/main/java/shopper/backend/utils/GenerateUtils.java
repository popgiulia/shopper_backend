package shopper.backend.utils;

public class GenerateUtils {
    //Clasă utilitară responsabilă pentru generarea unui nume de utilizator bazat pe adresa de email.
    // TODO a better generation algorithm
    public static String generateUsername(String email) {
        return email.split("@")[0];
    }
}
