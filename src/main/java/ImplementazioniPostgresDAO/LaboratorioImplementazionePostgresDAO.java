package ImplementazioniPostgresDAO;

import DAO.LaboratorioDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The type Laboratorio implementazione postgres dao.
 */
public class LaboratorioImplementazionePostgresDAO implements LaboratorioDAO {
    private Connection connection;

    /**
     * Instantiates a new Laboratorio implementazione postgres dao.
     */
    public LaboratorioImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setResponsabileScientifico(String nome, String topic, String cf) {
        PreparedStatement setResponsabileScientifico;
        try {
            setResponsabileScientifico = connection.prepareStatement(
                    "UPDATE laboratorio SET responsabile_scientifico = '" + cf + "' WHERE topic = '"
                            + topic + "' AND nome = '" + nome + "'"
            );
            setResponsabileScientifico.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
