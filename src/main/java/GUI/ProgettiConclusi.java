package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Progetti conclusi.
 */
public class ProgettiConclusi {
    private JTable progettiTable;
    private JButton indietroBtn;
    private JPanel panel1;
    private JTable laboratoriAssegnatiTable;
    /**
     * The Frame.
     */
    JFrame frame;
    /**
     * The Controller.
     */
    Controller controller;

    /**
     * Instantiates a new Progetti conclusi.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    ProgettiConclusi(Controller controller, JFrame chiamante) {
        frame = new JFrame("Area progetti");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.controller = controller;

        //Funzionamento JTable
        progettiTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[] {
                        "Nome","Cup", "Data inizio", "Data fine","Referente scientifico", "Responsabile"
                }
        ));

        progettiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableModel model = (DefaultTableModel) progettiTable.getModel();

        aggiornaTabellaProgetti();


        laboratoriAssegnatiTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "Nome","Topic", "Responsabile Scientifico"
                }
        ));

        DefaultTableModel model2 = (DefaultTableModel) laboratoriAssegnatiTable.getModel();

        //Gestione evento selezione dalla progettiTable
        progettiTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                model2.setRowCount(0);
                if (progettiTable.getSelectedRow() != -1){
                    aggiornaTabellaLaboratoriAssegnati();
                }
            }
        });

        //implementazione button
        indietroBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });

    }

    private void aggiornaTabellaProgetti() {
        ArrayList<String> nomiProgetti = new ArrayList<>();
        ArrayList<String> cupProgetti = new ArrayList<>();
        ArrayList<Date> dateInizio = new ArrayList<>();
        ArrayList<Date> dateFine = new ArrayList<>();
        ArrayList<String> responsabili = new ArrayList<>();
        ArrayList<String> referenti = new ArrayList<>();

        controller.getProgettiConclusi(nomiProgetti, cupProgetti, dateInizio, dateFine, responsabili, referenti);

        DefaultTableModel model = (DefaultTableModel) progettiTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i<nomiProgetti.size(); i++) {

            model.addRow(new Object[]{nomiProgetti.get(i), cupProgetti.get(i), dateInizio.get(i),
                    dateFine.get(i), referenti.get(i), responsabili.get(i)});
        }
    }

    private void aggiornaTabellaLaboratoriAssegnati() {
        ArrayList<String> nomiLaboratori = new ArrayList<>();
        ArrayList<String> topicLaboratori = new ArrayList<>();
        ArrayList<String> responsabiliScientifici = new ArrayList<>();

        controller.getLaboratoriAssegnatiConclusi(progettiTable.getSelectedRow(), nomiLaboratori,
                topicLaboratori, responsabiliScientifici);

        DefaultTableModel model = (DefaultTableModel) laboratoriAssegnatiTable.getModel();
        model.setRowCount(0);

        if(progettiTable.getSelectedRow() != -1) {
            for (int i = 0; i< nomiLaboratori.size(); i++) {
                model.addRow(new Object[]{nomiLaboratori.get(i), topicLaboratori.get(i),
                        responsabiliScientifici.get(i)});
            }
        }
    }
}
