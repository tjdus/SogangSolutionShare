package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    @Transactional
    public void createCategory(String name) {
        categoryRepository.save(new Category(name));
    }
}
