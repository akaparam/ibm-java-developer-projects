package com.retailstore.feedback.controller;

import com.retailstore.feedback.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import com.retailstore.feedback.model.EnhancedFeedback;
import java.io.IOException;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService service;

    @GetMapping("/")
    public String dashboard(Model model) throws Exception {
        model.addAttribute("summary", service.generateFeedbackSummary());
        return "dashboard";
    }
    /**
    * Returns all feedback data in JSON format.
    *
    * @return List of enhanced feedback entries
    */
    @GetMapping("/getfeedback")
    @ResponseBody
    public ResponseEntity<List<EnhancedFeedback>> getFeedback() {
        try {
            List<EnhancedFeedback> feedback = service.getEnhancedFeedback();
            return ResponseEntity.ok(feedback);
        }
        catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
