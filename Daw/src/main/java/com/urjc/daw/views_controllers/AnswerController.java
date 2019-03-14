package com.urjc.daw.views_controllers;

import com.urjc.daw.models.answer.Answer;
import com.urjc.daw.models.answer.AnswerService;
import com.urjc.daw.models.concept.ConceptService;
import com.urjc.daw.models.question.Question;
import com.urjc.daw.models.question.QuestionService;
import com.urjc.daw.models.user.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @Autowired
    ConceptService conceptService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserComponent userComponent;

    @ModelAttribute
    public void addUserToModel(Model model) {
        boolean logged = userComponent.getLoggedUser() != null;
        model.addAttribute("logged", logged);
    }


    @GetMapping(path = "/updateAnswerTrue/{idAnswer}/{idConcept}")
    public String updateTrue(Model model, @PathVariable long idAnswer, @PathVariable long idConcept) {
        Optional<Answer> ans = answerService.findOne(idAnswer);
        ans.get().setState("right");
        ans.get().correct();
        answerService.addAnswer(ans.get());
        questionService.addQuestion(ans.get().accesToQuestion());
        return "/TeacherConceptView/{idConcept}";
    }


    @GetMapping(path = "/updateAnswerFalse/{idAnswer}/{idConcept}")
    public String updateFalse(Model model, @PathVariable long idAnswer, @PathVariable long idConcept) {
        Optional<Answer> ans = answerService.findOne(idAnswer);
        ans.get().setState("wrong");
        ans.get().correct();
        answerService.addAnswer(ans.get());
        questionService.addQuestion(ans.get().accesToQuestion());
        return "/TeacherConceptView/{idConcept}";
    }

    @GetMapping(path = "/saveAnswer/{idQuestion}")
    public String addAnswer(Model model, Answer answer, @PathVariable long idQuestion) {
        Optional<Question> question = questionService.findOne(idQuestion);
        int type = question.get().getType();
        answer.setQuestion(question.get());
        answer.setState("pending");
        answer.setCorrect(false);
        answer.setIdUser(userComponent.getLoggedUser());
        answerService.addAnswer(answer);
        question.get().addAnswer(answer);
        question.get().metrics();
        questionService.addQuestion(question.get());
        return "/StudentConceptView/" + question.get().getIdConcept();
    }


    @GetMapping(path = "/sendAnswerTrue/{idQuestion}/{correct}")
    public String sendAnswer(Model model, @PathVariable long idQuestion, @PathVariable boolean correct) {
        String correct2 = "";
        if (correct)
            correct2 = "true";
        else
            correct2 = "false";

        Answer answer = new Answer(correct2);

        Question question = questionService.findOne(idQuestion).get();
        answer.setIdUser(userComponent.getLoggedUser());
        answer.setQuestion(question);
        question.addAnswer(answer);
        question.metrics();
        answerService.addAnswer(answer);
        questionService.addQuestion(question);
        answer.correctType1(correct);
        answerService.addAnswer(answer);
        return "/StudentConceptView/" + question.getIdConcept();
    }

    @GetMapping(path = "/sendSelectedItems/{idQuestion}/{ret}/{total}")
    public String sendItemsSelected(Model model, @PathVariable long idQuestion, @PathVariable String ret, @PathVariable String total) {
        String[] items = ret.split("sss");
        String[] all = total.split("sss");
        Answer answer = new Answer(ret);

        Question question = questionService.findOne(idQuestion).get();
        answer.setIdUser(userComponent.getLoggedUser());
        answer.setQuestion(question);
        question.addAnswer(answer);
        question.metrics();
        answerService.addAnswer(answer);
        questionService.addQuestion(question);
        answer.correctType2(items, all);
        answerService.addAnswer(answer);

        return "/StudentConceptView/" + question.getIdConcept();
    }


}
