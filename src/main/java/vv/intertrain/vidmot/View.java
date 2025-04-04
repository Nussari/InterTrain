package vv.intertrain.vidmot;

/**
 * @author Ebba Þóra Hvannberg
 * enum fyrir viðmótstrén
 */
public enum View {
    VELKOMINN("velkominn-view.fxml"),
    SPURNINGAR("spurningar-view.fxml"),
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