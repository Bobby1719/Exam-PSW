package com.realm.myrealm.services;

import com.realm.myrealm.repositories.CasaProduttriceRepository;
import com.realm.myrealm.entities.CasaProduttrice;
import com.realm.myrealm.support.exceptions.CasaProduttriceGiaEsistenteException;
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
public class CasaProduttriceService {
    @Autowired
    private CasaProduttriceRepository casaProduttriceRepository;

    @Transactional(readOnly=false,propagation = Propagation.REQUIRED)
    public void aggiungiCasaProduttrice(CasaProduttrice a) throws CasaProduttriceGiaEsistenteException
    {
        if(casaProduttriceRepository.existsCasaProduttriceByNomeAndSede(a.getNome(),a.getSede()))
            throw new CasaProduttriceGiaEsistenteException();
        casaProduttriceRepository.save(a);
    }

    @Transactional(readOnly=true)
    public List<CasaProduttrice> mostraListaCasaProduttrice()
    {
        return casaProduttriceRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<CasaProduttrice>mostraListaCasaProduttrice(int numPagina, int dimPagina, String sortBy)
    {
        Pageable paging= PageRequest.of(numPagina,dimPagina, Sort.by(sortBy));
        Page<CasaProduttrice>risultatoPaginato= casaProduttriceRepository.findAll(paging);

        if(risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly=true)
    public List<CasaProduttrice>mostraCasaProduttricePerNome(String nome)
    {
        return casaProduttriceRepository.findCasaProduttricesByNome(nome);
    }

    @Transactional(readOnly=true)
    public List<CasaProduttrice>mostraCasaProduttricePerNome(String nome, int numPagina, int dimPagina, String sortBy)
    {
        Pageable paging= PageRequest.of(numPagina,dimPagina, Sort.by(sortBy));
        Page<CasaProduttrice>risultatoPaginato= casaProduttriceRepository.findAll(paging);

        if(risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<CasaProduttrice>mostraCasaProduttricePerSede(String sede)
    {
        return casaProduttriceRepository.findCasaProduttricesBySede(sede);
    }

    @Transactional(readOnly = true)
    public List<CasaProduttrice>mostraCasaProduttricePerSede(String sede, int numPagina, int dimPagina, String sortBy)
    {
        Pageable p= PageRequest.of(numPagina,dimPagina,Sort.by(sortBy));
        Page<CasaProduttrice>risultatoPaginato= casaProduttriceRepository.findCasaProduttricesBySede(sede,p);
        if(risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }


    @Transactional(readOnly = true)
    public List<CasaProduttrice>mostraCasaProduttriceRicercaAvanzata(String nome, String sede, String contatto)
    {
        return casaProduttriceRepository.ricercaAvanzata(nome,sede,contatto);
    }

    @Transactional(readOnly = true)
    public List<CasaProduttrice>mostraCasaProduttriceRicercaAvanzata(String nome, String sede, String contatto, int numPagina, int dimPagina, String sortBy)
    {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<CasaProduttrice> risultatoPaginato = casaProduttriceRepository.ricercaAvanzata(nome, sede,contatto, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }
}