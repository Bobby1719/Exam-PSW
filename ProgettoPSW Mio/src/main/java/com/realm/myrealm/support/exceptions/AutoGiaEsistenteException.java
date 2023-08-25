package com.realm.myrealm.support.exceptions;

public class AutoGiaEsistenteException extends Exception {
    public AutoGiaEsistenteException()
    {
        System.out.println("Auto già presente nel catalogo.");
    }
}
