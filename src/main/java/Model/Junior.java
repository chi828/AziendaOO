package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Junior.
 */
public class Junior extends Impiegato{


    /**
     * Instantiates a new Junior.
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
     */
    public Junior(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita, String cf,
                  Date dataDiAssunzione, float stipendio, Laboratorio laboratorio) {

        super(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf, dataDiAssunzione, stipendio, laboratorio);


    }

    /**
     * Instantiates a new Junior.
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
     */
    public Junior(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita, String cf,
                  Date dataDiAssunzione, float stipendio, Laboratorio laboratorio, Date passaggioDirigente) {

        super(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf, dataDiAssunzione, stipendio, laboratorio, passaggioDirigente);


    }

    @Override
    public String getCategoria() {
        return "junior";
    }
}
