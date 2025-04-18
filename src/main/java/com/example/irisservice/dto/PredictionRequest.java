package com.example.irisservice.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la richiesta di predizione
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {
    private double sepalLength;
    private double sepalWidth;
    private double petalLength;
    private double petalWidth;
    
    /**
     * Converte l'oggetto DTO in un array di feature per il modello
     */
    public double[] toFeaturesArray() {
        return new double[] {sepalLength, sepalWidth, petalLength, petalWidth};
    }
}

/**
 * DTO per la risposta di predizione
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {
    private int classIndex;
    private String className;
    private Map<String, Double> probabilities = new HashMap<>();
    
    /**
     * Crea un oggetto risposta dal risultato della predizione
     */
    public static PredictionResponse fromPredictionResult(
            com.example.irisservice.service.ModelService.PredictionResult result) {
        
        PredictionResponse response = new PredictionResponse();
        response.setClassIndex(result.getPredictedClass());
        response.setClassName(result.getPredictedClassName());
        
        // Mappa delle probabilità per classe
        String[] classNames = {"Setosa", "Versicolor", "Virginica"};
        Map<String, Double> probs = new HashMap<>();
        double[] probArray = result.getProbabilities();
        
        for (int i = 0; i < probArray.length; i++) {
            probs.put(classNames[i], probArray[i]);
        }
        
        response.setProbabilities(probs);
        return response;
    }
}