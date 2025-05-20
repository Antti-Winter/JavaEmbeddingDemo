// src/main/java/com/vitec/model/TranslationProcess.java
package com.vitec.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TranslationProcess {
    private List<Token> sourceTokens;
    private List<Token> targetTokens;
    private double[][] attentionMatrix;
    private List<TokenProbability> probabilities;
    private String sourceText;
    private String targetText;
    
    // Käännöksen eri vaiheet
    private List<DecoderStep> decoderSteps;
    
    // Getterit ja setterit
    public List<Token> getSourceTokens() {
        return sourceTokens;
    }
    
    public void setSourceTokens(List<Token> sourceTokens) {
        this.sourceTokens = sourceTokens;
    }
    
    public List<Token> getTargetTokens() {
        return targetTokens;
    }
    
    public void setTargetTokens(List<Token> targetTokens) {
        this.targetTokens = targetTokens;
    }
    
    public double[][] getAttentionMatrix() {
        return attentionMatrix;
    }
    
    public void setAttentionMatrix(double[][] attentionMatrix) {
        this.attentionMatrix = attentionMatrix;
    }
    
    public List<TokenProbability> getProbabilities() {
        return probabilities;
    }
    
    public void setProbabilities(List<TokenProbability> probabilities) {
        this.probabilities = probabilities;
    }
    
    public String getSourceText() {
        return sourceText;
    }
    
    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
    
    public String getTargetText() {
        return targetText;
    }
    
    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }
    
    public List<DecoderStep> getDecoderSteps() {
        return decoderSteps;
    }
    
    public void setDecoderSteps(List<DecoderStep> decoderSteps) {
        this.decoderSteps = decoderSteps;
    }
    
    // equals, hashCode ja toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationProcess that = (TranslationProcess) o;
        return Objects.equals(sourceTokens, that.sourceTokens) &&
               Objects.equals(targetTokens, that.targetTokens) &&
               Objects.deepEquals(attentionMatrix, that.attentionMatrix) &&
               Objects.equals(probabilities, that.probabilities) &&
               Objects.equals(sourceText, that.sourceText) &&
               Objects.equals(targetText, that.targetText) &&
               Objects.equals(decoderSteps, that.decoderSteps);
    }
    
    @Override
public int hashCode() {
    return Objects.hash(sourceTokens, targetTokens, 
                      Arrays.deepHashCode(attentionMatrix),
                      probabilities, sourceText, targetText, decoderSteps);
}
    
    @Override
    public String toString() {
        return "TranslationProcess{" +
               "sourceTokens=" + sourceTokens +
               ", targetTokens=" + targetTokens +
               ", attentionMatrix=" + java.util.Arrays.deepToString(attentionMatrix) +
               ", probabilities=" + probabilities +
               ", sourceText='" + sourceText + '\'' +
               ", targetText='" + targetText + '\'' +
               ", decoderSteps=" + decoderSteps +
               '}';
    }
    
    // DecoderStep sisäluokka
    public static class DecoderStep {
        private List<Token> currentOutput;
        private List<TokenProbability> nextTokenProbabilities;
        
        // Getterit ja setterit
        public List<Token> getCurrentOutput() {
            return currentOutput;
        }
        
        public void setCurrentOutput(List<Token> currentOutput) {
            this.currentOutput = currentOutput;
        }
        
        public List<TokenProbability> getNextTokenProbabilities() {
            return nextTokenProbabilities;
        }
        
        public void setNextTokenProbabilities(List<TokenProbability> nextTokenProbabilities) {
            this.nextTokenProbabilities = nextTokenProbabilities;
        }
        
        // equals, hashCode ja toString
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DecoderStep that = (DecoderStep) o;
            return Objects.equals(currentOutput, that.currentOutput) &&
                   Objects.equals(nextTokenProbabilities, that.nextTokenProbabilities);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(currentOutput, nextTokenProbabilities);
        }
        
        @Override
        public String toString() {
            return "DecoderStep{" +
                   "currentOutput=" + currentOutput +
                   ", nextTokenProbabilities=" + nextTokenProbabilities +
                   '}';
        }
    }
    
    // TokenProbability sisäluokka
    public static class TokenProbability {
        private String token;
        private double probability;
        
        public TokenProbability(String token, double probability) {
            this.token = token;
            this.probability = probability;
        }
        
        // Getterit ja setterit
        public String getToken() {
            return token;
        }
        
        public void setToken(String token) {
            this.token = token;
        }
        
        public double getProbability() {
            return probability;
        }
        
        public void setProbability(double probability) {
            this.probability = probability;
        }
        
        // equals, hashCode ja toString
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TokenProbability that = (TokenProbability) o;
            return Double.compare(that.probability, probability) == 0 &&
                   Objects.equals(token, that.token);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(token, probability);
        }
        
        @Override
        public String toString() {
            return "TokenProbability{" +
                   "token='" + token + '\'' +
                   ", probability=" + probability +
                   '}';
        }
    }
}