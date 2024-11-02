package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.FundraisingCampaign_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraisingCampaign_Repo extends JpaRepository<FundraisingCampaign_model, Long> {
}
