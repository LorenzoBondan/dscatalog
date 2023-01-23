package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

	@Query("SELECT DISTINCT obj FROM Product obj "
			+ "	INNER JOIN obj.categories cats "
			+ "WHERE (COALESCE(:categories) IS NULL OR cats IN :categories) AND "
			+ "(UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) )")
	Page<Product> find(List<Category> categories, String name, Pageable pageable); // METODO CRIADO PARA BUSCAR PRODUTOS POR CATEGORIA, ULTIMO PARAMETRO TEM QUE SER UM PAGEABLE
	
	// PRA CORRIGIR O PROBLEMA DAS N+1 CONSULTAS
	@Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products") // JOIN FETCH BUSCA OS OBJETOS JUNTO COM O PRODUTO. SÓ FUNCIONA COM LISTA E NAO COM PAGINA, POR ISSO A CONSULTA EM DUAS ETAPAS
	List<Product> findProductsWithCategories(List<Product> products);
}
