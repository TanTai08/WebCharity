package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Payment_model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Payment_Repo extends JpaRepository<Payment_model, Long> {
}
