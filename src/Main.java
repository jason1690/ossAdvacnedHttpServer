import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {

        Config.parseArgs(args);
        Config.loadAllDefaults();
        new MainServer(Config.mainServerPort).start();
    }

}
