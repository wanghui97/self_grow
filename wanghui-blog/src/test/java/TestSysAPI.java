import com.wanghui.blog.BlogReceptionApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName: TestSysAPI
 * Package: com.wanghui.blog
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/23 17:52
 * @Version 1.0
 */
@SpringBootTest(classes = BlogReceptionApplication.class)
public class TestSysAPI {
    @Autowired
    private BlogReceptionApplication blogReceptionApplication;
    @Test
    public void test1(){
        boolean bool = "0".equals(0);
        System.out.println(bool);
    }
}
