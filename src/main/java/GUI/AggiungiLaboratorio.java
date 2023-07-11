package GUI;

import javax.swing.*;
import Controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interfaccia grafica per aggiungere un laboratorio.
 */
public class AggiungiLaboratorio {
    private JTextField nomeTextField;
    private JTextField topicTextField;
    private JComboBox responsabileScientificoBox;
    private JPanel panel1;
    private JButton annullaButton;
    private JButton confermaButton;

    /**
     * The Frame.
     */
    JFrame frame;

    /**
     * Instantiates a new Aggiungi laboratorio.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    AggiungiLaboratorio(Controller controller, JFrame chiamante) {
        frame = new JFrame("Aggiungi laboratorio");
        ImageIcon icona = new ImageIcon(getClass().getResource("/lab_nocolor.png"));
        frame.setIconImage(icona.getImage());
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(chiamante);
        frame.setSize(500,240);
        frame.setVisible(true);

        //Combo box
        for (String senior: controller.getSeniors()
             ) {
            responsabileScientificoBox.addItem(senior);
        }

        //button
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
                if (nomeTextField.getText().isBlank() || topicTextField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Nome e topic non possono essere vuoti.");
                    return;
                }

                try {
                    controller.addLaboratorio(nomeTextField.getText(), topicTextField.getText(),
                            responsabileScientificoBox.getSelectedIndex());
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
