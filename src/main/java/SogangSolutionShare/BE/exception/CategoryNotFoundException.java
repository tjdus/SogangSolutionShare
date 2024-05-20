package SogangSolutionShare.BE.exception;

public class CategoryNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "카테고리가 존재하지 않습니다.";
    public CategoryNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
