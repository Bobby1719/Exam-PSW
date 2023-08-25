package com.realm.myrealm.controllers_rest;

import com.realm.myrealm.entities.CasaProduttrice;
import com.realm.myrealm.entities.Auto;
import com.realm.myrealm.services.CasaProduttriceService;
import com.realm.myrealm.services.AutoService;
import com.realm.myrealm.support.exceptions.CasaProduttriceGiaEsistenteException;
import com.realm.myrealm.support.exceptions.AutoGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/catalogo")
public class ProductController {
    @Autowired
    CasaProduttriceService cp;
    @Autowired
    AutoService a;

    @PostMapping("/casaProduttrice/aggiungi")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity createCasaProduttrice(@RequestBody @Valid CasaProduttrice casaProduttrice) {
        try {
            cp.aggiungiCasaProduttrice(casaProduttrice);
        } catch (CasaProduttriceGiaEsistenteException e) {
            return new ResponseEntity("CasaProduttrice già esistente!!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("OK!", HttpStatus.OK);
    }

    @GetMapping("/casaProduttrice/paginati")
    public ResponseEntity getAllCasaProduttrice(@RequestParam(defaultValue = "0") int numPagina, @RequestParam(defaultValue = "10") int dimPagina, @RequestParam(value = "sortBy", defaultValue = "nome") String sortBy) {
        List<CasaProduttrice> risultato = cp.mostraListaCasaProduttrice(numPagina, dimPagina, sortBy);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti case produttrici nella lista", HttpStatus.OK);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }

    @PostMapping("/auto/aggiungi")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity createAuto(@RequestBody @Valid Auto auto) {
        try {
            System.out.println(auto);
            a.aggiungiAuto(auto);
        } catch (AutoGiaEsistenteException e) {
            return new ResponseEntity("Auto già esistente!!", HttpStatus.BAD_REQUEST);
        }
        ;
        return new ResponseEntity("OK!", HttpStatus.OK);
    }

    @GetMapping("/auto/paginati")
    public ResponseEntity getAllAuto(@RequestParam(defaultValue = "0") int numPagina, @RequestParam(defaultValue = "10") int dimPagina, @RequestParam(value = "sortBy", defaultValue = "modello") String sortBy) {
        List<Auto> risultato = a.mostraCatalogoAuto(numPagina, dimPagina, sortBy);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti auto in catalogo", HttpStatus.OK);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }

    @GetMapping("/auto/perModello")
    public ResponseEntity getAutoByModello(@RequestParam String titolo) {
        List<Auto> risultato = a.mostraAutoPerModello(titolo);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti auto del modello indicato", HttpStatus.OK);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/casaProduttrice/perNome")
    public ResponseEntity getCasaProduttricesByName(@RequestParam String nome) {
        List<CasaProduttrice> risultato = cp.mostraCasaProduttricePerNome(nome);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti case produttrici con il nome indicato", HttpStatus.OK);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/casaProduttrice/perSede/paginati")
    public ResponseEntity getCasaProduttriceBySede(@RequestParam String sede,@RequestParam(defaultValue = "0") int numPagina, @RequestParam(defaultValue = "10") int dimPagina, @RequestParam(value = "sortBy", defaultValue = "nome") String sortBy)
    {
        List<CasaProduttrice> risultato = cp.mostraCasaProduttricePerSede(sede,numPagina,dimPagina,sortBy);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti case produttrici con quella sede", HttpStatus.OK);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/auto/perAnnoUscita/paginati")
    public ResponseEntity getAutoByReleaseYear(@RequestParam(defaultValue ="2023")int annoUscita,@RequestParam(defaultValue = "0") int numPagina, @RequestParam(defaultValue = "10") int dimPagina, @RequestParam(value = "sortBy", defaultValue = "modello") String sortBy)
    {
        List<Auto> risultato = a.mostraAutoPerAnnoUscita(annoUscita,numPagina,dimPagina,sortBy);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti auto prodotte nell'anno indicato", HttpStatus.OK);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/auto/prezzoMin/paginati")
    public ResponseEntity getAutoByPriceLTE(@RequestParam(defaultValue = "5")int prezzo,@RequestParam(defaultValue = "0") int numPagina, @RequestParam(defaultValue = "10") int dimPagina, @RequestParam(value = "sortBy", defaultValue = "modello")String sortBy)
    {
        List<Auto> risultato = a.mostraAutoPerPrezzoMinoreUguale(prezzo,numPagina,dimPagina,sortBy);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti auto con prezzo minore del prezzo indicato", HttpStatus.OK);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/auto/prezzoMag/paginati")
    public ResponseEntity getAutoByPriceGTE(@RequestParam(defaultValue = "5")int prezzo,@RequestParam(defaultValue = "0") int numPagina, @RequestParam(defaultValue = "10") int dimPagina, @RequestParam(value = "sortBy", defaultValue = "modello")String sortBy)
    {
        List<Auto> risultato = a.mostraAutoPerPrezzoMaggioreUguale(prezzo,numPagina,dimPagina,sortBy);
        if (risultato.size() <= 0)
            return new ResponseEntity("Non sono presenti auto con prezzo maggiore del prezzo indicato", HttpStatus.OK);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/auto/ricercaAdv/paginati")
    public ResponseEntity getAutoByAdvancedSearch(@RequestParam(required=false) String modello, @RequestParam(required=false) Integer prezzo1,@RequestParam(required=false) Integer prezzo2,@RequestParam(defaultValue = "0",required=false) int numPagina,
                                                   @RequestParam(defaultValue = "10") int dimPagina,@RequestParam(defaultValue="titolo") String sortBy,@RequestParam(required = false) Integer annoUscita)
    {
        List<Auto>risultato=a.mostraAutoRicercaAvanzata(modello,annoUscita,prezzo1,prezzo2,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti auto che rispettano i criteri elencati",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    //TODO modificare per avere tutti i parametri in un solo oggetto.
    @GetMapping("/casaProduttrice/ricercaAdv/paginati")
    public ResponseEntity getCasaProduttricesByAdvancedSearch(@RequestParam(required = false) String nome,@RequestParam(required=false) String sede,@RequestParam(required=false) String contatto, @RequestParam(defaultValue="0") int numPagina,
                                                   @RequestParam(defaultValue = "10") int dimPagina,@RequestParam(defaultValue="nome") String sortBy)
    {
        List<CasaProduttrice>risultato=cp.mostraCasaProduttriceRicercaAvanzata(nome,sede,contatto,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti case produttrici che rispettano i criteri elencati",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }
}