package SogangSolutionShare.BE.exception;

public class TagNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "태그가 존재하지 않습니다.";

    public TagNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
