package it.epicode.erboristeria.categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.erboristeria.Products.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();
}
