package SogangSolutionShare.BE.exception;

public class AlreadyLikeException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "이미 좋아요를 누른 질문입니다.";
    public AlreadyLikeException() {
        super(DEFAULT_MESSAGE);
    }
}
