package com.example.wafi.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    @NotEmpty(message = "name can not be empty")
    @Size(max = 50, message = "name max length is 50")
    private String name;

    @Column(columnDefinition = "varchar(255) not null unique")
    @NotEmpty(message = "email can not be empty")
    @Email(message = "email must contain @")
    private String email;

    @Column(columnDefinition = "varchar(10) not null")
    @NotEmpty(message = "phone number can not be empty")
    @Size(min = 10, max = 10, message = "phone length should be 10")
    @Pattern(regexp = "^05.*", message = "phone number should start with 05")
    private String phoneNumber;


}