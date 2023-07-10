package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Progetto.
 */
public class Progetto {

    protected String nome;
    protected String cup;
    protected Date dataInizio;
    protected ArrayList<Laboratorio> laboratoriAssegnati;
    protected Senior referenteScientifico;
    protected Dirigente responsabile;

    /**
     * Instantiates a new Progetto.
     *
     * @param nome                 the nome
     * @param cup                  the cup
     * @param dataInizio           the data inizio
     * @param laboratoriAssegnati  the laboratori assegnati
     * @param referenteScientifico the referente scientifico
     * @param responsabile         the responsabile
     */
    public Progetto(String nome, String cup, Date dataInizio, ArrayList<Laboratorio> laboratoriAssegnati,
                    Senior referenteScientifico, Dirigente responsabile) throws RuntimeException{
        if(laboratoriAssegnati.size() > 3) {
            throw new RuntimeException("Il numero di laboratori assegnati supera il massimo di 3.");
        }

        this.nome = nome;
        this.cup = cup;
        this.dataInizio = dataInizio;
        this.laboratoriAssegnati = laboratoriAssegnati;
        this.referenteScientifico = referenteScientifico;
        this.responsabile = responsabile;

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
     * Get cup string.
     *
     * @return the string
     */
    public String getCup(){
        return  cup;
    }

    /**
     * Get data inizio date.
     *
     * @return the date
     */
    public Date getDataInizio(){
        return dataInizio;
    }

    /**
     * Get laboratori assegnati array list.
     *
     * @return the array list
     */
    public ArrayList<Laboratorio> getLaboratoriAssegnati(){
        return laboratoriAssegnati;
    }

    /**
     * Get referente scientifico senior.
     *
     * @return the senior
     */
    public Senior getReferenteScientifico(){
        return referenteScientifico;
    }

    /**
     * Get responsabile dirigente.
     *
     * @return the dirigente
     */
    public Dirigente getResponsabile(){
        return responsabile;
    }

    /**
     * Set nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     * Set cup.
     *
     * @param cup the cup
     */
    public void setCup(String cup){
        this.cup = cup;
    }

    /**
     * Set data inizio.
     *
     * @param dataInizio the data inizio
     */
    public void setDataInizio(Date dataInizio){
        this.dataInizio = dataInizio;
    }

    /**
     * Set laboratori associati.
     *
     * @param laboratoriAssociati the laboratori associati
     */
    public void setLaboratoriAssociati(ArrayList<Laboratorio> laboratoriAssociati){
        this.laboratoriAssegnati = laboratoriAssociati;
    }

    /**
     * Set referente scientifico.
     *
     * @param referenteScientifico the referente scientifico
     */
    public void setReferenteScientifico(Senior referenteScientifico){
        this.referenteScientifico = referenteScientifico;
    }

    /**
     * Set responsabile.
     *
     * @param responsabile the responsabile
     */
    public void setResponsabile(Dirigente responsabile){
        this.responsabile = responsabile;
    }

    /**
     * Is concluso boolean.
     *
     * @return the boolean
     */
    public Boolean isConcluso() {return false;}
}
