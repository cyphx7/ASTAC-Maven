package logic;

import com.ui.WindowManager;
import java.util.List;

/**
 * Manages a single game round with questions from one subject.
 * Handles answer submission, chatbot saves, and game state tracking.
 */
public class GameSession {
    private final Chatbot currentChatbot;
    private final List<Question> questions;
    private final WindowManager manager;
    private int currentQuestionIndex;
    private int score;

    public enum GameResult {
        CORRECT,
        WRONG_AND_FAILED,
        SAVED_BY_CHATBOT,
        GAME_OVER   
    }

    public GameSession(Chatbot chatbot, List<Question> questions, WindowManager manager) {
        this.currentChatbot = chatbot;
        this.questions = questions;
        this.manager = manager;
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }


    public String getSubject() {
        if (!questions.isEmpty()) {
            return questions.get(0).getSubject();
        }
        return "Unknown";
    }

    public Chatbot getCurrentChatbot() {
        return currentChatbot;
    }

    public GameResult submitAnswer(int answerIndex) {
        Question q = getCurrentQuestion();
        if (q == null) return GameResult.GAME_OVER;

        if (answerIndex == q.getCorrectAnswerIndex()) {
            score++;
            currentQuestionIndex++;
            return GameResult.CORRECT;
        } else {


            if (!manager.isSaveUsed()) {
                manager.markSaveUsed();
                boolean saved = currentChatbot.calculateSuccess(q.getSubject());

                if (saved) {
                    currentQuestionIndex++;
                    return GameResult.SAVED_BY_CHATBOT;
                } else {

                    return GameResult.WRONG_AND_FAILED;
                }
            } else {
                return GameResult.GAME_OVER;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameWon() {
        return currentQuestionIndex >= questions.size();
    }
}
