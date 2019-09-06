package cn.yiyang.system.exception;

/**
 * @ClassName CustomUnauthorizedException
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/5 10:54
 * @Version 1.0
 **/
public class CustomUnauthorizedException extends RuntimeException {
    public CustomUnauthorizedException(String msg){
        super(msg);
    }

    public CustomUnauthorizedException() {
        super();
    }
}
