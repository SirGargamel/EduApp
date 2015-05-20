package eduapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Petr Ječmen
 */
public class Utils {        

    public static final String ATOM_COUNT_PREFIX = "_";    
    private static final String BASE = "208";
    
    public static String convertNumbersToLowerIndexes(final String input) {
        String fixedP = input;
        final Matcher m = Pattern.compile(ATOM_COUNT_PREFIX + "[0-9]").matcher(input);
        String val;
        int code;
        while (m.find()) {
            val = m.group();
            code = Integer.valueOf(BASE.concat(val.substring(ATOM_COUNT_PREFIX.length())), 16);
            fixedP = fixedP.replaceAll(
                    val,
                    String.valueOf((char) code));
        }
        return fixedP;
    }
    
    public static String generateIconFilename(final String itemId) {
        return "icons\\" + itemId + ".png";
    }

    private Utils() {
    }
}
