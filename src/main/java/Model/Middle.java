package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Rappresenta gli impiegati appartenenti alla categoria middle.
 */
public class Middle extends Impiegato{

    private Date passaggioMiddle;

    /**
     * Costruttore per impiegato middle non dirigente.
     *
     * @param nome             the nome
     * @param cognome          the cognome
     * @param sesso            the sesso
     * @param dataDiNascita    the data di nascita
     * @param luogoDiNascita   the luogo di nascita
     * @param cf               the cf
     * @param dataDiAssunzione the data di assunzione
     * @param stipendio        the stipendio
     * @param laboratorio      the laboratorio
     * @param passaggioMiddle  the passaggio middle
     */
    public Middle(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita, String cf,
                  Date dataDiAssunzione, float stipendio, Laboratorio laboratorio, Date passaggioMiddle){

        super(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf, dataDiAssunzione, stipendio, laboratorio);

        this.passaggioMiddle = passaggioMiddle;
    }

    /**
     * Costruttore per impiegato middle non dirigente.
     *
     * @param nome               the nome
     * @param cognome            the cognome
     * @param sesso              the sesso
     * @param dataDiNascita      the data di nascita
     * @param luogoDiNascita     the luogo di nascita
     * @param cf                 the cf
     * @param dataDiAssunzione   the data di assunzione
     * @param stipendio          the stipendio
     * @param laboratorio        the laboratorio
     * @param passaggioDirigente the passaggio dirigente
     * @param passaggioMiddle    the passaggio middle
     */
    public Middle(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita, String cf,
                  Date dataDiAssunzione, float stipendio, Laboratorio laboratorio, Date passaggioDirigente, Date passaggioMiddle){

        super(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf, dataDiAssunzione, stipendio, laboratorio, passaggioDirigente);

        this.passaggioMiddle = passaggioMiddle;
    }

    /**
     * Get passaggio middle date.
     *
     * @return the date
     */
    public Date getPassaggioMiddle(){

        return passaggioMiddle;
    }

    /**
     * Set passaggio middle.
     *
     * @param passaggioMiddle the passaggio middle
     */
    public void setPassaggioMiddle(Date passaggioMiddle){

        this.passaggioMiddle = passaggioMiddle;
    }

    @Override
    public String getCategoria() {
        return "middle";
    }
}
