package DAO;

import java.util.ArrayList;
import java.util.Date;

/**
 * The interface Progetto dao.
 */
public interface ProgettoDAO {
    /**
     * Read assegnazioni.
     *
     * @param cup        the cup
     * @param laboratori the laboratori
     */
    public void readAssegnazioni(String cup, ArrayList<String[]> laboratori);
}
