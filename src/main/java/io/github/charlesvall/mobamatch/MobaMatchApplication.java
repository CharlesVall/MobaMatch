package io.github.charlesvall.mobamatch;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "MobaMatch",
                version = "0.1",
                description = "Documentation of a moba matchmaking api service"
        )
)
@SpringBootApplication
public class MobaMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobaMatchApplication.class, args);
        System.out.println("Application started 🚀");
	}

}
