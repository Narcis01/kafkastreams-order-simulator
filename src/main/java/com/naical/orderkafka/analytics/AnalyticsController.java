package com.naical.orderkafka.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Slf4j
@Component
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/city")
    public ResponseEntity<?> cityCount(){
        return ResponseEntity.ok(analyticsService.cityCount());
    }

    @GetMapping("/subscribe")
    public ResponseEntity<?> subscribeCount(){
        return ResponseEntity.ok(analyticsService.subscribeCount());
    }
}
