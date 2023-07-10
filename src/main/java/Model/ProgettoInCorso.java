package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Progetto in corso.
 */
public class ProgettoInCorso extends Progetto {

    /**
     * Instantiates a new Progetto in corso.
     *
     * @param nome                 the nome
     * @param cup                  the cup
     * @param dataInizio           the data inizio
     * @param laboratoriAssegnati  the laboratori assegnati
     * @param referenteScientifico the referente scientifico
     * @param responsabile         the responsabile
     */
    public ProgettoInCorso(String nome, String cup, Date dataInizio, ArrayList<Laboratorio> laboratoriAssegnati, Senior referenteScientifico, Dirigente responsabile) {
        super(nome, cup, dataInizio, laboratoriAssegnati, referenteScientifico, responsabile);
    }

    /**
     * Add assegnazione laboratorio.
     *
     * @param laboratorio the laboratorio
     * @throws RuntimeException the runtime exception
     */
    public void addAssegnazioneLaboratorio(Laboratorio laboratorio) throws RuntimeException{
        if(laboratoriAssegnati.contains(laboratorio)) {
            throw new RuntimeException("Laboratorio già assegnato al progetto.");
        }
        if(laboratoriAssegnati.size() == 3) {
            throw new RuntimeException("Aggiunzione annullata: numero massimo di laboratori assegnati raggiunto");
        }

        laboratoriAssegnati.add(laboratorio);
    }

    /**
     * Remove assegnazione laboratorio.
     *
     * @param laboratorio the laboratorio
     * @throws RuntimeException the runtime exception
     */
    public void removeAssegnazioneLaboratorio(Laboratorio laboratorio) throws RuntimeException {

        if(laboratoriAssegnati.size() == 1) {
            throw new RuntimeException("Rimozione annullata: il progetto deve essere assegnato ad almeno un laboratorio");
        }
        laboratoriAssegnati.add(laboratorio);
    }
}
