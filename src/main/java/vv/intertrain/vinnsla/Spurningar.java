package vv.intertrain.vinnsla;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Vinnsluklasi fyrir spurningar
 *
 *
 *****************************************************************************/
public class Spurningar {
    // flokkar spurninga
    private final ObservableList<String> flokkar = FXCollections.observableArrayList();
    // tenging á milli heiti flokks og spurningalista
    private final Map<String, ObservableList<String>> spurningar = new HashMap<>();
    // fjöldi svaraðra spurning
    private final SimpleStringProperty fjoldiSvaradraSpurninga = new SimpleStringProperty("0");


    /**
     * Býr til spurningar og flokka. Seinna er hægt að lesa spurningarnar úr skrá
     */
    public Spurningar() {
        flokkar.add("Tæknispurningar");
        flokkar.add("Færnispurningar");

        ObservableList<String> flokkur1 = FXCollections.observableArrayList();
        flokkur1.add("Hvaða forritunarmál kanntu?");
        flokkur1.add("Hvaða stýrikerfi hefurðu notað?");
        flokkur1.add("Hvernig hefurðu notað debugger?");
        ObservableList<String> flokkur2 = FXCollections.observableArrayList();
        flokkur2.add("Hverjir eru styrkleikar þínir? ");
        flokkur2.add("Hverju viltu bæta þig í");
        flokkur2.add("Hvar viltu vera eftir fimm ár?");
        spurningar.put(flokkar.getFirst(), flokkur1);
        spurningar.put(flokkar.getLast(), flokkur2);
    }

    /**
     * get aðferði fyrir fjoldiSvaradraSpurninga
     *
     * @return fjöldi svaraðra spurninga
     */
    public SimpleStringProperty propertyfjoldiSvaradraSpurninga() {
        return fjoldiSvaradraSpurninga;
    }

    /**
     * get aðferð fyrir flokka
     *
     * @return flokkar spurninga
     */
    public ObservableList<String> getFlokkar() {
        return flokkar;
    }

    /**
     * get aðferð fyrir spurningalista með ákveðnum nafni first
     *
     * @param first spurningalisti
     * @return spurningalisti
     */
    public ObservableList<String> getSpurningalisti(String first) {
        return spurningar.get(first);
    }

    /**
     * uppfærir fjölda svaraðra spurninga um einn
     */
    public void plusEinnSpurningar() {
        fjoldiSvaradraSpurninga.set((Integer.parseInt(fjoldiSvaradraSpurninga.get()) + 1) + "");
    }

    /**
     * Prófunaraktygi fyrir klasann
     *
     * @param args ónotað
     */
    public static void main(String[] args) {
        Spurningar spurningar = new Spurningar();
        System.out.println(spurningar.getSpurningalisti("Færnispurningar").get(1));
        System.out.println(spurningar.getSpurningalisti("Tæknispurningar").get(0));

        System.out.println(spurningar.getFlokkar());
    }
}