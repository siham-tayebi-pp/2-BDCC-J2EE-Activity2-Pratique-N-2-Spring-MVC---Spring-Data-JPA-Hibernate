package net.tayebi.bdccensetspringmvcactivity2;

import net.tayebi.bdccensetspringmvcactivity2.entities.Product;
import net.tayebi.bdccensetspringmvcactivity2.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class BdccEnsetSpringMvcActivity2Application {


	public static void main(String[] args) {

		SpringApplication.run(BdccEnsetSpringMvcActivity2Application.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(ProductRepository productRepository) {
		return args -> {
			productRepository.save(Product.builder()
					.name("Computer")
					.price(5400)
					.quantity(12)
					.build()
			);
			productRepository.save(Product.builder()
					.name("Printer")
					.price(1200)
					.quantity(11)
					.build()
			);
			productRepository.save(Product.builder()
					.name("Smartphone")
					.price(12000)
					.quantity(33)
					.build()
			);
			productRepository.findAll().forEach(p->System.out.println(p.toString()));
		};
	}

}
