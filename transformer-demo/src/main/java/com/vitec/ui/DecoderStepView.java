package com.vitec.ui;

import com.vitec.model.TranslationProcess.DecoderStep;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DecoderStepView extends VBox {
    
    private TokenSequenceView tokenSequenceView;
    private ProbabilityView probabilityView;
    
    public DecoderStepView() {
        setSpacing(10);
        
        tokenSequenceView = new TokenSequenceView();
        probabilityView = new ProbabilityView();
        
        Text title = new Text("Decoder-vaihe");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        getChildren().addAll(title, tokenSequenceView, probabilityView);
    }
    
    public void setDecoderStep(DecoderStep step) {
        if (step == null) {
            return;
        }
        
        tokenSequenceView.setTokens(step.getCurrentOutput());
        probabilityView.setProbabilities(step.getNextTokenProbabilities());
    }
}