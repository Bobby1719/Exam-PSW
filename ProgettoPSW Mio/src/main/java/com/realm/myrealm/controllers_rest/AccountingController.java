package com.realm.myrealm.controllers_rest;

import com.realm.myrealm.entities.Utente;
import com.realm.myrealm.services.AccountingService;
import com.realm.myrealm.support.exceptions.UtenteGiaEsistenteException;
import com.realm.myrealm.support.exceptions.UtenteNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/utenti")
public class AccountingController {

    @Autowired
    private AccountingService accountingService;

    @PostMapping("/crea")
    public ResponseEntity create(@RequestBody @Valid ReqObj reqObj)
    {
        try
        {
            Utente added = accountingService.aggiungiUtente(reqObj.getU(),reqObj.getPassword());
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (UtenteGiaEsistenteException e)
        {
            return new ResponseEntity<>("Utente già esistente", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancella")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity delete(@RequestBody @Valid Utente u)
    {
        if(u.getEmail()!=null)
        {
            try
            {
                accountingService.eliminaUtentePerMail(u.getEmail());
            }
            catch(UtenteNotFoundException e) {return new ResponseEntity("Utente non esistente.",HttpStatus.BAD_REQUEST);}
        }
        else {
            return new ResponseEntity("Impossibile trovare un utente con la mail specificata ", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Utente eliminato correttamente.",HttpStatus.OK);
    }

    @GetMapping("/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getAll(@RequestParam int numPagina,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente>risultato=accountingService.mostraTuttiUtenti(numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non ci sono utenti registrati nel sistema",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perNome/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getUserByFirstName(@RequestParam String nome,@RequestParam int numPagina ,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente> risultato=accountingService.mostraUtentiPerNome(nome,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti utenti con il nome indicato",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perCognome/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getUserByLastName(@RequestParam String cognome,@RequestParam int numPagina ,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente> risultato=accountingService.mostraUtentiPerCognome(cognome,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti utenti con il cognome indicato",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perNomeCompleto/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getUserByName(@RequestParam String nome,@RequestParam String cognome,@RequestParam int numPagina ,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente> risultato=accountingService.mostraUtentiPerNomeCompleto(nome,cognome,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti utenti con il nome indicato",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perEmail/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getUserByMail(@RequestParam String email,@RequestParam String cognome,@RequestParam int numPagina ,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente> risultato=accountingService.mostraUtentiPerEmail(email,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti utenti con la mail indicata",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perAnno/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getUserByYear(@RequestParam int anno,@RequestParam String cognome,@RequestParam int numPagina ,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente> risultato=accountingService.mostraUtentiPerAnnoNascita(anno,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti utenti nati nell'anno indicato",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @GetMapping("/perRangeAnni/paginati")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity getUserByRangeYear(@RequestParam int anno1,@RequestParam int anno2,@RequestParam String cognome,@RequestParam int numPagina ,@RequestParam int dimPagina,@RequestParam String sortBy)
    {
        List<Utente> risultato=accountingService.mostraUtentiPerRangeAnno(anno1,anno2,numPagina,dimPagina,sortBy);
        if(risultato.size()<=0)
            return new ResponseEntity("Non sono presenti utenti nati nel range indicato",HttpStatus.OK);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }
}
@Data
class ReqObj
{
    private Utente u;
    private String password;
}