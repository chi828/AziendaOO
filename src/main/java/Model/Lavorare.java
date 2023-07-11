package Model;

/**
 * The type Lavorare. Associazione fra Impiegato e Progetto. Rappresenta il lavoro che è assegnato ad un
 * impiegato su uno specifico progetto.
 */
public class Lavorare {

    private int ore;
    private Impiegato impiegato;
    private ProgettoInCorso progetto;

    /**
     * Instantiates a new Lavorare.
     *
     * @param impiegato L'impiegato che lavora al progetto.
     * @param progetto  Il progetto su cui l'impiegato lavora.
     * @param ore       Rappresenta il numero di ore settimanali che l'impiegato ha assegnate al progetto.
     */
    public Lavorare(Impiegato impiegato, ProgettoInCorso progetto, int ore){
        this.impiegato = impiegato;
        this.progetto = progetto;
        this.ore = ore;
    }

    /**
     * Gets impiegato.
     *
     * @return the impiegato
     */
    public Impiegato getImpiegato() {
        return impiegato;
    }

    /**
     * Get progettoin corso progetto in corso.
     *
     * @return the progetto in corso
     */
    public ProgettoInCorso getProgettoinCorso(){
        return progetto;
    }

    /**
     * Get ore int.
     *
     * @return the int
     */
    public int getOre(){
        return ore;
    }

    /**
     * Set ore.
     *
     * @param ore the ore
     */
    public void setOre(int ore){
        this.ore = ore;
    }

}
