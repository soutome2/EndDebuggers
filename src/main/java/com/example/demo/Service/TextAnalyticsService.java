package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentenceSentiment;
import com.azure.core.credential.AzureKeyCredential;

/**
 * このクラスはAzureのTextAnalytics関連のメソッドを用意するクラスです。<br>
 * endpointとapiKeyが必要な処理はすべてここに記述して下さい。
 * @author kachi
 */
@Service
public class TextAnalyticsService {

	private final String endpoint = "https://acetextanalytics.cognitiveservices.azure.com/";
	private final String apiKey = "10db3d4fde344e469f01a776d482df8a";

	private final TextAnalyticsClient client;

	/**
	 * endpointやapiKeyをもとにテキストアナリティクスを使用できるクライアントを作成。
	 * @author kachi
	 */
	public TextAnalyticsService() {
		this.client = new TextAnalyticsClientBuilder()
				.credential(new AzureKeyCredential(apiKey))
				.endpoint(endpoint)
				.buildClient();
	}

	/**
	 * 感情分析の結果を出力するメソッドです。<br>
	 * DocumentSentimentの形式で値を返します。<br>
	 * Sysoutの記述にあるようにSentenceSentimentを利用することでセンテンスごとの数値も確認可能です。
	 * @param text 感情分析を行いたいテキスト
	 * @return textの分析結果を保持したDocumentSentiment
	 * @author kachi
	 */
	public DocumentSentiment analyzeSentiment(String text) {
		try {
			//日本語で判断
			String language = "ja";
			DocumentSentiment documentSentiment = client.analyzeSentiment(text, language);

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


	/**
	 * 
	 * @param positiveRate
	 * @param neutralRate
	 * @param negativeRate
	 * @return
	 * @author seino
	 */
	public String MaxRateSentiment(Double positiveRate, Double neutralRate, Double negativeRate) {
		
		String flagSentiment = "neutral";
		
		Double epsilon=0.0000001;
		//positiveとnegaive等しい場合neutral扱い
		if (Math.abs(positiveRate-negativeRate)<epsilon) {
			flagSentiment = "neutral";
		}
		else if (positiveRate >= neutralRate && positiveRate > negativeRate) {
			flagSentiment = "positive";
			System.out.println("1");
		}
	
		//positiveのスコアがもっとも高くないかつnegativeの信頼度がneutral以上場合、もっとも高いのはneutral
		else if (negativeRate >= neutralRate) {
			flagSentiment = "negative";
		} 
		
		//今までの条件式を満たさない場合はpositiveとnegativeが等しい場合のみその場合はneutral
		return flagSentiment;

	}
}