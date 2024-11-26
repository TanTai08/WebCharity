package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Authorization_model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface Authorization_Repo extends JpaRepository<Authorization_model, Long> {
    Optional<Authorization_model> findByUsername(String username);
}
