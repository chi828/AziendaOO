package DAO;

/**
 * The interface Lavorare dao.
 */
public interface LavorareDAO {

    /**
     * Modifica in database le ore di lavoro su un progetto assegnate ad un impiegato.
     *
     * @param cf  the cf
     * @param cup the cup
     * @param ore the ore
     * @return the boolean
     */
    public boolean updateOre(String cf, String cup, int ore);

}
