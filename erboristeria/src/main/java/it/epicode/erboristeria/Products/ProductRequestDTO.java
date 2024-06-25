package it.epicode.erboristeria.Products;

import it.epicode.erboristeria.categories.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Category categoryId;
}
