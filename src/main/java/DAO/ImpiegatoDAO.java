package DAO;

import java.util.ArrayList;
import java.util.Date;

/**
 * The interface Impiegato dao.
 */
public interface ImpiegatoDAO {
    /**
     * Recupera i cup dei progetti che rappresentano i contributi passati dell'impiegato del codice fiscale inserito
     * come primo parametro.
     *
     * @param CF       codice fiscale dell'impiegato di cui intendiamo reperire i contributi passati.
     * @param progetti progetti per i quali l'impiegato a contribuito in passato.
     */
    public void readContributiPassati(String CF, ArrayList<String> progetti);

    /**
     * Recupera le informazioni riguardanti i lavori di cui si sta occupando l'impiegato con codice fiscale passato
     * come stringa al primo parametro.
     *
     * @param cf       codice fiscale dell'impiegato di cui si intende recuperare i lavori.
     * @param ore      ore di lavoro settimanale assegnate all'impiegato sul progetto.
     * @param progetti cup del progetto su cui l'impiegato lavora.
     */
    public void readLavorare(String cf, ArrayList<Integer> ore, ArrayList<String> progetti);

    /**
     * Sostituisce nel db il laboratorio di afferenza dell'impiegato il cui codice fiscale è passato come stringa al primo
     * parametro. Il nuovo laboratorio di afferenza è indicato dal nome passato a secondo parametro ed il topic
     * al terzo.
     *
     * @param cf      codice fiscale dell'impiegato di cui si intende modificare l'afferenza.
     * @param nomeLab nome del nuovo laboratorio di afferenza per l'impiegato
     * @param topic   topic del nuovo laboratorio di afferenza per l'impiegato
     * @return the boolean
     */
    public boolean updateAfferenza(String cf, String nomeLab, String topic);

    /**
     * Inserisce in database un nuovo lavoro per l'impiegato con codice fiscale rappresentato al primo parametro, per
     * il progetto con cup rappresentato al secondo parametro, per un numero di ore rappresentato al terzo parametro.
     *
     * @param cf  codice fiscale dell'impiegato a cui si sta assegnando un nuovo lavoro.
     * @param cup cup del progetto a cui lavorerà l'impiegato.
     * @param ore ore di lavoro settimanali che l'impiegato avrà assegnate al progetto.
     * @return conferma dell'inserimento
     */
    public boolean insertLavorare(String cf, String cup, int ore);

    /**
     * Inserisce in database un contributo passato per l'impiegato con codice fiscale rappresentato al primo parametro, per
     * il progetto con cup rappresentato al secondo parametro.
     *
     * @param cf  codice fiscale dell'impiegato a cui si sta assegnando un contributo passato.
     * @param cup cup del progetto a cui ha lavorato l'impiegato.
     * @return the boolean
     */
    public boolean insertContributoPassato(String cf, String cup);
}
