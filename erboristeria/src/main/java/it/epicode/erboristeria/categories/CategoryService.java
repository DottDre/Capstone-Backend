package it.epicode.erboristeria.categories;

import it.epicode.erboristeria.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryResponseDTO responseDto = new CategoryResponseDTO();
                    BeanUtils.copyProperties(category, responseDto);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + id));
        CategoryResponseDTO responseDto = new CategoryResponseDTO();
        BeanUtils.copyProperties(category, responseDto);
        return responseDto;
    }

    @Transactional
    public CategoryResponseDTO create(@Valid CategoryRequestDTO categoryRequestDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryRequestDto, category);
        categoryRepository.save(category);
        CategoryResponseDTO responseDto = new CategoryResponseDTO();
        BeanUtils.copyProperties(category, responseDto);
        return responseDto;
    }

    public CategoryResponseDTO modify(Long id, @Valid CategoryRequestDTO categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + id));
        BeanUtils.copyProperties(categoryRequestDto, category);
        categoryRepository.save(category);
        CategoryResponseDTO responseDto = new CategoryResponseDTO();
        BeanUtils.copyProperties(category, responseDto);
        return responseDto;
    }

    public String delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found for this id :: " + id);
        }
        categoryRepository.deleteById(id);
        return "Category deleted";
    }
}
