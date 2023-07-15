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

    /**
     * Chiama la procedura che si occupa di aggiornare le categorie degli impiegati nel database di Postgres.
     */
    @Override
    public void updateCategorie() {
        PreparedStatement updateCategoriePS;
        try {
            updateCategoriePS = connection.prepareStatement(
                    "CALL updatecategoria()"
            );
            updateCategoriePS.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica dal database di Postgres i dati relativi a tutti gli impiegati junior non dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita e data
     *                        assunzione dell'impiegato
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
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
    /**
     * Carica dal database di postgres i dati relativi a tutti gli impiegati junior dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione e passaggio dirigente dell'impiegato
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
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

    /**
     * Carica dal database di Postgres i dati relativi a tutti gli impiegati middle non dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione e passaggio middle dell'impiegato
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
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

    /**
     * Carica dal database di Postgres i dati relativi a tutti gli impiegati middle dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione, passaggio middle dell'impiegato e passaggio dirigente
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
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

    /**
     * Carica dal database i dati relativi a tutti gli impiegati senior non dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione, passaggio middle dell'impiegato e passaggio senior
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */

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


    /**
     * Carica dal database i dati relativi a tutti gli impiegati senior dirigenti.
     *
     * @param impiegatiString A fine metodo ciascun array di stringe conterrà nome, conome, sesso, luogo di nascita,
     *                       CF dell'impiegato
     * @param impiegatiDate   A fine metodo ciascun array di date conterrà in ordine data di nascita, data
     *                        assunzione, passaggio middle dell'impiegato, passaggio senior e passaggio dirigente
     * @param afferenze       Ciascun array di stringhe in ordine contiene nome e topic dell'impiegato
     * @param stipendi        Ciascun float rappresenta lo stipendio dell'impiegato
     */
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

    /**
     * Carica dal database i dati relativi ai laboratori.
     *
     * @param laboratori ciascun array di stringe contiene in ordine nome, topic e codice fiscale del responsabile
     *                   scientifico del laboratorio.
     */
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

    /**
     * Carica dal database i dati relativi ai progetti in corso.
     *
     * @param progettiString Ciascun array di stringhe contiene in ordine nome, cup, referente scientifico,
     *                       responsabile del progetto
     * @param progettiDate   la date di inizio dei progetti
     */
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


    /**
     * Carica dal database i dati relativi ai progetti conclusi.
     *
     * @param progettiString Ciascun array di stringhe contiene in ordine nome, cup, referente scientifico,
     *                       responsabile del progetto
     * @param progettiDate   la date di inizio e di fine dei progetti in questo ordine.
     */
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

    /**
     * Add progetto in corso.
     *
     * @param cup                  the cup
     * @param nome                 the nome
     * @param dataInizio           the data inizio
     * @param assegnazioni         the assegnazioni
     * @param referenteScientifico the referente scientifico
     * @param responsabile         the responsabile
     */
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

    /**
     * Inserimento laboratorio nel database
     *
     * @param nome                    the nome
     * @param topic                   the topic
     * @param responsabileScientifico the responsabile scientifico
     */


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

    /**
     * Cancellazione laboratorio dal database
     *
     * @param topic the topic
     * @param nome  the nome
     */

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

    /**
     * Cancellazione di impiegato dal database
     *
     * @param cf the cf
     * @return
     */

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

    /**
     * Inserimento impiegato nel database
     *
     * @param nome             the nome
     * @param cognome          the cognome
     * @param sesso            the sesso
     * @param dataDiNascita    the data di nascita
     * @param luogoDiNascita   the luogo di nascita
     * @param cf               the cf
     * @param dataDiAssunzione the data di assunzione
     * @param stipendio        the stipendio
     * @param dirigente        the dirigente
     * @return
     */

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

    /**
     * Inserimento dirigente nel database
     *
     * @param cf
     * @param dataPromo
     * @return
     */

    public boolean inserisciDirigente(String cf, Date dataPromo){

        boolean flag = false;

        PreparedStatement inserisciDirigentePS;
        try {
            inserisciDirigentePS = connection.prepareStatement("INSERT INTO dirigente VALUES('" + cf + "','" +
                    dataPromo + "')");
            inserisciDirigentePS.executeUpdate();
            flag = true;
            connection.close();

        }catch (SQLException e){

            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

}
