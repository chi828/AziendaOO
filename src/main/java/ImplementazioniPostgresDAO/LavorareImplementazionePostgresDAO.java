package ImplementazioniPostgresDAO;

import DAO.LavorareDAO;
import Database.ConnessioneDatabase;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The type Lavorare implementazione postgres dao.
 */
public class LavorareImplementazionePostgresDAO implements LavorareDAO {

    private Connection connection;

    /**
     * Instantiates a new Lavorare implementazione postgres dao.
     */
    public LavorareImplementazionePostgresDAO(){

        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateOre(String cf, String cup, int ore) {

        boolean flag = false;

        PreparedStatement updateOrePS;
        try{
            updateOrePS = connection.prepareStatement("UPDATE lavorare SET ore = " + ore + " WHERE cf = '" +
                    cf + "' AND cup = '" + cup + "'");
            updateOrePS.executeUpdate();

            flag = true;

            connection.close();

        }catch (SQLException e){

            flag = false;

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return flag;
    }
}
