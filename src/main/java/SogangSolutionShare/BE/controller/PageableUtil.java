package SogangSolutionShare.BE.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
    public static Pageable createPageable(Integer page, Integer size, String orderBy){
        Sort sort;
        switch (orderBy) {
            case "most-answered":
                sort = Sort.by("answerCount").descending();
                break;
            case "least-answered":
                sort = Sort.by("answerCount").ascending();
                break;
            case "most-viewed":
                sort = Sort.by("viewCount").descending();
                break;
            case "most-liked":
                sort = Sort.by("likeCount").descending();
                break;
            case "latest":
            default:
                sort = Sort.by("createdAt").descending();
                break;
        }
        return PageRequest.of(page, size, sort);
    }
}
