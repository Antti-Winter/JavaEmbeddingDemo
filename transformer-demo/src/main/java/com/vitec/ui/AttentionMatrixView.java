package com.vitec.ui;

import com.vitec.model.Token;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.List;

public class AttentionMatrixView extends GridPane {
    
    private double[][] attentionData;
    private List<Token> sourceTokens;
    private List<Token> targetTokens;
    
    public AttentionMatrixView() {
        setHgap(2);
        setVgap(2);
    }
    
    public void setData(double[][] attentionData, List<Token> sourceTokens, List<Token> targetTokens) {
        this.attentionData = attentionData;
        this.sourceTokens = sourceTokens;
        this.targetTokens = targetTokens;
        
        updateView();
    }
    
    private void updateView() {
        getChildren().clear();
        
        if (attentionData == null || sourceTokens == null || targetTokens == null) {
            return;
        }
        
        // Lisää otsikkorivi (source tokens)
        for (int i = 0; i < sourceTokens.size(); i++) {
            Text text = new Text(sourceTokens.get(i).getText());
            text.setRotate(270);
            add(text, i + 1, 0);
        }
        
        // Lisää ensimmäinen sarake (target tokens)
        for (int i = 0; i < targetTokens.size(); i++) {
            Text text = new Text(targetTokens.get(i).getText());
            add(text, 0, i + 1);
        }
        
        // Lisää attention-solut
        for (int i = 0; i < targetTokens.size(); i++) {
            for (int j = 0; j < sourceTokens.size(); j++) {
                double attentionValue = attentionData[i][j];
                
                // Luo väri attention-arvon perusteella (punaiset sävyt - korkeampi arvo = tummempi punainen)
                Color cellColor = Color.rgb(
                    255, 
                    (int) (255 * (1 - attentionValue)), 
                    (int) (255 * (1 - attentionValue))
                );
                
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(cellColor);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0.5);
                
                Text valueText = new Text(String.format("%.2f", attentionValue));
                valueText.setFill(attentionValue > 0.5 ? Color.WHITE : Color.BLACK);
                
                StackPane cell = new StackPane(rect, valueText);
                add(cell, j + 1, i + 1);
                
                // Lisää hover-efekti korostamaan arvoa
                cell.setOnMouseEntered(e -> {
                    rect.setStroke(Color.BLUE);
                    rect.setStrokeWidth(2);
                });
                
                cell.setOnMouseExited(e -> {
                    rect.setStroke(Color.BLACK);
                    rect.setStrokeWidth(0.5);
                });
            }
        }
    }
}