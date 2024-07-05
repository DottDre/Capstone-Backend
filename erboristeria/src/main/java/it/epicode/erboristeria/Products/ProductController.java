package it.epicode.erboristeria.Products;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @ModelAttribute ProductRequestDTO productRequestDTO) throws IOException {
        ProductResponseDTO productResponseDTO = productService.create(productRequestDTO);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductRequestDTO productRequestDTO) throws IOException {
        ProductResponseDTO productResponseDTO = productService.modify(id, productRequestDTO);
        return ResponseEntity.ok(productResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String response = productService.delete(id);
        return ResponseEntity.ok(response);
    }
}
