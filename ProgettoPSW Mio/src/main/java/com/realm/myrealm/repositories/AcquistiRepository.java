package com.realm.myrealm.repositories;

import com.realm.myrealm.entities.Acquisto;
import com.realm.myrealm.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AcquistiRepository extends JpaRepository<Acquisto,Integer> {

    List<Acquisto> findAcquistosByUtente(Utente u);
    Page<Acquisto> findAcquistosByUtente(Utente u, Pageable p);
    List<Acquisto> findAcquistosByTempoAcquisto(Date tempoAcquisto);
    Page<Acquisto> findAcquistosByTempoAcquisto(Date tempoAcquisto,Pageable p);

    @Query("SELECT a FROM Acquisto a WHERE a.tempoAcquisto>?1 AND a.tempoAcquisto<?2 AND a.utente=?3")
    Page<Acquisto>findAcquistosByPeriodoAndUtente(Date tempo1, Date tempo2, Utente u,Pageable p);

    @Query("SELECT a FROM Acquisto a WHERE a.tempoAcquisto>?1 AND a.tempoAcquisto<?2 AND a.utente=?3")
    List<Acquisto>findAcquistosByPeriodoAndUtente(Date tempo1, Date tempo2, Utente u);
}