package com.example.irisservice.service;

import java.io.File;
import java.io.IOException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class ModelService {
    
    private static final Logger log = LoggerFactory.getLogger(ModelService.class);
    
    @Value("${model.file.path:models/irisModel.zip}")
    private String modelFilePath;
    
    private MultiLayerNetwork model;
    
    @PostConstruct
    public void init() {
        try {
            File modelFile = new File(modelFilePath);
            if (!modelFile.exists()) {
                throw new IOException("Il file del modello non esiste: " + modelFilePath);
            }
            
            log.info("Caricamento del modello da: {}", modelFilePath);
            model = MultiLayerNetwork.load(modelFile, true);
            log.info("Modello caricato con successo");
        } catch (IOException e) {
            log.error("Errore durante il caricamento del modello", e);
            throw new RuntimeException("Impossibile caricare il modello", e);
        }
    }
    
    /**
     * Esegue la predizione utilizzando il modello caricato.
     * 
     * @param features Array di feature (4 valori per Iris: lunghezza sepalo, larghezza sepalo, 
     *                 lunghezza petalo, larghezza petalo)
     * @return Risultato della predizione con classe e probabilità
     */
    public PredictionResult predict(double[] features) {
        INDArray input = Nd4j.create(features).reshape(1, features.length);
        INDArray output = model.output(input);
        
        int predictedClass = output.argMax(1).getInt(0);
        double[] probabilities = output.getRow(0).toDoubleVector();
        
        return new PredictionResult(predictedClass, probabilities);
    }
    
    /**
     * Classe interna per rappresentare il risultato della predizione
     */
    public static class PredictionResult {
        private final int predictedClass;
        private final double[] probabilities;
        
        // Mapping delle classi per Iris (può essere personalizzato per altri dataset)
        private static final String[] CLASS_NAMES = {"Setosa", "Versicolor", "Virginica"};
        
        public PredictionResult(int predictedClass, double[] probabilities) {
            this.predictedClass = predictedClass;
            this.probabilities = probabilities;
        }
        
        public int getPredictedClass() {
            return predictedClass;
        }
        
        public String getPredictedClassName() {
            return CLASS_NAMES[predictedClass];
        }
        
        public double[] getProbabilities() {
            return probabilities;
        }
    }
}