package es.studium.psp;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MusicPlayer {
    private Map<String, AdvancedPlayer> players = new HashMap<>();

    public void play(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
            players.put(filePath, player); // Almacena el reproductor en el mapa
            new Thread(() -> {
                try {
                    player.play(); // Reproduce el archivo en un nuevo hilo
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void stop(String filePath) {
        AdvancedPlayer player = players.get(filePath);
        if (player != null) {
            player.close(); // Cierra el reproductor
            players.remove(filePath); // Elimina el reproductor
        }
    }
}
