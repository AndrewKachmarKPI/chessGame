package com.chess.chessgame;

import com.chess.chessgame.serviceImpl.GameFileServiceImpl;
import com.chess.chessgame.services.GameFileService;

public class Main {
    public static void main(String[] args) {
        ChessGameApplication.main(args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            GameFileService gameFileService = new GameFileServiceImpl();
//            gameFileService.deleteWorkingFiles();
        }));
    }
}
