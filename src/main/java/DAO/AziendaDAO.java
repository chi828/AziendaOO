package DAO;

import Model.Impiegato;
import Model.Laboratorio;
import Model.Progetto;
import Model.ProgettoInCorso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * The interface Azienda dao.
 */
public interface AziendaDAO {

    /**
     * Update categorie.
     */
    public void updateCategorie();

    /**
     * Read all junior non dirigenti.
     *
     * @param impiegatiString the impiegati string
     * @param impiegatiDate   the impiegati date
     * @param afferenze       the afferenze
     * @param stipendi        the stipendi
     */
    public void readAllJuniorNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                          ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Read all junior dirigenti.
     *
     * @param impiegatiString the impiegati string
     * @param impiegatiDate   the impiegati date
     * @param afferenze       the afferenze
     * @param stipendi        the stipendi
     */
    public void readAllJuniorDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                       ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Read all middle non dirigenti.
     *
     * @param impiegatiString the impiegati string
     * @param impiegatiDate   the impiegati date
     * @param afferenze       the afferenze
     * @param stipendi        the stipendi
     */
    public void readAllMiddleNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                          ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Read all middle dirigenti.
     *
     * @param impiegatiString the impiegati string
     * @param impiegatiDate   the impiegati date
     * @param afferenze       the afferenze
     * @param stipendi        the stipendi
     */
    public void readAllMiddleDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Read all senior non dirigenti.
     *
     * @param impiegatiString the impiegati string
     * @param impiegatiDate   the impiegati date
     * @param afferenze       the afferenze
     * @param stipendi        the stipendi
     */
    public void readAllSeniorNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Read all senior dirigenti.
     *
     * @param impiegatiString the impiegati string
     * @param impiegatiDate   the impiegati date
     * @param afferenze       the afferenze
     * @param stipendi        the stipendi
     */
    public void readAllSeniorDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Read all laboratori.
     *
     * @param laboratori the laboratori
     */
    public void readAllLaboratori(ArrayList<String[]> laboratori);

    /**
     * Read all progetti in corso.
     *
     * @param progettiString the progetti string
     * @param progettiDate   the progetti date
     */
    public void readAllProgettiInCorso(ArrayList<String[]> progettiString, ArrayList<Date> progettiDate);

    /**
     * Read all progetti conclusi.
     *
     * @param progettiString the progetti string
     * @param progettiDate   the progetti date
     */
    public void readAllProgettiConclusi(ArrayList<String[]> progettiString, ArrayList<Date[]> progettiDate);

    /**
     * Add progetto in corso.
     *
     * @param cup                  the cup
     * @param nome                 the nome
     * @param dataInizio           the data inizio
     * @param assegnazioni         the assegnazioni
     * @param referenteScientifico the referente scientifico
     * @param responsabile         the responsabile
     */
    public void addProgettoInCorso(String cup, String nome, String dataInizio, ArrayList<String[]> assegnazioni,
                                   String referenteScientifico, String responsabile);

    /**
     * Add laboratorio.
     *
     * @param nome                    the nome
     * @param topic                   the topic
     * @param responsabileScientifico the responsabile scientifico
     */
    public void addLaboratorio(String nome, String topic, String responsabileScientifico);

    /**
     * Delete laboratorio.
     *
     * @param topic the topic
     * @param nome  the nome
     */
    public void deleteLaboratorio(String topic, String nome);

    /**
     * Delete impiegato boolean.
     *
     * @param cf the cf
     * @return the boolean
     */
    public boolean deleteImpiegato(String cf);

    /**
     * Inserisci impiegato boolean.
     *
     * @param nome             the nome
     * @param cognome          the cognome
     * @param sesso            the sesso
     * @param dataDiNascita    the data di nascita
     * @param luogoDiNascita   the luogo di nascita
     * @param cf               the cf
     * @param dataDiAssunzione the data di assunzione
     * @param stipendio        the stipendio
     * @param dirigente        the dirigente
     * @return the boolean
     */
    public boolean inserisciImpiegato(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita,
                                      String cf, Date dataDiAssunzione, String stipendio, boolean dirigente);
}
