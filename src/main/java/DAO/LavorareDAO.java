package DAO;

/**
 * The interface Lavorare dao.
 */
public interface LavorareDAO {

    /**
     * Update ore boolean.
     *
     * @param cf  the cf
     * @param cup the cup
     * @param ore the ore
     * @return the boolean
     */
    public boolean updateOre(String cf, String cup, int ore);

}
