USE progetto;

DROP TABLE acquisto_auto;
DROP TABLE acquisto;
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
                                 FOREIGN KEY(id_acquisto)REFERENCES acquisto(id),
                                 FOREIGN KEY(id_auto)REFERENCES auto(id)
);

