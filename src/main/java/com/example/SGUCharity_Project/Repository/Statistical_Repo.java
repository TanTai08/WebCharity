package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Payment_model;
import com.example.SGUCharity_Project.Model.Statistical_model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Statistical_Repo extends JpaRepository<Statistical_model, Long> {
}
