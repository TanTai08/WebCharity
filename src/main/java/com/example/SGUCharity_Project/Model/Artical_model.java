package com.example.SGUCharity_Project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "goal_amount")
    private double goalAmount;

    @Column(name = "amount_raised")
    private double amountRaised;
    private String img;
    private String status;
    private String displaycategory;


    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "artical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Articaldetail_model> articalDetails;

    @Transient
    private String formattedEndDate;

    public String getFormattedEndDate() {
        return formattedEndDate;
    }

    public void setFormattedEndDate(String formattedEndDate) {
        this.formattedEndDate = formattedEndDate;
    }

    @Transient
    public int getProgressPercentage() {
        if (goalAmount > 0) {
            // Ensure the progress is capped at 100%
            return Math.min(100, (int) Math.round((amountRaised * 100) / goalAmount));
        } else {
            return 0;
        }
    }

    @Transient
    public boolean isActive() {
        return !LocalDate.now().isAfter(endDate);
    }
}