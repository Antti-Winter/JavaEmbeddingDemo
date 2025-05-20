package com.vitec.ui;

import com.vitec.model.TranslationProcess.TokenProbability;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.List;

public class ProbabilityView extends VBox {
    
    private BarChart<String, Number> chart;
    
    public ProbabilityView() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
        chart = new BarChart<>(xAxis, yAxis);
        
        chart.setTitle("Todennäköisyysjakauma");
        xAxis.setLabel("Seuraava sana");
        yAxis.setLabel("Todennäköisyys");
        
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        
        getChildren().add(chart);
    }
    
    public void setProbabilities(List<TokenProbability> probabilities) {
        chart.getData().clear();
        
        if (probabilities == null || probabilities.isEmpty()) {
            // Luo selkeä viesti tyhjälle tilalle
            XYChart.Series<String, Number> emptySeries = new XYChart.Series<>();
            emptySeries.getData().add(new XYChart.Data<>("Ei seuraavaa sanaa", 0.0));
            chart.getData().add(emptySeries);
            
            // Tyylitä tyhjä palkki harmaaksi
            for (XYChart.Data<String, Number> item : emptySeries.getData()) {
                item.getNode().setStyle("-fx-bar-fill: #cccccc;");
            }
            
            return;
        }
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        for (TokenProbability prob : probabilities) {
            series.getData().add(new XYChart.Data<>(prob.getToken(), prob.getProbability()));
        }
        
        chart.getData().add(series);
        
        // Värikoodaa palkit
        int colorIndex = 0;
        String[] colors = {"#4CAF50", "#2196F3", "#FFC107", "#FF5722", "#9C27B0"};
        
        for (XYChart.Data<String, Number> item : series.getData()) {
            String color = colors[colorIndex % colors.length];
            item.getNode().setStyle("-fx-bar-fill: " + color + ";");
            colorIndex++;
        }
    }
}