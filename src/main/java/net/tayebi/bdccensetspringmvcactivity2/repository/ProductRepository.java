package net.tayebi.bdccensetspringmvcactivity2.repository;

import net.tayebi.bdccensetspringmvcactivity2.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
