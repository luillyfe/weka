/*
 * 
 */
package gettingStarted;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Inicia el Sistema de Deteccion de Intrusos, carga el modelo. 
 * El primer paso en la función del IDS es escanear el directorio 
 * 'c:\Users\luilly\Documents\Data' en busca de estadisticas 
 * a nivel de conexion sobre la interfaz de una red informatica. 
 * Es importante aclarar que esta version del IDS no se encarga de
 * la recolección, ni de la estructuacion de los datos, tampoco de
 * colocarlos en el directorio antes indicado para su analisis,
 * se espera que de alguna manera (ya sea manualmente) estos datos 
 * se ubiquen allí.
 * Seguidamente analizamos estas conexiones a fin de 
 * clasificarlas entre intrusivas y normales.
 * @author luilly
 */
public class IDS {

    public static void main(String[] args) throws IOException, Exception {
        Path model = Paths.get( "models/120E.model" );
        
        new Thread(new Sniffer()).start();
        Analyze analyze = new Analyze();
        analyze.load(model);
        
        int index = 0;
        for(;;) {
            try {
                if(Sniffer.dataSets.size()>index) {
                    analyze.analyze(Sniffer.dataSets.get(index));
                    index++;
                }
            } catch(NullPointerException npe) { continue; }
        }
    }
}
