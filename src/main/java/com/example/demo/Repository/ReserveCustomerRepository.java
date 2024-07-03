package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.ReserveCustomer;

public interface ReserveCustomerRepository extends JpaRepository<ReserveCustomer,String>{

	
	@Query(value="SELECT cid,cname,password,tel,mailaddress,reserveid,ename,"
			+ "reservedate,reservetime,detail "
			+ "FROM m_customer "
			+ "JOIN t_reserve using(cid) WHERE cid=:cid",nativeQuery = true)
	List<ReserveCustomer> findAIIBycustomerId(@Param("cid") String cid);
}
