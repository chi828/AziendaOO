package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Progetto concluso.
 */
public class ProgettoConcluso extends Progetto{

    private Date dataFine;

    /**
     * Instantiates a new Progetto concluso.
     *
     * @param nome                 the nome
     * @param cup                  the cup
     * @param dataInizio           the data inizio
     * @param laboratoriAssociati  the laboratori associati
     * @param referenteScientifico the referente scientifico
     * @param responsabile         the responsabile
     * @param dataFine             the data fine
     */
    public ProgettoConcluso(String nome, String cup, Date dataInizio, ArrayList<Laboratorio> laboratoriAssociati, Senior referenteScientifico,
                            Dirigente responsabile, Date dataFine) {
        super(nome, cup, dataInizio, laboratoriAssociati, referenteScientifico, responsabile);
        this.dataFine = dataFine;
    }

    /**
     * Get data fine date.
     *
     * @return the date
     */
    public Date getDataFine(){
        return dataFine;
    }

    /**
     * Set data fine date.
     *
     * @param dataFine the data fine
     * @return the date
     */
    public Date setDataFine(Date dataFine){
        return dataFine;
    }

    @Override
    public Boolean isConcluso() {
        return true;
    }
}
