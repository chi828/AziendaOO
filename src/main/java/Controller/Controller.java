package Controller;

import DAO.*;
import ImplementazioniPostgresDAO.*;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The type Controller.
 */
public class Controller {

    private Azienda azienda;

    /**
     * Istanzia il controller e chiama la funzione per caricare i dati dal database.
     */
    public Controller() {
        azienda = new Azienda();
        loadModel();
    }

    /**
     * Coordina il caricamento dei dati in memoria reperendoli dal database tramite il DAO e funzioni che lo
     * interrogano.
     */
    private void loadModel() {

        AziendaDAO aziendaDAO = new AziendaImplementazionePostgresDAO();
        aziendaDAO.updateCategorie();

        ArrayList<String[]> afferenze = new ArrayList<>();//nome lab, topic lab

        loadImpiegati(afferenze); //Carichiamo in memoria gli impiegati, per ora senza le associazioni
        // "contributo passato" e "lavorare", ma recuperiamo topic e nome dei laboratori di afferenza nell'ArrayList afferenze

        loadLaboratori(); //Carichiamo in memoria i laboratori

        //Ora che abbiamo i laboratori, assegnamo a ciascun impiegato il proprio laboratorio di afferenza
        int size = azienda.getImpiegati().size();

        for(int i=0; i < size; i++) {
            Impiegato imp = azienda.getImpiegati().get(i);
            imp.setAfferenza(findLaboratorio(afferenze.get(i)[0], afferenze.get(i)[1]));
        }

        loadProgetti(); //Carichiamo in memoria i progetti

        loadContributiPassatiELavorare(); //Carichiamo in memoria i contributi passati di ciascun impiegato
    }

    /**
     * Carica tutti gli impiegati dal database e li inserisce nella ArrayList impiegati di
     * {@link Azienda}.
     * Siccome al momento della chiamata di questa funzione non ci si aspetta di avere i laboratori
     * già presenti in {@link Azienda}, le afferenze sono momentaneamente rappresentate a parte
     * nel parametro afferenze. Similmente, non ci si aspetta di avere i progetti già pronti
     * in memoria, dunque anche gli attributi contributiPassati e lavoriProgettiAssegnati di
     * {@link Impiegato} sono vuoti al termine dell'esecuzione di questo metodo.
     * @param afferenze array list di array di stringhe. Alla fine dell'esecuzione del metodo ciascun
     *                  array contiene, in posizione 0, il nome del laboratorio, in posizione 1 il topic.
     *                  L'array in posizione i rappresenta il laboratorio d'afferenza dell'i-esimo impiegato
     *                  in azienda.
     */
    private void loadImpiegati(ArrayList<String[]> afferenze) {

        //INSERIMENTO JUNIOR NON DIRIGENTI
        ArrayList<String[]> juniorNonDirigentiString = new ArrayList<>(); //nome, conome, sesso, luogo di nascita, CF
        ArrayList<Date[]> juniorNonDirigentiDate = new ArrayList<>();//data di nascita, data assunzione
        ArrayList<Float> juniorNDStipendi = new ArrayList<>();

        AziendaImplementazionePostgresDAO aziendaDB = new AziendaImplementazionePostgresDAO();

        aziendaDB.readAllJuniorNonDirigenti(juniorNonDirigentiString, juniorNonDirigentiDate, afferenze, juniorNDStipendi);

        String[] impiegatoS;
        Date[] impiegatoD;
        for (int i=0; i<juniorNonDirigentiString.size(); i++) {
            impiegatoD = juniorNonDirigentiDate.get(i);
            impiegatoS = juniorNonDirigentiString.get(i);

            azienda.addImpiegato(new Junior(impiegatoS[0], impiegatoS[1], impiegatoS[2].charAt(0),
                    impiegatoD[0], impiegatoS[3], impiegatoS[4], impiegatoD[1], juniorNDStipendi.get(i), null));
        }


        //INSERIMENTO JUNIOR DIRIGENTI
        ArrayList<String[]> juniorDirigentiString = new ArrayList<>(); //nome, conome, sesso, luogo di nascita, CF
        ArrayList<Date[]> juniorDirigentiDate = new ArrayList<>();//data di nascita, data assunzione, passaggio dirigente
        ArrayList<Float> juniorDStipendi = new ArrayList<>();

        aziendaDB = new AziendaImplementazionePostgresDAO();

        aziendaDB.readAllJuniorDirigenti(juniorDirigentiString, juniorDirigentiDate, afferenze, juniorDStipendi);

        for (int i=0; i < juniorDirigentiString.size(); i++) {
            impiegatoD = juniorDirigentiDate.get(i);
            impiegatoS = juniorDirigentiString.get(i);

            azienda.addImpiegato(new Junior(impiegatoS[0], impiegatoS[1], impiegatoS[2].charAt(0),
                    impiegatoD[0], impiegatoS[3], impiegatoS[4], impiegatoD[1], juniorDStipendi.get(i), null, impiegatoD[2]));
        }


        //INSERIMENTO MIDDLE NON DIRIGENTI
        ArrayList<String[]> middleNonDirigentiString = new ArrayList<>(); //nome, conome, sesso, luogo di nascita, CF
        ArrayList<Date[]> middleNonDirigentiDate = new ArrayList<>();//data di nascita, data assunzione, passaggio middle
        ArrayList<Float> middleNDStipendi = new ArrayList<>();

        aziendaDB = new AziendaImplementazionePostgresDAO();

        aziendaDB.readAllMiddleNonDirigenti(middleNonDirigentiString, middleNonDirigentiDate, afferenze, middleNDStipendi);

        for (int i=0; i < middleNonDirigentiString.size(); i++) {
            impiegatoD = middleNonDirigentiDate.get(i);
            impiegatoS = middleNonDirigentiString.get(i);

            azienda.addImpiegato(new Middle(impiegatoS[0], impiegatoS[1], impiegatoS[2].charAt(0),
                    impiegatoD[0], impiegatoS[3], impiegatoS[4], impiegatoD[1], middleNDStipendi.get(i), null, impiegatoD[2]));
        }


        //INSERIMENTO MIDDLE DIRIGENTI
        ArrayList<String[]> middleDirigentiString = new ArrayList<>(); //nome, conome, sesso, luogo di nascita, CF
        ArrayList<Date[]> middleDirigentiDate = new ArrayList<>();//data di nascita, data assunzione, passaggio middle, passaggio dirigente
        ArrayList<Float> middleDStipendi = new ArrayList<>();

        aziendaDB = new AziendaImplementazionePostgresDAO();

        aziendaDB.readAllMiddleDirigenti(middleDirigentiString, middleDirigentiDate, afferenze, middleDStipendi);

        for (int i=0; i < middleDirigentiString.size(); i++) {
            impiegatoD = middleDirigentiDate.get(i);
            impiegatoS = middleDirigentiString.get(i);

            azienda.addImpiegato(new Middle(impiegatoS[0], impiegatoS[1], impiegatoS[2].charAt(0),
                    impiegatoD[0], impiegatoS[3], impiegatoS[4], impiegatoD[1], middleDStipendi.get(i), null, impiegatoD[3], impiegatoD[2]));
        }


        //INSERIMENTO SENIOR NON DIRIGENTI
        ArrayList<String[]> seniorNonDirigentiString = new ArrayList<>(); //nome, conome, sesso, luogo di nascita, CF
        ArrayList<Date[]> seniorNonDirigentiDate = new ArrayList<>();//data di nascita, data assunzione, passaggio middle, passaggio senior
        ArrayList<Float> seniorNDStipendi = new ArrayList<>();

        aziendaDB = new AziendaImplementazionePostgresDAO();

        aziendaDB.readAllSeniorNonDirigenti(seniorNonDirigentiString, seniorNonDirigentiDate, afferenze, seniorNDStipendi);

        for (int i=0; i < seniorNonDirigentiString.size(); i++) {
            impiegatoD = seniorNonDirigentiDate.get(i);
            impiegatoS = seniorNonDirigentiString.get(i);

            azienda.addImpiegato(new Senior(impiegatoS[0], impiegatoS[1], impiegatoS[2].charAt(0),
                    impiegatoD[0], impiegatoS[3], impiegatoS[4], impiegatoD[1], seniorNDStipendi.get(i), null, impiegatoD[2], impiegatoD[3]));
        }


        //INSERIMENTO SENIOR DIRIGENTI
        ArrayList<String[]> seniorDirigentiString = new ArrayList<>(); //nome, conome, sesso, luogo di nascita, CF
        ArrayList<Date[]> seniorDirigentiDate = new ArrayList<>();//data di nascita, data assunzione, passaggio middle, passaggio senior, passaggio dirigente
        ArrayList<Float> seniorDStipendi = new ArrayList<>();

        aziendaDB = new AziendaImplementazionePostgresDAO();

        aziendaDB.readAllSeniorDirigenti(seniorDirigentiString, seniorDirigentiDate, afferenze, seniorDStipendi);

        for (int i=0; i < seniorDirigentiString.size(); i++) {
            impiegatoD = seniorDirigentiDate.get(i);
            impiegatoS = seniorDirigentiString.get(i);

            azienda.addImpiegato(new Senior(impiegatoS[0], impiegatoS[1], impiegatoS[2].charAt(0),
                    impiegatoD[0], impiegatoS[3], impiegatoS[4], impiegatoD[1], seniorDStipendi.get(i), null,impiegatoD[4], impiegatoD[2], impiegatoD[3]));
        }
    }

    /**
     * Carica tutti i laboratori dal database e li inserisce nella ArrayList laboratori di azienda.
     * La chiamata di questo metodo và effettuata a seguito della chiamata di loadImpiegati(...).
     * Alla fine dell'esecuzione del metodo, tutti i laboratori saranno presenti in memoria,
     * ciascuno con tutti gli attributi correttamente compilati.
     */
    private void loadLaboratori() {
        AziendaImplementazionePostgresDAO aziendaDB = new AziendaImplementazionePostgresDAO();

        ArrayList<String[]> laboratori = new ArrayList<>(); //nome, topic, responsabile scientifico
        aziendaDB.readAllLaboratori(laboratori);

        for (String[] laboratorio: laboratori) {

            Impiegato responsabileScientifico = findImpiegato(laboratorio[2]);
            azienda.addLaboratorio(new Laboratorio(laboratorio[0], laboratorio[1],(Senior) responsabileScientifico));
        }
    }

    /**
     * Carica tutti i progetti dal database e li inserisce nell'ArrayList progetti (attributo
     * di azienda). La chiamata di questo metodo và effettuata a seguito delle chiamate dei metodi
     * loadImpiegati(...) e loadLaboratori. Alla fine dell'esecuzione di questo metodo, tutti i
     * progetti, conclusi ed in corso, saranno presenti in memoria, ciascuno con tutti gli
     * attributi correttamente assegnati.
     */
    private void loadProgetti() {
        AziendaImplementazionePostgresDAO aziendaDB = new AziendaImplementazionePostgresDAO();

        //________________________________CARICHIAMO I PROGETTI IN CORSO___________________________________________
        ArrayList<String[]> progettiInCorsoS = new ArrayList<>();//nome, cup, referente scientifico, responsabile
        ArrayList<Date> progettiInCorsoD = new ArrayList<>();//data inizio
        aziendaDB.readAllProgettiInCorso(progettiInCorsoS, progettiInCorsoD);


        //Creiamo un istanza di progetto in corso in memoria per ciascn progetto
        for (int i = 0; i < progettiInCorsoS.size(); i++) {

            Impiegato responsabile;
            //Cerchiamo il responsabile del progetto conoscendone il cf
            responsabile = findImpiegato(progettiInCorsoS.get(i)[3]);

            Impiegato referenteScientifico;
            //cerchiamo il referente scientifico del progetto conoscendone il cf
            referenteScientifico = findImpiegato(progettiInCorsoS.get(i)[2]);

            //Recuperiamo i laboratori assegnati al progetto dal database
            ArrayList<String[]> assegnazioni = new ArrayList<>();

            ProgettoDAO progettoDB = new ProgettoImplementazionePostgresDAO();
            progettoDB.readAssegnazioni(progettiInCorsoS.get(i)[1],assegnazioni);

            ArrayList<Laboratorio> assegnazioniProgetto = new ArrayList<>();

            //Associamo ai dati recuperati dal database, le istanze di laboratorio rappresentate in memoria
            for (String[] assegnazione: assegnazioni) {
                assegnazioniProgetto.add(findLaboratorio(assegnazione[0], assegnazione[1]));
            }

            azienda.addProgetti(new ProgettoInCorso(progettiInCorsoS.get(i)[0], progettiInCorsoS.get(i)[1], progettiInCorsoD.get(i), assegnazioniProgetto,(Senior) referenteScientifico, responsabile.getDirigente()));
        }

        //________________________________CARICHIAMO I PROGETTI CONCLUSI___________________________________________
        aziendaDB = new AziendaImplementazionePostgresDAO();

        ArrayList<String[]> progettiConclusiS = new ArrayList<>();//nome, cup, referente scientifico, responsabile
        ArrayList<Date[]> progettiConclusiD = new ArrayList<>();//data inizio
        aziendaDB.readAllProgettiConclusi(progettiConclusiS, progettiConclusiD);

        //Creiamo un istanza di progetto concluso in memoria per ciascn progetto
        for (int i = 0; i < progettiConclusiS.size(); i++) {

            Impiegato responsabile;
            //Cerchiamo il responsabile del progetto conoscendone il cf
            responsabile = findImpiegato(progettiConclusiS.get(i)[3]);

            Impiegato referenteScientifico;
            //cerchiamo il referente scientifico del progetto conoscendone il cf
            referenteScientifico = findImpiegato(progettiConclusiS.get(i)[2]);

            //Recuperiamo i laboratori assegnati al progetto dal database
            ArrayList<String[]> assegnazioni = new ArrayList<>();

            ProgettoDAO progettoDB = new ProgettoImplementazionePostgresDAO();
            progettoDB.readAssegnazioni(progettiConclusiS.get(i)[1],assegnazioni);

            ArrayList<Laboratorio> assegnazioniProgetto = new ArrayList<>();

            //Associamo ai dati recuperati dal database, le istanze di laboratorio rappresentate in memoria
            for (String[] assegnazione: assegnazioni) {
                assegnazioniProgetto.add(findLaboratorio(assegnazione[0], assegnazione[1]));
            }

            azienda.addProgetti(new ProgettoConcluso(progettiConclusiS.get(i)[0], progettiConclusiS.get(i)[1], progettiConclusiD.get(i)[0], assegnazioniProgetto,(Senior) referenteScientifico, responsabile.getDirigente(), progettiConclusiD.get(i)[1]));
        }
    }

    /**
     * Metodo che completa il caricamento dei dati degli impiegati. Và chiamato a seguito di
     * loadImpieati(...) e loadProgetti(). Recupera dal database i lavori (rappresentati
     * nel model dalla classe {@link Lavorare}) ed i contributi passati e li inserisce
     * negli attributi lavoriProgettiAssegnati e contributiPassati di ciascun oggetto
     * {@link Impiegato}.
     */
    private void loadContributiPassatiELavorare() {
        for (Impiegato impiegato: azienda.getImpiegati()
             ) {
            ImpiegatoDAO impiegatoDB = new ImpiegatoImplementazionePostgresDAO();

            //Reperiamo i contrbuti passati dal database
            ArrayList<String> contributiPassati = new ArrayList<>();
            impiegatoDB.readContributiPassati(impiegato.getCf(), contributiPassati);

            //salviamo i contributi passati nel modello
            for (String contributoPassato: contributiPassati
                 ) {

                impiegato.addContributoPassato(findProgetto(contributoPassato));
            }

            //Reperiamo i lavori dal database
            ArrayList<String> lavori = new ArrayList<>();
            ArrayList<Integer> ore = new ArrayList<>();
            impiegatoDB = new ImpiegatoImplementazionePostgresDAO();
            impiegatoDB.readLavorare(impiegato.getCf(), ore, lavori);

            //Creiamo istanze di lavorare da salvare nel modello
            for(int i = 0; i< lavori.size(); i++) {
                Lavorare lavorare = new Lavorare(impiegato,(ProgettoInCorso) findProgetto(lavori.get(i)), ore.get(i));
                impiegato.addLavoro(lavorare);
            }
        }
    }



    //_______________________________________________________________________________________________

    /**
     * Trova nell'ArrayList impiegati nell'attributo azienda {@link Azienda} di Controller, l'impiegato con codice
     * fiscele, rappresentato come Stringa nell'attributo cf, uguale a quello passato come parametro.
     * @param cf    Codice fiscale dell'impiegato da trovare
     * @return      Restituisce l'ogetto della classe Impiegato che ha per attributo cf la stringa data in parametro.
     */
    private Impiegato findImpiegato(String cf) {
        Impiegato result = null;
        for (Impiegato impiegato : azienda.getImpiegati()
        ) {
            if (impiegato.getCf().equalsIgnoreCase(cf)) {
                 result = impiegato;
                break;
            }
        }
        return result;
    }

    /**
     * Trova nell'ArrayList laboratori nell'attributo azienda {@link Azienda} di Controller, il laboratorio che ha
     * sia nome uguale al valore della stringa nel paramentro nome, che topic uguale al valore della stringa nel
     * parametro topic. Se non è presente un laboratorio con tali nome e topic, restituisce null.
     * @param nome  nome del laboratorio da cercare.
     * @param topic topic del laboratorio da cercare.
     * @return      Restituisce l'ogetto della classe Laboratorio che ha per attributo nome la stringa passata come
     * primo parametro e per topic la stringa passata come secondo parametro. Se tale laboratorio non esiste nel sistema,
     * restituisce null
     */
    private Laboratorio findLaboratorio(String nome, String topic) {
        Laboratorio result = null;
        if (nome != null) {
            for (Laboratorio laboratorio : azienda.getLaboratori()) {
                if (laboratorio.getNome().equals(nome) && laboratorio.getTopic().equals(topic)) {
                    result = laboratorio;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Trova nell'ArrayList progetti, contenuto nell'attributo azienda di tipo {@link Azienda}, il progetto
     * che il valore dell'attributo cup uguale alla stringa passata per parametro. Restituisce, se esiste tale
     * progetto.
     * @param cup     Cup del progetto da cercare in azienda.
     * @return      Oggetto progetto con cup di valore uguale al cup parametro.
     */
    private Progetto findProgetto(String cup) {
        Progetto result = null;
        for (Progetto progetto : azienda.getProgetti()
        ) {
            if (progetto.getCup().equals(cup)) {
                result = progetto;
                break;
            }
        }
        return result;
    }

    /**
     * Impiegati afferenti array list. Trova tutti gli impiegati che afferiscono al laboratorio passato come
     * parametro. Se il laboratorio non ha impiegati che vi afferiscono, restituisce un array list vuoto.
     *
     * @param laboratorio Laboratorio di cui cerchiamo gli impiegati afferenti.
     * @return Lista di impiegati afferenti al laboratorio passato in parametro.
     */
    private ArrayList<Impiegato> impiegatiAfferenti(Laboratorio laboratorio) {
        ArrayList<Impiegato> result = new ArrayList<>();

        for (Impiegato impiegato : getImpiegati()
             ) {
            if(impiegato.getAfferenza()  == laboratorio) {
                result.add(impiegato);
            }
        }
        return result;
    }

    /***
     * Recupera una array list di impiegati da azienda.
     * @return array list contenenti tutti gli impiegati in azienda.
     */
    private ArrayList<Impiegato> getImpiegati() {return azienda.getImpiegati();}

    /***
     * Recupera una array list di laboratori da azienda.
     * @return array list contenente tutti i laboratori in azienda.
     */
    private ArrayList<Laboratorio> getLaboratori(){return azienda.getLaboratori();}

    /***
     * Recupera una array list di laboratori da azienda.
     * @return array list contenente tutti i progetti in azienda.
     */
    private ArrayList<Progetto> getProgetti(){return azienda.getProgetti();}

    //___________________________________________OPERAZIONI SUI DATI__________________________________________

    //Letture

    /**
     * Metodo per passare i dati dei laboratori alle gui in forma di array list di stringhe. Popola gli array list
     * passati per parametro con tali dati.
     *
     * @param nomiLaboratori          array list che al termine dell'esecuzione del metodo conterrà i nomi dei
     *                               laboratori
     * @param topicLaboratori         array list che al termine dell'esecuzione del metodo conterrà i topic dei
     *                               laboratori
     * @param responsabiliScientifici array list che al termine dell'esecuzione del metodo conterrà i responsabili
     *                                scientifici dei laboratori
     */
    public void getLaboratori(ArrayList<String> nomiLaboratori, ArrayList<String> topicLaboratori,
                              ArrayList<String> responsabiliScientifici) {

        for (Laboratorio laboratorio: azienda.getLaboratori()
        ) {
            nomiLaboratori.add(laboratorio.getNome());
            topicLaboratori.add(laboratorio.getTopic());
            Senior responsabileScientifico = laboratorio.getResponsabileScientifico();
            responsabiliScientifici.add(responsabileScientifico.getNome() + " " +
                    responsabileScientifico.getCognome() + " " +
                    responsabileScientifico.getCf());
        }
    }

    /**
     * Metodo per passare alle gui i dati dei laboratori assegnati ad un progetto in corso sotto forma di stringhe.
     * Popola gli array list passati per secondo, terzo e quarto parametro con tali dati.
     *
     * @param indiceProgetto          Indice del progetto in corso di cui ci interessano i laboratori assegnati
     * @param nomiLaboratori          array list che al termine dell'esecuzione del metodo conterrà i nomi dei
     *                               laboratori assegnati al progetto
     * @param topicLaboratori         array list che al termine dell'esecuzione del metodo conterrà i topic dei
     *                               laboratori assegnati al progetto
     * @param responsabiliScientifici array list che al termine dell'esecuzione del metodo conterrà i responsabili
     *                                scientifici dei laboratori assegnati al progetto
     */
    public void getLaboratoriAssegnati(int indiceProgetto, ArrayList<String> nomiLaboratori, ArrayList<String> topicLaboratori,
                                       ArrayList<String> responsabiliScientifici) {

        for (Laboratorio laboratorio: azienda.getProgettiInCorso().get(indiceProgetto).getLaboratoriAssegnati()
        ) {
            nomiLaboratori.add(laboratorio.getNome());
            topicLaboratori.add(laboratorio.getTopic());
            Senior responsabileScientifico = laboratorio.getResponsabileScientifico();
            responsabiliScientifici.add(responsabileScientifico.getNome() + " " +
                    responsabileScientifico.getCognome() + " " +
                    responsabileScientifico.getCf());
        }
    }

    /**
     * Metodo per passare alle gui i dati dei laboratori assegnati ad un progetto concluso sotto forma di stringhe.
     * Popola gli array list passati per secondo, terzo e quarto parametro con tali dati.
     *
     * @param indiceProgetto          Indice del progetto concluso di cui ci interessano i laboratori assegnati
     * @param nomiLaboratori          array list che al termine dell'esecuzione del metodo conterrà i nomi dei
     *                                laboratori assegnati al progetto
     * @param topicLaboratori         array list che al termine dell'esecuzione del metodo conterrà i topic dei
     *                               laboratori assegnati al progetto
     * @param responsabiliScientifici array list che al termine dell'esecuzione del metodo conterrà i responsabili
     *                               scientifici dei laboratori assegnati al progetto
     */
    public void getLaboratoriAssegnatiConclusi(int indiceProgetto, ArrayList<String> nomiLaboratori,
                                               ArrayList<String> topicLaboratori, ArrayList<String> responsabiliScientifici) {

        for (Laboratorio laboratorio: azienda.getProgettiConclusi().get(indiceProgetto).getLaboratoriAssegnati()
        ) {
            nomiLaboratori.add(laboratorio.getNome());
            topicLaboratori.add(laboratorio.getTopic());
            Senior responsabileScientifico = laboratorio.getResponsabileScientifico();
            responsabiliScientifici.add(responsabileScientifico.getNome() + " " +
                    responsabileScientifico.getCognome() + " " +
                    responsabileScientifico.getCf());
        }
    }

    /**
     * Gets progetti in corso. Metodo per fornire alla gui i dati dei progetti in corso sotto forma di array list
     * si stringhe e date. Popola gli array list passati per parametro con tali dati.
     *
     * @param nomiProgetti array list che al termine dell'esecuzione del metodo conterrà i nomi dei progetti
     * @param cupProgetti  array list che al termine dell'esecuzione del metodo conterrà i cup dei progetti
     * @param dateInizio   array list che al termine dell'esecuzione del metodo conterrà le date di inizio
     * @param responsabili array list che al termine dell'esecuzione del metodo conterrà i responsabili
     * @param referenti    array list che al termine dell'esecuzione del metodo conterrà i referenti
     */
    public void getProgettiInCorso(ArrayList<String> nomiProgetti, ArrayList<String> cupProgetti,
                                        ArrayList<Date> dateInizio, ArrayList<String> responsabili, ArrayList<String> referenti) {
        for (int i = 0; i < azienda.getProgetti().size(); i++) {
            Progetto progetto = azienda.getProgetti().get(i);
            if(!progetto.isConcluso()){
                nomiProgetti.add(progetto.getNome());
                cupProgetti.add(progetto.getCup());
                dateInizio.add(progetto.getDataInizio());
                Impiegato responsabile = progetto.getResponsabile().getImpiegato();
                responsabili.add(responsabile.getNome() + " " + responsabile.getCognome());
                referenti.add(progetto.getReferenteScientifico().getNome() + " " +
                        progetto.getReferenteScientifico().getCognome());
            }
        }

    }

    /**
     * Gets progetti conclusi. Metodo per fornire alla gui i dati dei progetti conclusi sotto forma di array list
     * * si stringhe e date. Popola gli array list passati per parametro con tali dati.
     *
     * @param nomiProgetti array list che al termine dell'esecuzione del metodo conterrà i nomi dei progetti
     * @param cupProgetti  array list che al termine dell'esecuzione del metodo conterrà i cup dei progetti
     * @param dateInizio   array list che al termine dell'esecuzione del metodo conterrà le date di inizio
     * @param dateFine     array list che al termine dell'esecuzione del metodo conterrà le date di fine
     * @param responsabili array list che al termine dell'esecuzione del metodo conterrà i responsabili
     * @param referenti    array list che al termine dell'esecuzione del metodo conterrà i referenti
     */
    public void getProgettiConclusi(ArrayList<String> nomiProgetti, ArrayList<String> cupProgetti,
                                   ArrayList<Date> dateInizio, ArrayList<Date> dateFine,
                                    ArrayList<String> responsabili, ArrayList<String> referenti) {
        for (int i = 0; i < azienda.getProgetti().size(); i++) {
            Progetto progetto = azienda.getProgetti().get(i);
            if(progetto.isConcluso()){
                nomiProgetti.add(progetto.getNome());
                cupProgetti.add(progetto.getCup());
                dateInizio.add(progetto.getDataInizio());
                dateFine.add(((ProgettoConcluso)progetto).getDataFine());
                Impiegato responsabile = progetto.getResponsabile().getImpiegato();
                responsabili.add(responsabile.getNome() + " " + responsabile.getCognome());
                referenti.add(progetto.getReferenteScientifico().getNome() + " " +
                        progetto.getReferenteScientifico().getCognome());
            }
        }

    }

    /**
     * Crea un array list prendendo, dai progetti in azienda, solo quelli in corso.
     *
     * @return the progetti in corso
     */
    public ArrayList<ProgettoInCorso> getProgettiInCorso() {
        ArrayList<ProgettoInCorso> result = new ArrayList<>();
        for (Progetto progetto: azienda.getProgetti()) {
            if(!progetto.isConcluso()) {
                result.add((ProgettoInCorso) progetto);
            }
        }
        return result;
    }

    /**
     * Crea un array list prendendo, dai progetti in azienda, solo quelli conclusi.
     *
     * @return the progetti conclusi
     */
    public ArrayList<ProgettoConcluso> getProgettiConclusi() {
        ArrayList<ProgettoConcluso> result = new ArrayList<>();
        for (Progetto progetto: azienda.getProgetti()) {
            if(progetto.isConcluso()) {
                result.add((ProgettoConcluso) progetto);
            }
        }
        return result;
    }


    /**
     * Metodo per passare alle gui una array list di String con dati degli impiegati con ruolo di dirigente.
     * ciascuna string contiene nome cognome e codice fiscale dell'impiegato in questo ordine.
     *
     * @return array list di stringhe composte da nome, cognome e codice fiscale di ciascun impiegato dirigente
     */
    public ArrayList<String> getImpiegatiDirigenti() {
        ArrayList<String> result = new ArrayList<>();
        for (Impiegato impiegato : azienda.getDirigenti()
             ) {
            result.add(impiegato.getNome() + " " + impiegato.getCognome() + " " + impiegato.getCf());
        }
        return result;
    }

    /**
     * Metodo per passare alla gui una array list di String con dati degli impiegati senior.
     * ciascuna string contiene nome cognome e codice fiscale dell'impiegato in questo ordine.
     *
     * @return array list di stringhe composte da nome, cognome e codice fiscale di ciascun impiegato senior.
     */
    public ArrayList<String> getSeniors() {
        ArrayList<String> result = new ArrayList<>();
        for (Impiegato impiegato : azienda.getSeniors()
        ) {
            result.add(impiegato.getNome() + " " + impiegato.getCognome() + " " + impiegato.getCf());
        }
        return result;
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati dell'azienda.
     *
     * @param modelTable model della tabella principale degli impiegati
     */
    public void setAllImpiegati(DefaultTableModel modelTable){

        for(Impiegato imp: getImpiegati()){

            String sesso;

            if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
            else sesso = "femmina";

            if(imp.getAfferenza() != null) {

                String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                        imp.getCf(),Float.toString(imp.getStipendio()),imp.getAfferenza().getNome(),
                        imp.getAfferenza().getTopic()};

                modelTable.addRow(dati);

            } else {
                String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),imp.getCf(),
                        Float.toString(imp.getStipendio()),
                        null, null};

                modelTable.addRow(dati);
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati che appartegono alla categoria passata come parametro.
     *
     * @param modelTable model della tabella degli impiegati
     * @param categoria  the categoria può esssumere valori da 1 a 4 che corrispondono rispettivamente a Junior,Middle,Senior,Dirigente
     */
    public void setPerCategoria(DefaultTableModel modelTable, int categoria){

        if(categoria == 1){

            for(Impiegato imp : getImpiegati()){

                if(imp instanceof Junior){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(), Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(categoria == 2){

            for(Impiegato imp : getImpiegati()){

                if(imp instanceof Middle){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(categoria == 3){

            for(Impiegato imp : getImpiegati()){

                if(imp instanceof Senior){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(categoria == 4){

            for(Impiegato imp : getImpiegati()){

                if(imp.getDirigente()!= null){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verrà visualizzato l'impiegato che corrisponde al codice fiscale passato come parametro.
     *
     * @param modelTable model della tabella degli impiegati.
     * @param cf         codice fiscale dell'impiegato da visualizzare
     */
    public void setImpiegatiPerCF(DefaultTableModel modelTable, String cf){

        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().equalsIgnoreCase(cf)){

                String sesso;

                if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                else sesso = "femmina";

                if(imp.getAfferenza() != null) {

                    String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                    modelTable.addRow(dati);

                } else {
                    String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            null, null};

                    modelTable.addRow(dati);
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati che appartegono al sesso passata come parametro.
     *
     * @param modelTable modello tabella degli impiegati
     * @param sesso      the sesso degli impiegati da visualizzare
     */
    public void setImpiegatiPerSesso(DefaultTableModel modelTable, char sesso){

        if(Character.toLowerCase(sesso) == 'm'){

            for(Impiegato imp  : getImpiegati()){

                if(Character.toLowerCase(imp.getSesso()) == 'm'){

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),"maschio",imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), "maschio", imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }else {

            for(Impiegato imp  : getImpiegati()){

                if(Character.toLowerCase(imp.getSesso()) == 'f'){

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),"femmina",imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), "femmina", imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati che hanno il nome corrispondente parametro passato.
     *
     * @param modelTable modello tabella degli impiegati
     * @param nome       nome corrispondente agli impiegati da visualizzare
     */
    public void setImpiegatiPerNome(DefaultTableModel modelTable, String nome){

        for(Impiegato imp : getImpiegati()){

            if(imp.getNome().toLowerCase().equals(nome.toLowerCase())){

                String sesso;

                if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                else sesso = "femmina";

                if(imp.getAfferenza() != null) {

                    String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                    modelTable.addRow(dati);

                } else {
                    String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            null, null};

                    modelTable.addRow(dati);
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati che hanno il cognome corrispondente parametro passato.
     *
     * @param modelTable modello tabella degli impiegati
     * @param cognome    cognome corrispondente agli impiegati da visualizzare
     */
    public void setImpiegatiPerCognome(DefaultTableModel modelTable, String cognome){

        for(Impiegato imp : getImpiegati()){

            if(imp.getCognome().toLowerCase().equals(cognome.toLowerCase())){

                String sesso;

                if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                else sesso = "femmina";

                if(imp.getAfferenza() != null) {

                    String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                    modelTable.addRow(dati);

                } else {
                    String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            null, null};

                    modelTable.addRow(dati);
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati la cui data di assunzione corrisponde all'itervallo passato come parametro.
     *
     * @param modelTable modello tabella degli impiegati
     * @param data       data di assunzione di riferimento per l'intervallo
     * @param inervallo  può assumere valori "<=", "<", "=", ">=" ">" relativamente alla data passato come secondo parametro
     */
    public void setImpiegatiPerData(DefaultTableModel modelTable, Date data, String inervallo){

        if(inervallo.equals("<=")){

            for (Impiegato imp : getImpiegati()){

                if(imp.getDataDiAssunzione().before(data) || imp.getDataDiAssunzione().equals(data)){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(inervallo.equals("<")){

            for (Impiegato imp : getImpiegati()){

                if(imp.getDataDiAssunzione().before(data) && !imp.getDataDiAssunzione().equals(data)){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(inervallo.equals("=")){

            for (Impiegato imp : getImpiegati()){

                if(imp.getDataDiAssunzione().equals(data)){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(inervallo.equals(">=")){

            for (Impiegato imp : getImpiegati()){

                if(imp.getDataDiAssunzione().after(data) || imp.getDataDiAssunzione().equals(data)){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }

        if(inervallo.equals(">")){

            for (Impiegato imp : getImpiegati()){

                if(imp.getDataDiAssunzione().after(data) && !imp.getDataDiAssunzione().equals(data)){

                    String sesso;

                    if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                    else sesso = "femmina";

                    if(imp.getAfferenza() != null) {

                        String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                        modelTable.addRow(dati);

                    } else {
                        String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                imp.getCf(),Float.toString(imp.getStipendio()),
                                null, null};

                        modelTable.addRow(dati);
                    }
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati tutti gli impiegati che lavorano per il laboratorio corrispondente al parametro passato.
     *
     * @param modelTable  modello tabella degli impiegati
     * @param laboratorio è un array di String con due valori: nome laboratorio, topic laboratorio
     */
    public void setImpiegatiPerLaboratorio(DefaultTableModel modelTable, String[] laboratorio){

        for(Impiegato imp : getImpiegati()){

            String sesso;

            if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
            else sesso = "femmina";

            if(imp.getAfferenza() != null){

                if(imp.getAfferenza().getNome().toLowerCase().equals(laboratorio[0].toLowerCase())
                        && imp.getAfferenza().getTopic().toLowerCase().equals(laboratorio[1].toLowerCase())){

                    String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                            imp.getCf(),Float.toString(imp.getStipendio()),
                            imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                    modelTable.addRow(dati);
                }
            }
        }
    }

    /**
     * Metodo che aggiorna la tabella degli impiegati in area impiegato.Metodo che aggiorna la tabella degli impiegati in area impiegato.
     * Nella tabella verranno visualizzati gli impiegati a cui è stato assegnato il progetto passato come parametro
     *
     * @param modelTable modello tabella degli impiegati
     * @param cup        progetto
     */
    public void setImpiegatiPerProgettoAssegnato(DefaultTableModel modelTable, String cup){

        for(Impiegato imp : getImpiegati()){

            if(imp.getLavoriProgettiAssegnati() != null){

                for(Lavorare lav : imp.getLavoriProgettiAssegnati()){

                    if(lav.getProgettoinCorso().getCup().toLowerCase().equals(cup.toLowerCase())){

                        String sesso;

                        if(Character.toLowerCase(imp.getSesso()) == 'm') sesso = "maschio";
                        else sesso = "femmina";

                        if(imp.getAfferenza() != null) {

                            String dati[] = {imp.getNome(),imp.getCognome(),sesso,imp.getDataDiNascita().toString(),
                                    imp.getCf(),Float.toString(imp.getStipendio()),
                                    imp.getAfferenza().getNome(),imp.getAfferenza().getTopic()};

                            modelTable.addRow(dati);

                            break;

                        } else {
                            String[] dati = {imp.getNome(), imp.getCognome(), sesso, imp.getDataDiNascita().toString(),
                                    imp.getCf(),Float.toString(imp.getStipendio()),
                                    null, null};

                            modelTable.addRow(dati);

                            break;
                        }
                    }
                }
            }
        }
    }


    /**
     * In questo metodo vengono passati come parametro tutti i modelli delle tabelle secondarie dell'aerea impiegato.
     * Nell'ultimo parametro invece viene passato il codice fiscale dell'impiegato per cui verranno settate le tabelle.
     * Per cui nelle tabelle verranno visualizzate le informazioni relative al codice fiscale.
     *
     * @param carriera             modello tabella che contiene la carriera dell'impiegato
     * @param labResp              modello tabella che contiene i laboratori per cui l'impiegato è responsabile
     * @param progRef              modello tabella che contiene i progetti per cui l'impiegato è referente
     * @param progResp             modello tabella che contiene i progetti per cui l'impiegato è responsabile
     * @param progInCorsoAssegnati modello tabella che contiene i progetti in corso a cui sono stati assegnati all'impiegato
     * @param progInCorsoContr     modello tabella che contiene i progetti in corso a cui l'impiegato ha contribuito
     * @param progConclusiContr    modello tabella che contiene i progetti conclusi a cui l'impiegato ha contribuito
     * @param cfImp                stringa contentete il codice fiscale
     */
    public void setModelliInfoImpiegato(DefaultTableModel carriera, DefaultTableModel labResp, DefaultTableModel progRef,
                                        DefaultTableModel progResp, DefaultTableModel progInCorsoAssegnati,
                                        DefaultTableModel progInCorsoContr, DefaultTableModel progConclusiContr, String cfImp){

        for(Impiegato imp: getImpiegati()){

            if(imp.getCf().toUpperCase().equals(cfImp.toUpperCase())) {

                //Riempimento modello carriera
                if (imp instanceof Junior && imp.getDirigente() == null) {

                    String[] rigaCarriera = {imp.getDataDiAssunzione().toString()};

                    carriera.addRow(rigaCarriera);
                }

                if (imp instanceof Junior && imp.getDirigente() != null) {

                    String[] rigaCarriera = {imp.getDataDiAssunzione().toString(), null, null, imp.getDirigente().getPassaggioDirigente().toString()};

                    carriera.addRow(rigaCarriera);
                }

                if (imp instanceof Middle && imp.getDirigente() == null) {

                    String[] rigaCarriera = {imp.getDataDiAssunzione().toString(), ((Middle) imp).getPassaggioMiddle().toString()};

                    carriera.addRow(rigaCarriera);
                }

                if (imp instanceof Middle && imp.getDirigente() != null) {

                    String[] rigaCarriera = {imp.getDataDiAssunzione().toString(), ((Middle) imp).getPassaggioMiddle().toString(),
                            null, imp.getDirigente().getPassaggioDirigente().toString()};

                    carriera.addRow(rigaCarriera);
                }

                if (imp instanceof Senior && imp.getDirigente() == null) {

                    String[] rigaCarriera = {imp.getDataDiAssunzione().toString(), ((Senior) imp).getPassaggioMiddle().toString(),
                            ((Senior) imp).getPassaggioSenior().toString()};

                    carriera.addRow(rigaCarriera);
                }

                if (imp instanceof Senior && imp.getDirigente() != null) {

                    String[] rigaCarriera = {imp.getDataDiAssunzione().toString(), ((Senior) imp).getPassaggioMiddle().toString(),
                            ((Senior) imp).getPassaggioSenior().toString(), imp.getDirigente().getPassaggioDirigente().toString()};

                    carriera.addRow(rigaCarriera);
                }

                //Riempimento modello lab di cui è reponsabile
                for (Laboratorio lab : getLaboratori()) {

                    if (imp instanceof Senior && lab.getResponsabileScientifico() == imp) {

                        String[] rigaLabResp = {lab.getNome(), lab.getTopic()};

                        labResp.addRow(rigaLabResp);
                    }
                }

                //Riempimento modello progetti referenti e progetti responsabili
                for (Progetto prog : getProgetti()) {

                    if (imp instanceof Senior && prog.getReferenteScientifico() == imp) {

                        String[] rigaProgRef = {prog.getNome(), prog.getCup()};

                        progRef.addRow(rigaProgRef);
                    }

                    if (prog.getResponsabile() == imp.getDirigente()) {

                        String[] rigaProgResp = {prog.getNome(), prog.getCup()};

                        progResp.addRow(rigaProgResp);
                    }
                }

                //Riempimento modello progetti in corso asseganti
                if (imp.getLavoriProgettiAssegnati() != null) {

                    for (Lavorare lav : imp.getLavoriProgettiAssegnati()) {

                        String[] rigaProgAssegnati = {lav.getProgettoinCorso().getNome(), lav.getProgettoinCorso().getCup(),Integer.toString(lav.getOre())};

                        progInCorsoAssegnati.addRow(rigaProgAssegnati);
                    }
                }

                //Riempimento modello progetti in corso a cui ha contribuito e modello progetti conclusi a cui ha contibuito
                if (imp.getContributiPassati() != null) {

                    for (Progetto prog : imp.getContributiPassati()) {

                        if (prog instanceof ProgettoInCorso) {

                            String[] rigaProgInCorsoContr = {prog.getNome(), prog.getCup()};

                            progInCorsoContr.addRow(rigaProgInCorsoContr);
                        } else {

                            String[] rigaProgConclusoContr = {prog.getNome(), prog.getCup()};

                            progConclusiContr.addRow(rigaProgConclusoContr);
                        }
                    }
                }

                break;
            }
        }
    }

    /**
     * Add progetto in corso.
     *
     * @param nome                       the nome
     * @param cup                        the cup
     * @param dataInizio                 the data inizio
     * @param indiceResponsabile         the indice responsabile
     * @param indiceReferenteScientifico the indice referente scientifico
     * @param indiciAssegnazioni         the indici assegnazioni
     * @throws RuntimeException the runtime exception
     */
//Scritture
    public void addProgettoInCorso(String nome, String cup, Date dataInizio, int indiceResponsabile,
                                   int indiceReferenteScientifico, ArrayList<Integer> indiciAssegnazioni) {

        Senior referenteScientifico = azienda.getSeniors().get(indiceReferenteScientifico);
        Dirigente responsabile = azienda.getDirigenti().get(indiceResponsabile).getDirigente();
        ArrayList<Laboratorio> assegnazioni = new ArrayList<>();

        for (Integer i: indiciAssegnazioni
        ) {
            assegnazioni.add(azienda.getLaboratori().get(i));
        }

        //Aggiunzione in memoria
        Progetto progetto = new ProgettoInCorso(nome, cup, dataInizio, assegnazioni, referenteScientifico, responsabile);
        azienda.addProgetti(progetto);

        //Aggiunzione in database
        AziendaDAO aziendaDB = new AziendaImplementazionePostgresDAO();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataInizioString = dateFormat.format(dataInizio);
        ArrayList<String[]> assegnazioniString = new ArrayList<>();
        for (Laboratorio laboratorio: assegnazioni
        ) {
            String[] assegnazione = new String[2];
            assegnazione[0] = laboratorio.getNome();
            assegnazione[1] = laboratorio.getTopic();
            assegnazioniString.add(assegnazione);
        }
        aziendaDB.addProgettoInCorso(cup, nome, dataInizioString, assegnazioniString, referenteScientifico.getCf(),
                responsabile.getImpiegato().getCf());

    }

    /**
     * Purché non esistano laboratori con la stessa coppia di topic e nome in azieda, aggiunge il laboratorio,
     * altrimenti propaga l'eccezione che parte da addLaboratorio(...) in {@link Azienda}.
     *
     * @param nome                          Nome del laboratorio da inserire
     * @param topic                         Topic del laboratorio da inserire
     * @param indiceResponsabileScientifico Indice del senior a cui viene assegnato il ruolo di responsabile                                      scientifico per il nuovo laboratorio.
     * @throws RuntimeException Eccezione propagata da addLaboratorio.
     * @see Azienda
     */
    public void addLaboratorio(String nome, String topic, int indiceResponsabileScientifico) throws RuntimeException{

        //Aggiunzione in memoria
        Senior responsabileScientifico = azienda.getSeniors().get(indiceResponsabileScientifico);
        azienda.addLaboratorio(new Laboratorio(nome, topic, responsabileScientifico));

        //Aggiunzione in database
        AziendaDAO aziendaDB = new AziendaImplementazionePostgresDAO();
        aziendaDB.addLaboratorio(nome, topic, responsabileScientifico.getCf());
    }

    /**
     * metodo che assegna all'impiegato passato come parametro un lavoro
     *
     * @param cf  stringa codice fiscale impiegato
     * @param cup stringa cup progetto
     * @param ore valore intero che rappresenta le ore settimane dedicate al progetto
     * @return the boolean, ritorna true se l'inserimento ha avuto successo false altrimenti.
     */
    public boolean insertLavorare(String cf, String cup, int ore){

        //Controllo esistenza progetto
        if(findProgetto(cup) == null){

            JOptionPane.showMessageDialog(null,"Progetto da assegnare inesistente", "Errore", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if(findProgetto(cup).isConcluso()) {
            JOptionPane.showMessageDialog(null,"Il progetto inserito si è già concluso.", "Errore", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        //Controllo che il progetto da assegnare sia assegnato allo stesso laboratorio per cui lavora l'impiegato
        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().equalsIgnoreCase(cf)){

                if(imp.getAfferenza() != null){

                    for(Progetto prog : getProgetti()){

                        if(prog.getCup().equalsIgnoreCase(cup)){

                            if(!prog.getLaboratoriAssegnati().contains(imp.getAfferenza())){

                                JOptionPane.showMessageDialog(null,"Progetto da assegnare non corrisponde ai progetti assegnati al laboratorio per cui lavora l'impiegato", "Errore", JOptionPane.ERROR_MESSAGE);

                                return false;

                            } else break;
                        }
                    }

                } else {

                    JOptionPane.showMessageDialog(null,"L'impiegato non lavora per nessun laboratorio, assegnare prima un laboratorio", "Errore", JOptionPane.ERROR_MESSAGE);

                    return false;
                }

                break;
            }
        }

        boolean flag = false;

        Impiegato impiegato = findImpiegato(cf);
        if(impiegato.getOreLavoro() + ore > 48) {
            throw new RuntimeException("Inserimento annullato. Numero di ore lavorative settimanali supera il massimo.");
        }

        //Inserimento lavorare in DB
        ImpiegatoDAO insertLavorare = new ImpiegatoImplementazionePostgresDAO();
        flag = insertLavorare.insertLavorare(cf,cup,ore);

        //Inserimento in memoria
        if(flag){

            for(Impiegato imp : getImpiegati()){

                if(imp.getCf().equalsIgnoreCase(cf)){

                    for(Progetto prog : getProgetti()){

                        if(prog.getCup().equalsIgnoreCase(cup) && prog instanceof ProgettoInCorso){

                            if(imp.getLavoriProgettiAssegnati() != null){

                                imp.addLavoro(new Lavorare(imp,(ProgettoInCorso) prog,ore));

                                flag = true;

                                JOptionPane.showMessageDialog(null, "L'assegnazione è andata a buon fine");
                            }else{

                                imp.setLavoriProgettiAssegnati(new ArrayList<Lavorare>());

                                imp.getLavoriProgettiAssegnati().add(new Lavorare(imp,(ProgettoInCorso) prog,ore));

                                flag = true;

                                JOptionPane.showMessageDialog(null, "L'assegnazione è andata a buon fine");
                            }

                            break;
                        }
                    }

                    break;
                }
            }
        }

        return flag;
    }

    /**
     * metodo che inserisce un impiegato nel sistema
     *
     * @param nome             stringa nome impiegato
     * @param cognome          stringa cognome impiegato
     * @param sesso            carattere sesso con possibili valori 'm' o 'f'
     * @param dataDiNascita    date relativo alla data di nascita
     * @param luogoDiNascita   stringa del luogo di nascita
     * @param cf               stringa codice fiscale
     * @param dataDiAssunzione date relativa alla data di assunzione
     * @param stipendio        String di caratteri numerici relativi allo stipendio
     * @param dirigente        boolean: false se non entra come dirigente, true altrimenti.
     * @return the boolean: true se l'inserimento ha avuto successo, false altrimenti.
     */
    public boolean inserisciImpiegato(String nome, String cognome, char sesso, Date dataDiNascita,
                                      String luogoDiNascita, String cf, Date dataDiAssunzione,
                                      String stipendio, boolean dirigente){


        AziendaImplementazionePostgresDAO insertImpiegatoPS = new AziendaImplementazionePostgresDAO();

        boolean checkInserimento;

        ///SALVATAGGIO IN DB
        checkInserimento = insertImpiegatoPS.inserisciImpiegato(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,
                dataDiAssunzione,stipendio,dirigente);

        //Salvataggio in memoria
        if(checkInserimento) {

            Date date = new Date();
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(date);
            Calendar passaggioMiddle = Calendar.getInstance();
            passaggioMiddle.setTime(dataDiAssunzione);
            passaggioMiddle.add(Calendar.YEAR, 3);
            Calendar passaggioSenior = Calendar.getInstance();
            passaggioSenior.setTime(dataDiAssunzione);
            passaggioSenior.add(Calendar.YEAR, 7);

            if(currentDate.after(passaggioSenior)) {
                if(dirigente) {
                    azienda.addImpiegato(new Senior(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf,
                            dataDiAssunzione, Float.parseFloat(stipendio), null, date, passaggioMiddle.getTime(),
                            passaggioSenior.getTime()));
                } else {
                    azienda.addImpiegato(new Senior(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf,
                            dataDiAssunzione, Float.parseFloat(stipendio), null, passaggioMiddle.getTime(),
                            passaggioSenior.getTime()));
                }
            } else if(currentDate.after(passaggioMiddle)) {
                if(dirigente) {
                    azienda.addImpiegato(new Middle(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf,
                            dataDiAssunzione, Float.parseFloat(stipendio), null, date, passaggioMiddle.getTime()));
                } else {
                    azienda.addImpiegato(new Middle(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf,
                            dataDiAssunzione, Float.parseFloat(stipendio), null, passaggioMiddle.getTime()));
                }
            } else {
                if(dirigente) {
                    azienda.addImpiegato(new Junior(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf,
                            dataDiAssunzione, Float.parseFloat(stipendio), null, date));
                } else {
                    azienda.addImpiegato(new Junior(nome, cognome, sesso, dataDiNascita, luogoDiNascita, cf,
                            dataDiAssunzione, Float.parseFloat(stipendio), null));
                }
            }
        }

        return checkInserimento;
    }

    /**
     * Conclude un progetto in corso eliminandolo e reinserendolo come progetto concluso.
     *
     * @param indiceProgetto indice del progetto in corso da concludere.
     * @param dataFine       data di conclusione del progetto.
     */
//Modifiche
    public void concludiProgetto(int indiceProgetto, Date dataFine) {
        ProgettoInCorso progetto = getProgettiInCorso().get(indiceProgetto);
        //In memoria
       azienda.concludiProgetto(progetto, dataFine);

        //In database
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataInizioString = dateFormat.format(progetto.getDataInizio());
        String dataFineString = dateFormat.format(dataFine);

        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.concludiProgetto(progetto.getCup(), progetto.getNome(), dataInizioString, dataFineString,
                progetto.getReferenteScientifico().getCf(), progetto.getResponsabile().getImpiegato().getCf());
    }

    /**
     * Se il progetto non ha già tre laboratorio assengati, assegna al progetto il laboratorio in input.
     * Altrimenti propaga l'eccezione proveniente da ProgettoInCorso.addAssegnazioneLaboratorio(...).
     *
     * @param indiceProgettoInCorso l'indice del progetto in corso a cui aggiungere l'assegnazione
     * @param nomeLab               Nome del laboratorio da assegnare al progetto.
     * @param topicLab              Topic del laboratorio da assegnare al progetto.
     * @throws RuntimeException eccezione propagata da addAssegnazioneLaboratori(...) in progetto in corso nel caso in cui il progetto abbia già tre laboratori assegnati.
     */
    public void addAssegnazioneLaboratorioProgetto(int indiceProgettoInCorso, String nomeLab,
                                                   String topicLab) throws RuntimeException {

        Laboratorio laboratorio = findLaboratorio(nomeLab, topicLab);
        ProgettoInCorso progettoInCorso = azienda.getProgettiInCorso().get(indiceProgettoInCorso);

        //controlliamo che il laboratorio esista
        if(laboratorio == null) {
            throw new RuntimeException("Laboratorio non trovato");
        }

        //In memoria
        progettoInCorso.addAssegnazioneLaboratorio(laboratorio);

        //In database
        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.addAssegnazione(progettoInCorso.getCup(), laboratorio.getNome(),
                laboratorio.getTopic());

    }

    /**
     * Se il progetto in corso ha almeno due laboratori assegnati, e non esistono impiegati afferenti al laboratorio
     * da eliminare che lavorano al progetto, elimina l'assegnazione del laboratorio al progetto. Altrimenti si
     * riscontra un'eccezione
     *
     * @param indiceProgettoInCorso La posizione del progetto in corso in azienda.
     * @param indiceLaboratorio     La posizione del laboratorio nella lista di assegnazioni nel progetto.
     * @throws RuntimeException L'eccezione avviene quando ci sono impiegati afferenti al laboratorio che lavorano al progetto, oppure quando il laboratorio che si vuole eliminare è l'unico assegnato al progetto.
     */
    public void rimuoviAssegnazione(int indiceProgettoInCorso, int indiceLaboratorio) throws RuntimeException {
        ProgettoInCorso progettoInCorso = getProgettiInCorso().get(indiceProgettoInCorso);
        ArrayList<String> nomiLab = new ArrayList<>();
        ArrayList<String> topicLab = new ArrayList<>();
        getLaboratoriAssegnati(indiceProgettoInCorso, nomiLab, topicLab, new ArrayList<>());

        Laboratorio laboratorio = findLaboratorio(nomiLab.get(indiceLaboratorio), topicLab.get(indiceLaboratorio));

        //Controlliamo che non ci siano impiegati del laboratorio che ancora lavorano al progetto
        Boolean violazione = false;

        for (Impiegato impiegato : impiegatiAfferenti(laboratorio)
        ) {
            for (Lavorare lavorare : impiegato.getLavoriProgettiAssegnati()
            ) {
                if(lavorare.getProgettoinCorso().equals(progettoInCorso)) {
                    violazione = true;
                    break;
                }
            }
        }

        if(violazione) {
            throw new RuntimeException("Rimozione annullata. Nel laboratorio ci sono " +
                    "ancora impiegati che stanno lavorando al progetto");
        }

        //in memoria
        progettoInCorso.removeAssegnazioneLaboratorio(laboratorio);

        //in database
        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.removeAssegnazione(progettoInCorso.getCup(), laboratorio.getNome(), laboratorio.getTopic());
    }

    /**
     * Sostituisce il referente scientifico del progetto in corso indicato dall'indice progetto in input con
     * l'impiegato di codice fiscale pari alla stringa al secondo input
     *
     * @param indiceProgetto indice del progetto in corso di cui vogliamo sostituire il referente scientifico.
     * @param cfImpiegato    codice fiscale dell'impiegato che assumerà il ruolo di referente scientifico.
     * @throws RuntimeException sollevata quando il codice fiscale non appartiene ad un impiegato senior o quando non appartiene ad alcun impiegato in azienda.
     */
    public void setReferenteScientifico(int indiceProgetto, String cfImpiegato) throws RuntimeException {
        ProgettoInCorso progettoInCorso = azienda.getProgettiInCorso().get(indiceProgetto);
        Impiegato impiegato = findImpiegato(cfImpiegato);

        if(impiegato == null) throw new RuntimeException("Impiegato non trovato");
        if(!impiegato.getCategoria().equals("senior")) throw new RuntimeException("L'impiegato non è senior");

        //in memoria
        progettoInCorso.setReferenteScientifico((Senior)impiegato);

        //in database
        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.setReferenteScientifico(progettoInCorso.getCup(), cfImpiegato);
    }

    /**
     * Sostituisce il responsabile del progetto in corso indicato dall'indice progetto in input con
     * l'impiegato di codice fiscale pari alla stringa al secondo input
     *
     * @param indiceProgetto indice del progetto in corso di cui vogliamo sostituire il responsabile.
     * @param cfImpiegato    codice fiscale dell'impiegato che assumerà il ruolo di responsabile.
     * @throws RuntimeException sollevata quando il codice fiscale non appartiene ad un impiegato dirigente o quando non appartiene ad alcun impiegato in azienda.
     */
    public void setResponsabile(int indiceProgetto, String cfImpiegato) throws RuntimeException {
        ProgettoInCorso progettoInCorso = azienda.getProgettiInCorso().get(indiceProgetto);
        Impiegato impiegato = findImpiegato(cfImpiegato);

        if(impiegato == null) throw new RuntimeException("Impiegato non trovato");
        if(impiegato.getDirigente() == null) throw new RuntimeException("L'impiegato non è dirigente");

        //in memoria
        progettoInCorso.setResponsabile(impiegato.getDirigente());

        //in database
        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.setResponsabile(progettoInCorso.getCup(), cfImpiegato);
    }

    /**
     * Sostituisce il responsabile scientifico del progetto in corso indicato dall'indice progetto in input con
     * l'impiegato di codice fiscale pari alla stringa al secondo input
     *
     * @param indiceLaboratorio indice del laboratorio di cui vogliamo sostituire il responsabile scientifico.
     * @param cfImpiegato       codice fiscale dell'impiegato che assumerà il ruolo di responsabile scientifico.
     * @throws RuntimeException sollevata quando il codice fiscale non appartiene ad un impiegato senior o quando non appartiene ad alcun impiegato in azienda.
     */
    public void setResponsabileScientifico(int indiceLaboratorio, String cfImpiegato) throws RuntimeException {
        //in memoria
        Laboratorio laboratorio = azienda.getLaboratori().get(indiceLaboratorio);
        Impiegato impiegato = findImpiegato(cfImpiegato);

        if(impiegato == null) {
            throw new RuntimeException("Impiegato non trovato");
        }
        if(impiegato.getDirigente() == null) {
            throw new RuntimeException("L'impiegato non è dirigente");
        }


        laboratorio.setResponsabileScientifico((Senior) impiegato);

        //in database
        LaboratorioDAO laboratorioDAO = new LaboratorioImplementazionePostgresDAO();
        laboratorioDAO.setResponsabileScientifico(laboratorio.getNome(), laboratorio.getTopic(), cfImpiegato);
    }


    /**
     * Metodo che assegna o modifica un laboratorio di un impiegato.
     *
     * @param cf      stringa codice fiscale impiegato
     * @param nomeLab stringa nome laboratorio
     * @param topic   stringa topic laboratorio
     * @return the boolean: true se la modifica è avvenuta con successo, false altrimenti
     */
    public boolean updateAfferenzaImp(String cf, String nomeLab, String topic) {

        boolean flag = false;

        //Verifica che il laboratorio esiste
       if(findLaboratorio(nomeLab, topic) == null){

           JOptionPane.showMessageDialog(null,"Laboratorio inesistente", "Errore", JOptionPane.ERROR_MESSAGE);

           return false;
       }

       //Verifica che il nuovo laboratorio afferito siano stati assegnati i progetti per cui lavora l'impiegato
        Impiegato impiegato = null;
        Laboratorio laboratorio = null;
        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().equalsIgnoreCase(cf)){

                impiegato = imp;
                break;
            }
        }

        for(Laboratorio lab : getLaboratori()){

            if(lab.getNome().equalsIgnoreCase(nomeLab) && lab.getTopic().equalsIgnoreCase(topic)){

                laboratorio = lab;
                break;
            }
        }

        for(Lavorare lavAssegnato : impiegato.getLavoriProgettiAssegnati()){

           if (!lavAssegnato.getProgettoinCorso().getLaboratoriAssegnati().contains(laboratorio)){

               JOptionPane.showMessageDialog(null,"Al laboratorio non sono stati assegnati i progetti per cui lavora l'impiegato", "Errore", JOptionPane.ERROR_MESSAGE);

               return false;
           }

        }
        //////Fine verifica


        //Update in DB
        ImpiegatoDAO updateLab = new ImpiegatoImplementazionePostgresDAO();
        flag = updateLab.updateAfferenza(cf,nomeLab,topic);

        //Update in memoria
        if(flag){

            for(Impiegato imp : getImpiegati()){

                if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                    for(Laboratorio lab : getLaboratori()){

                        if(lab.getNome().toLowerCase().equals(nomeLab.toLowerCase()) &&
                                lab.getTopic().toLowerCase().equals(topic.toLowerCase())){

                            imp.setAfferenza(lab);

                            JOptionPane.showMessageDialog(null, "Modifica effettuata");

                            break;
                        }
                    }

                    break;
                }
            }
        }

        return flag;
    }

    /**
     * metodo che modifica le ore di un lavoro assegnato all'impiegato passato come parametro
     *
     * @param cf  stringa cofice fiscaale
     * @param cup stringa cup progetto
     * @param ore valore intero che rappresenta le nuove ore da assegnare
     * @return the boolean: true se la modifica è avvenuta, false altrimenti
     */
    public boolean updateOre(String cf, String cup, int ore){

        boolean flag = false;

        Impiegato impiegato = findImpiegato(cf);
        Lavorare lavorare = null;

        //cerchiamo l'impiegato ed il lavoro da modificare


        for(Lavorare lav: impiegato.getLavoriProgettiAssegnati()) {
            if(lav.getProgettoinCorso().getCup().equalsIgnoreCase(cup)) {
                lavorare = lav;
            }
        }

        if (lavorare == null) {
            return false;
        }
        //verifichiamo che la modifica sia lecita

        if(impiegato.getOreLavoro() - lavorare.getOre() + ore > 48){
            JOptionPane.showMessageDialog(null, "Aggiornamento annullato: " +
                    "ore di lavoro settimanali massime superate.");
            return false;
        }

        //Modifica ore in DB
        LavorareDAO updateOre = new LavorareImplementazionePostgresDAO();
        flag = updateOre.updateOre(impiegato.getCf(), lavorare.getProgettoinCorso().getCup(),ore);

        //Modifica in memoria
        if(flag){
            lavorare.setOre(ore);
        }

        return flag;
    }

    /**
     * metodo che promuove dirigente l'impiegato relativo al codice fiscale passato come parametro.
     *
     * @param cf
     * @param dataPromo
     * @return
     */

    public boolean updateDirigenza(String cf, Date dataPromo){

        Impiegato impiegato = null;

        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().equalsIgnoreCase(cf)){

                impiegato = imp;

                if(imp.getDirigente() != null){

                    JOptionPane.showMessageDialog(null,"L'impiegato è già dirigente");

                    return false;
                }

                break;
            }
        }

        boolean flag = false;
        //Inseirmento dirigente in DB
        AziendaDAO inserisciDirigente = new AziendaImplementazionePostgresDAO();
        flag = inserisciDirigente.inserisciDirigente(cf, dataPromo);

        //Modifica in memoria
        if(flag){

            Dirigente dirigente = new Dirigente(dataPromo,impiegato);

            impiegato.setDirigente(dirigente);

            flag = true;
        }

        return flag;

    }

    /**
     * Purché non vi siano impiegati che vi afferiscono o progetti in corso che vi sono assegnati, elimina il laboratorio
     * in posizione corrispondente all'indice in input. Altrimenti, propaga una eccezione.
     *
     * @param indiceLaboratorio indice del laboratorio da eliminare.
     * @throws RuntimeException sollevata nel caso in cui vi siano progetti in corso assegnati al laboratorio o impiegati che vi afferiscono.
     */


//////////////Cancellazioni
    public void deleteLaboratorio(int indiceLaboratorio) throws RuntimeException {
        Laboratorio laboratorio = azienda.getLaboratori().get(indiceLaboratorio);

        //in memoria
        azienda.removeLaboratorio(laboratorio);

        //in database
        AziendaDAO aziendaDAO = new AziendaImplementazionePostgresDAO();
        aziendaDAO.deleteLaboratorio(laboratorio.getTopic(), laboratorio.getNome());
    }

    /**
     * Conclude il lavoro relativo al progetto con cup uguale alla stringa passato per secondo parametro ed
     * all'impiegato con cf uguale alla string passata come primo parametro.
     * Verrà quindi eliminato dalla arrayList di Lavori dell'impiegato e aggiunto all'ArrayList di contributi passati.
     *
     * @param cf  stringa impiegato
     * @param cup stringa cup progetto
     * @return the boolean: true se la conclusione del progetto è avvenuto con successo, false altrimenti
     */
    public boolean insertLavoroContribuito(String cf, String cup){

        boolean flag = false;

        //Inserimento contributo passato in DB
        ImpiegatoDAO contributoInsert = new ImpiegatoImplementazionePostgresDAO();
        flag = contributoInsert.insertContributoPassato(cf,cup);

        //Inseriemtno contributo in memoria
        if(flag){

            Lavorare lavoroDaEliminare = null;

            for(Impiegato imp : getImpiegati()){

                if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                        //Inserimento contributo passato
                        for(Progetto prog : getProgetti()){

                            if(prog.getCup().toLowerCase().equals(cup.toLowerCase())){

                                if(imp.getContributiPassati() != null){

                                    imp.getContributiPassati().add(prog);
                                }else{

                                    imp.setContributiPassati(new ArrayList<Progetto>());

                                    imp.getContributiPassati().add(prog);
                                }
                            }
                        }

                        //Eliminazione lavoro
                        for(Lavorare lav : imp.getLavoriProgettiAssegnati()){

                            if(lav.getProgettoinCorso().getCup().toLowerCase().equals(cup.toLowerCase())){

                                lavoroDaEliminare = lav;

                                JOptionPane.showMessageDialog(null,"operazione effettuata");
                            }
                        }

                        if(lavoroDaEliminare != null)
                            imp.getLavoriProgettiAssegnati().remove(lavoroDaEliminare);

                        break;
                    }
                }
            }

        return flag;
    }


    /**
     * Metodo che elimina un impiegato dal sistema, assicurandosi che non abbia vincoli nel sistema.
     *
     * @param cf stringa codice fiscale
     * @return the boolean: true se l'elimianzione è avvenuto, false altrimenti.
     */
    public boolean deleteImpiegato(String cf){

        Impiegato impiegato = null;

        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().equalsIgnoreCase(cf)){

                impiegato = imp;

                break;
            }
        }

        if(impiegato.getLavoriProgettiAssegnati() != null && impiegato.getLavoriProgettiAssegnati().size() > 0){

            JOptionPane.showMessageDialog(null,"L'impiegato da eliminare ha dei lavori assegnati", "Erore", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        for(Laboratorio lab : getLaboratori()){

            if(lab.getResponsabileScientifico() == impiegato){

                JOptionPane.showMessageDialog(null,"L'impiegato da eliminare è responsabile di un laboratorio", "Erore", JOptionPane.ERROR_MESSAGE);

                return false;
            }
        }

        for(Progetto prog : getProgetti()){

            if(prog.getReferenteScientifico() == impiegato){

                JOptionPane.showMessageDialog(null,"L'impiegato da eliminare è referente di un progetto", "Erore", JOptionPane.ERROR_MESSAGE);

                return false;

            }
        }

        for (Progetto prog : getProgetti()){

            if(prog.getResponsabile() == impiegato.getDirigente()){

                JOptionPane.showMessageDialog(null,"L'impiegato da eliminare è responsabile di un progetto", "Erore", JOptionPane.ERROR_MESSAGE);

                return false;
            }
        }

        boolean flag = false;

        //Cancellazione in DB
        AziendaDAO deleteImp = new AziendaImplementazionePostgresDAO();
        flag = deleteImp.deleteImpiegato(cf);

        //Cancellazione in memoria
        if(flag){

            flag = false;

            Impiegato impDaEliminare = null;

            for(Impiegato imp: getImpiegati()){

                if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                    impDaEliminare = imp;

                    flag = true;

                    JOptionPane.showMessageDialog(null,"Eliminazione effettuata");

                    break;
                }
            }

            if(flag) {
                azienda.removeImpiegato(impDaEliminare);
            }
        }

        return flag;
    }

    /**
     * Elimina un progetto in corso.
     *
     * @param indiceProgetto indice del progetto da eliminare nella lista di progetti in corso.
     */
    public void removeProgettoInCorso(int indiceProgetto) {

        //in memoria
        ProgettoInCorso progetto = getProgettiInCorso().get(indiceProgetto);

        azienda.removeProgettoInCorso(progetto);

        //in database
        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.removeProgettoInCorso(progetto.getCup());
    }

    /**
     * Elimina un progetto concluso.
     *
     * @param indiceProgetto indice del progetto da eliminare nella lista di progetti conclusi.
     */
    public void removeProgettoConcluso(int indiceProgetto) {

        //in memoria
        ProgettoConcluso progetto = getProgettiConclusi().get(indiceProgetto);

        azienda.removeProgettoConcluso(progetto);
        //in database
        ProgettoConclusoDAO progettoConclusoDAO = new ProgettoConclusoImplementazionePostgresDAO();

        progettoConclusoDAO.removeProgettoConcluso(progetto.getCup());
    }


    /**
     * Calcolo cf string.
     *
     * @param cognome        the cognome
     * @param nome           the nome
     * @param dataDiNascita  the data di nascita
     * @param sesso          the sesso
     * @param isEstero       the is estero
     * @param luogoDiNascita the luogo di nascita
     * @return the string
     */
///////////////////METODI PER IL CALCOLO DEL CF//////////////////////////////////////////////////////////////////////////////

    /**
     * Calcola il codice fiscale a partire dai dati di input.
     * @param cognome cognome
     * @param nome nome
     * @param dataDiNascita data di nascita
     * @param sesso genere anagrafico
     * @param isEstero true se l'impiegato è straniero, false se è italiano
     * @param luogoDiNascita luogo di nascita.
     * @return restituisce il codice fiscale calcolato
     */
    public String calcoloCF(String cognome, String nome, Date dataDiNascita, char sesso, boolean isEstero, String luogoDiNascita){

        String cf = new String();

        cognome = cognome.replaceAll("\\s","");

        cognome = cognome.toLowerCase();

        nome = nome.replaceAll("\\s","");

        nome = nome.toLowerCase();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));

        cal.setTime(dataDiNascita);

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH);

        int day = cal.get(Calendar.DAY_OF_MONTH);

        if(eliminaVocali(cognome).length() >= 3) {

            cf = eliminaVocali(cognome).substring(0,3);
        }

        if(eliminaVocali(cognome).length() == 2) {

            cf = eliminaVocali(cognome).substring(0,2);

            cf = cf + eliminaConsonanti(cognome).substring(0,1);
        }

        if(eliminaVocali(cognome).length() == 1 && eliminaConsonanti(cognome).length() >= 2) {

            cf = eliminaVocali(cognome);

            cf = cf + eliminaConsonanti(cognome).substring(0,2);
        }

        if(eliminaVocali(cognome).length() == 1 && eliminaConsonanti(cognome).length() == 1) {

            cf = eliminaVocali(cognome);

            cf = cf + eliminaConsonanti(cognome);

            cf = cf + "x";
        }

        if(eliminaVocali(cognome).length() == 0 && eliminaConsonanti(cognome).length() == 2) {

            cf = eliminaConsonanti(cognome);

            cf = cf + "x";
        }

        if(eliminaVocali(nome).length() >= 4) {

            cf = cf + eliminaVocali(nome).substring(0,1);

            cf = cf + eliminaVocali(nome).substring(2,4);
        }

        if(eliminaVocali(nome).length() == 3) {

            cf = cf + eliminaVocali(nome).substring(0,3);
        }

        if(eliminaVocali(nome).length() == 2) {

            cf = cf + eliminaVocali(nome);

            cf = cf + eliminaConsonanti(nome).substring(0,1);
        }

        if(eliminaVocali(nome).length() == 1 && eliminaConsonanti(nome).length() == 2) {

            cf = cf + eliminaVocali(nome);

            cf = cf + eliminaConsonanti(nome);
        }

        if(eliminaVocali(nome).length() == 1 && eliminaConsonanti(nome).length() == 1) {

            cf = cf + eliminaVocali(nome);

            cf = cf + eliminaConsonanti(nome);

            cf = cf + "x";
        }

        if(eliminaVocali(nome).length() == 0 && eliminaConsonanti(nome).length() == 2) {

            cf = cf + eliminaConsonanti(nome);

            cf = cf + "x";
        }

        int charAnno = year%100;

        if(charAnno > 10) cf = cf + charAnno;

        else if(charAnno > 0) cf = cf + "0" + charAnno;

        else cf = cf + "00";

        String charMese = convertiMese(month);

        cf = cf + charMese;

        if(sesso == 'f') day = day +40;

        String dayString;

        dayString = (day < 10) ? ("0" + String.valueOf(day)) : String.valueOf(day);

        cf = cf + dayString;

        cf = cf + calcolaCodiceCatastale(luogoDiNascita, isEstero);

        cf = cf.toLowerCase();

        String letterePari;

        String lettereDispari;

        letterePari = letterePari(cf);

        lettereDispari = lettereDispari(cf);

        int somma = 0;

        for(int i = 0; i<letterePari.length(); i++) {

            somma = somma + convertiPari(letterePari.substring(i,i+1));
        }

        for(int i = 0; i<lettereDispari.length(); i++) {

            somma = somma + convertiDispari(lettereDispari.substring(i,i+1));
        }

        somma = somma%26;

        cf = cf + checkDigit(somma);

        cf = cf.toUpperCase();

        return cf;
    }

    /**
     * Funzione ausiliare per il calcolo del cf.
     * @param parola parole
     * @return
     */
    private String eliminaVocali(String parola) {

        String decomposta;

        decomposta = parola.replace("a", "");

        decomposta = decomposta.replace("e", "");

        decomposta = decomposta.replace("i", "");

        decomposta = decomposta.replace("o", "");

        decomposta = decomposta.replace("u", "");

        return decomposta;
    }

    /**
     *Funzione ausiliare per il calcolo del cf.
     * @param parola
     * @return
     */
    private String eliminaConsonanti(String parola) {

        String decomposta;

        decomposta = parola.replace("b", "");

        decomposta = decomposta.replace("c", "");

        decomposta = decomposta.replace("d", "");

        decomposta = decomposta.replace("f", "");

        decomposta = decomposta.replace("g", "");

        decomposta = decomposta.replace("h", "");

        decomposta = decomposta.replace("j", "");

        decomposta = decomposta.replace("k", "");

        decomposta = decomposta.replace("l", "");

        decomposta = decomposta.replace("m", "");

        decomposta = decomposta.replace("n", "");

        decomposta = decomposta.replace("p", "");

        decomposta = decomposta.replace("q", "");

        decomposta = decomposta.replace("r", "");

        decomposta = decomposta.replace("s", "");

        decomposta = decomposta.replace("t", "");

        decomposta = decomposta.replace("v", "");

        decomposta = decomposta.replace("w", "");

        decomposta = decomposta.replace("x", "");

        decomposta = decomposta.replace("y", "");

        decomposta = decomposta.replace("z", "");

        return  decomposta;
    }

    /**
     * Funzione ausiliare per il calcolo del cf.
     * @param mese mese
     * @return
     */
    private String convertiMese(int mese) {

        mese = mese + 1;

        switch (mese) {

            case 1:

                return "a";

            case 2:

                return "b";

            case 3:

                return "c";

            case 4:

                return "d";


            case 5:

                return "e";

            case 6:

                return "h";

            case 7:

                return "l";

            case 8:

                return "m";

            case 9:

                return "p";


            case 10:

                return "r";

            case 11:

                return "s";

            case 12:

                return "t";

            default:

                return "null";

        }

    }

    /**
     * funzione ausiliare per il calcolo del cf
     * @param luogoDiNascita luogo di nascita
     * @param estero true se il luogo è una nazione straniera, false se è una provincia italiana.
     * @return
     */
    private String calcolaCodiceCatastale(String luogoDiNascita, boolean estero){

        String[] codiciIta = new String[8000];
        String[] codiciEsteri = new String[300];

        ////////////////////CARICAMENTO CODICI CATASTALI DAI FILE////////////////////////////
        int i = 0;
        String line;
        InputStream is = getClass().getResourceAsStream("/codici_ita.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            while((line = br.readLine()) != null)  {
                codiciIta[i++] = line;
            }
            br.close();
            isr.close();
            is.close();
        }
        catch(Exception e){

            //
        }

        i = 0;
        is = getClass().getResourceAsStream("/codici_esteri.txt");
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        try {
            while((line = br.readLine()) != null)  {
                codiciEsteri[i++] = line;
            }
            br.close();
            isr.close();
            is.close();
        }
        catch(Exception e){

            //
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(!estero){

            i = 0;
            is = getClass().getResourceAsStream("/comuniPerCodice.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            try {
                while((line = br.readLine()) != null)  {
                    if (line.equals(luogoDiNascita)) break;
                    else i++;
                }
                br.close();
                isr.close();
                is.close();
            }
            catch(Exception e){

                //
            }

            return codiciIta[i];

        } else{

            i = 0;
            is = getClass().getResourceAsStream("/esteriPerCodice.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            try {
                while((line = br.readLine()) != null)  {
                    if (line.equals(luogoDiNascita)) break;
                    else i++;
                }
                br.close();
                isr.close();
                is.close();
            }
            catch(Exception e){

                //
            }

            return codiciEsteri[i];
        }
    }

    /**
     * Funzione ausiliaria per il calcolo del codice fiscale
     * @param parola
     * @return
     */
    private String letterePari(String parola) {

        String pari = "";

        for(int i = 1; i<=parola.length(); i++) {

            if(i%2 == 0) {

                pari = pari + parola.substring(i-1, i);

            }
        }

        return pari;
    }

    /**
     * funzione ausiliaria per il calcolo del codice fiscale
     * @param parola parola
     * @return
     */
    private String lettereDispari(String parola) {

        String dispari = "";

        for(int i = 1; i<=parola.length(); i++) {

            if(i%2 != 0) {

                dispari = dispari + parola.substring(i-1, i);
            }
        }

        return dispari;
    }

    /**
     * funzione ausiliaria per il calcolo del codice fiscale
     * @param lettera lettera
     * @return
     */
    private int convertiPari(String lettera) {

        if(lettera.equals("a") || lettera.equals("0")) return 0;

        else if(lettera.equals("b") || lettera.equals("1")) return 1;

        else if(lettera.equals("c") || lettera.equals("2")) return 2;

        else if(lettera.equals("d") || lettera.equals("3")) return 3;

        else if(lettera.equals("e") || lettera.equals("4")) return 4;

        else if(lettera.equals("f") || lettera.equals("5")) return 5;

        else if(lettera.equals("g") || lettera.equals("6")) return 6;

        else if(lettera.equals("h") || lettera.equals("7")) return 7;

        else if(lettera.equals("i") || lettera.equals("8")) return 8;

        else if(lettera.equals("j") || lettera.equals("9")) return 9;

        else if(lettera.equals("k")) return 10;

        else if(lettera.equals("l")) return 11;

        else if(lettera.equals("m")) return 12;

        else if(lettera.equals("n")) return 13;

        else if(lettera.equals("o")) return 14;

        else if(lettera.equals("p")) return 15;

        else if(lettera.equals("q")) return 16;

        else if(lettera.equals("r")) return 17;

        else if(lettera.equals("s")) return 18;

        else if(lettera.equals("t")) return 19;

        else if(lettera.equals("u")) return 20;

        else if(lettera.equals("v")) return 21;

        else if(lettera.equals("w")) return 22;

        else if(lettera.equals("x")) return 23;

        else if(lettera.equals("y")) return 24;

        else return 25;
    }

    /**
     * funzione ausiliaria per il calcolo del codice fiscale
     * @param lettera lettera
     * @return
     */
    private int convertiDispari(String lettera) {

        if(lettera.equals("a") || lettera.equals("0")) return 1;

        else if(lettera.equals("b") || lettera.equals("1")) return 0;

        else if(lettera.equals("c") || lettera.equals("2")) return 5;

        else if(lettera.equals("d") || lettera.equals("3")) return 7;

        else if(lettera.equals("e") || lettera.equals("4")) return 9;

        else if(lettera.equals("f") || lettera.equals("5")) return 13;

        else if(lettera.equals("g") || lettera.equals("6")) return 15;

        else if(lettera.equals("h") || lettera.equals("7")) return 17;

        else if(lettera.equals("i") || lettera.equals("8")) return 19;

        else if(lettera.equals("j") || lettera.equals("9")) return 21;

        else if(lettera.equals("k")) return 2;

        else if(lettera.equals("l")) return 4;

        else if(lettera.equals("m")) return 18;

        else if(lettera.equals("n")) return 20;

        else if(lettera.equals("o")) return 11;

        else if(lettera.equals("p")) return 3;

        else if(lettera.equals("q")) return 6;

        else if(lettera.equals("r")) return 8;

        else if(lettera.equals("s")) return 12;

        else if(lettera.equals("t")) return 14;

        else if(lettera.equals("u")) return 16;

        else if(lettera.equals("v")) return 10;

        else if(lettera.equals("w")) return 22;

        else if(lettera.equals("x")) return 25;

        else if(lettera.equals("y")) return 24;

        else return 23;
    }

    /**
     * funzione ausiliaria per il calcolo del codice fiscale
     * @param val valore
     * @return
     */
    private String checkDigit(int val) {

        switch (val) {

            case 0:

                return "a";

            case 1:

                return "b";

            case 2:

                return "c";

            case 3:

                return "d";

            case 4:

                return "e";

            case 5:

                return "f";

            case 6:

                return "g";

            case 7:

                return "h";

            case 8:

                return "i";

            case 9:

                return "j";

            case 10:

                return "k";

            case 11:

                return "l";

            case 12:

                return "m";

            case 13:

                return "n";

            case 14:

                return "o";

            case 15:

                return "p";

            case 16:

                return "q";

            case 17:

                return "r";

            case 18:

                return "s";

            case 19:

                return "t";

            case 20:

                return "u";

            case 21:

                return "v";

            case 22:

                return "w";

            case 23:

                return "x";

            case 24:

                return "y";

            case 25:

                return "z";

            default:

                return "0";
        }
    }


}
