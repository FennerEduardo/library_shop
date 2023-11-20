package fenner.library_shop;

import fenner.library_shop.view.BookForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class LibraryShopApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext contextSpring =
				new SpringApplicationBuilder(LibraryShopApplication.class)
						.headless(false)
						.web(WebApplicationType.NONE)
						.run(args);

		EventQueue.invokeLater(() -> {
			BookForm bookForm = contextSpring.getBean(BookForm.class);
			bookForm.setVisible(true);
		});
	}

}
