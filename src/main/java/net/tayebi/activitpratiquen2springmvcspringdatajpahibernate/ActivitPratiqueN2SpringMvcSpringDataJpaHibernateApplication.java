package net.tayebi.activitpratiquen2springmvcspringdatajpahibernate;

import net.tayebi.activitpratiquen2springmvcspringdatajpahibernate.entities.Product;
import net.tayebi.activitpratiquen2springmvcspringdatajpahibernate.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActivitPratiqueN2SpringMvcSpringDataJpaHibernateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitPratiqueN2SpringMvcSpringDataJpaHibernateApplication.class, args);
    }
    @Bean
    public CommandLineRunner init (ProductRepository productRepository) {
        return args -> {
//            Aouter des prods dans la bd
            productRepository.save(Product.builder()
                    .name("Computer")
                    .price(5400)
                    .quantity(12)
                    .build());
            productRepository.save(Product.builder()
                    .name("Printer")
                    .price(120)
                    .quantity(12)
                    .build());
            productRepository.save(Product.builder()
                    .name("Smart Phone")
                    .price(12000)
                    .quantity(12)
                    .build());
            productRepository.findAll().forEach(p-> System.out.println(p.toString()));
        };
    }

}
