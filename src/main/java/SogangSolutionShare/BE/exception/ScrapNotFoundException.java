package SogangSolutionShare.BE.exception;

public class ScrapNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "스크랩한 글을 찾을 수 없습니다.";
    public ScrapNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
