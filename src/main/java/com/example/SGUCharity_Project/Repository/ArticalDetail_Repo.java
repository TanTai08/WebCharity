package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Articaldetail_model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticalDetail_Repo extends JpaRepository<Articaldetail_model, Long> {
    List<Articaldetail_model> findByartical_id(Long artical_id);
}
