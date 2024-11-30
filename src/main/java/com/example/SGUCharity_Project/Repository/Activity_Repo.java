package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Activity_model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface Activity_Repo extends JpaRepository<Activity_model, Long> {
    List<Activity_model> findByUsername(String username);

    List<Activity_model> findByDatetimeBetween(LocalDateTime start, LocalDateTime end);

    List<Activity_model> findByUsernameAndDatetimeBetween(String username, LocalDateTime start, LocalDateTime end);
}

