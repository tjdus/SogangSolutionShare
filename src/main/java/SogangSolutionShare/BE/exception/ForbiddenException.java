package SogangSolutionShare.BE.exception;

public class ForbiddenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "권한이 없습니다.";
    public ForbiddenException() {
        super(DEFAULT_MESSAGE);
    }

}
