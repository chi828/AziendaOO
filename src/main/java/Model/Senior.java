package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Senior.
 */
public class Senior extends Impiegato{

    private Date passaggioMiddle;
    private Date passaggioSenior;

    /**
     * Instantiates a new Senior.
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
     * @param passaggioSenior  the passaggio senior
     */
    public Senior(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita,
                  String cf, Date dataDiAssunzione, float stipendio, Laboratorio laboratorio, Date passaggioMiddle, Date passaggioSenior){

        super(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf, dataDiAssunzione, stipendio, laboratorio);
        this.passaggioMiddle = passaggioMiddle;
        this.passaggioSenior = passaggioSenior;
    }

    /**
     * Instantiates a new Senior.
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
     * @param passaggioSenior    the passaggio senior
     */
    public Senior(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita,
                  String cf, Date dataDiAssunzione, float stipendio, Laboratorio laboratorio, Date passaggioDirigente, Date passaggioMiddle, Date passaggioSenior){

        super(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf, dataDiAssunzione, stipendio, laboratorio, passaggioDirigente);
        this.passaggioMiddle = passaggioMiddle;
        this.passaggioSenior = passaggioSenior;
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
     * Get passaggio senior date.
     *
     * @return the date
     */
    public Date getPassaggioSenior(){
        return passaggioSenior;
    }

    /**
     * Set passaggio middle.
     *
     * @param passaggioMiddle the passaggio middle
     */
    public void setPassaggioMiddle(Date passaggioMiddle){
        this.passaggioMiddle = passaggioMiddle;
    }

    /**
     * Set passaggio senior.
     *
     * @param passaggioSenior the passaggio senior
     */
    public void setPassaggioSenior(Date passaggioSenior){
        this.passaggioSenior = passaggioSenior;
    }

    public String getCategoria() {
        return "senior";
    }
}
