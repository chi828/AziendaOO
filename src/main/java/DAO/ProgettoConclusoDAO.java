package DAO;

public interface ProgettoConclusoDAO {

    /**
     * Elimina un progetto concluso dal database.
     * @param cup cup del progetto da eliminare.
     */
    public void removeProgettoConcluso(String cup);
}
