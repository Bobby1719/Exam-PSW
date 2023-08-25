package com.realm.myrealm.services;
import com.realm.myrealm.entities.Auto;
import com.realm.myrealm.entities.Utente;
import com.realm.myrealm.support.exceptions.AcquistoAnonimoException;
import com.realm.myrealm.support.exceptions.AcquistoVuotoException;
import com.realm.myrealm.entities.Acquisto;
import com.realm.myrealm.entities.AutoAcquisto;
import com.realm.myrealm.repositories.AcquistiRepository;
import com.realm.myrealm.repositories.AutoAcquistoRepository;
import com.realm.myrealm.repositories.AutoRepository;
import com.realm.myrealm.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AcquistoService {
    @Autowired
    private AcquistiRepository acquistoRep;
    @Autowired
    private AutoAcquistoRepository autoAcquistoRep;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private UtenteRepository utenteRep;
    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public Acquisto aggiungiAcquisto(Acquisto a)throws AcquistoVuotoException,AcquistoAnonimoException {
        Acquisto result=null;
        if(a.getUtente()==null)
            throw new AcquistoAnonimoException();

        result = acquistoRep.save(a);//lavoro con il risultato della save perchè si
        if(a.getAutoAcquistate()!=null)
        {//ho acquistato delle Auto
            for (AutoAcquisto cia : a.getAutoAcquistate()) {
                Auto auto = autoRepository.findById(cia.getAuto().getId()).get();
                entityManager.lock(auto, LockModeType.WRITE);
                cia.setAcquisto(result);
                AutoAcquisto appenaAggiunta = autoAcquistoRep.save(cia);
                entityManager.refresh(appenaAggiunta);
            }
        }
        entityManager.refresh(result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraTuttiAcquisti() {
        return acquistoRep.findAll();
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraTuttiAcquisti(int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Acquisto> risultatoPaginato = acquistoRep.findAll(p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraAcquistiPerUtente(Utente u) {
        return acquistoRep.findAcquistosByUtente(u);
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraAcquistiPerUtente(String email, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Utente u=utenteRep.findUtentesByEmail(email).get(0);
        Page<Acquisto> risultatoPaginato = acquistoRep.findAcquistosByUtente(u, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraAcquistiPerTempoAcquisto(Date tempoAcquisto) {
        return acquistoRep.findAcquistosByTempoAcquisto(tempoAcquisto);
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraAcquistiPerTempoAcquisto(Date tempoAcquisto, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Acquisto> risultatoPaginato = acquistoRep.findAcquistosByTempoAcquisto(tempoAcquisto, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Acquisto> mostraAcquistiUtentePeriodo(Utente u, Date lim1, Date lim2) {
        return acquistoRep.findAcquistosByPeriodoAndUtente(lim1, lim2, u);
    }
    @Transactional(readOnly = true)
    public List<Acquisto> mostraAcquistiUtentePeriodo(String email, Date lim1, Date lim2, int numPagina, int dimPagina, String sortBy) {
        Pageable p = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Utente u=utenteRep.findUtentesByEmail(email).get(0);
        Page<Acquisto> risultatoPaginato = acquistoRep.findAcquistosByPeriodoAndUtente(lim1, lim2, u, p);
        if (risultatoPaginato.hasContent())
            return risultatoPaginato.getContent();
        return new ArrayList<>();
    }
}