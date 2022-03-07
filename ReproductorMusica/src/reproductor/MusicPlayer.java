package reproductor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {

	FileInputStream FIS;
	BufferedInputStream BIS;
	public long SongTotalLength;
	public long PauseLocation;
	public String FilePath;	
	public Player player;
	public ClaseMain gui;
	
	
	
	public void Stop(){
		if(player != null){
				
				PauseLocation = SongTotalLength;
				
				player.close();		
		}
	}
	
	public void pause(){
		if(player != null){
			try {
				PauseLocation = FIS.available();				
				player.close();
			} catch (IOException e) {
				
			}
		}
	}
	
	public void resume(){
		try {
			FIS = new FileInputStream(FilePath);
			SongTotalLength = FIS.available();
			FIS.skip(SongTotalLength - PauseLocation);
			BIS = new BufferedInputStream(FIS);
			player = new Player(BIS);
						
			}catch(FileNotFoundException | JavaLayerException e) {
			}		
		    catch (IOException e) {
		    }
		new Thread(){//thread anonimo
			@Override
			public void run(){
				try {
					player.play();					
				} catch (JavaLayerException e) {
					
				}
			}
		}.start();	
		
	}
	
	public void Play(String path){
		try {
			FIS = new FileInputStream(path);
			BIS = new BufferedInputStream(FIS);
			player = new Player(BIS);
			FilePath = path + "";
			SongTotalLength = FIS.available();		
			} catch (JavaLayerException | FileNotFoundException ex) {
				
			}
			catch (IOException e) {
			  //	FIS>availabe
			}	
			
		new Thread(){
			@Override
			
			public void run(){
				try {
					player.play();
				} catch (JavaLayerException e) {
					
				}
			}
		}.start();
			
		
	}
	
}
