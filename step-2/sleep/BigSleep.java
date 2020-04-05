import java.util.concurrent.*;

public class BigSleep {
    public static void main(String[] args) throws Exception {
        long timeToSleep = 10L;
        TimeUnit time = TimeUnit.MINUTES;
        time.sleep(timeToSleep);
    }
}