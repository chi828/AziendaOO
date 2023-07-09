package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Impiegato.
 */
public class Impiegato {

    private String nome;
    private String cognome;
    private char sesso;
    private Date dataDiNascita;
    private String luogoDiNascita;
    private String cf;
    private Date dataDiAssunzione;
    private float stipendio;
    private ArrayList<Lavorare> lavoriProgettiAssegnati;
    private ArrayList<Progetto> contributiPassati;
    private Laboratorio afferenza;
    private Dirigente dirigente;

    /**
     * Costruttore per impiegato non dirigente. Non consente l'inizializzazione dell'attributo dirigente
     * che rimarrà a null.
     *
     * @param nome             the nome
     * @param cognome          the cognome
     * @param sesso            the sesso
     * @param dataDiNascita    the data di nascita
     * @param luogoDiNascita   the luogo di nascita
     * @param cf               the cf
     * @param dataDiAssunzione the data di assunzione
     * @param stipendio        the stipendio
     * @param laboratorio      the laboratorio
     */
//COSTRUTTORE PER IMPIEGATO NON DIRIGENTE
    public Impiegato(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita, String cf,
                     Date dataDiAssunzione, float stipendio, Laboratorio laboratorio){
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.dataDiNascita = dataDiNascita;
        this.luogoDiNascita = luogoDiNascita;
        this.cf = cf;
        this.dataDiAssunzione = dataDiAssunzione;
        this.stipendio = stipendio;
        this.afferenza = laboratorio;
        this.lavoriProgettiAssegnati = new ArrayList<Lavorare>();
        this.contributiPassati = new ArrayList<Progetto>();
    }

    /**
     * Costruttore per impiegato dirigente. Se il parametro passaggioDirigente non viene passato come
     * null, inizializza l'attributo dirigente come un nuovo ogetto Dirigente che rappresenta il
     * ruolo di dirigente dell'impiegato.
     *
     * @param nome              the nome
     * @param cognome           the cognome
     * @param sesso             the sesso
     * @param dataDiNascita     the data di nascita
     * @param luogoDiNascita    the luogo di nascita
     * @param cf                the cf
     * @param dataDiAssunzione  the data di assunzione
     * @param stipendio         the stipendio
     * @param laboratorio       the laboratorio
     * @param passagioDirigente the passagio dirigente
     */
//COSTRUTTORE PER IMPIEGATO DIRIGENTE
    public Impiegato(String nome, String cognome, char sesso, Date dataDiNascita, String luogoDiNascita, String cf,
                     Date dataDiAssunzione, float stipendio, Laboratorio laboratorio, Date passagioDirigente){
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.dataDiNascita = dataDiNascita;
        this.luogoDiNascita = luogoDiNascita;
        this.cf = cf;
        this.dataDiAssunzione = dataDiAssunzione;
        this.stipendio = stipendio;
        this.afferenza = laboratorio;
        this.lavoriProgettiAssegnati = new ArrayList<Lavorare>();
        this.contributiPassati = new ArrayList<Progetto>();

        if (passagioDirigente != null) {
            this.dirigente = new Dirigente(passagioDirigente, this);
        }
    }

    /**
     * Get nome string.
     *
     * @return the string
     */
    public String getNome(){
        return nome;
    }

    /**
     * Get cognome string.
     *
     * @return the string
     */
    public String getCognome(){
        return cognome;
    }

    /**
     * Get sesso char.
     *
     * @return the char
     */
    public char getSesso(){
        return sesso;
    }

    /**
     * Get data di nascita date.
     *
     * @return the date
     */
    public Date getDataDiNascita(){
        return dataDiNascita;
    }

    /**
     * Get luogo di nascita string.
     *
     * @return the string
     */
    public String getLuogoDiNascita(){
        return luogoDiNascita;
    }

    /**
     * Get cf string.
     *
     * @return the string
     */
    public String getCf(){
        return cf;
    }

    /**
     * Get data di assunzione date.
     *
     * @return the date
     */
    public Date getDataDiAssunzione(){
        return dataDiAssunzione;
    }

    /**
     * Get stipendio float.
     *
     * @return the float
     */
    public float getStipendio(){
        return stipendio;
    }

    /**
     * Gets afferenza.
     *
     * @return the afferenza
     */
    public Laboratorio getAfferenza() {
        return afferenza;
    }

    /**
     * Get lavori progetti assegnati array list.
     *
     * @return the array list
     */
    public ArrayList<Lavorare> getLavoriProgettiAssegnati(){
        return  lavoriProgettiAssegnati;
    }

    /**
     * Get contributi passati array list.
     *
     * @return the array list
     */
    public ArrayList<Progetto> getContributiPassati(){return contributiPassati;}

    /**
     * Set nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     * Set cognome.
     *
     * @param cognome the cognome
     */
    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    /**
     * Set sesso.
     *
     * @param sesso the sesso
     */
    public void setSesso(char sesso){
        this.sesso = sesso;
    }

    /**
     * Set data di nascita.
     *
     * @param dataDiNascita the data di nascita
     */
    public void setDataDiNascita(Date dataDiNascita){
        this.dataDiNascita = dataDiNascita;
    }

    /**
     * Set luogo di nascita.
     *
     * @param luogoDiNascita the luogo di nascita
     */
    public void setLuogoDiNascita(String luogoDiNascita){
        this.luogoDiNascita = luogoDiNascita;
    }

    /**
     * Set cf.
     *
     * @param cf the cf
     */
    public void setCf(String cf){
        this.cf = cf;
    }

    /**
     * Set data di assunzione.
     *
     * @param dataDiAssunzione the data di assunzione
     */
    public void setDataDiAssunzione(Date dataDiAssunzione){
        this.dataDiAssunzione = dataDiAssunzione;
    }

    /**
     * Set stipendio.
     *
     * @param stipendio the stipendio
     */
    public void setStipendio(float stipendio){
        this.stipendio = stipendio;
    }

    /**
     * Set afferenza.
     *
     * @param laboratorio the laboratorio
     */
    public void setAfferenza(Laboratorio laboratorio){
        this.afferenza = laboratorio;
    }

    /**
     * Set lavori progetti assegnati.
     *
     * @param lavoriProgettiAssegnati the lavori progetti assegnati
     */
    public void setLavoriProgettiAssegnati(ArrayList<Lavorare> lavoriProgettiAssegnati){
        this.lavoriProgettiAssegnati = lavoriProgettiAssegnati;
    }

    /**
     * Set contributi passati.
     *
     * @param progettiContribuito the progetti contribuito
     */
    public void setContributiPassati(ArrayList<Progetto> progettiContribuito){this.contributiPassati = progettiContribuito;}

    /**
     * Gets dirigente.
     *
     * @return the dirigente
     */
    public Dirigente getDirigente() {return dirigente;}

    /**
     * Gets ore lavoro.
     *
     * @return the ore lavoro
     */
    public int getOreLavoro() {
        int result = 0;
        for (Lavorare lavorare: lavoriProgettiAssegnati) {
            result += lavorare.getOre();
        }
        return result;
    }

    /**
     * Add lavoro.
     *
     * @param lavoro the lavoro
     */
    public void addLavoro(Lavorare lavoro){
        if (contributiPassati.contains(lavoro.getProgettoinCorso())) {
            contributiPassati.remove(lavoro.getProgettoinCorso());
        }
        lavoriProgettiAssegnati.add(lavoro);
    }

    /**
     * Add contributo passato.
     *
     * @param progetto the progetto
     */
    public void addContributoPassato(Progetto progetto) {
        contributiPassati.add(progetto);
    }

    /**
     * Rimuove l'ogetto Lavorare che riceve come parametro lavoro, ed aggiunge all'ArrayList
     * contributiPassati il progetto che era presente come attributo di lavoro
     *
     * @param lavoro the lavoro
     */
    public void concludiLavoro(Lavorare lavoro) {
        lavoriProgettiAssegnati.remove(lavoro);
        contributiPassati.add(lavoro.getProgettoinCorso());
    }

    /**
     * Se l'impiegato non ne ha già, crea un ogetto {@link Dirigente} da assegnare come valore
     * dell'attributo dirigente. Il nuovo ogetto dirigente avrà come attributo impiegato questo
     * impiegato e come attributo passaggioDirigente l'omonimo parametro.
     *
     * @param passaggioDirigente the passaggio dirigente
     */
    public void promuoviADirigente(Date passaggioDirigente) {
        if(dirigente == null) {
            dirigente = new Dirigente(passaggioDirigente, this);
        }
    }

    /**
     * Gets categoria.
     *
     * @return the categoria
     */
    public String getCategoria() {
        return "none";
    }
}
