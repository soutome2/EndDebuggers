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

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEname(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	// 日付の範囲で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroup(@Param("ename") String ename, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	//下限のみの設定
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameAndStartDate(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	//上限のみの設定
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameAndEndDate(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	//並び替え星淳
	List<Review> findAIIByEnameOrderByStar(String ename);

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameOrderByStar(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByStar(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByStar(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByStar(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	//星順逆順
	List<Review> findAIIByEnameOrderByStarDesc(String ename);

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameOrderByStarDesc(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByStarDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByStarDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByStarDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	//並び替え日時順
	List<Review> findAIIByEnameOrderByReviewdateAscReviewtimeAsc(String ename);

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameOrderByReviewdateAscReviewtimeAsc(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByReviewdateAscReviewtimeAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByReviewdateAscReviewtimeAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByReviewdateAscReviewtimeAsc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	//日時順逆順
	List<Review> findAIIByEnameOrderByReviewdateDescReviewtimeDesc(String ename);

	List<Review> findAIIByEnameOrderByReviewdate(String ename);

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename", nativeQuery = true)
	List<Review> findByEnameOrderByReviewdateDescReviewtimeDesc(@Param("ename") String ename,
			@Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	//日付順逆順
	List<Review> findAIIByEnameOrderByReviewdateDesc(String ename);

}
