package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The type Area impiegato.
 */
public class AreaImpiegato extends JFrame {

    /**
     * Instantiates a new Area impiegato.
     *
     * @param controller the controller
     * @param chiamante  the chiamante
     */
    public AreaImpiegato(Controller controller, JFrame chiamante){

        ///////////////////////////////////////IMPOSTAZIONI FINESTRA//////////////////////////////////////////////////////////////////////////

        super("Area impieagato");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1300,800);

        setLocationRelativeTo(null);

        setResizable(true);

        ImageIcon icona = new ImageIcon(getClass().getResource("/emp_nocolor.png"));

        setIconImage(icona.getImage());

        setLayout(new BorderLayout());

        JFrame questo = this;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////ZONA NORD: FILTRI DI RICERCA//////////////////////////////////////////////////////////////////

        //Gestione MacroFiltro (ricerca per macro categorie): PRIMA COMBO-BOX
        String[] filtroRicerca = {"Tutti gli impiegati", "Categoria", "Codice fiscale", "Sesso", "Nome", "Cognome", "Data assunzione", "Laboratorio","Progetto"};
        JComboBox ricercaPer = new JComboBox<>(filtroRicerca);
        ricercaPer.setSelectedIndex(-1);

        //Gestione filtro per ricerca specifica: SECONDA COMBO-BOX
        JComboBox ricercaSpecifica = new JComboBox<>();
        ricercaSpecifica.setEnabled(false);
        DefaultComboBoxModel modelRicerca = new DefaultComboBoxModel();
        ricercaSpecifica.setModel(modelRicerca);

        //Gestione filtro per intervallo di data assunzione
        JPanel panelloIntervalliData = new JPanel(new FlowLayout());
        panelloIntervalliData.setVisible(false);
        //Gestione bordo panello principale
        Border bordoInternoPanelIntData = BorderFactory.createTitledBorder("Intervallo data");
        Border bordoEsternoPanelIntData = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinalePanelIntdata = BorderFactory.createCompoundBorder(bordoEsternoPanelIntData, bordoInternoPanelIntData);
        panelloIntervalliData.setBorder(bordoFinalePanelIntdata);
        //Campo data
        panelloIntervalliData.add(new JLabel("Data assunzione: yyyy-mm-dd: "));
        JTextField dataAssunzioneField = new JTextField(10);
        panelloIntervalliData.add(dataAssunzioneField);
        //Gestione radio button
        JRadioButton minoreuguale = new JRadioButton("<=");
        JRadioButton minore = new JRadioButton("<");
        JRadioButton uguale = new JRadioButton("=");
        JRadioButton maggioreUguale = new JRadioButton(">=");
        JRadioButton maggiore = new JRadioButton(">");
        ButtonGroup intervallo = new ButtonGroup();
        intervallo.add(minoreuguale);
        intervallo.add(minore);
        intervallo.add(uguale);
        intervallo.add(maggioreUguale);
        intervallo.add(maggiore);
        panelloIntervalliData.add(new JLabel("Intervallo di tempo da filtrare: "));
        panelloIntervalliData.add(minoreuguale);
        panelloIntervalliData.add(minore);
        panelloIntervalliData.add(uguale);
        panelloIntervalliData.add(maggioreUguale);
        panelloIntervalliData.add(maggiore);
        //Bottone di ricerca
        JButton cercaButton = new JButton("Cerca");
        panelloIntervalliData.add(cercaButton);

        //Gestione filtro per laboratorio
        JPanel panelloFiltroLab = new JPanel(new FlowLayout());
        panelloFiltroLab.setVisible(false);
        //Gestione bordo panello principale
        Border bordoInternoFiltroLab = BorderFactory.createTitledBorder("Filtraggio per laboratorio");
        Border bordoEsternoFiltroLab = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinaleFiltroLab = BorderFactory.createCompoundBorder(bordoEsternoFiltroLab, bordoInternoFiltroLab);
        panelloFiltroLab.setBorder(bordoFinaleFiltroLab);
        //Campi
        panelloFiltroLab.add(new JLabel("Nome laboratorio: "));
        JTextField nomeLabField = new JTextField(10);
        panelloFiltroLab.add(nomeLabField);
        panelloFiltroLab.add(new JLabel("Topc: "));
        JTextField topicLabField = new JTextField(10);
        panelloFiltroLab.add(topicLabField);
        //Bottone di ricerca
        JButton cercaPerLab = new JButton("Cerca");
        panelloFiltroLab.add(cercaPerLab);

        //Impostazione panello Nord
        JPanel panelNord = new JPanel();
        panelNord.setLayout(new BoxLayout(panelNord, BoxLayout.PAGE_AXIS));

        JPanel rigaLabel = new JPanel();
        rigaLabel.setLayout(new BoxLayout(rigaLabel, BoxLayout.X_AXIS));

        JLabel cercaLabel = new JLabel("Cerca per:");
        rigaLabel.add(cercaLabel);
        rigaLabel.add(Box.createHorizontalGlue());

        panelNord.add(rigaLabel);
        panelNord.add(ricercaPer);
        panelNord.add(ricercaSpecifica);
        panelNord.add(panelloIntervalliData);
        panelNord.add(panelloFiltroLab);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////AREA OVEST: INFO CARRIERA IMPIEGATO///////////////////////////////////////////

        //inizializzazione panello ovest
        JPanel panelOvest = new JPanel();
        panelOvest.setLayout(new BoxLayout(panelOvest, BoxLayout.PAGE_AXIS));
        //Gestione bordo
        Border bordoInternoOvest = BorderFactory.createTitledBorder("Info impiegato");
        Border bordoEsternoOvest = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinaleOvest = BorderFactory.createCompoundBorder(bordoEsternoOvest, bordoInternoOvest);
        panelOvest.setBorder(bordoFinaleOvest);
        //Dimensioni
        Dimension d1 = new Dimension();
        d1.width = 470;
        panelOvest.setPreferredSize(d1);

        //Gestione tabella carriera
        JTable tabellaCarriera = new JTable();
        String[] intestazioneTabellaCarriera = {"Data assunzione", "Passaggio middle", "Passaggio Senior", "Passaggio Dirigente"};
        DefaultTableModel modelTableCarriera = new DefaultTableModel(intestazioneTabellaCarriera, 0);
        tabellaCarriera.setModel(modelTableCarriera);
        //Panello scrol per tabella carriera
        JScrollPane panelOvestSuperiore = new JScrollPane(tabellaCarriera);
        //Bordo panello tabella carriera
        Border bordoInternoNordOvest = BorderFactory.createTitledBorder("Carriera impiegato");
        Border bordoEsternoNordOvest = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinaleNordOvest = BorderFactory.createCompoundBorder(bordoEsternoNordOvest, bordoInternoNordOvest);
        panelOvestSuperiore.setBorder(bordoFinaleNordOvest);

        //Gestione tabella laboratori di cui l'impiegato è responsabile
        JTable tabellaLaboratoriResponsabile = new JTable();
        String[] intestazioneTabellaResponsabile = {"Nome laboratorio", "Topic"};
        DefaultTableModel modelTableLabResponsabile = new DefaultTableModel(intestazioneTabellaResponsabile, 0);
        tabellaLaboratoriResponsabile.setModel(modelTableLabResponsabile);
        //Panello scrol per tabella laboratori responsabile
        JScrollPane panelOvestInferiore = new JScrollPane(tabellaLaboratoriResponsabile);
        //Bordo pnanello tabella laboratori responsabile
        Border bordoInternoSudOvest = BorderFactory.createTitledBorder("Laboratori di cui è responsabile");
        Border bordoEsternoSudOvest = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinaleSudOvest = BorderFactory.createCompoundBorder(bordoEsternoSudOvest, bordoInternoSudOvest);
        panelOvestInferiore.setBorder(bordoFinaleSudOvest);

        //Gestione tabella progetti di cui l'impiegato è referente
        JTable tabellaProgettiReferente = new JTable();
        String[] intestazioneTabellaProgettiReferente = {"Nome progetto", "CUP"};
        DefaultTableModel modelTableProgReferente = new DefaultTableModel(intestazioneTabellaProgettiReferente, 0);
        tabellaProgettiReferente.setModel(modelTableProgReferente);
        //Panello scrol per tabella progetti referente
        JScrollPane panelOvestProgRef = new JScrollPane(tabellaProgettiReferente);
        //Bordo pannello tabella progetti referenti
        Border bordoInternoProgRef = BorderFactory.createTitledBorder("Progetti di cui è referente");
        Border bordoEsternoProgRef = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinaleProgRef = BorderFactory.createCompoundBorder(bordoEsternoProgRef, bordoInternoProgRef);
        panelOvestProgRef.setBorder(bordoFinaleProgRef);

        //Gestione tabella progetti di cui l'impiegato è responsabile
        JTable tabellaProgettiResponsabile = new JTable();
        String[] intestazioneTabellaProgettiResponsabile = {"Nome progetto", "CUP"};
        DefaultTableModel modelTableProgResponsabile = new DefaultTableModel(intestazioneTabellaProgettiResponsabile, 0);
        tabellaProgettiResponsabile.setModel(modelTableProgResponsabile);
        //Panello scrol per tabella progetti responsabile
        JScrollPane panelOvestProgResp = new JScrollPane(tabellaProgettiResponsabile);
        //Bordo pannello tabella progetti responsabile
        Border bordoInternoProgResp = BorderFactory.createTitledBorder("Progetti di cui è responsabile");
        Border bordoEsternoProgResp = BorderFactory.createEmptyBorder(0,5,5,5);
        Border bordoFinaleProgResp = BorderFactory.createCompoundBorder(bordoEsternoProgResp, bordoInternoProgResp);
        panelOvestProgResp.setBorder(bordoFinaleProgResp);

        //Panello riga bottoni relativi alla tabella carriera
        JPanel rigaBottoniCarriera = new JPanel(new FlowLayout());
        //Bottone visualizza carriera
        JButton visualizzaCarriera = new JButton("Visualizza carriera");
        rigaBottoniCarriera.add(visualizzaCarriera);

        panelOvest.add(panelOvestSuperiore);
        panelOvest.add(panelOvestInferiore);
        panelOvest.add(panelOvestProgRef);
        panelOvest.add(panelOvestProgResp);
        //panelOvest.add(rigaBottoniCarriera);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////AREA CENTARLE: TABELLA IMPIEGATI/////////////////////////////////////////////////////////////////

        //Gestione tabella
        JTable table = new JTable();
        table.setEnabled(true);
        String[] colonne = {"Nome", "Cognome", "Sesso", "Data di nascita", "Codice Fiscale", "Stipendio", "Nome laboratorio afferente", "Topic"};
        DefaultTableModel modelTable = new DefaultTableModel(colonne, 0);
        table.setModel(modelTable);
        JScrollPane panelTable = new JScrollPane(table);

        //Impostazioni panello
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));
        //Gestione bordo
        Border bordoInterno2 = BorderFactory.createTitledBorder("Risultati ricerca");
        Border bordoEsterno2 = BorderFactory.createEmptyBorder(0,5,5,5);
        panelCenter.setBorder(bordoInterno2);
        panelCenter.add(panelTable);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////AREA EST: TABELLE RELATIVE AI PROG ASSEGNATI////////////////////////////////////////////////////////////////////////////////
        JPanel panelEst = new JPanel();
        panelEst.setLayout(new BoxLayout(panelEst,BoxLayout.PAGE_AXIS));
        //Gestione bordo
        Border bordoInteroEst = BorderFactory.createTitledBorder("Info progetti assegnati");
        Border bordoEsternoEst = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleEst = BorderFactory.createCompoundBorder(bordoEsternoEst, bordoInteroEst);
        panelEst.setBorder(bordoFinaleEst);
        //Dimensioni
        Dimension d2 = new Dimension();
        d2.width = 300;
        panelEst.setPreferredSize(d2);

        //Gestione tabella progetti in corso assegnati
        JTable tabellaProgettiInCorso = new JTable();
        String colonneTabellaProgettiInCorso[] = {"Nome progetto", "CUP", "Ore assegnate"};
        DefaultTableModel modelloProgettiInCorso = new DefaultTableModel(colonneTabellaProgettiInCorso,0);
        tabellaProgettiInCorso.setModel(modelloProgettiInCorso);
        //Gestione JScrollPanel
        JScrollPane panelTabellaProgettiInCorso =new JScrollPane(tabellaProgettiInCorso);
        //Gestione bordo
        Border bordoInteroProg = BorderFactory.createTitledBorder("Progetti in corso assegnati");
        Border bordoEsternoProg = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleProg = BorderFactory.createCompoundBorder(bordoEsternoProg, bordoInteroProg);
        panelTabellaProgettiInCorso.setBorder(bordoFinaleProg);

        //Gestione tabella progetti in corso di cui l'impiegato ha contribuito
        JTable tabellaProgettiInCorsoContribuito = new JTable();
        String[] colonneProgContribuito = {"Nome progetto", "CUP"};
        DefaultTableModel modelloProgettiInCorsoContribuito = new DefaultTableModel(colonneProgContribuito,0);
        tabellaProgettiInCorsoContribuito.setModel(modelloProgettiInCorsoContribuito);
        //Gestione JScroll
        JScrollPane panelTabellaProgInCorsoContr = new JScrollPane(tabellaProgettiInCorsoContribuito);
        //Gestione bordo
        Border bordoInteroProgInCorsoContr = BorderFactory.createTitledBorder("Progetti in corso a cui ha contribuito");
        Border bordoEsternoProgInCorsoContr = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleProgInCorsoContr = BorderFactory.createCompoundBorder(bordoEsternoProgInCorsoContr, bordoInteroProgInCorsoContr);
        panelTabellaProgInCorsoContr.setBorder(bordoFinaleProgInCorsoContr);

        //Gestione tabella progetti conclusi di cui l'impiegato ha contribuito
        JTable tabellaProgConclusiContr = new JTable();
        String[] colonneProgConclusiContr = {"Nome progetto", "CUP"};
        DefaultTableModel modelloProgConclusiContr = new DefaultTableModel(colonneProgConclusiContr,0);
        tabellaProgConclusiContr.setModel(modelloProgConclusiContr);
        //Gestione JScroll
        JScrollPane panelTabellaProgIConclusiContr = new JScrollPane(tabellaProgConclusiContr);
        //Gestione bordo
        Border bordoInteroProgConclusiContr = BorderFactory.createTitledBorder("Progetti conclusi a cui ha contribuito");
        Border bordoEsternoProgConclusiContr = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleProgConclusiContr = BorderFactory.createCompoundBorder(bordoEsternoProgConclusiContr, bordoInteroProgConclusiContr);
        panelTabellaProgIConclusiContr.setBorder(bordoFinaleProgConclusiContr);

        panelEst.add(panelTabellaProgettiInCorso);
        panelEst.add(panelTabellaProgInCorsoContr);
        panelEst.add(panelTabellaProgIConclusiContr);

        JLabel ore = new JLabel();
        ore.setFont(new Font("Serif", Font.PLAIN, 18));
        JPanel panelOre = new JPanel(new FlowLayout());
        panelOre.add(ore);

        panelEst.add(ore);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////AREA SUD: BOTTONE INFO/////////////////////////////////////////////////////////////////

        //Pannello principale sud
        JPanel panelSud = new JPanel();
        panelSud.setLayout(new BoxLayout(panelSud,BoxLayout.PAGE_AXIS));
        panelSud.setMaximumSize(new Dimension(600,600));
        //Gestione bordo
        Border bordoInteroSud = BorderFactory.createTitledBorder("Menu opzioni");
        Border bordoEsternoSud = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleSud = BorderFactory.createCompoundBorder(bordoEsternoSud, bordoInteroSud);
        panelSud.setBorder(bordoFinaleSud);

        //Dichiarazione dei bottoni di opzioni
        JButton setLabButton = new JButton("Assegna/Cambia laboratorio");
        JButton gestioneLavoriButton = new JButton("Gestione lavori");
        JButton eliminaImpiegatoButton = new JButton("Elimina impiegato");
        JButton inserisciImpiegatoButton = new JButton("Inserisci impiegato");
        JButton homeButton = new JButton("Torna alla home");

        //Pannello riga bottoni di opzioni
        JPanel rigaOpzioniUnoBottoni = new JPanel(new FlowLayout());

        rigaOpzioniUnoBottoni.add(setLabButton);
        rigaOpzioniUnoBottoni.add(gestioneLavoriButton);
        rigaOpzioniUnoBottoni.add(eliminaImpiegatoButton);
        rigaOpzioniUnoBottoni.add(inserisciImpiegatoButton);
        rigaOpzioniUnoBottoni.add(homeButton);

        //Modulo di cambio lab
        JPanel moduloSetLab = new JPanel(new FlowLayout());
        moduloSetLab.setVisible(false);
        moduloSetLab.add(new JLabel("Nome laboratorio: "));
        JTextField setNomeLabField = new JTextField(10);
        moduloSetLab.add(setNomeLabField);
        moduloSetLab.add(new JLabel("Topic laboratorio: "));
        JTextField setTopicLabField = new JTextField(10);
        moduloSetLab.add(setTopicLabField);
        JButton confermaSetLabButton = new JButton("Conferma");
        moduloSetLab.add(confermaSetLabButton);
        JButton annullaSetLabButton = new JButton("Annulla");
        moduloSetLab.add(annullaSetLabButton);
        //Gestione bordo
        Border bordoInteroSetLab = BorderFactory.createTitledBorder("Assegnazione/Modifica laboratorio");
        Border bordoEsternoSetLab = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleSetLab = BorderFactory.createCompoundBorder(bordoEsternoSetLab, bordoInteroSetLab);

        //Menu gestione lavori
        JPanel menuGestioneLavori = new JPanel(new FlowLayout());
        menuGestioneLavori.setVisible(false);
        JButton addLavoroButton = new JButton("Assegna progetto");
        menuGestioneLavori.add(addLavoroButton);
        JButton setOreButton = new JButton("Modifica ore assegnate");
        menuGestioneLavori.add(setOreButton);
        JButton concludiLavoroButton = new JButton("Concludi un lavoro");
        menuGestioneLavori.add(concludiLavoroButton);
        JButton backFromLav = new JButton("Torna indietro");
        menuGestioneLavori.add(backFromLav);
        //Gestione bordo
        Border bordoInteroMenuLav = BorderFactory.createTitledBorder("Gestione lavori");
        Border bordoEsternoMenuLav = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleMenuLav = BorderFactory.createCompoundBorder(bordoEsternoMenuLav, bordoInteroMenuLav);

        //Modulo setLavoro
        JPanel moduloSetLavoro = new JPanel(new FlowLayout());
        moduloSetLavoro.setVisible(false);
        moduloSetLavoro.add(new JLabel("CUP progetto: "));
        JTextField setCupField = new JTextField(10);
        moduloSetLavoro.add(setCupField);
        moduloSetLavoro.add(new JLabel("Ore settimanale da assegnare: "));
        JTextField setOreField = new JTextField(10);
        moduloSetLavoro.add(setOreField);
        JButton confermaLavoroButton = new JButton("Conferma assegnazione");
        moduloSetLavoro.add(confermaLavoroButton);
        JButton backFromAssegnazione = new JButton("Torna indietro");
        moduloSetLavoro.add(backFromAssegnazione);
        //Gestione bordo
        Border bordoInteroAssLav = BorderFactory.createTitledBorder("Assegnazione lavoro");
        Border bordoEsternoAssLav = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleAssLav = BorderFactory.createCompoundBorder(bordoEsternoAssLav, bordoInteroAssLav);

        //Modulo setOre
        JPanel moduloSetOre = new JPanel(new FlowLayout());
        moduloSetOre.setVisible(false);
        moduloSetOre.add(new JLabel("Nuove ore da assegnare: "));
        JTextField changeOreField = new JTextField(10);
        moduloSetOre.add(changeOreField);
        JButton confermaChangeOreButton = new JButton("Conferma");
        moduloSetOre.add(confermaChangeOreButton);
        JButton backFromChangeOre = new JButton("Torna indietro");
        moduloSetOre.add(backFromChangeOre);
        //Gestione bordo
        Border bordoInteroCambioOre = BorderFactory.createTitledBorder("Modifica ore di un lavoro");
        Border bordoEsternoCambioOre = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleCambioOre = BorderFactory.createCompoundBorder(bordoEsternoCambioOre, bordoInteroCambioOre);

        //Sezione concludi progetto
        JPanel panelConcludi = new JPanel(new FlowLayout());
        panelConcludi.setVisible(false);
        JButton selezioneProgettoButton = new JButton("Seleziona il progetto da concludere");
        panelConcludi.add(selezioneProgettoButton);
        JButton backFromConcludi = new JButton("Annulla");
        panelConcludi.add(backFromConcludi);
        //Gestione bordo
        Border bordoInteroConcludi = BorderFactory.createTitledBorder("Sezione concludi progetto");
        Border bordoEsternoConcludi = BorderFactory.createEmptyBorder(5,5,5,5);
        Border bordoFinaleConcludi = BorderFactory.createCompoundBorder(bordoEsternoConcludi, bordoInteroConcludi);

        //Dimensione bottoni
        setLabButton.setPreferredSize(new Dimension(200,50));
        gestioneLavoriButton.setPreferredSize(new Dimension(200,50));
        eliminaImpiegatoButton.setPreferredSize(new Dimension(200,50));
        inserisciImpiegatoButton.setPreferredSize(new Dimension(200,50));
        homeButton.setPreferredSize(new Dimension(200,50));

        panelSud.add(rigaOpzioniUnoBottoni);
        panelSud.add(moduloSetLab);
        panelSud.add(menuGestioneLavori);
        panelSud.add(moduloSetLavoro);
        panelSud.add(moduloSetOre);
        panelSud.add(panelConcludi);

        //////////////////////////////////////////////////AGGIUNTA PANELLI///////////////////////////////////////////////////////////////////
        add(panelNord, BorderLayout.NORTH);
        add(panelOvest, BorderLayout.WEST);
        add(panelCenter, BorderLayout.CENTER);
        add(panelEst, BorderLayout.EAST);
        add(panelSud, BorderLayout.SOUTH);

        setVisible(true);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////GESTIONE EVENTI/////////////////////////////////////////////////////////////////////////////

        //Gestione evento sulla prima barra di ricerca
        ricercaPer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int indexRicerca = ricercaPer.getSelectedIndex();

                switch (indexRicerca) {

                    case 0:

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        ricercaSpecifica.setEnabled(false);

                        panelloIntervalliData.setVisible(false);

                        panelloFiltroLab.setVisible(false);

                        controller.setAllImpiegati(modelTable);

                        break;

                    case 1:

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        modelRicerca.addElement("Junior");

                        modelRicerca.addElement("Middle");

                        modelRicerca.addElement("Senior");

                        modelRicerca.addElement("Dirigente");

                        ricercaSpecifica.setEnabled(true);

                        ricercaSpecifica.setSelectedIndex(-1);

                        panelloIntervalliData.setVisible(false);

                        panelloFiltroLab.setVisible(false);

                        break;

                    case 2:

                        ricercaSpecifica.setEnabled(false);

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        ricercaSpecifica.setEnabled(false);

                        panelloIntervalliData.setVisible(false);

                        panelloFiltroLab.setVisible(false);

                        String inputDialog = JOptionPane.showInputDialog("Inserisci il codice fiscale");

                        if(inputDialog != null && inputDialog.length() == 16) {
                            controller.setImpiegatiPerCF(modelTable, inputDialog);
                            if(modelTable.getRowCount()<1)
                                JOptionPane.showMessageDialog(null,"Impiegato non trovato");
                        }
                        else
                            JOptionPane.showMessageDialog(null,"Il codice fiscale deve contenere 16 caratteri");

                        break;

                    case 3:

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        modelRicerca.addElement("Femmina");

                        modelRicerca.addElement("Maschio");

                        ricercaSpecifica.setEnabled(true);

                        ricercaSpecifica.setSelectedIndex(-1);

                        panelloIntervalliData.setVisible(false);

                        panelloFiltroLab.setVisible(false);

                        break;

                    case 4:

                        ricercaSpecifica.setEnabled(false);

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        ricercaSpecifica.setEnabled(false);

                        panelloIntervalliData.setVisible(false);

                        panelloFiltroLab.setVisible(false);

                        inputDialog = JOptionPane.showInputDialog("Inserisci il nome");

                        controller.setImpiegatiPerNome(modelTable, inputDialog);


                        break;

                    case 5:

                        ricercaSpecifica.setEnabled(false);

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        ricercaSpecifica.setEnabled(false);

                        panelloIntervalliData.setVisible(false);

                        panelloFiltroLab.setVisible(false);

                        inputDialog = JOptionPane.showInputDialog("Inserisci il cognome");

                        controller.setImpiegatiPerCognome(modelTable, inputDialog);

                        break;


                    case 6:

                        ricercaSpecifica.setEnabled(false);

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        ricercaSpecifica.setEnabled(false);

                        panelloIntervalliData.setVisible(true);

                        panelloFiltroLab.setVisible(false);

                        break;

                    case 7:

                        ricercaSpecifica.setEnabled(false);

                        panelloIntervalliData.setVisible(false);

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        panelloFiltroLab.setVisible(true);

                        break;

                    case 8:

                        ricercaSpecifica.setEnabled(false);

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        inputDialog = JOptionPane.showInputDialog("Inserisci il cup");

                        controller.setImpiegatiPerProgettoAssegnato(modelTable, inputDialog);

                        break;



                    default:

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        modelRicerca.removeAllElements();

                        ricercaSpecifica.setEnabled(false);

                        break;

                }
            }
        });

        //Gestione evento sulla seconda barra di ricerca
        ricercaSpecifica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                svuotaModelTable(modelTable);
                svuotaModelTable(modelTableCarriera);
                svuotaModelTable(modelTableLabResponsabile);
                svuotaModelTable(modelTableProgReferente);
                svuotaModelTable(modelTableProgResponsabile);
                svuotaModelTable(modelloProgettiInCorso);
                svuotaModelTable(modelloProgettiInCorsoContribuito);
                svuotaModelTable(modelloProgConclusiContr);

                ComboBoxModel model_tmp = ricercaSpecifica.getModel();
                Object objectModel = model_tmp.getSelectedItem();
                String casoModel = "null";
                if(objectModel != null) {

                    casoModel = objectModel.toString();
                }

                switch (casoModel) {

                    case "Junior":

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setPerCategoria(modelTable,1);

                        break;

                    case "Middle":

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setPerCategoria(modelTable,2);

                        break;

                    case "Senior":

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setPerCategoria(modelTable,3);

                        break;

                    case "Dirigente":

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setPerCategoria(modelTable,4);

                        break;

                    case "Femmina":

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setImpiegatiPerSesso(modelTable, 'f');

                        break;

                    case "Maschio":

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setImpiegatiPerSesso(modelTable, 'm');

                        break;
                }
            }
        });

        //Gestione evento bottone filtraggio data assunzione
        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                svuotaModelTable(modelTable);
                svuotaModelTable(modelTableCarriera);
                svuotaModelTable(modelTableLabResponsabile);
                svuotaModelTable(modelTableProgReferente);
                svuotaModelTable(modelTableProgResponsabile);
                svuotaModelTable(modelloProgettiInCorso);
                svuotaModelTable(modelloProgettiInCorsoContribuito);
                svuotaModelTable(modelloProgConclusiContr);

                String dataInserita = dataAssunzioneField.getText();

                boolean flag = false;

                if(!isValidDate(dataInserita) ) {

                    JOptionPane.showMessageDialog(questo, "Formato data non valido");
                } else {

                    if(minoreuguale.isSelected()) {

                        flag = true;

                        controller.setImpiegatiPerData(modelTable, fromStringToDate(dataInserita), "<=");

                    }

                    if(minore.isSelected()) {

                        flag = true;

                        controller.setImpiegatiPerData(modelTable, fromStringToDate(dataInserita), "<");

                    }

                    if(uguale.isSelected()) {

                        flag = true;

                        controller.setImpiegatiPerData(modelTable, fromStringToDate(dataInserita), "=");
                    }

                    if(maggioreUguale.isSelected()) {

                        flag = true;

                        controller.setImpiegatiPerData(modelTable, fromStringToDate(dataInserita), ">=");

                    }

                    if(maggiore.isSelected()) {

                        flag = true;

                        controller.setImpiegatiPerData(modelTable, fromStringToDate(dataInserita), ">");
                    }

                    if(!flag) {

                        JOptionPane.showMessageDialog(questo, "Attenzione selezionare l'intervallo di tempo su cui filtrare");
                    }
                }
            }
        });

        //Gestione bottone filtraggio per laboratorio
        cercaPerLab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                panelloFiltroLab.setVisible(true);

                svuotaModelTable(modelTable);
                svuotaModelTable(modelTableCarriera);
                svuotaModelTable(modelTableLabResponsabile);
                svuotaModelTable(modelTableProgReferente);
                svuotaModelTable(modelTableProgResponsabile);
                svuotaModelTable(modelloProgettiInCorso);
                svuotaModelTable(modelloProgettiInCorsoContribuito);
                svuotaModelTable(modelloProgConclusiContr);

                String[] lab = new String[2];
                if(nomeLabField.getText()!=null && topicLabField.getText()!=null) {
                    lab[0] = nomeLabField.getText();
                    lab[1] = topicLabField.getText();
                }

                controller.setImpiegatiPerLaboratorio(modelTable, lab);
            }
        });



        //Gestione evento riga tabella impiegati (Visualizzazione info impiegato)
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                int rigaSelezionata = table.getSelectedRow();

                if(rigaSelezionata >= 0) {

                    svuotaModelTable(modelTableCarriera);
                    svuotaModelTable(modelTableLabResponsabile);
                    svuotaModelTable(modelTableProgReferente);
                    svuotaModelTable(modelTableProgResponsabile);
                    svuotaModelTable(modelloProgettiInCorso);
                    svuotaModelTable(modelloProgettiInCorsoContribuito);
                    svuotaModelTable(modelloProgConclusiContr);

                    controller.setModelliInfoImpiegato(modelTableCarriera, modelTableLabResponsabile, modelTableProgReferente,
                            modelTableProgResponsabile, modelloProgettiInCorso, modelloProgettiInCorsoContribuito,
                            modelloProgConclusiContr, (String) modelTable.getValueAt(rigaSelezionata, 4));
                }
            }
        });

        //Gestione bottone setLab
        setLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                rigaOpzioniUnoBottoni.setVisible(false);

                moduloSetLab.setVisible(true);

                panelSud.setBorder(bordoFinaleSetLab);
            }
        });

        //Gestione bottone di confermaSetLab
        confermaSetLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int rigaSelezionata = table.getSelectedRow();

                if(rigaSelezionata >= 0){

                    String cfSelezionato = (String) modelTable.getValueAt(rigaSelezionata,4);

                    if(controller.updateAfferenzaImp(cfSelezionato, setNomeLabField.getText(), setTopicLabField.getText())){

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        setNomeLabField.setText("");
                        setTopicLabField.setText("");
                    }

                }else JOptionPane.showMessageDialog(null,"Seleziona l'impiegato a cui assegnare il laboratorio");
            }
        });

        //Gestione bottone annullaSetLab
        annullaSetLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setNomeLabField.setText("");
                setTopicLabField.setText("");

                moduloSetLab.setVisible(false);
                rigaOpzioniUnoBottoni.setVisible(true);
                panelSud.setBorder(bordoFinaleSud);
            }
        });

        //Gestione bottone "gestione lavori"
        gestioneLavoriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                rigaOpzioniUnoBottoni.setVisible(false);
                menuGestioneLavori.setVisible(true);
                panelSud.setBorder(bordoFinaleMenuLav);
            }
        });

        //gestione bottone "torna indietro" del menu lavori
        backFromLav.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menuGestioneLavori.setVisible(false);
                rigaOpzioniUnoBottoni.setVisible(true);
                panelSud.setBorder(bordoFinaleSud);
            }
        });

        //gestione bottone "assegna progetto"
        addLavoroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menuGestioneLavori.setVisible(false);
                moduloSetLavoro.setVisible(true);
                panelSud.setBorder(bordoFinaleAssLav);
            }
        });

        //gestione bottone "indietro" dal menu di assegnazione lavoro
        backFromAssegnazione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setNomeLabField.setText("");
                setTopicLabField.setText("");

                moduloSetLavoro.setVisible(false);
                menuGestioneLavori.setVisible(true);
                panelSud.setBorder(bordoFinaleMenuLav);


            }
        });

        //Gestione bottone "conferma" nel modulo di assegnazione lavoro
        confermaLavoroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int rigaSelezionata = table.getSelectedRow();

                if(rigaSelezionata >= 0) {

                    boolean isIntero = false;

                    try {
                        Integer.parseInt(setOreField.getText());
                        isIntero = true;
                    } catch (NumberFormatException ex) {
                        isIntero = false;
                    } catch (NullPointerException ex) {
                        isIntero = false;
                    }

                    if (isIntero && Integer.parseInt(setOreField.getText()) > 0) {

                        String cf = (String) modelTable.getValueAt(rigaSelezionata,4);

                       if (controller.insertLavorare(cf,setCupField.getText(),Integer.parseInt(setOreField.getText()))){

                           //Aggiornamento tabelle
                           svuotaModelTable(modelTableCarriera);
                           svuotaModelTable(modelTableLabResponsabile);
                           svuotaModelTable(modelTableProgReferente);
                           svuotaModelTable(modelTableProgResponsabile);
                           svuotaModelTable(modelloProgettiInCorso);
                           svuotaModelTable(modelloProgettiInCorsoContribuito);
                           svuotaModelTable(modelloProgConclusiContr);

                           controller.setModelliInfoImpiegato(modelTableCarriera, modelTableLabResponsabile, modelTableProgReferente,
                                   modelTableProgResponsabile, modelloProgettiInCorso, modelloProgettiInCorsoContribuito,
                                   modelloProgConclusiContr, (String) modelTable.getValueAt(rigaSelezionata, 4));

                           setCupField.setText("");
                           setOreField.setText("");
                       }

                    } else JOptionPane.showMessageDialog(null, "campo ore non valido, assegnare un intero positivo");
                }else JOptionPane.showMessageDialog(null, "Selezionare l'impiegato a cui assegnare il lavoro");
            }
        });

        //Gestione bottone "modifica ore"
        setOreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menuGestioneLavori.setVisible(false);
                moduloSetOre.setVisible(true);
                panelSud.setBorder(bordoFinaleCambioOre);

            }
        });

        //Bottone conferma nel modulo cambio ore
        confermaChangeOreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean isIntero = false;

                try {
                    Integer.parseInt(changeOreField.getText());
                    isIntero = true;
                } catch (NumberFormatException ex) {
                    isIntero = false;
                } catch (NullPointerException ex) {
                    isIntero = false;
                }

                int rigaImpSelezionato = table.getSelectedRow();

                int rigaProgettoSelezionata = tabellaProgettiInCorso.getSelectedRow();

                if(rigaProgettoSelezionata >= 0){

                    if(isIntero && Integer.parseInt(changeOreField.getText()) > 0){

                        if(table.getRowCount() > 0){

                            boolean update = false;

                            update = controller.updateOre((String) modelTable.getValueAt(rigaImpSelezionato,4),
                                    (String) modelloProgettiInCorso.getValueAt(rigaProgettoSelezionata,1),
                                    Integer.parseInt(changeOreField.getText()));
                            if(update){

                                //Aggiornamento tabelle
                                svuotaModelTable(modelTableCarriera);
                                svuotaModelTable(modelTableLabResponsabile);
                                svuotaModelTable(modelTableProgReferente);
                                svuotaModelTable(modelTableProgResponsabile);
                                svuotaModelTable(modelloProgettiInCorso);
                                svuotaModelTable(modelloProgettiInCorsoContribuito);
                                svuotaModelTable(modelloProgConclusiContr);

                                controller.setModelliInfoImpiegato(modelTableCarriera, modelTableLabResponsabile, modelTableProgReferente,
                                        modelTableProgResponsabile, modelloProgettiInCorso, modelloProgettiInCorsoContribuito,
                                        modelloProgConclusiContr, (String) modelTable.getValueAt(rigaImpSelezionato, 4));

                                changeOreField.setText("");

                                moduloSetOre.setVisible(false);
                                menuGestioneLavori.setVisible(true);
                                panelSud.setBorder(bordoFinaleMenuLav);
                            }
                        }

                    }else JOptionPane.showMessageDialog(null,"Errore formato ore, seleziona un intero positivo");

                }else JOptionPane.showMessageDialog(null, "Selezionare il progetto a cui modificare le ore");
            }
        });

        //Bottone indietro dal modulo cambio ore
        backFromChangeOre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                table.setEnabled(true);
                ricercaPer.setEnabled(true);
                svuotaModelTable(modelTable);
                svuotaModelTable(modelloProgettiInCorso);

                changeOreField.setText("");

                moduloSetOre.setVisible(false);
                menuGestioneLavori.setVisible(true);
                panelSud.setBorder(bordoFinaleMenuLav);
            }
        });

        //Bottone Concludi
        concludiLavoroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    menuGestioneLavori.setVisible(false);
                    panelConcludi.setVisible(true);
                    panelSud.setBorder(bordoFinaleConcludi);

            }
        });

        //Bottone selezione progetto nella sezione Concludi
        selezioneProgettoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean flag = false;

                int progettoSelezionato = tabellaProgettiInCorso.getSelectedRow();

                int impSelezionato = table.getSelectedRow();

                if(progettoSelezionato >= 0){

                    flag = controller.insertLavoroContribuito((String) modelTable.getValueAt(impSelezionato,4),
                            (String) modelloProgettiInCorso.getValueAt(progettoSelezionato,1));

                    if(flag){

                        //Aggiornamento tabelle
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                        svuotaModelTable(modelloProgConclusiContr);

                        controller.setModelliInfoImpiegato(modelTableCarriera, modelTableLabResponsabile, modelTableProgReferente,
                                modelTableProgResponsabile, modelloProgettiInCorso, modelloProgettiInCorsoContribuito,
                                modelloProgConclusiContr, (String) modelTable.getValueAt(impSelezionato, 4));

                        panelConcludi.setVisible(false);
                        menuGestioneLavori.setVisible(true);
                        panelSud.setBorder(bordoFinaleMenuLav);

                    }

                }else JOptionPane.showMessageDialog(null,"Selezionare il progetto da concludere");
            }
        });

        //Bottone torna indietro nella sezione Concludi
        backFromConcludi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                table.setEnabled(true);
                ricercaPer.setEnabled(true);

                panelConcludi.setVisible(false);
                menuGestioneLavori.setVisible(true);
                panelSud.setBorder(bordoFinaleMenuLav);
            }
        });

        //Bottone elimina impiegato
        eliminaImpiegatoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int rigaImpSelezionata = table.getSelectedRow();

                if(rigaImpSelezionata >= 0){

                    if(controller.deleteImpiegato((String) modelTable.getValueAt(rigaImpSelezionata,4))) {

                        svuotaModelTable(modelTable);
                        svuotaModelTable(modelTableCarriera);
                        svuotaModelTable(modelTableLabResponsabile);
                        svuotaModelTable(modelTableProgReferente);
                        svuotaModelTable(modelTableProgResponsabile);
                        svuotaModelTable(modelloProgettiInCorso);
                        svuotaModelTable(modelloProgConclusiContr);
                        svuotaModelTable(modelloProgettiInCorsoContribuito);
                    }

                }else JOptionPane.showMessageDialog(null,"Seleziona l'impiegato da eliminare");
            }
        });

        //Bottone inserisci impiegato
        inserisciImpiegatoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setVisible(false);

                svuotaModelTable(modelTable);
                svuotaModelTable(modelTableCarriera);
                svuotaModelTable(modelTableLabResponsabile);
                svuotaModelTable(modelTableProgReferente);
                svuotaModelTable(modelTableProgResponsabile);
                svuotaModelTable(modelloProgettiInCorso);
                svuotaModelTable(modelloProgettiInCorsoContribuito);
                svuotaModelTable(modelloProgConclusiContr);

                setVisible(false);

                new AggiungiImpiegato(controller,questo);
            }
        });

        //Bottone per tornare alla home
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                chiamante.setVisible(true);

                dispose();
            }
        });

    }

    private void svuotaModelTable(DefaultTableModel model) {

        while(model.getRowCount() > 0) {

            model.removeRow(0);
        }

    }

    private boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    private Date fromStringToDate(String data){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(data, formatter);

        Date date = java.sql.Date.valueOf(localDate);

        return date;

    }
}
