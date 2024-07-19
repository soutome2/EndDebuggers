package com.example.demo.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

/**
 * このクラスはJson形式にするwebAPI関連のメソッドを用意するクラスです。<br>
 * エラー処理や複数回使用される処理を保存するのに適しています。
 * @author seino
 */
@Service
@AllArgsConstructor
public class JsonConverterService {

	private ObjectMapper objectMapper; // JacksonのObjectMapperをDI（Dependency Injection）する

	/**
	 * EntityをJSON文字列に変換。
	 * @param list
	 * @return
	 * @author seino
	 */
	public String EntityToJson(List<Review> list) {

		try {
			// リストをJSON形式の文字列に変換する
			String json = objectMapper.writeValueAsString(list);
			//JsonNode jsons = objectMapper.readTree(json);
			//System.out.println(jsons);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.println("ダメじゃん");
			return null; // エラー時はnullを返すなどの適切な処理を行う
		}
	}

	
	/**
	 * json文字列をEntityに変換。
	 * @param json
	 * @return
	 * @author seino
	 */
	public List<Review> JsonToEntity(String json) {

		try {
			List<Review> reviewList = objectMapper.readValue(json, new TypeReference<List<Review>>() {
			});
			return reviewList;
		} catch (IOException e) {
			//取得したjsonがnull(レビューがない)ときは空のリストを返す。htmlでレビューがないときの判定
			List<Review> emptyList = new ArrayList<>();
			return emptyList;
		}
	}

	/**
	 * URL文字列の連結処理。
	 * @param baseUrl
	 * @param endpoint
	 * @param queryename
	 * @return
	 * @author seino
	 */
	public String MakeFilePath(String baseUrl, String endpoint, String queryename) {
		String apiUrl = baseUrl + endpoint + queryename;
		return apiUrl;
	}

	/**
	 * オーバーライドで引数が与えられない場合の処理はここに書くデフォルト引数の処理。
	 * @param baseUrl
	 * @param endpoint
	 * @return
	 * @author seino
	 */
	public String MakeFilePath(String baseUrl, String endpoint) {
		String queryename = "?ename=田中太郎";
		String apiUrl = baseUrl + endpoint + queryename;
		return apiUrl;
	}

	/**
	 * 
	 * @param baseUrl
	 * @param endpoint
	 * @param queryename
	 * @param querytitle
	 * @param querycomment
	 * @param querystar
	 * @return
	 * @author seino
	 */
	public String MakeFilePathInsert(String baseUrl, String endpoint,
			String queryename, String querytitle, String querycomment, String querystar) {
		String apiUrl = baseUrl + endpoint
				+ "?ename=" + queryename
				+ "&title=" + querytitle
				+ "&comment=" + querycomment
				+ "&star=" + querystar;
		return apiUrl;
	}

}
