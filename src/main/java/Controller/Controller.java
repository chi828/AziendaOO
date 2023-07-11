package Controller;

import DAO.*;
import ImplementazioniPostgresDAO.*;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The type Controller.
 */
public class Controller {

    private Azienda azienda;

    /**
     * Instantiates a new Controller.
     */
    public Controller() {
        azienda = new Azienda();
        loadModel();
    }

    /**
     * Carica i dati in memoria reperendoli dal database.
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

        impiegatoD = new Date[3];
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

        impiegatoD = new Date[4];
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

        impiegatoD = new Date[4];
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
            if (impiegato.getCf().equals(cf)) {
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

    private ArrayList<Impiegato> getImpiegati() {return azienda.getImpiegati();}

    private ArrayList<Laboratorio> getLaboratori(){return azienda.getLaboratori();}

    private ArrayList<Progetto> getProgetti(){return azienda.getProgetti();}

    //___________________________________________OPERAZIONI SUI DATI__________________________________________

    //Letture

    /**
     * Metodo per passare i dati dei laboratori alle gui in forma di array list di stringhe. Popola gli array list
     * passati per parametro con tali dati.
     *
     * @param nomiLaboratori          the nomi laboratori
     * @param topicLaboratori         the topic laboratori
     * @param responsabiliScientifici the responsabili scientifici
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
     * @param indiceProgetto          the indice progetto
     * @param nomiLaboratori          the nomi laboratori
     * @param topicLaboratori         the topic laboratori
     * @param responsabiliScientifici the responsabili scientifici
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
     * Metodo per passare alle gui i dati dei laboratori assegnati ad un progetto in corso sotto forma di stringhe.
     * Popola gli array list passati per secondo, terzo e quarto parametro con tali dati.
     *
     * @param indiceProgetto          the indice progetto
     * @param nomiLaboratori          the nomi laboratori
     * @param topicLaboratori         the topic laboratori
     * @param responsabiliScientifici the responsabili scientifici
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
     * @param nomiProgetti the nomi progetti
     * @param cupProgetti  the cup progetti
     * @param dateInizio   the date inizio
     * @param responsabili the responsabili
     * @param referenti    the referenti
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
     *      * si stringhe e date. Popola gli array list passati per parametro con tali dati.
     *
     * @param nomiProgetti the nomi progetti
     * @param cupProgetti  the cup progetti
     * @param dateInizio   the date inizio
     * @param dateFine     the date fine
     * @param responsabili the responsabili
     * @param referenti    the referenti
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
     * Gets progetti in corso.
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
     * Gets progetti conclusi.
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
     * @return the impiegati dirigenti
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
     * @return the seniors
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
     * Set all impiegati.
     *
     * @param modelTable the model table
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
     * Set per categoria.
     *
     * @param modelTable the model table
     * @param categoria  the categoria
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
     * Set impiegati per cf.
     *
     * @param modelTable the model table
     * @param cf         the cf
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
     * Set impiegati per sesso.
     *
     * @param modelTable the model table
     * @param sesso      the sesso
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
     * Set impiegati per nome.
     *
     * @param modelTable the model table
     * @param nome       the nome
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
     * Set impiegati per cognome.
     *
     * @param modelTable the model table
     * @param cognome    the cognome
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
     * Set impiegati per data.
     *
     * @param modelTable the model table
     * @param data       the data
     * @param inervallo  the inervallo
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
     * Set impiegati per laboratorio.
     *
     * @param modelTable  the model table
     * @param laboratorio the laboratorio
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
     * Set impiegati per progetto assegnato.
     *
     * @param modelTable the model table
     * @param cup        the cup
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
     * Set modelli info impiegato.
     *
     * @param carriera             the carriera
     * @param labResp              the lab resp
     * @param progRef              the prog ref
     * @param progResp             the prog resp
     * @param progInCorsoAssegnati the prog in corso assegnati
     * @param progInCorsoContr     the prog in corso contr
     * @param progConclusiContr    the prog conclusi contr
     * @param cfImp                the cf imp
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
     * Add laboratorio.
     *
     * @param nome                          the nome
     * @param topic                         the topic
     * @param indiceResponsabileScientifico the indice responsabile scientifico
     * @throws RuntimeException the runtime exception
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
     * Insert lavorare boolean.
     *
     * @param cf  the cf
     * @param cup the cup
     * @param ore the ore
     * @return the boolean
     */
    public boolean insertLavorare(String cf, String cup, int ore){

        //Controllo esistenza progetto
        if(findProgetto(cup) == null){

            JOptionPane.showMessageDialog(null,"Progetto da assegnare inesistente", "Errore", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        //Controllo che il progetto da assegnare sia assegnato allo stesso laboratorio per cui lavora l'impiegato
        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                if(imp.getAfferenza() != null){

                    for(Progetto prog : getProgetti()){

                        if(prog.getCup().toLowerCase().equals(cup.toLowerCase())){

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

        //Inserimento lavorare in DB
        ImpiegatoDAO insertLavorare = new ImpiegatoImplementazionePostgresDAO();
        flag = insertLavorare.insertLavorare(cf,cup,ore);

        //Inserimento in memoria
        if(flag){

            for(Impiegato imp : getImpiegati()){

                if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                    for(Progetto prog : getProgetti()){

                        if(prog.getCup().toLowerCase().equals(cup.toLowerCase()) && prog instanceof ProgettoInCorso){

                            if(imp.getLavoriProgettiAssegnati() != null){

                                imp.getLavoriProgettiAssegnati().add(new Lavorare(imp,(ProgettoInCorso) prog,ore));

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
    public boolean inserisciImpiegato(String nome, String cognome, char sesso, Date dataDiNascita,
                                      String luogoDiNascita, String cf, Date dataDiAssunzione,
                                      String stipendio, boolean dirigente){


        AziendaImplementazionePostgresDAO insertImpiegatoPS = new AziendaImplementazionePostgresDAO();

        boolean checkInserimento;

        ///SALVATAGGIO IN DB
        checkInserimento = insertImpiegatoPS.inserisciImpiegato(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,
                dataDiAssunzione,stipendio,dirigente);

        //Salvataggio in memoria
        String categoria;

        ImpiegatoImplementazionePostgresDAO leggiCategoriaPS = new ImpiegatoImplementazionePostgresDAO();

        categoria = leggiCategoriaPS.getInfoCategoria(dataDiAssunzione);

        if(checkInserimento){

            if(!dirigente){

                if(categoria.equals("Junior")){

                    Junior nuovoImpiegato = new Junior(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,
                            dataDiAssunzione,Float.parseFloat(stipendio),null,null);

                    azienda.getImpiegati().add(nuovoImpiegato);
                }

                if(categoria.equals("Middle")){

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dataDiAssunzione);
                    calendar.add(Calendar.YEAR, 3);
                    Date dataPromozioneMiddle = calendar.getTime();

                    Middle nuovoImpiegato = new Middle(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,
                            dataDiAssunzione,Float.parseFloat(stipendio),null,null, dataPromozioneMiddle);

                    azienda.getImpiegati().add(nuovoImpiegato);
                }

                if(categoria.equals("Senior")){

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dataDiAssunzione);
                    calendar.add(Calendar.YEAR, 3);
                    Date dataPromozioneMiddle = calendar.getTime();

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(dataDiAssunzione);
                    calendar2.add(Calendar.YEAR, 7);
                    Date dataPromozioneSenior = calendar2.getTime();

                    azienda.getImpiegati().add(new Senior(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,dataDiAssunzione,
                            Float.parseFloat(stipendio),null,null,
                            dataPromozioneMiddle,dataPromozioneSenior));

                }

            }
        }

        if(checkInserimento && dirigente){

            if(categoria.equals("Junior")){

                Junior nuovoImpiegato = new Junior(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,
                        dataDiAssunzione,Float.parseFloat(stipendio),null,dataDiAssunzione);

                azienda.getImpiegati().add(nuovoImpiegato);
            }

            if(categoria.equals("Middle")){

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dataDiAssunzione);
                calendar.add(Calendar.YEAR, 3);
                Date dataPromozioneMiddle = calendar.getTime();

                Middle nuovoImpiegato = new Middle(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,
                        dataDiAssunzione,Float.parseFloat(stipendio),null,dataDiAssunzione, dataPromozioneMiddle);

                azienda.getImpiegati().add(nuovoImpiegato);
            }

            if(categoria.equals("Senior")){

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dataDiAssunzione);
                calendar.add(Calendar.YEAR, 3);
                Date dataPromozioneMiddle = calendar.getTime();

                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(dataDiAssunzione);
                calendar2.add(Calendar.YEAR, 7);
                Date dataPromozioneSenior = calendar2.getTime();

                azienda.getImpiegati().add(new Senior(nome,cognome,sesso,dataDiNascita,luogoDiNascita,cf,dataDiAssunzione,
                        Float.parseFloat(stipendio),null,dataDiAssunzione,
                        dataPromozioneMiddle,dataPromozioneSenior));

            }
        }

        return checkInserimento;
    }

    /**
     * Concludi progetto.
     *
     * @param indiceProgetto the progetto
     * @param dataFine the data fine
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
     * Add assegnazione laboratorio progetto.
     *
     * @param indiceProgettoInCorso the indice progetto in corso
     * @param nomeLab               the nome lab
     * @param topicLab              the topic lab
     * @throws RuntimeException the runtime exception
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
     * Rimuovi assegnazione.
     *
     * @param indiceProgettoInCorso the progetto in corso
     * @param indiceLaboratorio     the laboratorio
     * @throws RuntimeException the runtime exception
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
     * Sets referente scientifico.
     *
     * @param indiceProgetto the indice progetto
     * @param cfImpiegato    the cf impiegato
     * @throws RuntimeException the runtime exception
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
     * Sets responsabile.
     *
     * @param indiceProgetto the indice progetto
     * @param cfImpiegato    the cf impiegato
     * @throws RuntimeException the runtime exception
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
     * Sets responsabile scientifico.
     *
     * @param indiceLaboratorio the indice laboratorio
     * @param cfImpiegato       the cf impiegato
     * @throws RuntimeException the runtime exception
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
     * Update afferenza imp boolean.
     *
     * @param cf      the cf
     * @param nomeLab the nome lab
     * @param topic   the topic
     * @return the boolean
     */
    public boolean updateAfferenzaImp(String cf, String nomeLab, String topic) {

        boolean flag = false;

        //Verifica che il laboratorio esiste
       if(findLaboratorio(nomeLab, topic) == null){

           JOptionPane.showMessageDialog(null,"Laboratorio inesistente", "Errore", JOptionPane.ERROR_MESSAGE);

           return false;
       }

        //Verifica che non lavoro per qualche progetto
        for(Impiegato imp : getImpiegati()){

            if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                if(imp.getLavoriProgettiAssegnati() != null && imp.getLavoriProgettiAssegnati().size() > 0){

                    JOptionPane.showMessageDialog(null,"Prima di assegnare un nuovo laboratorio, concludere tutti i lavori", "Errore", JOptionPane.ERROR_MESSAGE);

                    return false;

                }

                break;
            }
        }

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
     * Update ore boolean.
     *
     * @param cf  the cf
     * @param cup the cup
     * @param ore the ore
     * @return the boolean
     */
    public boolean updateOre(String cf, String cup, int ore){

        boolean flag = false;

        //Modifica ore in DB
        LavorareDAO updateOre = new LavorareImplementazionePostgresDAO();
        flag = updateOre.updateOre(cf,cup,ore);

        //Modifica in memoria
        if(flag){

            for(Impiegato imp : getImpiegati()){

                if(imp.getCf().toUpperCase().equals(cf.toUpperCase())){

                    for(Lavorare lav : imp.getLavoriProgettiAssegnati()){

                        if(lav.getProgettoinCorso().getCup().toLowerCase().equals(cup.toLowerCase())){

                            lav.setOre(ore);

                            flag = true;

                            JOptionPane.showMessageDialog(null,"Ore modificate");

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
     * Delete laboratorio.
     *
     * @param indiceLaboratorio the indice laboratorio
     * @throws RuntimeException the runtime exception
     */
//Cancellazioni
    public void deleteLaboratorio(int indiceLaboratorio) throws RuntimeException {
        Laboratorio laboratorio = azienda.getLaboratori().get(indiceLaboratorio);

        //in memoria
        azienda.removeLaboratorio(laboratorio);

        //in database
        AziendaDAO aziendaDAO = new AziendaImplementazionePostgresDAO();
        aziendaDAO.deleteLaboratorio(laboratorio.getTopic(), laboratorio.getNome());
    }

    /**
     * Insert lavoro contribuito boolean.
     *
     * @param cf  the cf
     * @param cup the cup
     * @return the boolean
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
     * Delete impiegato boolean.
     *
     * @param cf the cf
     * @return the boolean
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

    public void removeProgettoInCorso(int indiceProgetto) {

        //in memoria
        ProgettoInCorso progetto = getProgettiInCorso().get(indiceProgetto);

        azienda.removeProgettoInCorso(progetto);

        //in database
        ProgettoInCorsoDAO progettoInCorsoDAO = new ProgettoInCorsoImplementazionePostgresDAO();
        progettoInCorsoDAO.removeProgettoInCorso(progetto.getCup());
    }

    public void removeProgettoConcluso(int indiceProgetto) {

        //in memoria
        ProgettoConcluso progetto = getProgettiConclusi().get(indiceProgetto);

        azienda.removeProgettoConcluso(progetto);
        //in database
        ProgettoConclusoDAO progettoConclusoDAO = new ProgettoConclusoImplementazionePostgresDAO();

        progettoConclusoDAO.removeProgettoConcluso(progetto.getCup());
    }


}
