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

	List<Review> findAIIByEnameOrderByReviewdateDescReviewtimeDesc(String ename);

	/*通常asc*/

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

	/*星順asc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename"
			+ "ORDER BY star ASC", nativeQuery = true)
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

	/*星順desc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY star DESC", nativeQuery = true)
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

	/*日時順asc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate ASC, reviewtime ASC", nativeQuery = true)
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

	/*日時順desc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY reviewdate DESC, reviewtime DESC", nativeQuery = true)
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

	/*positive順asc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate ASC", nativeQuery = true)
	List<Review> findByEnameOrderByPositiveAsc(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByPositiveAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByPositiveAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByPositiveAsc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	/*positive順desc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate DESC", nativeQuery = true)
	List<Review> findByEnameOrderByPositiveDesc(@Param("ename") String ename,
			@Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByPositiveDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByPositiveDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY positiverate DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByPositiveDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	/*negetive順asc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate ASC", nativeQuery = true)
	List<Review> findByEnameOrderByNegativeAsc(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByNegativeAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByNegativeAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByNegativeAsc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	/*negative順desc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate DESC", nativeQuery = true)
	List<Review> findByEnameOrderByNegativeDesc(@Param("ename") String ename,
			@Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByNegativeDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByNegativeDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY negativerate DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByNegativeDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	/*neutral順asc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate ASC", nativeQuery = true)
	List<Review> findByEnameOrderByNeutralAsc(@Param("ename") String ename, @Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate ASC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByNeutralAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate ASC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByNeutralAsc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate ASC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByNeutralAsc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	/*negative順desc*/

	// 様々な条件で抽出する
	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate DESC", nativeQuery = true)
	List<Review> findByEnameOrderByNeutralDesc(@Param("ename") String ename,
			@Param("star") Integer star,
			@Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate DESC", nativeQuery = true)
	List<Review> findByEnameAndReviewdateGroupOrderByNeutralDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate,
			LocalDate endDate,
			@Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate >= :startDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate DESC", nativeQuery = true)
	List<Review> findByEnameAndStartDateOrderByNeutralDesc(@Param("ename") String ename,
			@Param("startDate") LocalDate startDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

	@Query(value = "SELECT * "
			+ "FROM t_review "
			+ "WHERE reviewdate <= :endDate "
			+ "AND (:star IS NULL OR star = :star) "
			+ "AND  (:sentiment IS NULL OR sentiment = :sentiment) "
			+ "AND ename=:ename "
			+ "ORDER BY neutralrate DESC", nativeQuery = true)
	List<Review> findByEnameAndEndDateOrderByNeutralDesc(@Param("ename") String ename,
			@Param("endDate") LocalDate endDate, @Param("star") Integer star, @Param("sentiment") String sentiment);

}
