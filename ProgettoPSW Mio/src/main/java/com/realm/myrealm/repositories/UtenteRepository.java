package com.realm.myrealm.repositories;

import com.realm.myrealm.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente,Integer> {
    List<Utente>findUtentesByNome(String nome);
    Page<Utente>findUtentesByNome(String nome, Pageable p);
    List<Utente>findUtentesByCognome(String cognome);
    Page<Utente>findUtentesByCognome(String cognome,Pageable p);
    List<Utente>findUtentesByNomeAndCognome(String nome,String cognome);
    Page<Utente>findUtentesByNomeAndCognome(String nome,String cognome,Pageable p);
    List<Utente>findUtentesByEmail(String email);
    Page<Utente>findUtentesByEmail(String email,Pageable p);
    List<Utente>findUtentesByAnnoNascita(int annoNascita);
    Page<Utente>findUtentesByAnnoNascita(int annoNascita,Pageable p);
    List<Utente>findUtentesByAnnoNascitaBetween(int anno1,int anno2);
    Page<Utente>findUtentesByAnnoNascitaBetween(int anno1,int anno2,Pageable p);
    List<Utente>findUtentesById(int id);
    void deleteById(int id);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}