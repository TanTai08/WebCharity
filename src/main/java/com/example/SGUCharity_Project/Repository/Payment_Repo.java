package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Payment_model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Payment_Repo extends JpaRepository<Payment_model, Long> {

    // Find all payments by display status
    List<Payment_model> findByDisplay(int display);

    // Search by ID (number format)
    @Query("SELECT a FROM Payment_model a WHERE a.id = :searchTerm")
    Page<Payment_model> searchById(@Param("searchTerm") Long searchTerm, Pageable pageable);

    // Search by orderId (assuming it's a String, modify if it's a different type)
    @Query("SELECT a FROM Payment_model a WHERE a.orderId LIKE %:searchTerm%")
    Page<Payment_model> searchByOrderId(@Param("searchTerm") String searchTerm, Pageable pageable);


}
