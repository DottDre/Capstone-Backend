package it.epicode.erboristeria.Products;
import it.epicode.erboristeria.categories.Category;
import it.epicode.erboristeria.categories.CategoryRepository;
import it.epicode.erboristeria.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(product -> {
                    ProductResponseDTO responseDto = new ProductResponseDTO();
                    BeanUtils.copyProperties(product, responseDto);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        ProductResponseDTO responseDto = new ProductResponseDTO();
        BeanUtils.copyProperties(product, responseDto);
        return responseDto;
    }

    @Transactional
    public ProductResponseDTO create(@Valid ProductRequestDTO ProductRequestDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(ProductRequestDTO, product);

        Category category = categoryRepository.findById(ProductRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + ProductRequestDTO.getCategoryId()));
        product.setCategory(category);

        productRepository.save(product);
        ProductResponseDTO responseDto = new ProductResponseDTO();
        BeanUtils.copyProperties(product, responseDto);
        return responseDto;
    }

    public ProductResponseDTO modify(Long id, @Valid ProductRequestDTO ProductRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        BeanUtils.copyProperties(ProductRequestDTO, product);

        Category category = categoryRepository.findById(ProductRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + ProductRequestDTO.getCategoryId()));
        product.setCategory(category);

        productRepository.save(product);
        ProductResponseDTO responseDto = new ProductResponseDTO();
        BeanUtils.copyProperties(product, responseDto);
        return responseDto;
    }

    public String delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found for this id :: " + id);
        }
        productRepository.deleteById(id);
        return "Product deleted";
    }
}
