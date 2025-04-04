package vv.intertrain.vidmot;

/**
 * @author Ebba Þóra Hvannberg
 * enum fyrir viðmótstrén
 */
public enum View {
    VELKOMINN("velkominn-view.fxml"),
    VIDTAL("vidtal-view.fxml"),
    KVEDJA("kvedja-view.fxml");

    private final String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Nær í skráanafn
     * @return skráanafn
     */
    public String getFileName() {
        return fileName;
    }
}