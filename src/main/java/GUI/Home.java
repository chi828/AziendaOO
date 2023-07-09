package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The type Home.
 */
public class Home extends JFrame {

    /**
     * Instantiates a new Home.
     */
    public Home(){

        super("Home");

        JFrame homeFrame = this;

        //////////////////////////////////////////ISTANZIA CONTROLLER///////////////////////////////////////////

        Controller controller = new Controller();

        ////////////////////////////////IMPOSTAZIONI FINESTRA/////////////////////////////////////////////////////////////////////////////////

        ImageIcon icona = new ImageIcon(getClass().getResource("/iconaHome.png"));
        setIconImage(icona.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,350);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        //new MenuBarHome(this, controller); //Chiama la classe che costruisce il menu a barra


        ////////////////////////////////INIZIALIZZAZIONI VARIABILI CONTENENTI IMMAGINI DELLA FINESTRA////////////////////////////////////////////

        //Immagini sezione impiegato
        NoScalingIcon emp_NonSelezionato_img = new NoScalingIcon(new ImageIcon(getClass().getResource("/emp_nocolor.png")));
        NoScalingIcon emp_Selezionato_img = new NoScalingIcon(new ImageIcon(getClass().getResource("/emp_color.png")));
        JLabel labelEmpImg = new JLabel(emp_NonSelezionato_img);

        //Immagini sezione laboratorio
        NoScalingIcon lab_NonSelezionato_img = new NoScalingIcon(new ImageIcon(getClass().getResource("/lab_nocolor.png")));
        NoScalingIcon lab_Selezionato_img = new NoScalingIcon(new ImageIcon(getClass().getResource("/lab_color.png"))) ;
        JLabel labelLabImg = new JLabel(lab_NonSelezionato_img);

        //Immagini sezione progetto
        NoScalingIcon proj_NonSelezionato_img = new NoScalingIcon(new ImageIcon(getClass().getResource("/proj_nocolor.png")));
        NoScalingIcon proj_Selezionato_img = new NoScalingIcon(new ImageIcon(getClass().getResource("/proj_color.png")));
        JLabel labelProjImg = new JLabel(proj_NonSelezionato_img);

        /////////////////////////////////CREAZIONI BORDI DELLE SEZIONI///////////////////////////////////////////////////////////////////////////////

        //BORDO PRINCIPALE (CONTENITORE DELLE SEZIONI)
        Border bordoMainInterno = BorderFactory.createTitledBorder("SEZIONI");
        Border bordoMainEsterno = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoMainFinale = BorderFactory.createCompoundBorder(bordoMainEsterno, bordoMainInterno);

        //BORDO SEZIONE IMPIEGATO
        Border bordoImpiegatoInterno = BorderFactory.createTitledBorder("Area impiegato");
        Border bordoImpiegatoEsterno = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoImpiegatoFinale = BorderFactory.createCompoundBorder(bordoImpiegatoEsterno, bordoImpiegatoInterno);

        //BORDO SEZIONE LABORATORIO
        Border bordoLaboratorioInterno = BorderFactory.createTitledBorder("Area laboratorio");
        Border bordoLaboratorioEsterno = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoLaboratorioFinale = BorderFactory.createCompoundBorder(bordoLaboratorioEsterno, bordoLaboratorioInterno);

        //BORDO SEZIONE PROGETTO
        Border bordoProgettoInterno = BorderFactory.createTitledBorder("Area progetti");
        Border bordoProgettoEsterno = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoProgettooFinale = BorderFactory.createCompoundBorder(bordoProgettoEsterno, bordoProgettoInterno);


        ///////////////////////////////CREAZIONI SEZIONI///////////////////////////////////////////////////////////////////////////////////////////

        //INIZIALIZZAZIONE PANELLO PRINCIPALE (CONTENITORE SEZIONI)
        JPanel contenitoreSezioni = new JPanel();
        contenitoreSezioni.setLayout(new FlowLayout(50,100,50));
        contenitoreSezioni.setBorder(bordoMainFinale);

        //INIZIALIZZAZIONE PANELLO SEZIONE IMPIEGATO
        JPanel sezioneImpiegato = new JPanel();
        sezioneImpiegato.setSize(25,25);
        sezioneImpiegato.setBorder(bordoImpiegatoFinale);
        sezioneImpiegato.add(labelEmpImg);

        //INIZIALIZZAZIONE PANELLO SEZIONE LABORATORIO
        JPanel sezioneLaboratorio = new JPanel();
        sezioneLaboratorio.setSize(25,25);
        sezioneLaboratorio.setBorder(bordoLaboratorioFinale);
        sezioneLaboratorio.add(labelLabImg);

        //INIZIALIZZAZIONE PANELLO SEZIONE PROGETTO
        JPanel sezioneProgetto = new JPanel();
        sezioneProgetto.setSize(25,25);
        sezioneProgetto.setBorder(bordoProgettooFinale);
        sezioneProgetto.add(labelProjImg);

        //INCLUSIONE DELLE VARIE SEZIONI NELLA SEZIONE PRINCIPALE
        contenitoreSezioni.add(sezioneImpiegato);
        contenitoreSezioni.add(sezioneLaboratorio);
        contenitoreSezioni.add(sezioneProgetto);

        //INCLUSIONE DEL CONTENITORE SEZIONI NELLA FINESTRA
        add(contenitoreSezioni, BorderLayout.CENTER);

        //SET VISIBILE LA FINESTRA
        setVisible(true);


   ////////////////////////////////////GESTIONE EVENTI///////////////////////////////////////////////////////////////////////////////

        //Gestione eventi sulla label labelEmp(sezione impiegato)
        labelEmpImg.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent event)
            {
                labelEmpImg.setIcon(emp_Selezionato_img);
            }

            public void mouseExited(MouseEvent event)
            {
                labelEmpImg.setIcon(emp_NonSelezionato_img);
            }
            @Override
            public void mouseClicked(MouseEvent e) {

                setVisible(false);

                new AreaImpiegato(controller, homeFrame);

            }
        });

        //Gestione eventii sulla label labelLab(sezione laboratorio)
        labelLabImg.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent event)
            {
                labelLabImg.setIcon(lab_Selezionato_img);
            }

            public void mouseExited(MouseEvent event)
            {
                labelLabImg.setIcon(lab_NonSelezionato_img);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                AreaLaboratorio areaLaboratorio = new AreaLaboratorio(controller, homeFrame);
                areaLaboratorio.frame.setVisible(true);
                homeFrame.setVisible(false);
            }
        });

        //Gestione eventi sulla label labelProj(sezione progetto)
        labelProjImg.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent event)
            {
                labelProjImg.setIcon(proj_Selezionato_img);
            }

            public void mouseExited(MouseEvent event)
            {
               labelProjImg.setIcon(proj_NonSelezionato_img);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                AreaProgetti areaProgetti = new AreaProgetti(controller, homeFrame);
                areaProgetti.frame.setVisible(true);
                homeFrame.setVisible(false);
            }
        });

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
/////////////////////////////////////MAIN///////////////////////////////////////////////////////////////
    public static void main(String[] args) {

        new Home();
    }
}
