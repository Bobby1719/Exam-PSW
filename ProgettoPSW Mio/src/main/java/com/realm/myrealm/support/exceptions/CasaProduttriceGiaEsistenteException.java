package com.realm.myrealm.support.exceptions;

public class CasaProduttriceGiaEsistenteException extends Exception{
    public CasaProduttriceGiaEsistenteException()
    {
        System.out.println("Casa produttrice già presente nella lista!!!");
    }
}
