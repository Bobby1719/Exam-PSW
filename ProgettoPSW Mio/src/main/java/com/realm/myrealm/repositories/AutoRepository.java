package com.realm.myrealm.repositories;

import com.realm.myrealm.entities.Auto;
import com.realm.myrealm.entities.CasaProduttrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepository extends JpaRepository<Auto,Integer> {

    List<Auto>findAutosByModello(String modello);
    Page<Auto>findAutosByModello(String modello, Pageable p);
    List<Auto>findAutosByCasaProduttrice(CasaProduttrice casaProduttrice);
    Page<Auto>findAutosByCasaProduttrice(CasaProduttrice casaProduttrice, Pageable p);
    List<Auto>findAutosByAnnoUscita(int annoUscita);
    Page<Auto>findAutosByAnnoUscita(int annoUscita, Pageable p);
    List<Auto>findAutosByCasaProduttriceAndAnnoUscitaBefore(CasaProduttrice casaProduttrice, int annoUscita);
    Page<Auto>findAutosByCasaProduttriceAndAnnoUscitaBefore(CasaProduttrice casaProduttrice, int annoUscita, Pageable p);
    List<Auto>findAutosByCasaProduttriceAndAnnoUscitaAfter(CasaProduttrice casaProduttrice, int annoUscita);
    Page<Auto>findAutosByCasaProduttriceAndAnnoUscitaAfter(CasaProduttrice casaProduttrice, int annoUscita, Pageable p);
    List<Auto>findAutosByPrezzoLessThanEqual(int prezzo);
    Page<Auto>findAutosByPrezzoLessThanEqual(int prezzo, Pageable p);
    List<Auto>findAutosByPrezzoGreaterThanEqual(int prezzo);
    Page<Auto>findAutosByPrezzoGreaterThanEqual(int prezzo, Pageable p);
    @Query( "SELECT a " +
            "FROM Auto a " +
            "WHERE(UPPER(a.modello)LIKE UPPER(CONCAT('%',:modello,'%'))OR :modello IS NULL) AND"+
            "     (a.annoUscita=:annoUscita OR :annoUscita IS NULL) AND "+
            "     (a.prezzo>=:prezzo1 OR :prezzo1 IS NULL) AND"+
            "     (a.prezzo<=:prezzo2 OR :prezzo2 IS NULL)")
    List<Auto>ricercaAvanzata(String modello, int annoUscita, int prezzo1, int prezzo2);
    @Query( "SELECT a " +
            "FROM Auto a " +
            "WHERE(UPPER(a.modello)LIKE UPPER(CONCAT('%',:modello,'%'))OR :modello IS NULL) AND"+
            "     (a.annoUscita=:annoUscita OR :annoUscita IS NULL) AND "+
            "     (a.prezzo>=:prezzo1 OR :prezzo1 IS NULL) AND"+
            "     (a.prezzo<=:prezzo2 OR :prezzo2 IS NULL)")
    Page<Auto>ricercaAvanzata(String modello, Integer annoUscita, Integer prezzo1, Integer prezzo2, Pageable p);
    boolean existsAutoByModelloAndAnnoUscita(String modello, int anno);
}
