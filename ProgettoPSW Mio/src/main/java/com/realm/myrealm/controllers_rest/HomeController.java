package com.realm.myrealm.controllers_rest;

import com.realm.myrealm.entities.Acquisto;
import com.realm.myrealm.entities.Utente;
import com.realm.myrealm.services.AccountingService;
import com.realm.myrealm.services.AcquistoService;
import com.realm.myrealm.support.authentication.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    AccountingService accountingService;
    @Autowired
    AcquistoService acquistoService;
    @RequestMapping("/")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity home()
    {
        return new ResponseEntity("Ciao "+ Utils.getEmail()+" bentornato su Los Santos Custom Shop", HttpStatus.OK);
    }

    @RequestMapping("/getUser")
    @PreAuthorize("hasRole('user')")
    public Utente getUser()
    {
        String email=Utils.getEmail();
        return accountingService.mostraUtentiPerEmail(email).get(0);
    }

    @RequestMapping("/profilo")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity getProfile()
    {
        Utente u=getUser();
        return new ResponseEntity(u,HttpStatus.OK);
    }

    @RequestMapping("/acquisti/paginati")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity getPurchases(@RequestParam(defaultValue="0") int numPagina,@RequestParam(defaultValue="5") int dimPagina,@RequestParam(defaultValue="id") String sortBy)
    {
        String email=Utils.getEmail();
        List<Acquisto> risultato=acquistoService.mostraAcquistiPerUtente(email,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Attenzione, "+Utils.getEmail()+" non hai ancora effettuato acquisti sul nostro sito.",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }
    @RequestMapping("/acquisti/perPeriodo/paginati")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity getPurchasesPeriod(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date data1, @DateTimeFormat(pattern = "dd-MM-yyyy")@RequestParam Date data2 , @RequestParam(defaultValue="0") int numPagina, @RequestParam(defaultValue="5") int dimPagina, @RequestParam(defaultValue="id") String sortBy)
    {
        String email=Utils.getEmail();
        List<Acquisto>risultato=acquistoService.mostraAcquistiUtentePeriodo(email,data1,data2,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Attenzione "+Utils.getEmail()+" non hai effettuato acquisti sul nostro sito nel periodo indicato.",HttpStatus.OK);
        return new ResponseEntity<>(risultato,HttpStatus.OK);
    }
}