package DAO;

/**
 * The interface Progetto in corso dao.
 */
public interface ProgettoInCorsoDAO {
    /**
     * Conclude un progetto in corso
     *
     * @param cup                  cup del progetto da concludere
     * @param nome                 nome del progetto da concludere
     * @param dataInizio           data inizio del progetto da concludere
     * @param dataFine             data fine del progetto da concludere
     * @param referenteScientifico referente scientifico del progetto da concludere
     * @param responsabile         responsabile del progetto da concludere
     */
    public void concludiProgetto(String cup, String nome, String dataInizio, String dataFine,
                                 String referenteScientifico, String responsabile);

    /**
     * Aggiunge in database un laboratorio assegnato ad un progetto.
     *
     * @param cup              cup del progetto a cui assegnare un nuovo laboratorio.
     * @param nomeLaboratorio  nome del laboratorio che verrà assegnato al progetto.
     * @param topicLaboratorio topic del laboratorio che verrà assegnato al progetto.
     */
    public void addAssegnazione(String cup, String nomeLaboratorio, String topicLaboratorio);

    /**
     * Rimuove in database un progetto assegnato ad un laboratorio.
     *
     * @param cup              cup del progetto a cui rimuovere l'assegnazione.
     * @param nomeLaboratorio  nome del laboratorio di cui rimuovere l'assegnazione.
     * @param topicLaboratorio topic del laboratorio di cui rimuovere l'assegnazione.
     */
    public void removeAssegnazione(String cup, String nomeLaboratorio, String topicLaboratorio);

    /**
     * Riassegna in database un referente scientifico di un progetto.
     *
     * @param cup cup del progetto.
     * @param cf  cf del nuovo referente scientifico.
     */
    public void setReferenteScientifico(String cup, String cf);

    /**
     * Riassegna in database un responsabile di un progetto.
     *
     * @param cup cup del progetto
     * @param cf  cf del nuovo responsabile
     */
    void setResponsabile(String cup, String cf);

    /**
     * Elimina un progetto in corso dal database.
     * @param cup progetto da eliminare.
     */
    public void removeProgettoInCorso(String cup);
}
