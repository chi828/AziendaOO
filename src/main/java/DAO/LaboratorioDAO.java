package DAO;

/**
 * The interface Laboratorio dao.
 */
public interface LaboratorioDAO {
    /**
     * Sets responsabile scientifico.
     *
     * @param nome  the nome
     * @param topic the topic
     * @param cf    the cf
     */
    public void setResponsabileScientifico(String nome, String topic, String cf);
}
