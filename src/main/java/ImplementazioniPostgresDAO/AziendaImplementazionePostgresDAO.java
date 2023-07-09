package ImplementazioniPostgresDAO;

import DAO.AziendaDAO;
import Database.ConnessioneDatabase;
import Model.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Azienda implementazione postgres dao.
 */
public class AziendaImplementazionePostgresDAO implements AziendaDAO {

    private Connection connection;

    /**
     * Instantiates a new Azienda implementazione postgres dao.
     */
    public AziendaImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCategorie() {
        PreparedStatement updateCategoriePS;
        try {
            updateCategoriePS = connection.prepareStatement(
                    "CALL updateCategoria()"
            );
            updateCategoriePS.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAllJuniorNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate,
                                          ArrayList<String[]> afferenze, ArrayList<Float> stipendi) {
        PreparedStatement leggiJuniorNonDirigentiPS;

        try {
            leggiJuniorNonDirigentiPS = connection.prepareStatement(
                    "SELECT * FROM impiegato NATURAL JOIN junior WHERE cf NOT IN (SELECT cf FROM dirigente)");
            ResultSet rs = leggiJuniorNonDirigentiPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];//nome lab, topic lab
                String[] impiegatoString = new String[5]; //nome, conome, sesso, luogo di nascita, CF
                Date[] impiegatoDate = new Date[2];//data di nascita, data assunzione

                impiegatoString[0] = rs.getString("nome");
                impiegatoString[1] = rs.getString("cognome");
                impiegatoString[2] = rs.getString("sesso");
                impiegatoString[3] = rs.getString("luogo_di_nascita");
                impiegatoString[4] = rs.getString("cf");
                impiegatiString.add(impiegatoString);

                impiegatoDate[0] = rs.getDate("data_di_nascita");
                impiegatoDate[1] = rs.getDate("data_assunzione");
                impiegatiDate.add(impiegatoDate);

                stipendi.add(rs.getFloat("stipendio"));

                laboratorio[0] = rs.getString("nome_lab");
                laboratorio[1] = rs.getString("topic_lab");
                afferenze.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAllJuniorDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi) {

            PreparedStatement leggiJuniorDirigentiPS;
        try {
            leggiJuniorDirigentiPS = connection.prepareStatement(
                    "SELECT * FROM impiegato NATURAL JOIN junior NATURAL JOIN dirigente");
            ResultSet rs = leggiJuniorDirigentiPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];//nome lab, topic lab
                String[] impiegatoString = new String[5]; //nome, conome, sesso, luogo di nascita, CF
                Date[] impiegatoDate = new Date[3];//data di nascita, data assunzione, passaggio dirigente

                impiegatoString[0] = rs.getString("nome");
                impiegatoString[1] = rs.getString("cognome");
                impiegatoString[2] = rs.getString("sesso");
                impiegatoString[3] = rs.getString("luogo_di_nascita");
                impiegatoString[4] = rs.getString("cf");
                impiegatiString.add(impiegatoString);

                impiegatoDate[0] = rs.getDate("data_di_nascita");
                impiegatoDate[1] = rs.getDate("data_assunzione");
                impiegatoDate[2] = rs.getDate("passaggio_dirigente");
                impiegatiDate.add(impiegatoDate);

                stipendi.add(rs.getFloat("stipendio"));

                laboratorio[0] = rs.getString("nome_lab");
                laboratorio[1] = rs.getString("topic_lab");
                afferenze.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
   public void readAllMiddleNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi) {
        PreparedStatement leggiMiddleNonDirigentiPS;
        try {
            leggiMiddleNonDirigentiPS = connection.prepareStatement(
                    "SELECT * FROM impiegato NATURAL JOIN middle WHERE cf NOT IN (SELECT cf FROM dirigente)");
            ResultSet rs = leggiMiddleNonDirigentiPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];//nome lab, topic lab
                String[] impiegatoString = new String[5]; //nome, conome, sesso, luogo di nascita, CF
                Date[] impiegatoDate = new Date[3];//data di nascita, data assunzione, passaggio middle

                impiegatoString[0] = rs.getString("nome");
                impiegatoString[1] = rs.getString("cognome");
                impiegatoString[2] = rs.getString("sesso");
                impiegatoString[3] = rs.getString("luogo_di_nascita");
                impiegatoString[4] = rs.getString("cf");
                impiegatiString.add(impiegatoString);

                impiegatoDate[0] = rs.getDate("data_di_nascita");
                impiegatoDate[1] = rs.getDate("data_assunzione");
                impiegatoDate[2] = rs.getDate("passaggio_middle");
                impiegatiDate.add(impiegatoDate);

                stipendi.add(rs.getFloat("stipendio"));

                laboratorio[0] = rs.getString("nome_lab");
                laboratorio[1] = rs.getString("topic_lab");
                afferenze.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAllMiddleDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi) {
        PreparedStatement leggiMiddleDirigentiPS;
        try {
            leggiMiddleDirigentiPS = connection.prepareStatement(
                    "SELECT * FROM impiegato NATURAL JOIN middle NATURAL JOIN dirigente");
            ResultSet rs = leggiMiddleDirigentiPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];//nome lab, topic lab
                String[] impiegatoString = new String[5]; //nome, conome, sesso, luogo di nascita, CF
                Date[] impiegatoDate = new Date[4];//data di nascita, data assunzione, passaggio middle, passaggio dirigente

                impiegatoString[0] = rs.getString("nome");
                impiegatoString[1] = rs.getString("cognome");
                impiegatoString[2] = rs.getString("sesso");
                impiegatoString[3] = rs.getString("luogo_di_nascita");
                impiegatoString[4] = rs.getString("cf");
                impiegatiString.add(impiegatoString);

                impiegatoDate[0] = rs.getDate("data_di_nascita");
                impiegatoDate[1] = rs.getDate("data_assunzione");
                impiegatoDate[2] = rs.getDate("passaggio_middle");
                impiegatoDate[3] = rs.getDate("passaggio_dirigente");
                impiegatiDate.add(impiegatoDate);

                stipendi.add(rs.getFloat("stipendio"));

                laboratorio[0] = rs.getString("nome_lab");
                laboratorio[1] = rs.getString("topic_lab");
                afferenze.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
   public void readAllSeniorNonDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi) {
        PreparedStatement leggiSeniorNonDirigentiPS;
        try {
            leggiSeniorNonDirigentiPS = connection.prepareStatement(
                    "SELECT * FROM impiegato NATURAL JOIN senior WHERE cf NOT IN (SELECT cf FROM dirigente)");
            ResultSet rs = leggiSeniorNonDirigentiPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];//nome lab, topic lab
                String[] impiegatoString = new String[5]; //nome, conome, sesso, luogo di nascita, CF
                Date[] impiegatoDate = new Date[4];//data di nascita, data assunzione, passaggio middle, passaggio senior

                impiegatoString[0] = rs.getString("nome");
                impiegatoString[1] = rs.getString("cognome");
                impiegatoString[2] = rs.getString("sesso");
                impiegatoString[3] = rs.getString("luogo_di_nascita");
                impiegatoString[4] = rs.getString("cf");
                impiegatiString.add(impiegatoString);

                impiegatoDate[0] = rs.getDate("data_di_nascita");
                impiegatoDate[1] = rs.getDate("data_assunzione");
                impiegatoDate[2] = rs.getDate("passaggio_middle");
                impiegatoDate[3] = rs.getDate("passaggio_senior");
                impiegatiDate.add(impiegatoDate);

                stipendi.add(rs.getFloat("stipendio"));

                laboratorio[0] = rs.getString("nome_lab");
                laboratorio[1] = rs.getString("topic_lab");
                afferenze.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAllSeniorDirigenti(ArrayList<String[]> impiegatiString, ArrayList<Date[]> impiegatiDate, ArrayList<String[]> afferenze, ArrayList<Float> stipendi) {
        PreparedStatement leggiSeniorDirigentiPS;
        try {
            leggiSeniorDirigentiPS = connection.prepareStatement(
                    "SELECT * FROM impiegato NATURAL JOIN senior NATURAL JOIN dirigente");
            ResultSet rs = leggiSeniorDirigentiPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[2];//nome lab, topic lab
                String[] impiegatoString = new String[5]; //nome, conome, sesso, luogo di nascita, CF
                Date[] impiegatoDate = new Date[5];//data di nascita, data assunzione, passaggio middle, passaggio senior, passaggio dirigente

                impiegatoString[0] = rs.getString("nome");
                impiegatoString[1] = rs.getString("cognome");
                impiegatoString[2] = rs.getString("sesso");
                impiegatoString[3] = rs.getString("luogo_di_nascita");
                impiegatoString[4] = rs.getString("cf");
                impiegatiString.add(impiegatoString);

                impiegatoDate[0] = rs.getDate("data_di_nascita");
                impiegatoDate[1] = rs.getDate("data_assunzione");
                impiegatoDate[2] = rs.getDate("passaggio_middle");
                impiegatoDate[3] = rs.getDate("passaggio_senior");
                impiegatoDate[4] = rs.getDate("passaggio_dirigente");
                impiegatiDate.add(impiegatoDate);

                stipendi.add(rs.getFloat("stipendio"));

                laboratorio[0] = rs.getString("nome_lab");
                laboratorio[1] = rs.getString("topic_lab");
                afferenze.add(laboratorio);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void readAllLaboratori(ArrayList<String[]> laboratori) {
        PreparedStatement leggiLaboratoriPS;
        try {
            leggiLaboratoriPS = connection.prepareStatement(
                    "SELECT * FROM laboratorio");
            ResultSet rs = leggiLaboratoriPS.executeQuery();
            while (rs.next()) {
                String[] laboratorio = new String[3]; //nome, topic, responsabile scientifico

                laboratorio[0] = rs.getString("nome");
                laboratorio[1] = rs.getString("topic");
                laboratorio[2] = rs.getString("responsabile_scientifico");

                laboratori.add(laboratorio);

            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readAllProgettiInCorso(ArrayList<String[]> progettiString, ArrayList<Date> progettiDate) {
        PreparedStatement leggiProgettiPS;
        try {
            leggiProgettiPS = connection.prepareStatement(
                    "SELECT * FROM progetto_in_corso");
            ResultSet rs = leggiProgettiPS.executeQuery();
            while (rs.next()) {

                String[] progettoString = new String[4]; //nome, cup, referente scientifico, responsabile
                Date progettoDate = new Date();

                progettoString[0] = rs.getString("nome");
                progettoString[1] = rs.getString("cup");
                progettoString[2] = rs.getString("referente_scientifico");
                progettoString[3] = rs.getString("responsabile");
                progettiString.add(progettoString);

                progettoDate = rs.getDate("data_inizio");
                progettiDate.add(progettoDate);

            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAllProgettiConclusi(ArrayList<String[]> progettiString, ArrayList<Date[]> progettiDate) {
        PreparedStatement leggiProgettiPS;
        try {
            leggiProgettiPS = connection.prepareStatement(
                    "SELECT * FROM progetto_concluso");
            ResultSet rs = leggiProgettiPS.executeQuery();
            while (rs.next()) {

                String[] progettoString = new String[4]; //nome, cup, referente scientifico, responsabile
                Date[] progettoDate = new Date[2]; //data inizio, data fine

                progettoString[0] = rs.getString("nome");
                progettoString[1] = rs.getString("cup");
                progettoString[2] = rs.getString("referente_scientifico");
                progettoString[3] = rs.getString("responsabile");
                progettiString.add(progettoString);

                progettoDate[0] = rs.getDate("data_inizio");
                progettoDate[1] = rs.getDate("data_fine");
                progettiDate.add(progettoDate);

            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addProgettoInCorso(String cup, String nome, String dataInizio, ArrayList<String[]> assegnazioni,
                                   String referenteScientifico, String responsabile) {
        PreparedStatement inserisciProgetto;
        PreparedStatement inserisciAssegnazione;
        try {
            inserisciProgetto = connection.prepareStatement(
                    "INSERT INTO progetto_in_corso VALUES ('" + cup + "', '" + nome + "', '" + dataInizio +
                            "', '" + referenteScientifico + "', '" + responsabile + "')"
            );
            inserisciProgetto.executeUpdate();

            for (String[] assegnazione : assegnazioni
                 ) {
                inserisciAssegnazione = connection.prepareStatement(
                        "INSERT INTO assegnazione VALUES ('" + assegnazione[1] + "', '" + assegnazione[0] + "', '" + cup + "')"
                );
                inserisciAssegnazione.executeUpdate();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addLaboratorio(String nome, String topic, String responsabileScientifico) {
        PreparedStatement inserisciLaboratorio;
        try {
            inserisciLaboratorio = connection.prepareStatement(
                    "INSERT INTO laboratorio VALUES ('" + topic + "', '" + nome + "', '"
                            + responsabileScientifico + "')"
            );
            inserisciLaboratorio.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLaboratorio(String topic, String nome) {
        PreparedStatement deleteLaboratorio;
        try {
            deleteLaboratorio = connection.prepareStatement("DELETE FROM laboratorio WHERE topic = '" +
                    topic + "' AND nome = '" + nome + "'" );
            deleteLaboratorio.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean deleteImpiegato(String cf){

        boolean flag = false;

        PreparedStatement deleteImpPS;
        try{
            deleteImpPS = connection.prepareStatement("DELETE FROM impiegato WHERE cf = '" + cf + "'");
            deleteImpPS.executeUpdate();

            flag = true;

            connection.close();
        }catch (SQLException e){

            flag = false;

            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return flag;
    }

    public boolean inserisciImpiegato(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita,
                                      String cf, Date dataDiAssunzione, String stipendio, boolean dirigente){

        boolean flag = false;

        PreparedStatement inserisciImpiegatoPS;
        PreparedStatement inserisciDirigentePS;
        try{

            inserisciImpiegatoPS = connection.prepareStatement("INSERT INTO impiegato VALUES('" +
                    nome + "','" + cognome + "','" + sesso + "','" + dataDiNascita + "','" +
                    luogoDiNascita + "','" + cf + "','" + stipendio + "','" +
                    dataDiAssunzione + "',null,null)");

            inserisciDirigentePS = connection.prepareStatement("INSERT INTO dirigente VALUES('" + cf +
                    "','" + dataDiAssunzione + "')");

            inserisciImpiegatoPS.executeUpdate();

            if(dirigente)
                inserisciDirigentePS.executeUpdate();

            flag = true;

            connection.close();

        }catch (SQLException e){

            flag = false;

            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return flag;
    }

}
