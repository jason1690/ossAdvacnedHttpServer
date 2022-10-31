import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer extends Thread{
    private ServerSocket serverSocket;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public MainServer(int port)  {
        try{
            serverSocket= new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        while(true){
            try {
                threadPool.submit(new RequestHandler(serverSocket.accept()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
