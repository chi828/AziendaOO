package ImplementazioniPostgresDAO;

import DAO.ProgettoDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The type Progetto implementazione postgres dao.
 */
public class ProgettoImplementazionePostgresDAO implements ProgettoDAO {
    private Connection connection;

    /**
     * Instantiates a new Progetto implementazione postgres dao.
     */
    public ProgettoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void readAssegnazioni(String cup, ArrayList<String[]> laboratori) {
        PreparedStatement leggiLavorarePS;
        try {
            leggiLavorarePS = connection.prepareStatement(
                    "SELECT * FROM assegnazione WHERE cup = '" + cup + "'" +
                    "UNION SELECT * FROM assegnazione_conclusa WHERE cup = '" + cup + "'");
            ResultSet rs = leggiLavorarePS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];

                laboratorio[0] = rs.getString("nome");
                laboratorio[1] = rs.getString("topic");
                laboratori.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
