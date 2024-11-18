package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Communitynews_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityNews_Repo extends JpaRepository<Communitynews_model, Long> {
    // Tìm kiếm theo ID
    @Query("SELECT c FROM Communitynews_model c WHERE c.id = :searchTerm")
    Page<Communitynews_model> searchById(@Param("searchTerm") Long searchTerm, Pageable pageable);

    // Tìm kiếm theo tiêu đề
    @Query("SELECT c FROM Communitynews_model c WHERE c.title_news LIKE %:searchTerm%")
    Page<Communitynews_model> searchByTitle(@Param("searchTerm") String searchTerm, Pageable pageable);
}
