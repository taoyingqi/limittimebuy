package qed.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lonel on 2016/12/21.
 */
@SpringBootApplication //equivalent @Configuration, @EnableAutoConfiguration and @ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
