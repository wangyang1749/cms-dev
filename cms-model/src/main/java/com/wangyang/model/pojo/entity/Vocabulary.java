package com.wangyang.model.pojo.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "t_vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "word")
    private String word;
    @Column(name = "explains")
    private String explains;
    @Column(name = "uk_speech")
    private String ukSpeech;
    @Column(name = "uk_phonetic")
    private String ukPhonetic;
    @Column(name = "us_speech")
    private String usSpeech;
    @Column(name = "us_phonetic")
    private String usPhonetic;
}
