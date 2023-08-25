package com.realm.myrealm.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "acquisto", schema = "progetto")
public class Acquisto {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @ManyToOne
    @JoinColumn(name="id_utente")
    private Utente utente;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name="tempo_acquisto")
    private Date tempoAcquisto;


    @OneToMany(mappedBy="acquisto",cascade=CascadeType.MERGE)
    List<AutoAcquisto>AutoAcquistate;
}
