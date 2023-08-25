package com.realm.myrealm.support.authentication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import java.security.Principal;

@UtilityClass
@Log4j2
public class Utils {


    public Principal getPrincipal() {
        return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @SuppressWarnings("unchecked")
    private AccessToken getToken()
    {
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal=(KeycloakPrincipal<KeycloakSecurityContext>) getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext().getToken();
    }

    public String getEmail(){
        return getToken().getEmail();
    }

}