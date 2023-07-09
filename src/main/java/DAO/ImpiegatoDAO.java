package DAO;

import java.util.ArrayList;

/**
 * The interface Impiegato dao.
 */
public interface ImpiegatoDAO {
    /**
     * Read contributi passati.
     *
     * @param CF       the cf
     * @param progetti the progetti
     */
    public void readContributiPassati(String CF, ArrayList<String> progetti);

    /**
     * Read lavorare.
     *
     * @param cf       the cf
     * @param ore      the ore
     * @param progetti the progetti
     */
    public void readLavorare(String cf, ArrayList<Integer> ore, ArrayList<String> progetti);

    /**
     * Update afferenza boolean.
     *
     * @param cf      the cf
     * @param nomeLab the nome lab
     * @param topic   the topic
     * @return the boolean
     */
    public boolean updateAfferenza(String cf, String nomeLab, String topic);

    /**
     * Insert lavorare boolean.
     *
     * @param cf  the cf
     * @param cup the cup
     * @param ore the ore
     * @return the boolean
     */
    public boolean insertLavorare(String cf, String cup, int ore);

    /**
     * Insert contributo passato boolean.
     *
     * @param cf  the cf
     * @param cup the cup
     * @return the boolean
     */
    public boolean insertContributoPassato(String cf, String cup);

    /**
     * Gets info categoria.
     *
     * @param dataAssunzione the data assunzione
     * @return the info categoria
     */
    public String getInfoCategoria(java.util.Date dataAssunzione);
}
