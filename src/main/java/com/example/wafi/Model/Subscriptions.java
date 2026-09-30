package com.example.wafi.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @Positive(message = "userId must be positive")
    @NotNull(message = "userId can not be empty")
    private Integer userId;

    @Column(columnDefinition = "varchar(20) not null")
    @Size(max = 20, message = "max provideName length is 20" )
    @NotEmpty(message = "providerName can not be empty")
    private String providerName;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "planName can not be empty")
    @Size(max = 30, message = "max planName length is 30" )
    private String planName;

    @Column(columnDefinition = "varchar(10) not null")
    @Check(constraints = "category='compo' or category='data' or category = 'minutes'")
    @NotEmpty(message = "category can not be empty")
    @Pattern(regexp = "data|minutes|compo", message = "category must be one of: data, minutes, compo")
    private String category;

    @Column(columnDefinition = "double not null")
    @NotNull(message = "charge can not be empty")
    @PositiveOrZero(message = "charge can not be negative")
    private Double charge;

    @Column(columnDefinition = "double")
    private Double dataAllowanceGb;

    @Column(columnDefinition = "double")
    private Double minutesAllowance;

    @Column(columnDefinition = "double")
    private Double smsAllowance;

    @Column(columnDefinition = "varchar(15) not null")
    @NotEmpty(message = "period can not be empty")
    @Pattern(regexp = "DAY|WEEK|MONTH|THREE_MONTHS|SIX_MONTHS|YEAR",
            message = "period must be one of: DAY, WEEK, MONTH, THREE_MONTHS, SIX_MONTHS, YEAR")
    private String period;

    @Column(columnDefinition = "date not null")
    @NotNull(message = "startDate can not be empty")
    @PastOrPresent(message = "startDate cannot be in the future")
    private LocalDate startDate;

    // endDate calculated in service layer from startDate + period

//    @Column(columnDefinition = "varchar(7) not null")
//    @NotEmpty(message = "status can not be empty")
//    @Pattern(regexp = "ACTIVE|EXPIRED", message = "status must be ACTIVE or EXPIRED")
//    private String status;

    @Column(columnDefinition = "varchar(7) not null")
    private String status;

    @Column(columnDefinition = "boolean not null")
    @NotNull(message = "autoRenew can not be empty")
    private Boolean autoRenew;

    @Column(columnDefinition = "varchar(1000)")
    private String message;

    @Column(columnDefinition = "varchar(1000)")
    private String aiSuggestion;

    private Boolean sent;

}