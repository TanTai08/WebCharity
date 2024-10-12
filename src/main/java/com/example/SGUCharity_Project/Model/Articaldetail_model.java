package com.example.SGUCharity_Project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Articaldetail_model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content_1;
    private String img_content;
    private String content_2;

    @ManyToOne
    @JoinColumn(name = "artical_id", referencedColumnName = "id")
    private Artical_model artical;
}
