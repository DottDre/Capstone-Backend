package it.epicode.erboristeria.Products;

import it.epicode.erboristeria.categories.Category;
import it.epicode.erboristeria.categories.CategoryResponseDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private CategoryResponseDTO category;;
}
