package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Review;

public interface ReviewRepository extends JpaRepository <Review,Integer>{

}
