package DAO;

import java.util.ArrayList;
import java.util.Date;

/**
 * The interface Azienda dao.
 */
public interface AziendaDAO {

    /**
     * Chiama la procedura che si occupa di aggiornare le categorie degli impiegati nel database.
     */
    public void updateCategorie();

    /**
     * Carica dal database i dati relativi a tutti gli impiegati junior non dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita e data
     *                        assunzione dell'impiegato
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
    public void readAllJuniorNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                          ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Carica dal database i dati relativi a tutti gli impiegati junior dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione e passaggio dirigente dell'impiegato
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
    public void readAllJuniorDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                       ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Carica dal database i dati relativi a tutti gli impiegati middle non dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione e passaggio middle dell'impiegato
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
    public void readAllMiddleNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                          ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Carica dal database i dati relativi a tutti gli impiegati middle dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione, passaggio middle dell'impiegato e passaggio dirigente
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
    public void readAllMiddleDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Carica dal database i dati relativi a tutti gli impiegati senior non dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione, passaggio middle dell'impiegato e passaggio senior
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
    public void readAllSeniorNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Carica dal database i dati relativi a tutti gli impiegati senior dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione, passaggio middle dell'impiegato, passaggio senior e passaggio dirigente
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
    public void readAllSeniorDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi);

    /**
     * Carica dal database i dati relativi ai laboratori.
     *
     * @param laboratori ciascun array di stringe contiene in ordine nome, topic e codice fiscale del responsabile
     *                   scientifico del laboratorio.
     */
    public void readAllLaboratori(ArrayList<String[]> laboratori);

    /**
     * Carica dal database i dati relativi ai progetti in corso.
     *
     * @param progettiString Ciascun array di stringhe contiene in ordine nome, cup, referente scientifico,
     *                       responsabile del progetto
     * @param progettiDate   la date di inizio dei progetti
     */
    public void readAllProgettiInCorso(ArrayList<String[]> progettiString, ArrayList<Date> progettiDate);

    /**
     * Carica dal database i dati relativi ai progetti conclusi.
     *
     * @param progettiString Ciascun array di stringhe contiene in ordine nome, cup, referente scientifico,
     *                       responsabile del progetto
     * @param progettiDate   la date di inizio e di fine dei progetti in questo ordine.
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

    /**
     * Inserisce un dirigente relativo all'impiegato passato come parametro.
     *
     * @param cf
     * @param dataPromo
     * @return
     */
    public boolean inserisciDirigente(String cf, Date dataPromo);
}
