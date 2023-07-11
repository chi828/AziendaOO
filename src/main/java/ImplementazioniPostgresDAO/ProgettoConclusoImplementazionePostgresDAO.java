package ImplementazioniPostgresDAO;

import DAO.ProgettoConclusoDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProgettoConclusoImplementazionePostgresDAO implements ProgettoConclusoDAO {

    private Connection connection;

    /**
     * Instantiates a new Progetto concluso implementazione postgres dao.
     */
    public ProgettoConclusoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Elimina un progetto concluso dal database di postgres.
     * @param cup cup del progetto da eliminare.
     */
    public void removeProgettoConcluso(String cup) {
        PreparedStatement removeProgettoConcluso;
        try {
            removeProgettoConcluso = connection.prepareStatement(
                    "DELETE FROM progetto_concluso WHERE cup = '" + cup + "'"
            );
            removeProgettoConcluso.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
