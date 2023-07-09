package GUI;

import javax.swing.*;
import Controller.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The type Aggiungi progetto.
 */
public class AggiungiProgetto {
    private JTextField nomeText;
    private JTextField cupText;
    private JSpinner spinner1;
    private JComboBox laboratorio1Box;
    private JComboBox laboratorio2Box;
    private JComboBox laboratorio3Box;
    private JComboBox responsabileBox;
    private JComboBox referenteScientificoBox;
    private JPanel panel1;
    private JButton annullaButton;
    private JButton confermaButton;
    /**
     * The Frame.
     */
    JFrame frame;

    /**
     * Instantiates a new Aggiungi progetto.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    public AggiungiProgetto(Controller controller, JFrame chiamante) {
        frame = new JFrame("Aggiungi progetto");
        ImageIcon icona = new ImageIcon(getClass().getResource("/proj_nocolor.png"));
        frame.setIconImage(icona.getImage());
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(chiamante);
        frame.setVisible(true);

        //Selezione data inizio
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        spinner1.setModel(new SpinnerDateModel(initDate, earliestDate, initDate, Calendar.YEAR));
        spinner1.setEditor(new JSpinner.DateEditor(spinner1, "dd/MM/yyyy"));

        //Combo box
        //Responsabile
        ArrayList<String> dirigenti = controller.getImpiegatiDirigenti();

        for (String impiegato : dirigenti
             ) {
            responsabileBox.addItem(impiegato);
        }

        //Referente scientifico
        ArrayList<String> seniors = controller.getSeniors();
        for (String senior : seniors
             ) {
            referenteScientificoBox.addItem(senior);
        }

        ArrayList<String> nomiLaboratori = new ArrayList<String>();
        ArrayList<String> topicLaboratori = new ArrayList<String>();
        ArrayList<String> responsabiliScientifici = new ArrayList<String>();
        controller.getLaboratori(nomiLaboratori, topicLaboratori, responsabiliScientifici);
        //laboratorio 1
        for (int i = 0; i<nomiLaboratori.size(); i++) {
            laboratorio1Box.addItem(nomiLaboratori.get(i) + " " + topicLaboratori.get(i));
        }

        //laboratorio 2
        laboratorio2Box.addItem("Nessuno");
        for (int i = 0; i<nomiLaboratori.size(); i++) {
            laboratorio2Box.addItem(nomiLaboratori.get(i) + " " + topicLaboratori.get(i));
        }

        //laboratorio 3
        laboratorio3Box.addItem("Nessuno");
        for (int i = 0; i<nomiLaboratori.size(); i++) {
            laboratorio3Box.addItem(nomiLaboratori.get(i) + " " + topicLaboratori.get(i));
        }



        //Button
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                chiamante.setVisible(true);
                frame.dispose();
            }
        });

        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeText.getText();
                String cup = cupText.getText();

                Date dataInizio;
                try {
                    dataInizio = (Date) spinner1.getModel().getValue();
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(frame, "Inserire una data valida", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int indiceResponsabile = responsabileBox.getSelectedIndex();
                int indicieReferenteScientifico = referenteScientificoBox.getSelectedIndex();

                ArrayList<Integer> assegnazioni = new ArrayList<Integer>();

                assegnazioni.add(laboratorio1Box.getSelectedIndex());
                if(laboratorio2Box.getSelectedIndex() > 0 &&
                        laboratorio2Box.getSelectedIndex()-1 != laboratorio1Box.getSelectedIndex()) {
                    assegnazioni.add(laboratorio2Box.getSelectedIndex() - 1);
                }

                if(laboratorio3Box.getSelectedIndex() > 0 &&
                        laboratorio3Box.getSelectedIndex()-1 != laboratorio1Box.getSelectedIndex() &&
                        laboratorio3Box.getSelectedIndex()-1 != laboratorio2Box.getSelectedIndex() ) {
                    assegnazioni.add(laboratorio3Box.getSelectedIndex() - 1);
                }

                if (nomeText.getText().isBlank() || cupText.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Nome e cup non possono essere vuoti.");
                    return;
                }

                try {
                    controller.addProgettoInCorso(nome, cup, dataInizio, indiceResponsabile, indicieReferenteScientifico, assegnazioni);
                } catch (RuntimeException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                frame.setVisible(false);
                chiamante.setVisible(true);
                frame.dispose();
            }
        });

    }
}
