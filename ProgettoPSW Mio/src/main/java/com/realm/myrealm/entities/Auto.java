package com.realm.myrealm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="auto",schema="progetto")
public class Auto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @Basic
    @Column(name="modello",nullable=true,length=100)
    private String modello;


    @ManyToOne
    @JoinColumn(name="id_casa_prod",nullable=true)
    @JsonIgnore
    @ToString.Exclude
    private CasaProduttrice casaProduttrice;

    @Basic
    @Column(name="anno_uscita",nullable=true)
    private int annoUscita;

    @Basic
    @Column(name="url_foto",nullable=true,length=100)                          // era url_cover
    private String urlFoto;

    @Basic
    @Column(name="prezzo",nullable=true)
    private int prezzo;

    @Basic
    @Column(name="descrizione",nullable=true, length=8000)                                   //era artista
    private String descrizione;


    @OneToMany(mappedBy="auto",cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<AutoAcquisto> autoInAcquisto;
}