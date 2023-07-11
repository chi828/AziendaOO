package DAO;

/**
 * The interface Laboratorio dao.
 */
public interface LaboratorioDAO {
    /**
     * Riassegna in database il responsabile scientifico per il laboratorio scelto. Il laboratorio è indicato dal nome
     * a primo parametro e topic al secondo paramerto, mentre il responsabile scientifico è rappresentato dal codice
     * fiscale al terzo parametro.
     *
     * @param nome  the nome
     * @param topic the topic
     * @param cf    the cf
     */
    public void setResponsabileScientifico(String nome, String topic, String cf);
}
