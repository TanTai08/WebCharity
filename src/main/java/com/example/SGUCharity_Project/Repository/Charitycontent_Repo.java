package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Artical_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Charitycontent_Repo extends JpaRepository<Artical_model, Long> {
    Page<Artical_model> findAll(Pageable pageable);
}