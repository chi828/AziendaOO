package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Date;

/**
 * Interfaccia grafica per la visualizzazione e la modifica dei laboratorio
 */
public class AreaLaboratorio {

    /**
     * The Frame.
     */
    JFrame frame;
    private JPanel panel1;
    private JTable table1;
    private JButton eliminaBtn;
    private JButton responsabileSBtn;
    private JButton aggiugniBtn;
    private JButton homeBtn;

    /**
     * The Controller.
     */
    Controller controller;

    /**
     * Instantiates a new Area laboratorio.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    public AreaLaboratorio(Controller controller, JFrame chiamante) {
        frame = new JFrame("Area laboratori");
        ImageIcon icona = new ImageIcon(getClass().getResource("/lab_nocolor.png"));
        frame.setIconImage(icona.getImage());
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(chiamante);
        frame.setVisible(true);

        this.controller = controller;

        //Implementazione table
        table1.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[] {
                        "Nome", "Topic", "Responsabile scientifico"
                }
        ));

        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        aggiornaTabella();

        //Implementazioni button
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });

        aggiugniBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiLaboratorio aggiungiLaboratorio = new AggiungiLaboratorio(controller, frame);
                frame.setVisible(false);
            }
        });

        responsabileSBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un laboratorio.");
                    return;
                }
                String s = JOptionPane.showInputDialog("Inserisci il codice fiscale del nuovo responsabile scientifico");
                if(s == null) return;
                if(s.isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Inserimento invalido.");
                    return;
                }
                try {
                    controller.setResponsabileScientifico(table1.getSelectedRow(), s);
                    aggiornaTabella();
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un laboratorio");
                    return;
                }
                try {
                    controller.deleteLaboratorio(table1.getSelectedRow());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
                aggiornaTabella();
            }
        });

        //Eventi del frame
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                aggiornaTabella();
            }
        });
    }

    /**
     * Aggiorna tabella.
     */
    public void aggiornaTabella() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        ArrayList<String> nomiLaboratori = new ArrayList<String>();
        ArrayList<String> topicLaboratori = new ArrayList<String>();
        ArrayList<String> responsabiliScientifici = new ArrayList<String>();

        controller.getLaboratori(nomiLaboratori, topicLaboratori, responsabiliScientifici);

        for (int i = 0; i < topicLaboratori.size(); i++) {

            model.addRow(new Object[] {nomiLaboratori.get(i), topicLaboratori.get(i),
                    responsabiliScientifici.get(i)});

        }

    }

}
