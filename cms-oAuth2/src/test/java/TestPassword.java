import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class TestPassword {


    @Test
    public void test(){
        System.out.println(new BCryptPasswordEncoder().encode("secret"));
    }
}
