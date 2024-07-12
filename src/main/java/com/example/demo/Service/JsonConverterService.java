package com.example.demo.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JsonConverterService{

	private ObjectMapper objectMapper; // JacksonのObjectMapperをDI（Dependency Injection）する

	//Entityをjson文字列に変換
	public String EntityToJson(List<Review> list) {

		try {
			// リストをJSON形式の文字列に変換する
			String json = objectMapper.writeValueAsString(list);
			System.out.println("え");
			System.out.println("まじか");
			JsonNode jsons = objectMapper.readTree(json);
			System.out.println(jsons);
			System.out.println("まじか");
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.println("ダメじゃん");
			return null; // エラー時はnullを返すなどの適切な処理を行う
		}
	}
	
	//json文字列をEntityに変換
	public List<Review> JsonToEntity(String json) {

        try {
        	 List<Review> reviewList = objectMapper.readValue(json, new TypeReference<List<Review>>(){});
             return reviewList;
        } catch (IOException e) {
        	//取得したjsonがnull(レビューがない)ときは空のリストを返す。htmlでレビューがないときの判定
            List<Review> emptyList = new ArrayList<>();
            return emptyList ;
        }
    }

	
}
