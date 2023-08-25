DROP SCHEMA progetto;
CREATE SCHEMA progetto;
USE progetto;

CREATE TABLE utente(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50),
    cognome VARCHAR(50),
    anno_nascita INTEGER,
    email VARCHAR(90)
);

CREATE TABLE casaProduttrice(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    sede VARCHAR(100),
    contatto VARCHAR(100),
    url_stemma VARCHAR(100)
);

CREATE TABLE auto(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    modello VARCHAR(100),
    descrizione VARCHAR(8000),
    id_casaProd INTEGER,
    anno_uscita INTEGER,
    url_foto VARCHAR(100),
    prezzo INTEGER,
    FOREIGN KEY(id_casaProd) REFERENCES casaProduttrice (id)
);

CREATE TABLE acquisto(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    id_utente INTEGER,
    tempo_acquisto DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_utente) REFERENCES utente (id)
);

CREATE TABLE acquisto_auto(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    id_acquisto INTEGER,
    id_auto INTEGER,
    price INTEGER,
    FOREIGN KEY(id_acquisto)REFERENCES acquisto(id),
    FOREIGN KEY(id_auto)REFERENCES auto(id)
);

