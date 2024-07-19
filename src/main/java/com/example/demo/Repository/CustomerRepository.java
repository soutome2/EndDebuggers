package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Customer;

/**
 * m_customerテーブル用のリポジトリ
 * @author soutome
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, String>{

}
