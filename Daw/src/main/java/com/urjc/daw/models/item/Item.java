package com.urjc.daw.models.item;

import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.question.Question;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

    public interface BasicInfo{}
    public interface ConceptList{}
    public interface QuestionList{}

/**             Atributos               **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(BasicInfo.class)
    private long idItem;

    @ManyToOne
    @JsonView(ConceptList.class)
    private Concept idConcept;

    @Column
    @JsonView(BasicInfo.class)
    private Boolean state;

    @ManyToMany
    @JsonView(QuestionList.class)
    private Set<Question> setQuestion;

    @Column
    @JsonView(BasicInfo.class)
    private String info;
/*****************************************/


/**             Constructor             **/
    public Item( String info,boolean state) {
        this.info = info;
        this.state = state;
        this.setQuestion = new HashSet<>();
    }

    public Item(){}
/*************************************/



/**             GETTER & SETTER             **/
    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }

    public long getIdConcept() {
        return idConcept.getIdConcept();
    }


    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setIdConcept(Concept idConcept) {
        this.idConcept = idConcept;
    }

/*************************************/

    public void addQuestion(Question question){this.setQuestion.add(question);}



}
