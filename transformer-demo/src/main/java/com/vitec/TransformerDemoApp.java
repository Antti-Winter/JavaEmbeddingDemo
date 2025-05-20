package com.vitec;

import com.vitec.model.TranslationProcess;
import com.vitec.service.MockTranslationAPI;
import com.vitec.ui.AttentionMatrixView;
import com.vitec.ui.DecoderStepView;
import com.vitec.ui.ProbabilityView;
import com.vitec.ui.TokenSequenceView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TransformerDemoApp extends Application {
    
    private MockTranslationAPI translationAPI = new MockTranslationAPI();
    
    // UI-komponentit
    private ComboBox<String> exampleSentences;
    private TextArea inputText;
    private Button translateButton;
    private Label translationResultLabel;
    
    // Visualisointikomponentit
    private TokenSequenceView sourceTokensView;
    private TokenSequenceView targetTokensView;
    private AttentionMatrixView attentionMatrixView;
    private ProbabilityView probabilityView;
    private DecoderStepView decoderStepView;
    
    // Decoder-askel kontrolleja varten
    private int currentStepIndex = 0;
    private TranslationProcess currentProcess;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Transformer-käännösmallin visualisointi");
        
        // Alusta UI-komponentit
        initializeComponents();
        
        // Asettele käyttöliittymä
        BorderPane root = createLayout();
        
        // Aseta tapahtumankäsittelijät
        setupEventHandlers();
        
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void initializeComponents() {
        // UI-komponentit
        exampleSentences = new ComboBox<>();
        exampleSentences.getItems().addAll(
            "Yrityksemme kehittää innovatiivisia ohjelmistoratkaisuja",
            "Tietoturva on meille ensisijaisen tärkeää",
            "Asiakkaamme arvostavat luotettavuuttamme"
        );
        
        inputText = new TextArea();
        inputText.setPrefRowCount(3);
        
        translateButton = new Button("Käännä ja Visualisoi");
        
        translationResultLabel = new Label();
        translationResultLabel.setStyle("-fx-font-weight: bold;");
        
        // Visualisointikomponentit
        sourceTokensView = new TokenSequenceView();
        targetTokensView = new TokenSequenceView();
        attentionMatrixView = new AttentionMatrixView();
        probabilityView = new ProbabilityView();
        decoderStepView = new DecoderStepView();
    }
    
    private BorderPane createLayout() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Yläreunan kontrollit
        VBox topControls = new VBox(10);
        topControls.setPadding(new Insets(10));
        
        HBox comboBox = new HBox(10);
        comboBox.setAlignment(Pos.CENTER_LEFT);
        comboBox.getChildren().addAll(new Label("Esimerkkejä:"), exampleSentences);
        
        HBox textInput = new HBox(10);
        textInput.setAlignment(Pos.CENTER_LEFT);
        textInput.getChildren().addAll(new Label("Teksti:"), inputText);
        
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.getChildren().add(translateButton);
        
        topControls.getChildren().addAll(
            new Label("Transformer-käännösmallin visualisointi"),
            comboBox,
            textInput,
            buttonBar
        );
        
        root.setTop(topControls);
        
        // Keskialueen visualisaatio
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(10));
        
        // Lähdetokenit
        VBox sourceTokensBox = new VBox(5);
        sourceTokensBox.getChildren().addAll(
            new Label("Lähtöteksti tokenisoituna:"),
            sourceTokensView
        );
        
        // Attention-matriisi
        VBox attentionBox = new VBox(5);
        attentionBox.getChildren().addAll(
            new Label("Attention-matriisi:"),
            attentionMatrixView
        );
        
        // Kohdetokenit
        VBox targetTokensBox = new VBox(5);
        targetTokensBox.getChildren().addAll(
            new Label("Käännös tokenisoituna:"),
            targetTokensView
        );
        
        // Decoder-steps kontrollit
        HBox stepControls = new HBox(10);
        Button prevButton = new Button("Edellinen vaihe");
        Button nextButton = new Button("Seuraava vaihe");
        Label stepLabel = new Label("Decoder-vaihe: 0");
        
        prevButton.setOnAction(e -> {
            if (currentProcess != null && currentStepIndex > 0) {
                currentStepIndex--;
                updateDecoderStep();
                stepLabel.setText("Decoder-vaihe: " + currentStepIndex);
            }
        });
        
        nextButton.setOnAction(e -> {
            if (currentProcess != null && 
                currentProcess.getDecoderSteps() != null && 
                currentStepIndex < currentProcess.getDecoderSteps().size() - 1) {
                currentStepIndex++;
                updateDecoderStep();
                stepLabel.setText("Decoder-vaihe: " + currentStepIndex);
            }
        });
        
        stepControls.setAlignment(Pos.CENTER);
        stepControls.getChildren().addAll(prevButton, stepLabel, nextButton);
        
        // Käännöstulos
        HBox translationResult = new HBox(10);
        translationResult.setAlignment(Pos.CENTER_LEFT);
        translationResult.getChildren().addAll(
            new Label("Käännös:"),
            translationResultLabel
        );
        
        centerContent.getChildren().addAll(
            sourceTokensBox,
            attentionBox,
            targetTokensBox,
            new Separator(),
            new Label("Decoder-prosessi:"),
            decoderStepView,
            stepControls,
            new Separator(),
            translationResult
        );
        
        root.setCenter(new ScrollPane(centerContent));
        
        return root;
    }
    
    private void setupEventHandlers() {
        exampleSentences.setOnAction(e -> {
            String selected = exampleSentences.getValue();
            if (selected != null) {
                inputText.setText(selected);
            }
        });
        
        translateButton.setOnAction(e -> translate());
    }
    
    private void translate() {
        String text = inputText.getText();
        if (text == null || text.trim().isEmpty()) {
            showAlert("Anna käännettävä teksti");
            return;
        }
        
        // Hae käännös ja visualisoi
        currentProcess = translationAPI.getTranslationProcess(text);
        currentStepIndex = 0;
        
        // Päivitä UI
        sourceTokensView.setTokens(currentProcess.getSourceTokens());
        targetTokensView.setTokens(currentProcess.getTargetTokens());
        attentionMatrixView.setData(
            currentProcess.getAttentionMatrix(),
            currentProcess.getSourceTokens(),
            currentProcess.getTargetTokens()
        );
        probabilityView.setProbabilities(currentProcess.getProbabilities());
        translationResultLabel.setText(currentProcess.getTargetText());
        
        // Päivitä decoder-step
        updateDecoderStep();
    }
    
    private void updateDecoderStep() {
        if (currentProcess == null || 
            currentProcess.getDecoderSteps() == null || 
            currentProcess.getDecoderSteps().isEmpty()) {
            return;
        }
        
        decoderStepView.setDecoderStep(
            currentProcess.getDecoderSteps().get(currentStepIndex)
        );
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Huomio");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}