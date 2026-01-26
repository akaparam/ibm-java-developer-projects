package com.retailstore.feedback.service;

import com.retailstore.feedback.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private GeminiService geminiService;

    private static final String FILE = "sentiment_feedback_output.txt";
    private List<EnhancedFeedback> cache;

    public synchronized List<EnhancedFeedback> getEnhancedFeedback() throws IOException {
        if (cache != null) return cache;

        List<EnhancedFeedback> result = new ArrayList<>();
        for (FeedbackEntry entry : read()) {
            EnhancedFeedback ef = new EnhancedFeedback(entry);
            ef.setCategory("General");
            ef.setActionableInsight("Review feedback manually");
            result.add(ef);
        }
        return cache = result;
    }

    public FeedbackSummary generateFeedbackSummary() throws IOException {
        List<EnhancedFeedback> all = getEnhancedFeedback();
        FeedbackSummary s = new FeedbackSummary();

        s.setTotalFeedback(all.size());

        s.setSentimentCounts(count(all, EnhancedFeedback::getSentiment));
        s.setCategoryCounts(count(all, EnhancedFeedback::getCategory));
        s.setDepartmentCounts(count(all, EnhancedFeedback::getDepartment));

        s.setRecentFeedback(
            all.stream()
               .sorted(Comparator.comparing(EnhancedFeedback::getId).reversed())
               .limit(5)
               .collect(Collectors.toList())
        );
        return s;
    }

    private List<FeedbackEntry> read() throws IOException {
        List<FeedbackEntry> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            FeedbackEntry e = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Feedback #")) {
                    if (e != null) list.add(e);
                    e = new FeedbackEntry();
                    e.setId(Integer.parseInt(line.replaceAll("\\D+", "")));
                } else if (line.startsWith("Customer:")) e.setCustomer(line.substring(9));
                else if (line.startsWith("Department:")) e.setDepartment(line.substring(11));
                else if (line.startsWith("Date:")) e.setDate(line.substring(5));
                else if (line.startsWith("Comment:")) e.setComment(line.substring(8));
                else if (line.startsWith("Sentiment:")) e.setSentiment(line.substring(10));
            }
            if (e != null) list.add(e);
        }
        return list;
    }

    private Map<String, Integer> count(
            List<EnhancedFeedback> list,
            java.util.function.Function<EnhancedFeedback, String> fn) {

        Map<String, Integer> map = new HashMap<>();
        for (EnhancedFeedback e : list)
            map.put(fn.apply(e), map.getOrDefault(fn.apply(e), 0) + 1);
        return map;
    }
}
