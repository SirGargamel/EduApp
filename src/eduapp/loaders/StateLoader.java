package eduapp.loaders;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Petr Ječmen
 */
public class StateLoader implements AssetLoader {

    public static final String STATE_EXTENSION = "game";
    public static final String STATE_FILE = "state." + STATE_EXTENSION;    

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        int result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(assetInfo.openStream()))) {
            result = in.read();
        }

        return result;
    }
}
