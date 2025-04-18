package com.example.irisservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.irisservice.dto.PredictionRequest;
import com.example.irisservice.dto.PredictionResponse;
import com.example.irisservice.service.ModelService;
import com.example.irisservice.service.ModelService.PredictionResult;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {
    
    private static final Logger log = LoggerFactory.getLogger(PredictionController.class);
    
    private final ModelService modelService;
    
    @Autowired
    public PredictionController(ModelService modelService) {
        this.modelService = modelService;
    }
    
    @PostMapping
    public ResponseEntity<PredictionResponse> predict(@RequestBody PredictionRequest request) {
        log.info("Ricevuta richiesta di predizione: {}", request);
        
        // Estrai l'array di feature dalla richiesta
        double[] features = request.toFeaturesArray();
        
        // Effettua la predizione tramite il servizio
        PredictionResult result = modelService.predict(features);
        
        // Crea la risposta
        PredictionResponse response = PredictionResponse.fromPredictionResult(result);
        
        log.info("Predizione completata: classe={}, nome={}", 
                 response.getClassIndex(), response.getClassName());
        
        return ResponseEntity.ok(response);
    }
}