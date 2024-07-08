package com.example.demo.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve,Integer>{
	public List<Reserve> findAllByEname(String ename);
	public List<Reserve> findAllByCid(String cid);
	public Optional<Reserve> findByReservedateAndReservetimeAndEname(LocalDate reserveDate, LocalTime reserveTime, String ename);
	public Optional<Reserve> findByReservedateAndReservetimeAndCid(LocalDate reserveDate, LocalTime reserveTime, String cid);
	public Reserve findByReserveid(Integer reserveid);
	public void deleteByReserveid(Integer reserveid);
}
