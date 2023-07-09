package Model;

import java.util.ArrayList;

/**
 * The type Laboratorio.
 */
public class Laboratorio {

    private String topic;
    private String nome;
    private Senior responsabileScientifico;

    /**
     * Instantiates a new Laboratorio.
     *
     * @param nome                    the nome
     * @param topic                   the topic
     * @param responsabileScientifico the responsabile scientifico
     */
    public Laboratorio(String nome, String topic, Senior responsabileScientifico){
        this.nome = nome;
        this.topic = topic;
        this.responsabileScientifico = responsabileScientifico;
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
     * Get topic string.
     *
     * @return the string
     */
    public String getTopic(){
        return topic;
    }

    /**
     * Get responsabile scientifico senior.
     *
     * @return the senior
     */
    public Senior getResponsabileScientifico(){
        return responsabileScientifico;
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
     * Set topic.
     *
     * @param topic the topic
     */
    public void setTopic(String topic){
        this.topic = topic;
    }

    /**
     * Set responsabile scientifico.
     *
     * @param responsabileScientifico the responsabile scientifico
     */
    public void setResponsabileScientifico(Senior responsabileScientifico){
        this.responsabileScientifico = responsabileScientifico;
    }
}
