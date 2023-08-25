package com.realm.myrealm.services;

import com.realm.myrealm.repositories.UtenteRepository;
import com.realm.myrealm.entities.Utente;
import com.realm.myrealm.support.exceptions.UtenteGiaEsistenteException;
import com.realm.myrealm.support.exceptions.UtenteNotFoundException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class AccountingService {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${auth-properties.clientid}")
    private String clientId;

    @Value("${auth-properties.clientsecret}")
    private String clientSecret;

    @Value("${auth-properties.usernameadmin}")
    private String usernameAdmin;

    @Value("${auth-properties.passwordadmin}")
    private String passwordAdmin;

    @Value("${auth-properties.role}")
    private String role;

    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void eliminaUtentePerId(int id) throws UtenteNotFoundException
    {
        if(!utenteRepository.existsById(id))
            throw new UtenteNotFoundException();

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(usernameAdmin)
                .password(passwordAdmin)
                .build();

        Utente u=utenteRepository.findUtentesById(id).get(0);
        utenteRepository.deleteById(id);
        List<UserRepresentation> userList = keycloak.realm(realm).users().search(u.getEmail());
        for (UserRepresentation userRepresentation : userList)
        {
            if (userRepresentation.getUsername().equals(u.getEmail()))
            {
                keycloak.realm(realm).users().delete(userRepresentation.getId());
            }
        }
    }

    @Transactional(readOnly=false,propagation = Propagation.REQUIRED)
    public void eliminaUtentePerMail(String email) throws UtenteNotFoundException
    {
        if(!utenteRepository.existsByEmail(email))
            throw new UtenteNotFoundException();
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(usernameAdmin)
                .password(passwordAdmin)
                .build();

        Utente u=utenteRepository.findUtentesByEmail(email).get(0);
        utenteRepository.deleteByEmail(email);
        List<UserRepresentation> userList = keycloak.realm(realm).users().search(u.getEmail());
        for (UserRepresentation userRepresentation : userList)
        {
            if (userRepresentation.getUsername().equals(u.getEmail()))
            {
                keycloak.realm(realm).users().delete(userRepresentation.getId());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Utente> mostraTuttiUtenti()
    {
        return utenteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraTuttiUtenti(int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable= PageRequest.of(numPagina,dimPagina, Sort.by(sortBy));
        Page<Utente> p=utenteRepository.findAll(pageable);
        if(p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
    public Utente aggiungiUtente(Utente u,String password) throws UtenteGiaEsistenteException
    {
        if(utenteRepository.existsByEmail(u.getEmail()))
            throw new UtenteGiaEsistenteException();
        //KEYCLOAK FA COSE AIUTO

        Keycloak k= KeycloakBuilder.builder().
                serverUrl(serverUrl).
                realm(realm).
                grantType(OAuth2Constants.PASSWORD).
                clientId(clientId).clientSecret(clientSecret).
                username(usernameAdmin).password(passwordAdmin).build();
        UserRepresentation user=new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(u.getEmail());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getNome());
        user.setLastName(u.getCognome());


        // Get realm
        RealmResource realmResource = k.realm(realm);
        UsersResource usersResource = realmResource.users();
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        List<CredentialRepresentation>l=new LinkedList<>();
        l.add(passwordCred);
        user.setCredentials(l);
        // Create user (requires manage-users role)

        Response response = usersResource.create(user);

        String userId = CreatedResponseUtil.getCreatedId(response);
        // Define password credential
        UserResource userResource = usersResource.get(userId);

        ClientRepresentation app1Client = realmResource.clients().findByClientId(clientId).get(0);

        // Get client level role (requires view-clients role)
        RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()).roles().get(role).toRepresentation();

        // Assign client level role to user
        userResource.roles().clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));

        return utenteRepository.save(u);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerNome(String nome)
    {
        return utenteRepository.findUtentesByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerNome(String nome,int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable=PageRequest.of(numPagina,dimPagina,Sort.by(sortBy));
        Page<Utente>p=utenteRepository.findUtentesByNome(nome,pageable);
        if(p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerCognome(String cognome)
    {
        return utenteRepository.findUtentesByCognome(cognome);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerCognome(String cognome,int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable=PageRequest.of(numPagina,dimPagina,Sort.by(sortBy));
        Page<Utente>p=utenteRepository.findUtentesByCognome(cognome,pageable);
        if(p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerNomeCompleto(String nome,String cognome)
    {
        return utenteRepository.findUtentesByNomeAndCognome(nome,cognome);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerNomeCompleto(String nome,String cognome,int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable=PageRequest.of(numPagina,dimPagina,Sort.by(sortBy));
        Page<Utente>p=utenteRepository.findUtentesByNomeAndCognome(nome,cognome,pageable);
        if(p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerEmail(String email)
    {
        return utenteRepository.findUtentesByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerEmail(String email,int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Utente> p = utenteRepository.findUtentesByEmail(email, pageable);
        if (p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerAnnoNascita(int annoNascita)
    {
        return utenteRepository.findUtentesByAnnoNascita(annoNascita);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerAnnoNascita(int annoNascita,int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Utente> p = utenteRepository.findUtentesByAnnoNascita(annoNascita, pageable);
        if (p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerRangeAnno(int anno1,int anno2)
    {
        return utenteRepository.findUtentesByAnnoNascitaBetween(anno1,anno2);
    }

    @Transactional(readOnly = true)
    public List<Utente>mostraUtentiPerRangeAnno(int anno1,int anno2,int numPagina,int dimPagina,String sortBy)
    {
        Pageable pageable = PageRequest.of(numPagina, dimPagina, Sort.by(sortBy));
        Page<Utente>p=utenteRepository.findUtentesByAnnoNascitaBetween(anno1,anno2,pageable);
        if(p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }
}