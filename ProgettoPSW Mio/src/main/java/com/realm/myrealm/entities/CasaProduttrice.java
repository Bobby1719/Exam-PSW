package com.realm.myrealm.entities;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="casaProduttrice",schema="progetto")
public class CasaProduttrice {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @Basic
    @Column(name="nome",nullable=true,length=100)
    private String nome;

    @Basic
    @Column(name="sede",nullable=true,length=100)
    private String sede;

    @Basic
    @Column(name="contatto",nullable=true,length=100)                           //era genere
    private String contatto;

    @Basic
    @Column(name="url_stemma",nullable=true,length=100)                          //era url_cover
    private String urlStemma;


    @OneToMany(mappedBy="casaProduttrice",cascade = CascadeType.MERGE)
    private List<Auto> listaAuto;                                               //era tracklist

}