package it.epicode.erboristeria.categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO CategoryRequestDTO) {
        return ResponseEntity.ok(categoryService.create(CategoryRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> modify(@PathVariable Long id, @RequestBody CategoryRequestDTO CategoryRequestDTO) {
        return ResponseEntity.ok(categoryService.modify(id, CategoryRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
