package com.example.backend.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "manuals", schema = "food_safety")
public class Manuals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "manual1")
    private String manual1;

    @Column(name = "manual2")
    private String manual2;

    @Column(name = "manual3")
    private String manual3;

    @Column(name = "manual4")
    private String manual4;

    @Column(name = "manual5")
    private String manual5;

    @Column(name = "manual6")
    private String manual6;

    @Column(name = "manual7")
    private String manual7;

    @Column(name = "manual8")
    private String manual8;

    @Column(name = "manual9")
    private String manual9;

    @Column(name = "manual10")
    private String manual10;

    @Column(name = "manual11")
    private String manual11;

    @Column(name = "manual12")
    private String manual12;

    @Column(name = "manual13")
    private String manual13;

    @Column(name = "manual14")
    private String manual14;

    @Column(name = "manual15")
    private String manual15;

    @Column(name = "manual16")
    private String manual16;

    @Column(name = "manual17")
    private String manual17;

    @Column(name = "manual18")
    private String manual18;

    @Column(name = "manual19")
    private String manual19;

    @Column(name = "manual20")
    private String manual20;

}
