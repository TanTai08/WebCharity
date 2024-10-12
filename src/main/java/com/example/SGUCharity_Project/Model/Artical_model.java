package com.example.SGUCharity_Project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artical_model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String img;
    private String status;
    private String displaycategory;

    @OneToMany(mappedBy = "artical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Articaldetail_model> articalDetails;
}
