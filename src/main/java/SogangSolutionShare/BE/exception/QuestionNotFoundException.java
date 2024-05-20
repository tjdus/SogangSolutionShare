package SogangSolutionShare.BE.exception;

public class QuestionNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "글이 존재하지 않습니다.";
    public QuestionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
