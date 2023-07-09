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
}
