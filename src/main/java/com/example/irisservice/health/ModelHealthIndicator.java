package com.example.irisservice.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.example.irisservice.service.ModelService;
import com.example.irisservice.service.ModelService.PredictionResult;

@Component
public class ModelHealthIndicator implements HealthIndicator {

    private final ModelService modelService;
    
    @Autowired
    public ModelHealthIndicator(ModelService modelService) {
        this.modelService = modelService;
    }
    
    @Override
    public Health health() {
        try {
            // Verifica che il modello sia funzionante con un esempio tipico di Iris setosa
            double[] testFeatures = {5.1, 3.5, 1.4, 0.2}; // Esempio Iris setosa
            PredictionResult result = modelService.predict(testFeatures);
            
            // Verifica che la predizione sia quella attesa (classe 0 = Setosa)
            if (result.getPredictedClass() == 0) {
                return Health.up()
                        .withDetail("status", "Model is working correctly")
                        .withDetail("testPrediction", result.getPredictedClassName())
                        .build();
            } else {
                return Health.down()
                        .withDetail("status", "Model is not predicting correctly")
                        .withDetail("expectedClass", "Setosa")
                        .withDetail("actualClass", result.getPredictedClassName())
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("status", "Model prediction failed")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}