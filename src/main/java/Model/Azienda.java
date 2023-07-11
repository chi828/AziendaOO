package Model;

import DAO.ProgettoConclusoDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe rappresentante l'azienda. Contiene <code>ArrayList</code> di impiegati,
 * laboratori e progetti ed i metodi per svolgervici alcune operazioni di base.
 */
public class Azienda {
    private ArrayList<Impiegato> impiegati;
    private ArrayList<Laboratorio> laboratori;
    private ArrayList<Progetto> progetti;

    /**
     * Tutte gli attributi sono inizializzati come ArrayList vuote.
     */
    public Azienda(){
        this.impiegati = new ArrayList<>();
        this.laboratori = new ArrayList<>();
        this.progetti = new ArrayList<>();
    }

    /**
     * Gets impiegati.
     *
     * @return the impiegati
     */
    public ArrayList<Impiegato> getImpiegati() {
        return impiegati;
    }

    /**
     * Get laboratori array list.
     *
     * @return the array list
     */
    public ArrayList<Laboratorio> getLaboratori(){
        return laboratori;
    }

    /**
     * Get progetti array list.
     *
     * @return the array list
     */
    public ArrayList<Progetto> getProgetti(){
        return progetti;
    }

    /**
     * Seleziona e fornisce, dalla lista di progetti contenuta nella classe, solo quelli in corso.
     *
     * @return ArrayList contenente tutti i progetti in corso presenti nell'ogetto Azienda.
     * @see ProgettoInCorso
     */
    public ArrayList<ProgettoInCorso> getProgettiInCorso() {
        ArrayList<ProgettoInCorso> result = new ArrayList<>();
        for (Progetto progetto : progetti
             ) {
            if(!progetto.isConcluso()) {
                result.add((ProgettoInCorso) progetto);
            }
        }
        return result;
    }

    /**
     * Seleziona e fornisce, dalla lista di progetti contenuta nella classe, solo conclusi.
     *
     * @return ArrayList contenente tutti i progetti conclusi presenti nell'ogetto Azienda.
     * @see ProgettoConcluso
     */
    public ArrayList<ProgettoConcluso> getProgettiConclusi() {
        ArrayList<ProgettoConcluso> result = new ArrayList<>();
        for (Progetto progetto : progetti
        ) {
            if(progetto.isConcluso()) {
                result.add((ProgettoConcluso) progetto);
            }
        }
        return result;
    }

    /**
     * Reperisce tutti gli impiegati che ricoprono il ruolo di dirigente.
     *
     * @return ArrayList di impiegati con ruolo di dirigente
     */
    public ArrayList<Impiegato> getDirigenti() {
        ArrayList<Impiegato> result = new ArrayList<>();
        for (Impiegato impiegato: getImpiegati()) {
            if(impiegato.getDirigente() != null) {
                result.add(impiegato);
            }
        }
        return result;
    }

    /**
     * Recupera dall'array list impiegato, solo gli impiegati appartenenti alla categoria senior.
     *
     * @return the seniors
     */
    public ArrayList<Senior> getSeniors() {
        ArrayList<Senior> result = new ArrayList<>();
        for (Impiegato impiegato: getImpiegati()) {
            if(impiegato.getCategoria().equals("senior")) {
                result.add((Senior) impiegato);
            }
        }
        return result;
    }

    /**
     * Se non esistono altri impiegati con lo stesso codice fiscale in azienda, inserisce il nuovo impiegato, altrimenti
     * impedisce l'inserimento.
     *
     * @param impiegato the impiegato
     */
    public void addImpiegato(Impiegato impiegato) throws RuntimeException{
        for (Impiegato imp: impiegati
             ) {
            if(imp.getCf().equals(impiegato.getCf())) {
                throw new RuntimeException("Esiste già un impegato con questo codice fiscale in azienda.");
            }
        }
        {
            this.impiegati.add(impiegato);
        }
    }

    /**
     * Se non esistono laboratori con la stessa coppia di topic e nome, inserisce il nuovo laboratorio in azienda,
     * altimenti impdisce l'inserimento.
     *
     * @param laboratorio the laboratorio
     */
    public void addLaboratorio(Laboratorio laboratorio) throws RuntimeException {
        for (Laboratorio lab: laboratori
             ) {
            if(laboratorio.getNome().equals(lab.getNome()) && laboratorio.getTopic().equals(lab.getTopic())) {
                throw new RuntimeException("Il laboratorio è già presente nell'azienda.");
            }
        }
        this.laboratori.add(laboratorio);
    }

    /**
     * Se non esistono in azienda progetti con lo stesso nome o lo stesso cup, inserisce il progetto in azienda,
     * altrimenti annulla l'inserimento
     *
     * @param progetto Il progetto da aggiungere
     */
    public void addProgetti(Progetto progetto) throws RuntimeException {
        for (Progetto prog: progetti
             ) {
            if(prog.getCup().equals(progetto.getCup()) || prog.getNome().equals(progetto.getNome())) {
                throw new RuntimeException("Nome o cup del progetto già esistente nell'azienda.");
            }
        }
        this.progetti.add(progetto);
    }

    /**
     * Rimuove l'impiegato dall'azienda
     *
     * @param impiegato impiegato da eliminare.
     */
    public void removeImpiegato(Impiegato impiegato) {
        impiegati.remove(impiegato);
    }

    /**
     * Se non esistono progetti assegnati al laboratorio o impiegati che vi afferiscono, elimina dall'azienda il
     * laboratorio passato per parametro, altrimenti annulla l'eliminazione.
     *
     * @param laboratorio   Il laboratorio da eliminare
     */
    public void removeLaboratorio(Laboratorio laboratorio) {

        ArrayList<ProgettoConcluso> assegnazioniDaEliminare = new ArrayList<>();

        for (Progetto progetto: progetti
             ) {

            if(progetto.getLaboratoriAssegnati().contains(laboratorio)) {
                if(progetto.isConcluso()){
                    assegnazioniDaEliminare.add((ProgettoConcluso) progetto);
                } else {
                    throw new RuntimeException("Eliminazione annullata. Ci sono progetti in corso " +
                            "assegnati al laboratorio");
                }
            }
        }

        for (ProgettoConcluso progetto: assegnazioniDaEliminare
             ) {
            progetto.getLaboratoriAssegnati().remove(laboratorio);
        }

        for (Impiegato impiegato: impiegati) {
            if(impiegato.getAfferenza() != null){
                if (impiegato.getAfferenza().equals(laboratorio)) {
                    throw new RuntimeException("Eliminazione annullata. Ci sono impiegati che afferiscono al laboratorio");
                }
            }
        }

        laboratori.remove(laboratorio);
    }


    /**
     * Verifica che ogni impiegato in azienda appartenza alla classe di specializzazione corretta
     * tra Junior, Middle o Senior. Effettua uno scatto di carriera se trova un impiegato che ha
     * accumulato gli anni di carriera necessari ma non appartiene alla categoria corretta.
     * Con scatto di carriera si intende una rimozione dell'ogetto e riscrittura come ogetto della
     * classe Middle (se dalla data, valore dell'attributo dataDiAssunzione dell'impiegato è passato
     * un periodo di tempo compreso fra 3 e 7 anni) o Senior (se sono passati oltre 7 anni da
     * dataDiAssunzione)
     */
    public void aggiornaCategorieImpiegati() {
        for (Impiegato impiegato: impiegati
             ) {
            Date date = impiegato.getDataDiAssunzione();
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(date);
            Calendar passaggioMiddle = Calendar.getInstance();
            passaggioMiddle.setTime(date);
            passaggioMiddle.add(Calendar.YEAR, 3);
            Calendar passaggioSenior = Calendar.getInstance();
            passaggioSenior.setTime(date);
            passaggioSenior.add(Calendar.YEAR, 7);
             if(currentDate.after(passaggioSenior)) {
                 if(!impiegato.getCategoria().equals("senior")) {
                     Senior nuovaCategoria;
                     if(impiegato.getDirigente() != null) {
                          nuovaCategoria = new Senior(impiegato.getNome(), impiegato.getCognome(),
                                 impiegato.getSesso(), impiegato.getDataDiNascita(), impiegato.getLuogoDiNascita(),
                                 impiegato.getCf(), impiegato.getDataDiAssunzione(), impiegato.getStipendio(),
                                 impiegato.getAfferenza(), impiegato.getDirigente().getPassaggioDirigente(),
                                 passaggioMiddle.getTime(), passaggioSenior.getTime());
                         for (Progetto progetto: progetti
                              ) {
                             if(progetto.getResponsabile().equals(impiegato.getDirigente())) {
                                 progetto.setResponsabile(nuovaCategoria.getDirigente());
                             }
                         }
                     } else {
                          nuovaCategoria = new Senior(impiegato.getNome(), impiegato.getCognome(),
                                 impiegato.getSesso(), impiegato.getDataDiNascita(), impiegato.getLuogoDiNascita(),
                                 impiegato.getCf(), impiegato.getDataDiAssunzione(), impiegato.getStipendio(),
                                 impiegato.getAfferenza(), passaggioMiddle.getTime(), passaggioSenior.getTime());
                     }
                     ArrayList<Lavorare> lavoriImpiegato = new ArrayList<Lavorare>();
                     for (Lavorare lavorare: impiegato.getLavoriProgettiAssegnati()
                          ) {
                         lavoriImpiegato.add(new Lavorare(nuovaCategoria, lavorare.getProgettoinCorso(),
                                 lavorare.getOre()));
                     }
                     nuovaCategoria.setLavoriProgettiAssegnati(lavoriImpiegato);
                     nuovaCategoria.setContributiPassati(impiegato.getContributiPassati());
                 }
             } else if(currentDate.after(passaggioMiddle)) {
                 if(impiegato.getCategoria().equals("middle")) {
                     Middle nuovaCategoria;
                     if(impiegato.getDirigente() != null) {
                         nuovaCategoria = new Middle(impiegato.getNome(), impiegato.getCognome(),
                                 impiegato.getSesso(), impiegato.getDataDiNascita(), impiegato.getLuogoDiNascita(),
                                 impiegato.getCf(), impiegato.getDataDiAssunzione(), impiegato.getStipendio(),
                                 impiegato.getAfferenza(), impiegato.getDirigente().getPassaggioDirigente(),
                                 passaggioMiddle.getTime());
                         for (Progetto progetto: progetti
                         ) {
                             if(progetto.getResponsabile().equals(impiegato.getDirigente())) {
                                 progetto.setResponsabile(nuovaCategoria.getDirigente());
                             }
                         }
                     } else {
                         nuovaCategoria = new Middle(impiegato.getNome(), impiegato.getCognome(),
                                 impiegato.getSesso(), impiegato.getDataDiNascita(), impiegato.getLuogoDiNascita(),
                                 impiegato.getCf(), impiegato.getDataDiAssunzione(), impiegato.getStipendio(),
                                 impiegato.getAfferenza(), passaggioMiddle.getTime());
                     }
                     ArrayList<Lavorare> lavoriImpiegato = new ArrayList<>();
                     for (Lavorare lavorare: impiegato.getLavoriProgettiAssegnati()
                     ) {
                         lavoriImpiegato.add(new Lavorare(nuovaCategoria, lavorare.getProgettoinCorso(),
                                 lavorare.getOre()));
                     }
                     nuovaCategoria.setLavoriProgettiAssegnati(lavoriImpiegato);
                     nuovaCategoria.setContributiPassati(impiegato.getContributiPassati());
                 }
            }
        }
    }

    /**
     * Elimina il progetto in corso passatogli come parametro lo riscrive come progetto concluso,
     * con la dataDine specificata al secondo parametro omonimo. Ad ogni rappresentazione del
     * progetto in un ogetto lavorare, elimina tale ogetto lavorare ed inserisce il progettoConcluso
     * come contributo passato. Inoltre se il progetto (progettoInCorso) è contenuto nell'ArrayList
     * contributiPassati di un impiegato, lo rimuove ed aggiunge invece il progettoConcluso.
     *
     * @param progetto progettoInCorso
     * @param dataFine the data fine
     * @return Oggetto della classe ProgettoConcluso che sostituisce il prarametro ProgettoInCorso
     */
    public ProgettoConcluso concludiProgetto(ProgettoInCorso progetto, Date dataFine) {
        //Rimozione progetto in corso dall'azienda
        progetti.remove(progetto);
        //Crazione progetto concluso
        ProgettoConcluso progettoConcluso = new ProgettoConcluso(progetto.getNome(), progetto.getCup(), progetto.getDataInizio(),
                progetto.getLaboratoriAssegnati(), progetto.getReferenteScientifico(), progetto.getResponsabile(),
                dataFine);

        for (Impiegato impiegato: impiegati
             ) {
            //Eliminazione di lavorare e reinserimento come contributi passati della nuova rappresentazione del progetto
            ArrayList<Lavorare> daEliminare = new ArrayList<>();
            for (Lavorare lavorare: impiegato.getLavoriProgettiAssegnati()
                 ) {
                if(lavorare.getProgettoinCorso().equals(progetto)) {
                    daEliminare.add(lavorare);
                }
            }
            for (Lavorare lavorare: daEliminare
                 ) {
                impiegato.getLavoriProgettiAssegnati().remove(lavorare);
                impiegato.addContributoPassato(progettoConcluso);
            }
            //Eliminazione contributi passati relativi a progetto e reinserimento come progettoInCorso
            if(impiegato.getContributiPassati().contains(progetto)) {
                impiegato.getContributiPassati().remove(progetto);
                impiegato.addContributoPassato(progettoConcluso);
            }
        }
        addProgetti(progettoConcluso);
        return progettoConcluso;
    }

    /**
     * Elimina dell'azienda il progetto che riceve per parametro e i contributi passati e lavorare che vi si riferiscono.
     * @param progetto progetto da eliminare.
     */
    public void removeProgettoInCorso(ProgettoInCorso progetto) {
            for (Impiegato impiegato: impiegati
                 ) {
                ArrayList<Lavorare> lavoriDaEliminare = new ArrayList<>();
                for (Lavorare lavorare: impiegato.getLavoriProgettiAssegnati()
                     ) {
                    if(lavorare.getProgettoinCorso().equals(progetto)) {
                        lavoriDaEliminare.add(lavorare);
                    }
                }
                for (Lavorare lavorare: lavoriDaEliminare
                     ) {
                    impiegato.getLavoriProgettiAssegnati().remove(lavorare);
                }
                if (impiegato.getContributiPassati().contains(progetto)) {
                    impiegato.getContributiPassati().remove(progetto);
                }
            }

        progetti.remove(progetto);
    }

    /**
     * Elimina dell'azienda il progetto che riceve per parametro e i contributi passati e lavorare che vi si riferiscono.
     * @param progetto progetto da eliminare.
     */
    public void removeProgettoConcluso(ProgettoConcluso progetto) {
        for (Impiegato impiegato: impiegati
             ) {
            if (impiegato.getContributiPassati().contains(progetto)) {
                impiegato.getContributiPassati().remove(progetto);
            }
        }
        progetti.remove(progetto);
    }

}
