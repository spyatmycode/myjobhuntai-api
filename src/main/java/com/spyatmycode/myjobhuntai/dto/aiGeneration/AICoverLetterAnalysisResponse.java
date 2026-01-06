package com.spyatmycode.myjobhuntai.dto.aiGeneration;

import java.util.List;

public record AICoverLetterAnalysisResponse(
    int matchPercentage,           // e.g., 85
    List<String> matchingKeywords, // e.g., ["Java", "Spring"]
    List<String> missingKeywords,  // e.g., ["Kubernetes"]
    String coverLetter,            // The actual letter text
    List<String> interviewTips     // The 3 talking points
) {
    
}
