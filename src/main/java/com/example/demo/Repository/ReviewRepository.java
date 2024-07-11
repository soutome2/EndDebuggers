package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Review;

public interface ReviewRepository extends JpaRepository <Review, Integer>{
	
	//条件抽出
	List<Review> findAIIByEname(String ename);
	List<Review> findAIIByEnameAndStar(String ename, Integer star);
	List<Review> findAIIByCid(String Cid);
	
	//並び替え
	List<Review> findAIIByEnameOrderByStar(String ename);
	List<Review> findAIIByEnameOrderByReviewdate(String ename);
	List<Review> findAIIByEnameOrderByReviewdateAscReviewtimeAsc(String ename);
	
	//逆順
	List<Review> findAIIByEnameOrderByStarDesc(String ename);
	List<Review> findAIIByEnameOrderByReviewdateDesc(String ename);
	List<Review> findAIIByEnameOrderByReviewdateDescReviewtimeDesc(String ename);

}
