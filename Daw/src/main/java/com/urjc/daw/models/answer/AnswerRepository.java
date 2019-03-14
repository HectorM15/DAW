package com.urjc.daw.models.answer;

import com.urjc.daw.models.lessons.Lesson;
import com.urjc.daw.models.question.Question;
import com.urjc.daw.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByidQuestion(long idQuestion);

    Optional<Question> findByIdQuestion(Long aLong);

    Object findAnswerByIdUser(User user);

    List<Answer> findByState(String state);

    List<Answer> findByCorrectAndIdUser(Boolean correct, User idUser);

    Page<Answer> findAnswersByIdUserEquals(User idUser, Pageable page);


}
