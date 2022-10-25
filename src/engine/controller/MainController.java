package engine.controller;

import engine.quiz.Ans;
import engine.quiz.Quiz;
import engine.quiz.Answer;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@Validated
public class MainController {
    public List<Quiz> list = new CopyOnWriteArrayList<>();

    @PostMapping("/api/quizzes")
    public Quiz newTest(@Valid @RequestBody Quiz newTest) {
        newTest.setId(list.size() + 1);
        newTest.setAnswer(newTest.getAnswer() == null ? new int[]{}: newTest.getAnswer());
        list.add(newTest);
        return newTest;
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable int id) {
        if (id > list.size()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list.get(id - 1));
    }

    @GetMapping("/api/quizzes")
    public List<Quiz> getQuizzes() {
        return list;
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<Answer> solveQuiz(@PathVariable int id, @RequestBody Ans answer) {
        if (id > list.size()) {
            return ResponseEntity.notFound().build();
        }
        if (answer.getAnswer() == null) {
            answer.setAnswer(new int[]{});
        }
        if (Arrays.equals(answer.getAnswer(), list.get(id - 1).getAnswer())) {
            return ResponseEntity.ok(new Answer(true, "Congratulations, you're right!"));
        } else {
            return ResponseEntity.ok(new Answer(false, "Wrong answer! Please, try again."));
        }
    }
}