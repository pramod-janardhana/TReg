package com.treg.treg.database.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "Category")
@Table(
        name = "category",
        uniqueConstraints = {
                @UniqueConstraint(name = "category_name_uk", columnNames = "name")
        },
        indexes = {
                @Index(name = "category_name_idx", columnList = "name", unique = true)
        }
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(64)")
    private String name;

    @OneToMany(mappedBy = "categoryId", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Transaction> transactions;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
