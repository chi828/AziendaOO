package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe che rappresente il ruolo di dirigente assegnato ad un impiegato.
 */
public class Dirigente {
    private Date passaggioDirigente;
    private Impiegato impiegato;

    /**
     * Instantiates a new Dirigente.
     *
     * @param passaggioDirigente La data in cui l'impiegato è stato promosso a dirigente.
     * @param impiegato          L'impiegato che ha acquisito il ruolo di dirigente.
     */
    public Dirigente(Date passaggioDirigente, Impiegato impiegato) {
        this.passaggioDirigente = passaggioDirigente;
        this.impiegato = impiegato;
    }

    /**
     * Get impiegato impiegato.
     *
     * @return the impiegato
     */
    public Impiegato getImpiegato(){return impiegato;}

    /**
     * Get passaggio dirigente date.
     *
     * @return Data in cui l'impiegato è stato promosso a dirigente.
     */
    public Date getPassaggioDirigente(){return passaggioDirigente;}
}
