package ru.lightdigital.semenov.proc_sys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocConfig {

    @Bean
    public OpenAPI projectOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Система сборки и обработки заявок")
                        .description("Тестовое задание для Light digital")
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("Михаил Семенов")
                                .url("https://github.com/m1hail1337")
                                .email("mihail7enov@mail.ru")
                        ));
    }
}
