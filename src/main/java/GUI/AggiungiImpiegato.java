package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Interfaccia grafica per aggiungere un impiegato.
 */
public class AggiungiImpiegato extends JFrame {

    /**
     * Instantiates a new Aggiungi impiegato.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    public AggiungiImpiegato(Controller controller, JFrame chiamante){

        ///////////////////////////////////////////IMPOSTAZIONI FINESTRA/////////////////////////////////////////////////////////////////////////

        super("Registrazione impiegato");

        JFrame questoFrame = this;

        setSize(500,500);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        setResizable(false);

        ImageIcon iconaFrame = new ImageIcon(getClass().getResource("/emp_nocolor.png"));

        setIconImage(iconaFrame.getImage());


        ////////////////////////////////////CREAZIONE CONTENITORE CAMPI//////////////////////////////////////////////////////////////////////////

        //BORDO CONTENITORE
        Border bordoContenitoreInterno = BorderFactory.createTitledBorder("INSERIMENTO IMPIEGATO");
        Border bordoContenitoreEsterno = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoContenitoreFinale = BorderFactory.createCompoundBorder(bordoContenitoreEsterno, bordoContenitoreInterno);

        //INIZIALIZZAZIONE CONTENITORE
        JPanel contenitoreCampi = new JPanel();
        contenitoreCampi.setLayout(new BoxLayout(contenitoreCampi, BoxLayout.PAGE_AXIS));
        contenitoreCampi.setBorder(bordoContenitoreFinale);

        ///////////////////////////////CREAZIONE DELLA PRIMA RIGA DEL MODULO (NOME)/////////////////////////////////////////////////////////////////////

        JPanel primaRiga = new JPanel();
        primaRiga.setLayout(new FlowLayout());

        primaRiga.add(new JLabel("Nome:         "));

        JTextField campoNome = new JTextField(20);
        primaRiga.add(campoNome);

        contenitoreCampi.add(primaRiga);



        //////////////////////////CREAZIONE SECONDA RIGA DEL MODULO (COGNOME)////////////////////////////////////////////////////////////////////

        JPanel secondaRiga = new JPanel();
        secondaRiga.setLayout(new FlowLayout());

        secondaRiga.add(new JLabel("Cognome: "));

        JTextField campoCognome = new JTextField(20);
        secondaRiga.add(campoCognome);

        contenitoreCampi.add(secondaRiga);

        /////////////////////CREAZIONE TERZA RIGA DEL MODULO(SESSO)//////////////////////////////////////////////////////////////////////////////

        JPanel terzaRiga = new JPanel();
        terzaRiga.setLayout(new FlowLayout());

        terzaRiga.add(new JLabel("      Sesso: "));

        //GESTIONE RADIO BUTTON
        JRadioButton maschioButton = new JRadioButton("Maschio");
        JRadioButton femminaButton = new JRadioButton("Femmina");
        ButtonGroup gruppoSesso = new ButtonGroup();
        gruppoSesso.add(maschioButton);
        gruppoSesso.add(femminaButton);
        maschioButton.setSelected(true);

        terzaRiga.add(maschioButton);
        terzaRiga.add(femminaButton);

        contenitoreCampi.add(terzaRiga);

        ////////////////////CREAZIONE QUARTA RIGA DEL MODULO(DATA DI NASCITA)///////////////////////////////////////////////////////////////////

        ///////////GESTIONE CAMPO DATA E FORMATO DATA
        Date data = new Date();
        SpinnerDateModel modelloNascita = new SpinnerDateModel(data,null,null, Calendar.YEAR);
        JSpinner dataNascitaSpinner = new JSpinner();
        dataNascitaSpinner.setModel(modelloNascita);
        JSpinner.DateEditor ded = new JSpinner.DateEditor(dataNascitaSpinner,"yyyy/MM/dd");
        dataNascitaSpinner.setEditor(ded);

        //INIZIALIZZAZIONE RIGA
        JPanel quartaRiga = new JPanel();
        quartaRiga.setLayout(new FlowLayout());
        quartaRiga.add(new JLabel("Data di nascita (yyyy/mm/dd)"));
        quartaRiga.add(dataNascitaSpinner);

        contenitoreCampi.add(quartaRiga);

        ///////////////////CREAZIONE QUINTA RIGA DEL MODULO(PAESE DI NASCITA)//////////////////////////////////////////////////////////////////

        //GESTIONE RADIO BUTTON
        JRadioButton italiaButton = new JRadioButton("Italia");
        JRadioButton esteroButton = new JRadioButton("Estero");
        ButtonGroup gruppoPaese = new ButtonGroup();
        gruppoPaese.add(italiaButton);
        gruppoPaese.add(esteroButton);
        italiaButton.setSelected(true);

        //INIZIALIZZAZIONE RIGA
        JPanel quintaRiga = new JPanel();
        quintaRiga.setLayout(new FlowLayout());
        quintaRiga.add(new JLabel("Paese di nascita: "));
        quintaRiga.add(italiaButton);
        quintaRiga.add(esteroButton);

        contenitoreCampi.add(quintaRiga);

        ///////////////////CREAZIONE SESTA RIGA DEL MODULO(LUOGO DI NASCITA)//////////////////////////////////////////////////////////////////

        //GESTIONE CARICAMENTO FILE DEI LUOGHI DI NASCITA DA IMPORTARE POI NELLA COMBOBOX
        String[] comuni = new String[8000];
        String[] luoghiEsteri = new String[300];
        int i = 0;
        String line;
        InputStream is = getClass().getResourceAsStream("/comuni.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            while((line = br.readLine()) != null)  {
                comuni[i++] = line;
            }
            br.close();
            isr.close();
            is.close();
        }
        catch(Exception e){

            //
        }

        i = 0;
        InputStream is2 = getClass().getResourceAsStream("/esteri.txt");
        InputStreamReader isr2 = new InputStreamReader(is2);
        BufferedReader br2 = new BufferedReader(isr2);
        try {
            while((line = br2.readLine()) != null)  {
                luoghiEsteri[i++] = line;
            }
            br2.close();
            isr2.close();
            is2.close();
        }
        catch(Exception e){

            //
        }

        //GESTIONE COMBOBOX
        JComboBox comuniBox = new JComboBox<>(comuni);
        JComboBox esteriBox = new JComboBox(luoghiEsteri);
        esteriBox.setVisible(false);

        //INIZIALIZZAZIONE SESTA RIGA
        JPanel sestaRiga = new JPanel();
        sestaRiga.setLayout(new FlowLayout());
        sestaRiga.add(new JLabel("Luogo di nascita: "));
        sestaRiga.add(comuniBox);
        sestaRiga.add(esteriBox);

        contenitoreCampi.add(sestaRiga);


        ////////////////////CREAZIONE SETTIMA RIGA DEL MODULO (CODICE FISCALE)//////////////////////////////////////////////////////////////////////////////

        //INIZIALIZZAZIONE RIGA
        JPanel settimaRiga = new JPanel();
        settimaRiga.setLayout(new FlowLayout());
        settimaRiga.add(new JLabel("Codice fiscale: "));

        JTextField campoCF = new JTextField(20);
        settimaRiga.add(campoCF);

        JButton calcolaCfButton = new JButton("Calcola");
        settimaRiga.add(calcolaCfButton);

        contenitoreCampi.add(settimaRiga);

        /////////////////////////////////////CREAZIONE OTTAVA RIGA DEL MODULO(DATA ASSUNZIONE)//////////////////////////////////////////////////

        //GESTIONE ELEMENTI DATA
        SpinnerDateModel modelloAssunzione = new SpinnerDateModel(data,null,null, Calendar.YEAR);
        JSpinner dataAssunzioneSpinner = new JSpinner();
        dataAssunzioneSpinner.setModel(modelloAssunzione);
        JSpinner.DateEditor ded2 = new JSpinner.DateEditor(dataAssunzioneSpinner,"yyyy/MM/dd");
        dataAssunzioneSpinner.setEditor(ded2);

        //INIZIALIZZAZIONE RIGA
        JPanel ottavaRiga = new JPanel();
        ottavaRiga.setLayout(new FlowLayout());
        ottavaRiga.add(new JLabel("Data assunzione(yyyy/mm/dd): "));
        ottavaRiga.add(dataAssunzioneSpinner);

        contenitoreCampi.add(ottavaRiga);

        ///////////////////////////////////CREAZIONE NONA RIGA DEL MODULO(STIPENDIO)////////////////////////////////////////////////////////////

        //INIZIALIZZAZIONE RIGA
        JPanel nonaRiga = new JPanel();
        nonaRiga.setLayout(new FlowLayout());
        nonaRiga.add(new JLabel("Stipendio: "));

        JTextField campoStipendio = new JTextField(8);
        nonaRiga.add(campoStipendio);

        contenitoreCampi.add(nonaRiga);

        ////////////////////////////CREAZIONE DECIMA RIGA DEL MODULO(SET DIRIGENTE)/////////////////////////////////////////////////////////////

        JCheckBox isDirigente = new JCheckBox("Dirigente");

        //INIZIALIZZAZIONE RIGA
        JPanel decimaRiga = new JPanel();
        decimaRiga.setLayout(new FlowLayout());
        decimaRiga.add(new JLabel("Selezionare se ricopre il ruolo di dirigente: "));
        decimaRiga.add(isDirigente);

        contenitoreCampi.add(decimaRiga);

        ////////////////////////////////CREAZIONE UNDICESIMA RIGA DEL MODULO(BOTTONE DI INSERIMENTO)////////////////////////////////////////////

        JButton confermaButton = new JButton("Inserisci impiegato");

        //INIZIALIZZAZIONE RIGA
        JPanel undicesimaRiga = new JPanel();
        undicesimaRiga.setLayout(new FlowLayout());
        undicesimaRiga.add(confermaButton);

        contenitoreCampi.add(undicesimaRiga);

        ////////////////////////////////////INSERIMENTO MODULO NELLA FINESTRA/////////////////////////////////////////////////////////////////

        add(contenitoreCampi, BorderLayout.CENTER);

        ////////////////////////////////////SET VISIBILE LA FINESTRA//////////////////////////////////////////////////////////////////////////

        setVisible(true);

        ///////////////////////////////////////GESTIONE EVENTI//////////////////////////////////////////////////////////////////////////////////

        //Gestione clicl della 'X' del frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                dispose();
            }
        });

        //Cambio opzioni luoghi di nascita: caso: Italia; caso: estero
        italiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                esteriBox.setVisible(false);
                comuniBox.setVisible(true);
            }
        });

        esteroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                comuniBox.setVisible(false);
                esteriBox.setVisible(true);
            }
        });


        //Gestione evento al click del bottone calcola codice fiscale
        calcolaCfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                char sesso;
                boolean estero = false;
                String luogoNascita;

                if(maschioButton.isSelected()) sesso = 'm';
                else sesso = 'f';

                if(italiaButton.isSelected()){

                    estero = false;
                    luogoNascita = (String)comuniBox.getSelectedItem();

                } else{

                    estero = true;
                    luogoNascita = (String) esteriBox.getSelectedItem();
                }

                campoCF.setText(controller.calcoloCF(campoCognome.getText(), campoNome.getText(),
                        (Date) dataNascitaSpinner.getModel().getValue(),sesso,estero, luogoNascita));
            }
        });

        //Gestione evento al click del bottone inserimento
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean campiValidi = false;

                if(onlyLettersSpaces(campoNome.getText()) && campoNome.getText().length()>=3){
                    if(onlyLettersSpaces(campoCognome.getText()) && campoCognome.getText().length()>=3){
                        if(isBeforeToday((Date)dataNascitaSpinner.getModel().getValue())){
                            if(comuniBox.getSelectedIndex()>= 0 || esteriBox.getSelectedIndex() >= 0){
                                if(campoCF.getText().length() == 16) {
                                    if(isBeforeToday((Date) dataAssunzioneSpinner.getModel().getValue())){
                                        if(Pattern.matches("[a-zA-Z]+", campoStipendio.getText()) == false
                                                && campoStipendio.getText().length() > 2){
                                            campiValidi = true;
                                            char sesso;
                                            if (maschioButton.isSelected()) sesso = 'm';
                                            else sesso = 'f';
                                            String luogoDiNascita;
                                            if(italiaButton.isSelected()) luogoDiNascita = (String) comuniBox.getSelectedItem();
                                            else luogoDiNascita = (String) esteriBox.getSelectedItem();
                                            boolean checkCF;
                                            if(controller.calcoloCF(campoCognome.getText(), campoNome.getText(),
                                                    (Date) dataNascitaSpinner.getValue(),sesso,
                                                    esteroButton.isSelected(),luogoDiNascita).equals(campoCF.getText())) checkCF = true;
                                            else checkCF = false;
                                            boolean dirigente;
                                            if(isDirigente.isSelected()) dirigente = true;
                                            else dirigente = false;
                                            if(!checkCF){
                                                int dialogResult = JOptionPane.showConfirmDialog (null, "Il codice fiscale potrebbe essere errato. Confermare questo codice?",
                                                        "Warning",JOptionPane.YES_NO_OPTION);
                                                if(dialogResult == JOptionPane.YES_OPTION) checkCF = true;
                                            }
                                            if(checkCF){
                                                if( controller.inserisciImpiegato(campoNome.getText(), campoCognome.getText(),sesso,
                                                        (Date)dataNascitaSpinner.getValue(),luogoDiNascita,campoCF.getText(),
                                                        (Date)dataAssunzioneSpinner.getValue(), campoStipendio.getText(), dirigente) ) {

                                                    JOptionPane.showMessageDialog(null, "Inserimento completato");

                                                    chiamante.setVisible(true);

                                                    dispose();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if(!campiValidi) JOptionPane.showMessageDialog(questoFrame, "Attenzione campi non correttamente compilati");
            }
        });

        //Chiusura
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                chiamante.setVisible(true);

                dispose();
            }
        });

    }

    /////////////////////////////////////////////FUNZIONI AUSILIARI//////////////////////////////////////////////////////////////////////

    private boolean isOnlyLetters(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean onlyLettersSpaces(String s){
        for(int i=0;i<s.length();i++){
            char ch = s.charAt(i);
            if (Character.isLetter(ch) || ch == ' ') {
                continue;
            }
            return false;
        }
        return true;
    }

    private static boolean isBeforeToday(Date data)  {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(data);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        String dataString = Integer.toString(year);
        if (month < 10) {
            dataString = dataString + "/" + "0" + Integer.toString(month);
        } else dataString = dataString + "/" + Integer.toString(month);
        if(day < 10){
            dataString = dataString + "/" + "0" + Integer.toString(day);
        } else {
            dataString = dataString + "/" + Integer.toString(day);
        }

        cal.setTime(new Date());
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        String dataAttualeString = Integer.toString(year);
        if (month < 10) {
            dataAttualeString = dataAttualeString + "/" + "0" + Integer.toString(month);
        } else dataAttualeString = dataAttualeString + "/" + Integer.toString(month);
        if(day < 10){
            dataAttualeString = dataAttualeString + "/" + "0" + Integer.toString(day);
        } else {
            dataAttualeString = dataAttualeString + "/" + Integer.toString(day);
        }

        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date1 = sdf.parse(dataString);
            Date date2 = sdf.parse(dataAttualeString);

            if (date1.after(date2)) return false;
            else return true;

        } catch(ParseException e){

            System.out.println("Eccezione Parse");
            return false;
        }
    }

}
