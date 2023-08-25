package com.realm.myrealm.entities;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="utente",schema="progetto")
public class Utente {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @Basic
    @Column(name="nome",nullable=true,length=50)
    private String nome;

    @Basic
    @Column(name="cognome",nullable=true,length=50)
    private String cognome;

    @Basic
    @Column(name="anno_nascita",nullable=true)
    private int annoNascita;

    @Basic
    @Column(name="email",nullable=true)
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy="utente",cascade=CascadeType.MERGE)
    @JsonIgnore
    private List<Acquisto> listaAcquisti;
}
