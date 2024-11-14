package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Artical_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Charitycontent_Repo extends JpaRepository<Artical_model, Long> {
    Page<Artical_model> findAll(Pageable pageable);

    @Query("SELECT a FROM Artical_model a WHERE a.id = :searchTerm")
    Page<Artical_model> searchById(@Param("searchTerm") Long searchTerm, Pageable pageable);

    @Query("SELECT a FROM Artical_model a WHERE a.title LIKE %:searchTerm%")
    Page<Artical_model> searchByTitle(@Param("searchTerm") String searchTerm, Pageable pageable);

}
