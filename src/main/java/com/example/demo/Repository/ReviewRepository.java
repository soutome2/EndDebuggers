package com.example.demo.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	//条件抽出
	List<Review> findAIIByEname(String ename);
	


	List<Review> findAIIByCid(String Cid);
	
	List<Review> findAIIByCidOrderByReviewdateDescReviewtimeDesc(String Cid);

	List<Review> findAIIByEnameAndStar(String ename, Integer star);

	// 日付の範囲で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroup(String ename, LocalDate startDate, LocalDate endDate,
			@Param("star") Integer star);

	//下限のみの設定
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameAndStartDate(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star);

	//上限のみの設定
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameAndEndDate(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star);

	
	
	
	//並び替え星淳
	List<Review> findAIIByEnameOrderByStar(String ename);
	
	

	List<Review> findAIIByEnameAndStarOrderByStar(String ename, Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY star ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByStar(String ename, LocalDate startDate, LocalDate endDate,
			@Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY star ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByStar(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY star ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByStar(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star);

	
	
	//星順逆順
	List<Review> findAIIByEnameOrderByStarDesc(String ename);

	List<Review> findAIIByEnameAndStarOrderByStarDesc(String ename, Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByStarDesc(String ename, LocalDate startDate, LocalDate endDate,
			@Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByStarDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByStarDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star);
	

	
	
	//並び替え日時順
	List<Review> findAIIByEnameOrderByReviewdateAscReviewtimeAsc(String ename);

	List<Review> findAIIByEnameAndStarOrderByReviewdateAscReviewtimeAsc(String ename, Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByReviewdateAscReviewtimeAsc(String ename, LocalDate startDate,
			LocalDate endDate,
			@Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByReviewdateAscReviewtimeAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByReviewdateAscReviewtimeAsc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star);

	
	
	
	//日時順逆順
	List<Review> findAIIByEnameOrderByReviewdateDescReviewtimeDesc(String ename);

	List<Review> findAIIByEnameAndStarOrderByReviewdateDescReviewtimeDesc(String ename, Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(String ename, LocalDate startDate,
			LocalDate endDate,
			@Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star);

	//日付順逆順
	List<Review> findAIIByEnameOrderByReviewdateDesc(String ename);

}
