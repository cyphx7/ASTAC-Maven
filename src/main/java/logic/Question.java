package logic;

import java.util.List;

/**
 * Represents a programming question with multiple choice options.
 * Supports both theoretical questions and code-based questions.
 */
public class Question {
    private String text;
    private String codeSnippet;
    private List<String> options;
    private int correctAnswerIndex;
    private String subject;
    private QuestionType type;

    public enum QuestionType {
        THEORETICAL,
        PROGRAMMING
    }

    public Question(String text, String codeSnippet, List<String> options, int correctAnswerIndex, String subject, QuestionType type) {
        this.text = text;
        this.codeSnippet = codeSnippet;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.subject = subject;
        this.type = type;
    }

    public String getText() { return text; }
    public String getCodeSnippet() { return codeSnippet; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getSubject() { return subject; }
    public QuestionType getType() { return type; }
}
