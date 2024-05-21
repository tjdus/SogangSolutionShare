package SogangSolutionShare.BE.exception;

public class AnswerNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "답변이 존재하지 않습니다.";
    public AnswerNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
