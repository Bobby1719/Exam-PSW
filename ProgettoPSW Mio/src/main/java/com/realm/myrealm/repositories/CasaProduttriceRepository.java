package com.realm.myrealm.repositories;

import com.realm.myrealm.entities.CasaProduttrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasaProduttriceRepository extends JpaRepository<CasaProduttrice,Integer> {
    List<CasaProduttrice>findCasaProduttriceById(int id);                        // era findAlbumsByArtista
    Page<CasaProduttrice>findCasaProduttriceById(int id, Pageable pageable);     // stessa cosa ma con un pageable
    List<CasaProduttrice>findCasaProduttricesByNome(String nome);                 // era findAlbumsByNome
    Page<CasaProduttrice>findCasaProduttricesByNome(String nome, Pageable pageable);
    List<CasaProduttrice>findCasaProduttricesBySede(String sede);                    // era findAlbumsByAnnoUscita
    Page<CasaProduttrice>findCasaProduttricesBySede(String sede, Pageable pageable);
  /*  List<CasaProduttrice>findAlbumsByArtistaAndAnnoUscitaBefore(String artista, int annoUscita);
    Page<CasaProduttrice>findAlbumsByArtistaAndAnnoUscitaBefore(String artista, int annoUscita, Pageable pageable);
    List<CasaProduttrice>findAlbumsByArtistaAndAnnoUscitaAfter(String artista, int annoUscita);
    Page<CasaProduttrice>findAlbumsByArtistaAndAnnoUscitaAfter(String artista, int annoUscita, Pageable pageable);
    List<CasaProduttrice>findAlbumsByGenere(String genere);
    Page<CasaProduttrice>findAlbumsByGenere(String genere, Pageable pageable);
    List<CasaProduttrice>findAlbumsByPrezzoLessThanEqual(int prezzo);
    Page<CasaProduttrice>findAlbumsByPrezzoLessThanEqual(int prezzo, Pageable pageable);
    List<CasaProduttrice>findAlbumsByPrezzoGreaterThanEqual(int prezzo);
    Page<CasaProduttrice>findAlbumsByPrezzoGreaterThanEqual(int prezzo, Pageable pageable);
   */
    boolean existsCasaProduttriceByNomeAndSede(String nome,String sede);

    @Query( "SELECT c " +
            "FROM CasaProduttrice c " +
            "WHERE(UPPER(c.nome)LIKE UPPER(CONCAT('%',:nome,'%'))OR :nome IS NULL) AND"+
            "     (UPPER(c.sede)LIKE UPPER(CONCAT('%',:sede,'%'))OR :sede IS NULL) AND"+
            "     (UPPER(c.contatto)LIKE UPPER(CONCAT('%',:contatto,'%'))OR :contatto IS NULL) ")
    List<CasaProduttrice>ricercaAvanzata(String nome, String sede, String contatto);
    @Query( "SELECT c " +
            "FROM CasaProduttrice c " +
            "WHERE(UPPER(c.nome)LIKE UPPER(CONCAT('%',:nome,'%'))OR :nome IS NULL) AND"+
            "     (UPPER(c.sede)LIKE UPPER(CONCAT('%',:sede,'%'))OR :sede IS NULL) AND"+
            "     (UPPER(c.contatto)LIKE UPPER(CONCAT('%',:contatto,'%'))OR :contatto IS NULL) ")
    Page<CasaProduttrice>ricercaAvanzata(String nome, String sede, String contatto, Pageable p);

}
