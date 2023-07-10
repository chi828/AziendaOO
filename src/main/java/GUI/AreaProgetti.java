package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Area progetti.
 */
public class AreaProgetti {
    /**
     * The Frame.
     */
    JFrame frame;
    private JPanel panel1;
    private JTable progettiTable;
    private JButton concludiBtn;
    private JButton responsabileBtn;
    private JButton referenteSBtn;
    private JButton aggiungiBtn;
    private JButton pConclusiBtn;
    private JButton homeBtn;
    private JTable laboratoriAssegnatiTable;
    private JButton aggiungiLaboratorioButton;
    private JButton rimuoviButton;
    private JTextField nomeTextField;
    private JTextField topicTextField;

    /**
     * The Controller.
     */
    Controller controller;

    /**
     * Instantiates a new Area progetti.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    public AreaProgetti(Controller controller, JFrame chiamante) {
        frame = new JFrame("Area progetti");
        ImageIcon icona = new ImageIcon(getClass().getResource("/proj_nocolor.png"));
        frame.setIconImage(icona.getImage());
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.controller = controller;

        //Implementazione delle table
        progettiTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[] {
                        "Nome","Cup", "Data inizio", "Referente scientifico", "Responsabile"
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

        //Implementazioni button
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });

        pConclusiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProgettiConclusi progettiConclusi = new ProgettiConclusi(controller, frame);
                progettiConclusi.frame.setVisible(true);
                frame.setVisible(false);
            }
        });



        aggiungiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiProgetto aggiungiProgetto = new AggiungiProgetto(controller, frame);
                frame.setVisible(false);
            }
        });

        concludiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(progettiTable.getSelectedRow() == -1){
                    JOptionPane.showMessageDialog(frame, "Seleziona un progetto");
                } else {
                    controller.concludiProgetto(progettiTable.getSelectedRow(), new Date());
                    aggiornaTabellaProgetti();
                }
            }
        });

        aggiungiLaboratorioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(progettiTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un progetto.");
                    return;
                }
                if(laboratoriAssegnatiTable.getRowCount() == 3) {
                    JOptionPane.showMessageDialog(frame, "Impossibile aggiungere altri laboratori.");
                    return;
                }
                if(nomeTextField.getText().isBlank() || topicTextField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Inserire nome e topic del laboratorio da aggiungere.");
                    return;
                }
                try {
                    controller.addAssegnazioneLaboratorioProgetto(progettiTable.getSelectedRow(), nomeTextField.getText(),
                            topicTextField.getText());
                    aggiornaTabellaLaboratoriAssegnati();
                } catch (RuntimeException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
                nomeTextField.setText(null);
                topicTextField.setText(null);
            }
        });

        rimuoviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(progettiTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un progetto.");
                    return;
                }
                if(laboratoriAssegnatiTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona il laboratorio da rimuovere.");
                    return;
                }
                if(laboratoriAssegnatiTable.getRowCount() == 1) {
                    JOptionPane.showMessageDialog(frame, "Rimozione annullata: questo progetto non ha" +
                            " altri laboratori assegnati.");
                    return;
                }
                try {
                    controller.rimuoviAssegnazione(progettiTable.getSelectedRow(), laboratoriAssegnatiTable.getSelectedRow());
                    aggiornaTabellaLaboratoriAssegnati();
                } catch(RuntimeException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        referenteSBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progettiTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un progetto.");
                    return;
                }
                String s = JOptionPane.showInputDialog("Inserisci codice fiscale del nuovo referente scientifico");
                if(s == null) return;
                if(s.isBlank()) {
                    JOptionPane.showMessageDialog(frame,"Nessun valore inserito.");
                    return;
                }
                try {
                    controller.setReferenteScientifico(progettiTable.getSelectedRow(), s);
                    aggiornaTabellaProgetti();
                } catch (RuntimeException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        responsabileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progettiTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un progetto.");
                    return;
                }
                String s = JOptionPane.showInputDialog("Inserisci codice fiscale del nuovo responsabile");
                if(s == null) return;
                try {
                    controller.setResponsabile(progettiTable.getSelectedRow(), s);
                    aggiornaTabellaProgetti();;
                } catch (RuntimeException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Eventi del frame
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                aggiornaTabellaProgetti();
            }
        });
    }
    private void aggiornaTabellaProgetti() {
        ArrayList<String> nomiProgetti = new ArrayList<>();
        ArrayList<String> cupProgetti = new ArrayList<>();
        ArrayList<Date> dateInizio = new ArrayList<>();
        ArrayList<String> responsabili = new ArrayList<>();
        ArrayList<String> referenti = new ArrayList<>();

        controller.getProgettiInCorso(nomiProgetti, cupProgetti, dateInizio, responsabili, referenti);

        DefaultTableModel model = (DefaultTableModel) progettiTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i<nomiProgetti.size(); i++) {

            model.addRow(new Object[]{nomiProgetti.get(i), cupProgetti.get(i), dateInizio.get(i),
                    referenti.get(i), responsabili.get(i)});
        }
    }

    private void aggiornaTabellaLaboratoriAssegnati() {
        ArrayList<String> nomiLaboratori = new ArrayList<>();
        ArrayList<String> topicLaboratori = new ArrayList<>();
        ArrayList<String> responsabiliScientifici = new ArrayList<>();

        controller.getLaboratoriAssegnati(progettiTable.getSelectedRow(), nomiLaboratori,
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
