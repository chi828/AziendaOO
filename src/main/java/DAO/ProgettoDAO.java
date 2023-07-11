package DAO;

import java.util.ArrayList;

/**
 * The interface Progetto dao.
 */
public interface ProgettoDAO {
    /**
     * Recupera dal database i laboratori assegnati ad un progetto
     *
     * @param cup        cup del progetto
     * @param laboratori laboratori assegnati [nome, topic]
     */
    public void readAssegnazioni(String cup, ArrayList<String[]> laboratori);
}
