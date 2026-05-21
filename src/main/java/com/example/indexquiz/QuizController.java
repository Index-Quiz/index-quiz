package com.example.indexquiz;

import com.example.indexquiz.learn.application.port.in.LearnMaterialUseCase;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaries;
import com.example.indexquiz.question.application.port.in.QuestionUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.common.web.RedirectUrlBuilder;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuestionUseCase questionUseCase;
    private final LearnMaterialUseCase learnMaterialUseCase;

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/quiz")
    public String quiz(@RequestParam(value = "set", defaultValue = "A") String set, Model model) {
        model.addAttribute("questionSet", set);

        try {
            QuestionSet questionSet = QuestionSet.valueOf(set);
            GetQuestionResponses responses = questionUseCase.getQuestionsBySet(questionSet);
            model.addAttribute("questions", responses.questions());
            model.addAttribute("quizData", responses.questions());
        } catch (Exception e) {
            log.warn("SSR 문제 로딩 실패 (set={}): {}", set, e.getMessage());
            model.addAttribute("questions", List.of());
            model.addAttribute("quizData", List.of());
        }

        return "quiz";
    }

    @GetMapping("/learn")
    public String learn(
            @RequestParam(value = "set", defaultValue = "A") String set,
            @RequestParam(value = "id", required = false) Long id,
            Model model
    ) {
        model.addAttribute("questionSet", set);

        try {
            QuestionSet questionSet = QuestionSet.valueOf(set);
            GetLearnMaterialSummaries summaries = learnMaterialUseCase.getLearnMaterialsBySet(questionSet);
            model.addAttribute("materialsList", summaries.materials());

            if (id != null) {
                GetLearnMaterialResponse material = learnMaterialUseCase.getLearnMaterial(id);
                model.addAttribute("material", material);
            } else if (!summaries.materials().isEmpty()) {
                long firstId = summaries.materials().get(0).id();
                GetLearnMaterialResponse material = learnMaterialUseCase.getLearnMaterial(firstId);
                model.addAttribute("material", material);
            }
        } catch (Exception e) {
            log.warn("SSR 학습자료 로딩 실패 (set={}, id={}): {}", set, id, e.getMessage());
        }

        return "learn";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "forward:/privacy.html";
    }

    @GetMapping("/quiz.html")
    public String quizHtmlRedirect(@RequestParam(value = "set", required = false) String set) {
        return "redirect:/quiz" + (set != null ? "?set=" + set : "");
    }

    @GetMapping("/learn.html")
    public String learnHtmlRedirect(
            @RequestParam(value = "set", required = false) String set,
            @RequestParam(value = "id", required = false) Long id
    ) {
        return new RedirectUrlBuilder("/learn")
                .addParam("set", Optional.ofNullable(set))
                .addParam("id", Optional.ofNullable(id).map(String::valueOf))
                .build();
    }
}
