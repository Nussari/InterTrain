package vv.intertrain.vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 *
 * EÞH - changed to include caching of controllers
 */
public class ViewSwitcher {

    private static final Map<View, Parent> cache = new HashMap<>();

    // viðbót fyrir controllers
    private static final Map<View, Object> controllers = new HashMap<>();
    private static Scene scene;

    /**
     * setur scenuna scene
     * @param scene
     */
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    /**
     * Skiptir yfir viðmótstré í senunni
     * @param view rót viðmótstrésins
     * @param isCache á að nota skyndiminni?
     */
    public static void switchTo(View view, boolean isCache) {
        if (scene == null) {
            System.out.println("No scene was set");
            return;
        }
        try {
            Parent root;

            if (cache.containsKey(view) && isCache) {
                System.out.println("Loading from cache");
                root = cache.get(view);
            }
            else {
                System.out.println("Loading from FXML "+view.getFileName());
                FXMLLoader loader = new FXMLLoader(ViewSwitcher
                        .class.getResource(view.getFileName()));
                root = loader.load();

                cache.put(view, root);
                //   scene.setRoot(root);
                controllers.put(view, loader.getController());
            }
            scene.setRoot(root);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fletta upp controller klasa fyrir viðmótstréð
     * @param v viðmótstré
     * @return controller
     */
    public static Object lookup(View v) {
        return controllers.get(v);
    }
}