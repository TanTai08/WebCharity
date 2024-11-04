package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Contact_model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Contact_Repo extends JpaRepository<Contact_model, Long> {
}
