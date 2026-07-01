

import com.sun.net.httpserver.HttpServer;
import com.gambleking.controller.GameController; // ★修正：正しいインポートパス
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            // ポート8080でサーバーを起動
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            
            // ルーティングの処理をControllerクラスに委譲
            GameController controller = new GameController();
            controller.registerRoutes(server);

            server.setExecutor(null); 
            System.out.println("Server started on http://localhost:8080");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}