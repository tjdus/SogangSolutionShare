package SogangSolutionShare.BE.exception;

public class AlreadyScrapException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "이미 스크랩한 글입니다.";
    public AlreadyScrapException() {
        super(DEFAULT_MESSAGE);
    }
}
