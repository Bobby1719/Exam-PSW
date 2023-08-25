package com.realm.myrealm.controllers_rest;

import com.realm.myrealm.entities.Acquisto;
import com.realm.myrealm.services.AccountingService;
import com.realm.myrealm.services.AcquistoService;
import com.realm.myrealm.support.exceptions.AcquistoAnonimoException;
import com.realm.myrealm.support.exceptions.AcquistoVuotoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/acquisti")
public class PurchasingController {


    @Autowired
    AcquistoService acquistoService;
    @Autowired
    AccountingService accountingService;
    @PostMapping("/aggiungi")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity addPurchase(@RequestBody @Valid Acquisto a)
    {

        try {
            acquistoService.aggiungiAcquisto(a);
        }
        catch(Exception e)
        {
            if(e instanceof AcquistoVuotoException)
                return new ResponseEntity("Acquisto vuoto!!!",HttpStatus.BAD_REQUEST);
            if(e instanceof AcquistoAnonimoException)
                return new ResponseEntity("Acquisto anonimo!!!",HttpStatus.BAD_REQUEST);
            e.printStackTrace();
        }
        return new ResponseEntity("Acquisto aggiunto correttamente", HttpStatus.OK);
    }

    @GetMapping("/paginati")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity showPurchases(@RequestParam(defaultValue="0") int numPagina,@RequestParam(defaultValue="5") int dimPagina,@RequestParam(defaultValue="id") String sortBy)
    {
        List<Acquisto> risultato=acquistoService.mostraTuttiAcquisti(numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti acquisti nel sistema",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perUtente/paginati")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity showPurchasesByUser(@RequestBody @Valid String email, @RequestParam(defaultValue="0") int numPagina, @RequestParam(defaultValue="5") int dimPagina, @RequestParam(defaultValue="id") String sortBy)
    {
        List<Acquisto> risultato=acquistoService.mostraAcquistiPerUtente(email,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti acquisti dell'utente specificato nel sistema",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perDataAcquisto/paginati")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity showPurchasesByDate(@RequestBody @Valid @DateTimeFormat(pattern = "dd-MM-yyyy")Date d, @RequestParam(defaultValue="0") int numPagina,@RequestParam(defaultValue="5") int dimPagina,@RequestParam(defaultValue="id") String sortBy)
    {
        List<Acquisto>risultato=acquistoService.mostraAcquistiPerTempoAcquisto(d,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti acquisti effettuati nella data indicata.",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perUtentePeriodo/paginati")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity showPurchasesByUserPeriod(@RequestParam String email, @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")Date data1, @DateTimeFormat(pattern = "dd-MM-yyyy")@RequestParam Date data2 , @RequestParam(defaultValue="0") int numPagina, @RequestParam(defaultValue="5") int dimPagina, @RequestParam(defaultValue="id") String sortBy)
    {
        List<Acquisto>risultato=acquistoService.mostraAcquistiUtentePeriodo(email,data1,data2,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti acquisti nel periodo indicato.",HttpStatus.OK);
        return new ResponseEntity<>(risultato,HttpStatus.OK);
    }
}