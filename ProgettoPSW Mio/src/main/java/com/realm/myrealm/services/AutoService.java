package com.realm.myrealm.services;

import com.realm.myrealm.entities.CasaProduttrice;
import com.realm.myrealm.repositories.AutoRepository;
import com.realm.myrealm.entities.Auto;
import com.realm.myrealm.support.exceptions.AutoGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutoService {
    @Autowired
    AutoRepository autoRepository;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void aggiungiAuto(Auto a) throws AutoGiaEsistenteException {
        //System.out.println(a.getCasaProduttrice());
        if (autoRepository.existsAutoByModelloAndAnnoUscita( a.getModello(), a.getAnnoUscita()))
            throw new AutoGiaEsistenteException();
        autoRepository.save(a);
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraCatalogoAuto() {
        return autoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraCatalogoAuto(int numPagina, int dimPagina, String sortBy) {
        Pageable paging = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Auto> risultatoPaginato = autoRepository.findAll(paging);

        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerModello(String modello) {
        return autoRepository.findAutosByModello(modello);
    }

    public List<Auto> mostraAutoPerCasaProduttrice(CasaProduttrice casaProduttrice) {
        return autoRepository.findAutosByCasaProduttrice(casaProduttrice);
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerCasaProduttrice(CasaProduttrice casaProduttrice, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Auto> risultatoPaginato = autoRepository.findAutosByCasaProduttrice(casaProduttrice, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerAnnoUscita(int annoUscita) {
        return autoRepository.findAutosByAnnoUscita(annoUscita);
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerAnnoUscita(int annoUscita, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Auto> risultatoPaginato = autoRepository.findAutosByAnnoUscita(annoUscita, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerPrezzoMinoreUguale(int prezzo) {
        return autoRepository.findAutosByPrezzoLessThanEqual(prezzo);
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerPrezzoMinoreUguale(int prezzo, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Auto> risultatoPaginato = autoRepository.findAutosByPrezzoLessThanEqual(prezzo, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerPrezzoMaggioreUguale(int prezzo) {
        return autoRepository.findAutosByPrezzoGreaterThanEqual(prezzo);
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoPerPrezzoMaggioreUguale(int prezzo, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Auto> risultatoPaginato = autoRepository.findAutosByPrezzoGreaterThanEqual(prezzo, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoRicercaAvanzata(String modello, int annoUscita, int prezzo1, int prezzo2) {
        return autoRepository.ricercaAvanzata(modello, annoUscita, prezzo1, prezzo2);
    }

    @Transactional(readOnly = true)
    public List<Auto> mostraAutoRicercaAvanzata(String modello, int annoUscita, int prezzo1, int prezzo2, int numPagina, int dimPagina, String sortBy)
    {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Auto> risultatoPaginato = autoRepository.ricercaAvanzata(modello, annoUscita, prezzo1, prezzo2, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }
}