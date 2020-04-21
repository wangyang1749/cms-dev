import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wangyang.syscall.controller"})
public class SpringBootTest {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTest.class, args);

    }
}
