package it.epicode.erboristeria.Products;

import it.epicode.erboristeria.categories.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long categoryId;
    private MultipartFile imgFile;
}
