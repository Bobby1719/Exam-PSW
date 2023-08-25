package com.realm.myrealm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name="acquisto_auto",schema="progetto")
public class AutoAcquisto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name="id_acquisto")
    private Acquisto acquisto;

    @ManyToOne
    @JoinColumn(name="id_auto")
    private Auto auto;

    private int price;
}
