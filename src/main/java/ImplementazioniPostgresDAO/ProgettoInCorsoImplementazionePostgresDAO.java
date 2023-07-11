package ImplementazioniPostgresDAO;

import DAO.ProgettoInCorsoDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The type Progetto in corso implementazione postgres dao.
 */
public class ProgettoInCorsoImplementazionePostgresDAO implements ProgettoInCorsoDAO {

    private Connection connection;

    /**
     * Instantiates a new Progetto in corso implementazione postgres dao.
     */
    public ProgettoInCorsoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void concludiProgetto(String cup, String nome, String dataInizio, String dataFine,
                                 String referenteScientifico, String responsabile) {
        PreparedStatement concludiProgetto;
        try {
            concludiProgetto = connection.prepareStatement(
                    "INSERT INTO progetto_concluso VALUES ('" + cup + "','" + nome + "','" +
                            dataInizio + "','" + dataFine  + "','" + referenteScientifico +  "','"
                            + responsabile + "')");
            //I laboratori assegnati sono spostati automaticamente dal database
            concludiProgetto.executeUpdate();
            connection.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void addAssegnazione(String cup, String nomeLaboratorio, String topicLaboratorio) {
        PreparedStatement addAssegnazione;
        try {
            addAssegnazione = connection.prepareStatement(
                    "INSERT INTO assegnazione VALUES ('" + topicLaboratorio + "','" + nomeLaboratorio
                            + "','" + cup + "')"
            );
            addAssegnazione.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeAssegnazione(String cup, String nomeLaboratorio, String topicLaboratorio) {
        PreparedStatement removeAssegnazione;
        try {
            removeAssegnazione = connection.prepareStatement(
                    "DELETE FROM assegnazione WHERE cup = '" + cup + "' AND nome = '" + nomeLaboratorio +
                            "' AND topic ='" + topicLaboratorio + "'"
            );
            removeAssegnazione.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setReferenteScientifico(String cup, String cf) {
        PreparedStatement setReferenteScientifico;
        try {
            setReferenteScientifico = connection.prepareStatement(
                    "UPDATE progetto_in_corso SET referente_scientifico = '" + cf + "' WHERE cup = '"
                    + cup + "'"
            );
            setReferenteScientifico.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setResponsabile(String cup, String cf) {
        PreparedStatement setResponsabile;
        try {
            setResponsabile = connection.prepareStatement(
                    "UPDATE progetto_in_corso SET responsabile = '" + cf + "' WHERE cup = '"
                            + cup + "'"
            );
            setResponsabile.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Elimina un progetto in corso dal database di postgres.
     * @param cup cup del progetto da eliminare.
     */
    public void removeProgettoInCorso(String cup) {
        PreparedStatement removeProgettoInCorso;
        try {
            removeProgettoInCorso = connection.prepareStatement(
                    "DELETE FROM progetto_in_corso WHERE cup = '" + cup + "'"
            );
            removeProgettoInCorso.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
