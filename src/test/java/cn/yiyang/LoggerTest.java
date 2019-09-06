package cn.yiyang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName LoggerTest
 * @Description TODO
 * @Author Administrator
 * @Date 2018/11/30 8:52
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    @Test
    public void testLogger() {
      log.error("这里出现问题啦...");
      log.info("这里的问题只是info...");
      log.debug("debug...");
    }
}
