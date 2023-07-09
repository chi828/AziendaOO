package ImplementazioniPostgresDAO;

import DAO.ImpiegatoDAO;
import Database.ConnessioneDatabase;
import Model.Junior;
import Model.Middle;
import Model.Senior;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The type Impiegato implementazione postgres dao.
 */
public class ImpiegatoImplementazionePostgresDAO implements ImpiegatoDAO {

    private Connection connection;

    /**
     * Instantiates a new Impiegato implementazione postgres dao.
     */
    public ImpiegatoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readContributiPassati(String cf, ArrayList<String> progetti) {
        PreparedStatement leggiContributiPassatiPS;
        try {
            leggiContributiPassatiPS = connection.prepareStatement(
                    "SELECT cup FROM contributo_passato WHERE cf = '" + cf + "'" +
                            " UNION " + "SELECT cup FROM contributo_passato_concluso WHERE cf = '" +  cf + "'");
            ResultSet rs = leggiContributiPassatiPS.executeQuery();
            while (rs.next()) {
                progetti.add(rs.getString("cup"));
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readLavorare(String cf, ArrayList<Integer> ore, ArrayList<String> progetti){
        PreparedStatement leggiLavorarePS;
        try {
            leggiLavorarePS = connection.prepareStatement(
                    "SELECT * FROM lavorare WHERE cf = '" + cf + "'");
            ResultSet rs = leggiLavorarePS.executeQuery();
            while (rs.next()) {
                ore.add(rs.getInt("ore"));
                progetti.add(rs.getString("cup"));
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateAfferenza(String cf, String nomeLab, String topicLab){

        boolean flag = false;

        PreparedStatement updateAffPS;
        try {

            updateAffPS = connection.prepareStatement("UPDATE impiegato SET nome_lab = '" + nomeLab + "',topic_lab = '" +
                    topicLab + "' WHERE cf = '" + cf + "'");
            updateAffPS.executeUpdate();

            flag = true;

            connection.close();
        }catch (SQLException e){

            flag = false;

            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return flag;
    }

    public boolean insertLavorare(String cf, String cup, int ore){

        boolean flag = false;

        PreparedStatement insertLavorarePS;
        try{
            insertLavorarePS = connection.prepareStatement("INSERT INTO lavorare VALUES('" + cf + "','" +
                    cup + "'," + ore + ")");
            insertLavorarePS.executeUpdate();

            flag = true;

            connection.close();
        }catch (SQLException e){

            JOptionPane.showMessageDialog(null,e.getMessage());

            flag = false;
        }

        return flag;
    }

    public boolean insertContributoPassato(String cf, String cup){

        boolean flag = false;

        PreparedStatement insertContributoPS;
        try{
            insertContributoPS = connection.prepareStatement("INSERT INTO contributo_passato VALUES('" +
                    cf + "','" + cup + "')");

            insertContributoPS.executeUpdate();

            flag = true;

        }catch (SQLException e){

            flag = false;

            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return flag;
    }

    public String getInfoCategoria(java.util.Date dataAssunzione) {

        String query = "{ ? = CALL infocategoria(?) }";

        java.util.Date utilDate = dataAssunzione;
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        try{

            CallableStatement statement = connection.prepareCall(query);

            statement.registerOutParameter(1, Types.VARCHAR);
            statement.setDate(2, sqlDate);

            statement.execute();

            String returnValue = statement.getString(1);

            connection.close();

            return returnValue;


        }catch(SQLException e){

            e.printStackTrace();

            return null;
        }

    }
}
