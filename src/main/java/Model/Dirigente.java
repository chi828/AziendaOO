package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Dirigente.
 */
public class Dirigente {
    private Date passaggioDirigente;
    private Impiegato impiegato;

    /**
     * Instantiates a new Dirigente.
     *
     * @param passaggioDirigente the passaggio dirigente
     * @param impiegato          the impiegato
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
     * @return the date
     */
    public Date getPassaggioDirigente(){return passaggioDirigente;}
}
