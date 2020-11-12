package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "manual_images", schema = "food_safety")
public class ManualImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "manual_image1")
    private String manualImage1;

    @Column(name = "manual_image2")
    private String manualImage2;

    @Column(name = "manual_image3")
    private String manualImage3;

    @Column(name = "manual_image4")
    private String manualImage4;

    @Column(name = "manual_image5")
    private String manualImage5;

    @Column(name = "manual_image6")
    private String manualImage6;

    @Column(name = "manual_image7")
    private String manualImage7;

    @Column(name = "manual_image8")
    private String manualImage8;

    @Column(name = "manual_image9")
    private String manualImage9;

    @Column(name = "manual_image10")
    private String manualImage10;

    @Column(name = "manual_image11")
    private String manualImage11;

    @Column(name = "manual_image12")
    private String manualImage12;

    @Column(name = "manual_image13")
    private String manualImage13;

    @Column(name = "manual_image14")
    private String manualImage14;

    @Column(name = "manual_image15")
    private String manualImage15;

    @Column(name = "manual_image16")
    private String manualImage16;

    @Column(name = "manual_image17")
    private String manualImage17;

    @Column(name = "manual_image18")
    private String manualImage18;

    @Column(name = "manual_image19")
    private String manualImage19;

    @Column(name = "manual_image20")
    private String manualImage20;

    @OneToOne(mappedBy = "manual_images")
    private Recipes recipe;

}
