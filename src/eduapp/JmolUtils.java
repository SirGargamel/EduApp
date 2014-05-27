package eduapp;

import java.io.IOException;

/**
 *
 * @author Petr Ječmen
 */
public class JmolUtils {

    private static final String JMOL_COMMAND = "java -Duser.language=cs -jar Jmol\\Jmol.jar Jmol\\";

    public static void launchJmol(final String modelName) {
        try {
            Runtime.getRuntime().exec(JMOL_COMMAND.concat(modelName));
        } catch (IOException ex) {
            System.err.println("Error launching Jmol - " + ex.getLocalizedMessage());
        }
    }
}
