package com.vitec.service;

import com.vitec.model.*;
import java.util.*;

public class MockTranslationAPI {
    
    private static final String[] COLORS = {
        "#8dd3c7", "#ffffb3", "#bebada", "#fb8072", 
        "#80b1d3", "#fdb462", "#b3de69", "#fccde5"
    };
    
    public TranslationProcess getTranslationProcess(String text) {
        TranslationProcess process = new TranslationProcess();
        process.setSourceText(text);
        
        // Esimerkkikäännökset
        Map<String, String> translations = new HashMap<>();
        translations.put("Yrityksemme kehittää innovatiivisia ohjelmistoratkaisuja", 
                          "Our company develops innovative software solutions");
        translations.put("Tietoturva on meille ensisijaisen tärkeää", 
                          "Security is of primary importance to us");
        translations.put("Asiakkaamme arvostavat luotettavuuttamme", 
                          "Our customers value our reliability");
        
        // Jos käännöstä ei löydy esimerkeistä, käytetään oletuskäännöstä
        String translation = translations.getOrDefault(text, "This is a mock translation");
        process.setTargetText(translation);
        
        // Tokenisoidaan lähde- ja kohdetekstit
        process.setSourceTokens(tokenize(text, true));
        process.setTargetTokens(tokenize(translation, false));
        
        // Luodaan realistisempi attention-matriisi
        process.setAttentionMatrix(createMockAttentionMatrix(
                process.getSourceTokens().size(), 
                process.getTargetTokens().size()));
        
        // Luodaan todennäköisyydet
        process.setProbabilities(createMockProbabilities());
        
        // Luodaan decoder-vaiheet
        process.setDecoderSteps(createDecoderSteps(text, translation));
        
        return process;
    }
    
    private List<Token> tokenize(String text, boolean isSource) {
        // Käytä eri tokenisaatiota lähdetekstille (suomi) ja kohdetekstille (englanti)
        String[] words = text.split("\\s+");
        List<Token> tokens = new ArrayList<>();
        
        for (int i = 0; i < words.length; i++) {
            tokens.add(new Token(words[i], COLORS[i % COLORS.length]));
        }
        
        return tokens;
    }
    
    private double[][] createMockAttentionMatrix(int sourceSize, int targetSize) {
        double[][] matrix = new double[targetSize][sourceSize];
        
        // Esimerkkivastinavuuksia (suomi-englanti pareja)
        Map<Integer, Integer> wordPairs = new HashMap<>();
        
        // "Yrityksemme kehittää innovatiivisia ohjelmistoratkaisuja" ->
        // "Our company develops innovative software solutions"
        if (sourceSize >= 4 && targetSize >= 5) {
            wordPairs.put(0, 0); // "Yrityksemme" -> "Our"
            wordPairs.put(0, 1); // "Yrityksemme" -> "company" (sama suomen sana kääntyy kahdeksi)
            wordPairs.put(1, 2); // "kehittää" -> "develops"
            wordPairs.put(2, 3); // "innovatiivisia" -> "innovative"
            wordPairs.put(3, 4); // "ohjelmistoratkaisuja" -> "software" 
            wordPairs.put(3, 5); // "ohjelmistoratkaisuja" -> "solutions" (sama suomen sana kääntyy kahdeksi)
        }
        
        // Alusta matriisi pienillä arvoilla
        for (int i = 0; i < targetSize; i++) {
            for (int j = 0; j < sourceSize; j++) {
                matrix[i][j] = 0.1;
            }
        }
        
        // Lisää vahvempia yhteyksiä tiettyjen sanojen välille
        for (Map.Entry<Integer, Integer> pair : wordPairs.entrySet()) {
            int sourceIndex = pair.getKey();
            int targetIndex = pair.getValue();
            
            if (sourceIndex < sourceSize && targetIndex < targetSize) {
                matrix[targetIndex][sourceIndex] = 0.8;
            }
        }
        
        // Normalisoi todennäköisyydet
        for (int i = 0; i < targetSize; i++) {
            double sum = 0;
            for (int j = 0; j < sourceSize; j++) {
                sum += matrix[i][j];
            }
            if (sum > 0) {
                for (int j = 0; j < sourceSize; j++) {
                    matrix[i][j] /= sum;
                }
            }
        }
        
        return matrix;
    }
    
    private List<TranslationProcess.TokenProbability> createMockProbabilities() {
        List<TranslationProcess.TokenProbability> probs = new ArrayList<>();
        
        // Esimerkkitodennäköisyydet
        probs.add(new TranslationProcess.TokenProbability("develops", 0.78));
        probs.add(new TranslationProcess.TokenProbability("creates", 0.15));
        probs.add(new TranslationProcess.TokenProbability("builds", 0.05));
        probs.add(new TranslationProcess.TokenProbability("makes", 0.02));
        
        return probs;
    }
    
    private List<TranslationProcess.DecoderStep> createDecoderSteps(String sourceText, String translation) {
        List<TranslationProcess.DecoderStep> steps = new ArrayList<>();
        String[] targetWords = translation.split("\\s+");
        
        // Määrittele todennäköisyysjakaumat kaikille sanoille
        Map<Integer, List<TranslationProcess.TokenProbability>> stepProbabilitiesMap = new HashMap<>();
        
        // Ensimmäiselle sanalle "Our"
        List<TranslationProcess.TokenProbability> step1Probs = new ArrayList<>();
        step1Probs.add(new TranslationProcess.TokenProbability("Our", 0.85));
        step1Probs.add(new TranslationProcess.TokenProbability("The", 0.10));
        step1Probs.add(new TranslationProcess.TokenProbability("This", 0.05));
        stepProbabilitiesMap.put(0, step1Probs);
        
        // Toiselle sanalle "company"
        List<TranslationProcess.TokenProbability> step2Probs = new ArrayList<>();
        step2Probs.add(new TranslationProcess.TokenProbability("company", 0.70));
        step2Probs.add(new TranslationProcess.TokenProbability("business", 0.20));
        step2Probs.add(new TranslationProcess.TokenProbability("team", 0.10));
        stepProbabilitiesMap.put(1, step2Probs);
        
        // Kolmannelle sanalle "develops"
        List<TranslationProcess.TokenProbability> step3Probs = new ArrayList<>();
        step3Probs.add(new TranslationProcess.TokenProbability("develops", 0.78));
        step3Probs.add(new TranslationProcess.TokenProbability("creates", 0.15));
        step3Probs.add(new TranslationProcess.TokenProbability("builds", 0.05));
        step3Probs.add(new TranslationProcess.TokenProbability("makes", 0.02));
        stepProbabilitiesMap.put(2, step3Probs);
        
        // Neljännelle sanalle "innovative"
        List<TranslationProcess.TokenProbability> step4Probs = new ArrayList<>();
        step4Probs.add(new TranslationProcess.TokenProbability("innovative", 0.68));
        step4Probs.add(new TranslationProcess.TokenProbability("cutting-edge", 0.22));
        step4Probs.add(new TranslationProcess.TokenProbability("new", 0.10));
        stepProbabilitiesMap.put(3, step4Probs);
        
        // Viidennelle sanalle "software"
        List<TranslationProcess.TokenProbability> step5Probs = new ArrayList<>();
        step5Probs.add(new TranslationProcess.TokenProbability("software", 0.75));
        step5Probs.add(new TranslationProcess.TokenProbability("program", 0.15));
        step5Probs.add(new TranslationProcess.TokenProbability("application", 0.10));
        stepProbabilitiesMap.put(4, step5Probs);
        
        // Kuudennelle sanalle "solutions"
        List<TranslationProcess.TokenProbability> step6Probs = new ArrayList<>();
        step6Probs.add(new TranslationProcess.TokenProbability("solutions", 0.80));
        step6Probs.add(new TranslationProcess.TokenProbability("products", 0.15));
        step6Probs.add(new TranslationProcess.TokenProbability("tools", 0.05));
        stepProbabilitiesMap.put(5, step6Probs);
        
        // Myös viimeiselle vaiheelle oletettu todennäköisyysjakauma (lauseen loppu)
        List<TranslationProcess.TokenProbability> endProbs = new ArrayList<>();
        endProbs.add(new TranslationProcess.TokenProbability("<end>", 0.90));
        endProbs.add(new TranslationProcess.TokenProbability(".", 0.10));
        stepProbabilitiesMap.put(targetWords.length - 1, endProbs);
        
        // Luo decoder-vaiheet
        for (int i = 0; i < targetWords.length; i++) {
            TranslationProcess.DecoderStep step = new TranslationProcess.DecoderStep();
            
            // Tämänhetkiset tuotetut sanat
            List<Token> currentOutput = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                currentOutput.add(new Token(targetWords[j], COLORS[(j + 3) % COLORS.length]));
            }
            step.setCurrentOutput(currentOutput);
            
            // Seuraavan sanan todennäköisyydet
            List<TranslationProcess.TokenProbability> nextTokenProbs = stepProbabilitiesMap.get(i);
            if (nextTokenProbs != null) {
                step.setNextTokenProbabilities(nextTokenProbs);
            } else {
                // Jos ei ole määriteltyjä todennäköisyyksiä, luodaan oletusarvot
                List<TranslationProcess.TokenProbability> defaultProbs = new ArrayList<>();
                defaultProbs.add(new TranslationProcess.TokenProbability("(end of sentence)", 1.0));
                step.setNextTokenProbabilities(defaultProbs);
            }
            
            steps.add(step);
        }
        
        return steps;
    }
}