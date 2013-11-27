/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Escanea el directorio C:/Users/luilly/Documenst/Data/ en busca de nuevos 
 * archivos creados y ubicados aqui. Si un archivo nuevo es colocado aqui 
 * lanzara un notificación.
 * @author luilly
 */
public class Sniffer implements Runnable {
    
    private WatchService ws;
    private Map<WatchKey, Path> keys;
    protected static ArrayList<Path> dataSets; 
    
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    
    public Sniffer() throws IOException{
        /** Crear un servicio de vigilancia */
        ws = FileSystems.getDefault().newWatchService();
        keys = new HashMap<WatchKey, Path>();
        
        /** Registrar los objetos en el servico de vigilancia */
        Path dir = Paths.get( "C:/Users/luilly/Documents/Data/" );
        try{
            WatchKey key = dir.register(ws, ENTRY_CREATE, 
                                            ENTRY_DELETE,
                                            ENTRY_MODIFY);
            keys.put(key, dir);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    /** Procesar los eventos */
    public void On() {
        dataSets = new ArrayList<Path>();
        
        for(;;){
            /** Esperar la ocurrencia de un evento */
            WatchKey key = null;
            try {
                key = ws.take();
            } catch (InterruptedException x){
                System.err.println(x);
            }
            
            Path dir = keys.get(key);
            if(dir == null) {
                System.err.println("Clave no reconocido");
            }
            
            for( WatchEvent<?> event: key.pollEvents() ) {
                WatchEvent.Kind kind = event.kind();
                
                if(kind == OVERFLOW) {
                    continue;
                }
                
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
                
                dataSets.add( child );
                //System.out.println( child );
            }
            
            boolean valid = key.reset();
            if(!valid) {
                keys.remove(key);
                
                if(keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        On();
    }
    
}
