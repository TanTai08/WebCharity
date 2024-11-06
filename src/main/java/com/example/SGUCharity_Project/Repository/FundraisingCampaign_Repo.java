package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.FundraisingCampaign_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FundraisingCampaign_Repo extends JpaRepository<FundraisingCampaign_model, Long> {
    // Tìm các chiến dịch theo category
    List<FundraisingCampaign_model> findByCategory(String category);
}