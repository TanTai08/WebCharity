package com.example.SGUCharity_Project.Repository;

import com.example.SGUCharity_Project.Model.Account_admin_model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Account_Repo extends JpaRepository<Account_admin_model, Long> {
    // Tìm người dùng dựa trên tên đăng nhập
    Optional<Account_admin_model> findByUsername(String username);
}

