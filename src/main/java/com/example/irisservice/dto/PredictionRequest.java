package com.example.irisservice.dto;

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

