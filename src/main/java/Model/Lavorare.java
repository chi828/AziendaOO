package Model;

/**
 * The type Lavorare.
 */
public class Lavorare {

    private int ore;
    private Impiegato impiegato;
    private ProgettoInCorso progetto;

    /**
     * Instantiates a new Lavorare.
     *
     * @param impiegato the impiegato
     * @param progetto  the progetto
     * @param ore       the ore
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
     * Set impiegato.
     *
     * @param impiegato the impiegato
     */
    public void setImpiegato(Impiegato impiegato){
        this.impiegato = impiegato;
    }

    /**
     * Set progetto.
     *
     * @param progetto the progetto
     */
    public void setProgetto(ProgettoInCorso progetto){
        this.progetto = progetto;
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
