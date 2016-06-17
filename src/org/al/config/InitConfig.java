package org.al.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class InitConfig {
    public static void init() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(".config"), StandardCharsets.UTF_8);

        for (String line : lines) {
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }

            String[] parts = line.split(" = ");
            String variable = parts[0];
            String value = parts[1];

            switch (variable) {
                case "TOTAL_RUNS":
                    Config.TOTAL_RUNS = Integer.parseInt(value);
                    break;
                case "SAVE_CHUNK":
                    Config.SAVE_CHUNK = Integer.parseInt(value);
                    break;
                case "LEARN_GAME":
                    Config.LEARN_GAME = Boolean.parseBoolean(value);
                    break;
                case "DISPLAY_GAME":
                    Config.DISPLAY_GAME = Boolean.parseBoolean(value);
                    break;
                case "HATETRIS":
                    Config.HATETRIS = Boolean.parseBoolean(value);
                    break;
                case "SAVE_BEST_GAME":
                    Config.SAVE_BEST_GAME = Boolean.parseBoolean(value);
                    break;
                case "BOARD_WIDTH":
                    Config.BOARD_WIDTH = Integer.parseInt(value);
                    break;
                case "BOARD_HEIGHT":
                    Config.BOARD_HEIGHT = Integer.parseInt(value);
                    break;
                default:
                    break;
            }
        }


        Config.QMAP_PATH = "qMatrix_" + Config.BOARD_WIDTH + "x" + Config.BOARD_HEIGHT + ".qmap";
    }
}
