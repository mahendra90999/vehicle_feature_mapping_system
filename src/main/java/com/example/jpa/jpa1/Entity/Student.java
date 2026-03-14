package com.example.jpa.jpa1.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int grno;   // PRIMARY KEY

    private String name;
    private String email;
    private int age;
}