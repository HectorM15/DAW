package com.urjc.daw.models.user;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.answer.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity// This tells Hibernate to make a table out of this class
public class User {
    public interface BasicInfo{}
    public interface AnswerList{}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(BasicInfo.class)
    private long idUser;
    @Column
    @JsonView(BasicInfo.class)
    private String name;
    @Column
    @JsonView(BasicInfo.class)
    private String password;
    @Column
    @JsonView(BasicInfo.class)
    private String email;
    @Column
    @JsonView(BasicInfo.class)
    private String userType;

    @OneToMany
    @JsonView(AnswerList.class)
    private Set<Answer> setAnswer;


    public long getId() {
        return idUser;
    }

    public void setId(Integer id) {
        this.idUser = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String name) {
        this.password = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(String name, String password, String email, String userType) {
        this.name = name;
        this.password = new BCryptPasswordEncoder().encode(password);
        //this.password = password;
        this.email = email;
        this.userType = userType;
        this.setAnswer = new HashSet<>();
    }

    public User() {
        this.setAnswer = new HashSet<>();
    }

    public User(String visitor) {
        this.userType = visitor;
        this.setAnswer = new HashSet<>();
    }

    public User(String name, String password, String email){
        this.name = name;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.email = email;
        this.userType = "ROLE_TEACHER";
        this.setAnswer = new HashSet<>();
    }

    public void addAnswer(Answer answer) {
        this.setAnswer.add(answer);
    }

    public String toStringStatistics(long idConcept) {
        int [] result = new int[3];
        for (Answer ans : setAnswer) {
            if (ans.getQuestion().getIdConcept()==idConcept){
                switch (ans.getState()){
                    case ("pending"):
                        result[0]++;
                        break;
                    case ("wrong"):
                        result[1]++;
                        break;
                    case ("right"):
                        result[2]++;
                        break;
                }
            }
        }

        return (result[0]+":"+result[1]+":"+result[2]);

    }

}
