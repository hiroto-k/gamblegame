package com.gambleking.controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameController {

    public void registerRoutes(HttpServer server) {
        // 1. オープニング画面
        server.createContext("/", new StaticFileHandler("src/main/resources/public/index.html", "text/html"));
        server.createContext("/css/style.css", new StaticFileHandler("src/main/resources/public/css/style.css", "text/css"));

        // 2. ホーム画面
        server.createContext("/home", new StaticFileHandler("src/main/resources/public/home.html", "text/html"));
        server.createContext("/css/home.css", new StaticFileHandler("src/main/resources/public/css/home.css", "text/css"));

        // 3. バイト画面
        server.createContext("/work", new StaticFileHandler("src/main/resources/public/work.html", "text/html"));
        server.createContext("/css/work.css", new StaticFileHandler("src/main/resources/public/css/work.css", "text/css"));

        // 4. ゲーム選択ロビー画面
        server.createContext("/game/lobby", new StaticFileHandler("src/main/resources/public/game.html", "text/html"));
        server.createContext("/css/game.css", new StaticFileHandler("src/main/resources/public/css/game.css", "text/css"));

        // 5. じゃんけんゲーム画面
        server.createContext("/game/janken", new StaticFileHandler("src/main/resources/public/janken.html", "text/html"));
        server.createContext("/css/janken.css", new StaticFileHandler("src/main/resources/public/css/janken.css", "text/css"));

        // 6. ダイスゲーム画面
        server.createContext("/game/dice", new StaticFileHandler("src/main/resources/public/dice.html", "text/html"));
        server.createContext("/css/dice.css", new StaticFileHandler("src/main/resources/public/css/dice.css", "text/css"));

        // 7. ルーレットゲーム画面 (★競馬のURLを引き継ぎ、中身をルーレットのファイルに完全結合)
        server.createContext("/game/race", new StaticFileHandler("src/main/resources/public/roulette.html", "text/html"));
        server.createContext("/css/roulette.css", new StaticFileHandler("src/main/resources/public/css/roulette.css", "text/css"));

        // アクション・リダイレクト処理
        server.createContext("/start", new StartRedirectHandler());
    }

    // 静的ファイルハンドラ
    static class StaticFileHandler implements HttpHandler {
        private final String filePath;
        private final String contentType;

        public StaticFileHandler(String filePath, String contentType) {
            this.filePath = filePath;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                byte[] response = Files.readAllBytes(Paths.get(filePath));
                exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } catch (IOException e) {
                String errorMsg = "File not found: " + filePath;
                exchange.sendResponseHeaders(404, errorMsg.length());
                OutputStream os = exchange.getResponseBody();
                os.write(errorMsg.getBytes());
                os.close();
                System.out.println("[Error] " + errorMsg);
            }
        }
    }

    // indexから/homeへのリダイレクト
    static class StartRedirectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Location", "/home");
            exchange.sendResponseHeaders(302, -1);
        }
    }
}