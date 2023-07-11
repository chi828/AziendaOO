package DAO;

/**
 * The interface Progetto in corso dao.
 */
public interface ProgettoInCorsoDAO {
    /**
     * Concludi progetto.
     *
     * @param cup                  the cup
     * @param nome                 the nome
     * @param dataInizio           the data inizio
     * @param dataFine             the data fine
     * @param referenteScientifico the referente scientifico
     * @param responsabile         the responsabile
     */
    public void concludiProgetto(String cup, String nome, String dataInizio, String dataFine,
                                 String referenteScientifico, String responsabile);

    /**
     * Add assegnazione.
     *
     * @param cup              the cup
     * @param nomeLaboratorio  the nome laboratorio
     * @param topicLaboratorio the topic laboratorio
     */
    public void addAssegnazione(String cup, String nomeLaboratorio, String topicLaboratorio);

    /**
     * Remove assegnazione.
     *
     * @param cup              the cup
     * @param nomeLaboratorio  the nome laboratorio
     * @param topicLaboratorio the topic laboratorio
     */
    public void removeAssegnazione(String cup, String nomeLaboratorio, String topicLaboratorio);

    /**
     * Sets referente scientifico.
     *
     * @param cup the cup
     * @param cf  the cf
     */
    public void setReferenteScientifico(String cup, String cf);

    /**
     * Sets responsabile.
     *
     * @param cup the cup
     * @param cf  the cf
     */
    void setResponsabile(String cup, String cf);

    /**
     * Elimina un progetto in corso dal database.
     * @param cup progetto da eliminare.
     */
    public void removeProgettoInCorso(String cup);
}
