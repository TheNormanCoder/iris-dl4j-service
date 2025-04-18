package com.example.irisservice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.irisservice.service.ModelService;
import com.example.irisservice.service.ModelService.PredictionResult;

/**
 * Scheduler per monitorare le prestazioni del modello periodicamente
 */
@Component
public class ModelMonitorScheduler {

    private static final Logger log = LoggerFactory.getLogger(ModelMonitorScheduler.class);
    
    private final ModelService modelService;
    
    @Autowired
    public ModelMonitorScheduler(ModelService modelService) {
        this.modelService = modelService;
    }
    
    /**
     * Esegue un controllo periodico sul modello con dati di esempio
     * ogni ora (espresso in millisecondi = 60 * 60 * 1000)
     */
    @Scheduled(fixedRate = 3600000)
    public void monitorModelHealth() {
        log.info("Esecuzione controllo di salute periodico del modello");
        
        try {
            // Test con esempi noti di ciascuna classe
            double[][] testExamples = {
                {5.1, 3.5, 1.4, 0.2},  // Setosa
                {7.0, 3.2, 4.7, 1.4},  // Versicolor
                {6.3, 3.3, 6.0, 2.5}   // Virginica
            };
            
            int[] expectedClasses = {0, 1, 2};
            String[] classNames = {"Setosa", "Versicolor", "Virginica"};
            
            int correct = 0;
            
            for (int i = 0; i < testExamples.length; i++) {
                PredictionResult result = modelService.predict(testExamples[i]);
                boolean isCorrect = result.getPredictedClass() == expectedClasses[i];
                
                if (isCorrect) {
                    correct++;
                }
                
                log.info("Test esempio {}: atteso={}, predetto={}, corretto={}",
                         classNames[i], classNames[expectedClasses[i]], 
                         result.getPredictedClassName(), isCorrect);
            }
            
            log.info("Controllo completato: {}/{} predizioni corrette", correct, testExamples.length);
            
        } catch (Exception e) {
            log.error("Errore durante il controllo del modello", e);
        }
    }
}