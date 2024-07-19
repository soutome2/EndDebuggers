package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentenceSentiment;
import com.azure.core.credential.AzureKeyCredential;

@Service
public class TextAnalyticsService {

	private final String endpoint = "https://acetextanalytics.cognitiveservices.azure.com/";
	private final String apiKey = "10db3d4fde344e469f01a776d482df8a";

	private final TextAnalyticsClient client;

	public TextAnalyticsService() {
		this.client = new TextAnalyticsClientBuilder()
				.credential(new AzureKeyCredential(apiKey))
				.endpoint(endpoint)
				.buildClient();
	}

	public DocumentSentiment analyzeSentiment(String text) {
		try {
			DocumentSentiment documentSentiment = client.analyzeSentiment(text, "ja");

			// ログ出力例
			System.out.println("Document sentiment: " + documentSentiment.getSentiment());

			for (SentenceSentiment sentenceSentiment : documentSentiment.getSentences()) {
				System.out.println("Sentence: " + sentenceSentiment.getText());
				System.out.println("Sentiment: " + sentenceSentiment.getSentiment());
				System.out.println("Positive score: " + sentenceSentiment.getConfidenceScores().getPositive());
				System.out.println("Negative score: " + sentenceSentiment.getConfidenceScores().getNegative());
				System.out.println("Neutral score: " + sentenceSentiment.getConfidenceScores().getNeutral());
			}

			return documentSentiment; // 分析結果を返す
		} catch (Exception e) {
			// エラーハンドリング
			System.err.println("Analyze sentiment error: " + e.getMessage());
			return null;
		}
	}
	public String MaxRateSentiment(Double positiveRate,Double neutralRate,Double negativeRate) {
		String flagSentiment="neutral";
		
		if(positiveRate>neutralRate&&positiveRate>negativeRate) {
			flagSentiment="positive";
			System.out.println("1");
		}
		else if(neutralRate>negativeRate) {
			flagSentiment="neutral";
			System.out.println("2");
		}
		else if(negativeRate>neutralRate) {
			flagSentiment="negative";
			System.out.println("3");
		}
				
		
		return flagSentiment;
		
	}
}