package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

		private Long id;
		
		@Size(min = 1, max = 50, message = "Nome deve ter entre 1 e 50 caracteres")
		@NotBlank(message = "Campo obrigatório")
		private String name;
		
		@NotBlank(message = "Campo obrigatório")
		private String description;
		
		@Positive(message = "O preço deve ser um valor positivo")
		private Double price;
		private String imgUrl;
		
		@PastOrPresent(message = "A data do produto não pode ser futura")
		private Instant date; // ATRIBUTO A MAIS PRA ENSINAR COMO COLOCAR DATA
		
		private List<CategoryDTO> categories = new ArrayList<>();

		public ProductDTO() {}
		
		public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
			super();
			this.id = id;
			this.name = name;
			this.description = description;
			this.price = price;
			this.imgUrl = imgUrl;
			this.date = date;
		}
		
		public ProductDTO(Product entity) {
			super();
			this.id = entity.getId();
			this.name = entity.getName();
			this.description = entity.getDescription();
			this.price = entity.getPrice();
			this.imgUrl = entity.getImgUrl();
			this.date = entity.getDate();
		}
		
		// DTO QUE RECEBE AS CATEGORIAS -> VÃO PRA AQUELA LISTA DE CIMA
		public ProductDTO(Product entity, Set<Category> categories) {
			this(entity); // EXECUTA TUDO DO METODO DE CIMA
			categories.forEach(cat -> this.categories.add(new CategoryDTO(cat))); // PEGA CADA ELEMENTO E INSERE ELE TRANSFORMADO PARA DTO NA LISTA DE CATEGORIES DA CLASSE
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public Instant getDate() {
			return date;
		}

		public void setDate(Instant date) {
			this.date = date;
		}

		public List<CategoryDTO> getCategories() {
			return categories;
		}

		public void setCategories(List<CategoryDTO> categories) {
			this.categories = categories;
		}
}
