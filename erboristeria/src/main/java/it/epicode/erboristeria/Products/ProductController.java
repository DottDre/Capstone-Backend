package it.epicode.erboristeria.Products;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO ProductRequestDTO) {
        return ResponseEntity.ok(productService.create(ProductRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> modify(@PathVariable Long id, @RequestBody ProductRequestDTO ProductRequestDTO) {
        return ResponseEntity.ok(productService.modify(id, ProductRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(productService.delete(id));
    }
}
