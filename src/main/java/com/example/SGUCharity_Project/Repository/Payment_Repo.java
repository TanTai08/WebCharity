package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Payment_model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Payment_Repo extends JpaRepository<Payment_model, Long> {
    List<Payment_model> findByDisplay(int display);
}
