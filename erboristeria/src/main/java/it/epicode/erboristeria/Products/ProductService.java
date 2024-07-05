package it.epicode.erboristeria.Products;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.erboristeria.categories.Category;
import it.epicode.erboristeria.categories.CategoryRepository;
import it.epicode.erboristeria.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

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
    public ProductResponseDTO create(@Valid ProductRequestDTO productRequestDTO) throws IOException {
        Product product = new Product();
        BeanUtils.copyProperties(productRequestDTO, product);

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + productRequestDTO.getCategoryId()));
        product.setCategory(category);

        String imgUrl = uploadImage(productRequestDTO.getImgFile());
        product.setImg(imgUrl);

        productRepository.save(product);
        ProductResponseDTO responseDto = new ProductResponseDTO();
        BeanUtils.copyProperties(product, responseDto);
        return responseDto;
    }

    public ProductResponseDTO modify(Long id, @Valid ProductRequestDTO productRequestDTO) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        BeanUtils.copyProperties(productRequestDTO, product);

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + productRequestDTO.getCategoryId()));
        product.setCategory(category);

        if (productRequestDTO.getImgFile() != null && !productRequestDTO.getImgFile().isEmpty()) {
            String imgUrl = uploadImage(productRequestDTO.getImgFile());
            product.setImg(imgUrl);
        }

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

    private String uploadImage(MultipartFile imgFile) throws IOException {
        if (imgFile == null || imgFile.isEmpty()) {
            return null;
        }

        Map uploadResult = cloudinary.uploader().upload(imgFile.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
}
