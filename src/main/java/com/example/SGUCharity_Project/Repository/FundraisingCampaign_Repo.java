package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.FundraisingCampaign_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FundraisingCampaign_Repo extends JpaRepository<FundraisingCampaign_model, Long> {

    // Tìm các chiến dịch theo category
    List<FundraisingCampaign_model> findByCategory(String category);

    // Tìm chiến dịch theo ID
    @Query("SELECT f FROM FundraisingCampaign_model f WHERE f.id = :searchTerm")
    Page<FundraisingCampaign_model> searchById(@Param("searchTerm") Long searchTerm, Pageable pageable);

    // Tìm chiến dịch theo tiêu đề
    @Query("SELECT f FROM FundraisingCampaign_model f WHERE f.title LIKE %:searchTerm%")
    Page<FundraisingCampaign_model> searchByTitle(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Tìm chiến dịch theo cột code
    List<FundraisingCampaign_model> findByCode(String code);
}
