package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve,Integer>{

}
