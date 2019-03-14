package com.urjc.daw.models.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.answer.Answer;
import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.item.Item;

import javax.persistence.*;
import java.util.*;


@Entity
public class Question {

    public interface BasicInfo{}
    public interface AnswerList{}
    public interface ConceptDet{}
    public interface ItemList{}

/**         ATTRIBUTES      **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(BasicInfo.class)
    private long idQuestion;

    @Column
    @JsonView(BasicInfo.class)
    int type;

    @Column
    @JsonView(BasicInfo.class)
    private String info;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonView(AnswerList.class)
    private Set<Answer> setAnswer;

    @Column
    private int [] arrayRespuestas={0,0,0};

    @ManyToOne
    @JsonView(ConceptDet.class)
    private Concept idConcept;

    @ManyToMany
    @JsonView(ItemList.class)
    private Set<Item> setItem;



/**         CONSTRUCTOR         **/
    public Question(){}

    public Question(int type, String info){
        this.info=info;
        this.type=type;
        this.setAnswer=new HashSet<>();
        this.setItem= new HashSet<>();
        metrics();
    }


/**         GETTER & SETTER         **/

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public Set<Answer> getIdAnswer() {
        return setAnswer;
    }

    public void setIdAnswer(Set<Answer> idAnswer) {
        this.setAnswer = idAnswer;
    }

    public long getIdConcept() {
        return idConcept.getIdConcept();
    }

    public void setConcept(Concept idConcept) {
        this.idConcept = idConcept;
    }

    public int getAnswerCorrect(){
        return arrayRespuestas[0];
    }
    public int getAnswerIncorrect(){
        return arrayRespuestas[1];
    }
    public int getAnswerPending(){
        return arrayRespuestas[2];
    }

    public int getSizeQuestions(){
        return setAnswer.size();
    }

    public Concept getConcept(){
        return idConcept;
    }

    public void addAnswer(Answer answer){
        this.setAnswer.add(answer);
        if(answer.getState().equals("right")){
            arrayRespuestas[0]++;
        }else if(answer.getState().equals("wrong")){
            arrayRespuestas[1]++;
        }else{
            arrayRespuestas[2]++;
        }
    }

    public void addItem(Item item){this.setItem.add(item);}

    public void metrics(){
        int rigth=0;
        int wrong=0;
        int pending=0;

        for (Answer ans:setAnswer) {
            if(ans.getState().equals("right")){
                rigth++;
            }else if(ans.getState().equals("wrong")){
                wrong++;
            }else{
                pending++;
            }
        }
        arrayRespuestas[0]=rigth;
        arrayRespuestas[1]=wrong;
        arrayRespuestas[2]=pending;
    }


    public void randomize(long id){
            int type = (int) (Math.random() * 4);
            int item = 0;
            String info = "";
            switch (type) {
                case 0:
                    info = "¿Cuáles son " + idConcept.getTitle() + " ?";
                    break;
                case 1:
                    List<Item> itemList = new ArrayList<>(idConcept.getItemSet());
                    item = (int) (Math.random() * itemList.size() - 1);
                    info = "¿" + itemList.get(item).getInfo() + " es un elemento de " + idConcept.getTitle() + " ?";
                    break;
                case 2:
                    List<Item> itemsCorrect = new ArrayList<>();
                    for (Item i: idConcept.getItemSet()) {
                        if(i.getState()) {
                            itemsCorrect.add(i);
                        }
                    }
                    item = (int) (Math.random() * itemsCorrect.size() - 1);
                    itemsCorrect.remove(item);
                    String complementInfo = "";
                    for (int i = 0; i < itemsCorrect.size(); i++) {
                        complementInfo += itemsCorrect.get(i).getInfo() + ", ";
                    }
                    info = "¿Qué elemento falta en " + complementInfo + " para completar la lista de " + idConcept.getTitle() + " ?";
                    break;
                case 3:

                    List<Item> selected = new ArrayList<>();
                    List<Item> itemRandom = new ArrayList<>(idConcept.getItemSet());

                    int numItems = (int) Math.floor(Math.random() * (5 - 3 + 1) + 3);

                    String complement = "";
                    for (int i = 0; i < numItems; i++) {
                        item = (int) (Math.random() * itemRandom.size() - 1);
                        selected.add(itemRandom.get(item));
                        complement += itemRandom.get(item).getInfo() + ", ";
                        itemRandom.remove(item);
                    }
                    info = "¿Qué elementos de " + complement + " no son parte de " + idConcept.getTitle() + " ?";
                    break;

            }
            this.setInfo(info);
            this.setType(type);


    }
}
