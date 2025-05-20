package com.vitec.ui;

import com.vitec.model.Token;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.List;

public class TokenSequenceView extends HBox {
    
    public TokenSequenceView() {
        setSpacing(5);
    }
    
    public void setTokens(List<Token> tokens) {
        getChildren().clear();
        
        if (tokens == null) {
            return;
        }
        
        for (Token token : tokens) {
            // Luo laatikko jokaiselle tokenille
            Rectangle background = new Rectangle(80, 40);
            background.setFill(Color.web(token.getColor()));
            background.setArcWidth(10);
            background.setArcHeight(10);
            background.setStroke(Color.BLACK);
            background.setStrokeWidth(1);
            
            Text text = new Text(token.getText());
            text.setFill(Color.BLACK);
            
            StackPane tokenView = new StackPane(background, text);
            getChildren().add(tokenView);
        }
    }
}