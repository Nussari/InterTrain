package vv.intertrain.vinnsla;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import vv.intertrain.vinnsla.com.logaritex.ai.api.AssistantApi;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.Assistant;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Spurningar {

    private final ObservableList<String> flokkar = FXCollections.observableArrayList();

    private Map<String, ObservableList<String>> spurningar = new HashMap<>();

    private final SimpleStringProperty fjoldiSvaradraSpurninga = new SimpleStringProperty("0");

    private int fjoldiSvara;

    private String starfsheiti;
    private String vinnustadur;

    private InterviewPreparer interviewPreparer;


    public Spurningar(String starfsheiti, String vinnustadur, List<String> hopar) throws InterruptedException {

        this.starfsheiti = starfsheiti;
        this.vinnustadur = vinnustadur;
        this.interviewPreparer = new InterviewPreparer();
        this.fjoldiSvara = 0;


        this.flokkar.addAll(hopar);

        this.spurningar = interviewPreparer.requestQuestions(starfsheiti, vinnustadur, hopar);
    }

    public Map<String, ObservableList<String>> getSpurningaListi() {
        return this.spurningar;
    }

    public ObservableList<String> getFlokkar() {
        return this.flokkar;
    }

    public String getFjoldiSvaradraSpurninga() {
        return this.fjoldiSvaradraSpurninga.get();
    }

    public SimpleStringProperty getFjoldiSvaradraSpurningaProperty() {
        return this.fjoldiSvaradraSpurninga;
    }

    public void setFjoldiSvaradraSpurninga(String nyrFjoldi) {
        this.fjoldiSvaradraSpurninga.set(nyrFjoldi);
    }

    public String faEndurgjof(String spurning, String svar) throws InterruptedException {
        return interviewPreparer.requestFeedback(spurning, svar);
    }

    public void increaseFjoldiSvaradraSpurninga() {
        int fjoldi = Integer.parseInt(this.getFjoldiSvaradraSpurninga());
        this.setFjoldiSvaradraSpurninga(String.valueOf(fjoldi++));
    }
}