package reproductor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.TabableView;

import static javax.swing.JOptionPane.showMessageDialog;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class ClaseMain {

	private JFrame frame;
	JList<Song> list;
	public JButton btnStop;
	public JButton btnPlay;
	JFileChooser fileChooser;
	DefaultListModel<Song> dataList = new DefaultListModel<Song>();	     
    boolean pausado=false;
    boolean enReproduccion=false;
    MusicPlayer mreproductor= new MusicPlayer();
    JLabel lblReproduciendo;
    ActionListener updateClockAction;
    Timer t;
    int counter=0;
	
    
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClaseMain window = new ClaseMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public ClaseMain() {
		initialize();
	}
	public  void finalizo() {
		pausado=false;
		enReproduccion=false;
		btnPlay.setLabel("Play");
		lbltiempo.setText("");
		mreproductor.Stop();
		t.stop();
		t=null;
	
	}
	
	public void iniciarConteo(int duracion) {
		
	      updateClockAction = new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
			      //clock es un custom component
			      //clock es un JLabel
				  counter++;
				  lbltiempo.setText("" + minutosYsegundos(counter) + " De " + minutosYsegundos(duracion));  //00:01
				  if(counter==duracion) {
					  finalizo();
				  }
			    }
			};
		
	}
	
	public String minutosYsegundos(int duracion) {
		int minutos=0;
		int segundos=0;
		
		minutos= duracion / 60;
		
		segundos= duracion % 60;
		String fmin = String.format("%02d", minutos);
		String fseg = String.format("%02d", segundos);
		return "" + fmin +":"+fseg;
	}
	
	private JLabel lbltiempo;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;

    
	public void agregarFile() {
		fileChooser=new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Users\\Downloads"));
        
        fileChooser.setDialogTitle("Seleccionar");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files","mp3"));
        if(fileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
        	
        	
        	 try {
        		 String pathUrl = fileChooser.getSelectedFile().getPath();
             	String name =fileChooser.getSelectedFile().getName();
        		AudioFile audioFile = AudioFileIO.read(fileChooser.getSelectedFile());
				int length = audioFile.getAudioHeader().getTrackLength();
				Song song = new Song(pathUrl,name,length);
				//this.songs.add(song);
				this.dataList.addElement(song);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
	}

	
	private void initialize() {
		frame = new JFrame();
	
		frame.getContentPane().setBackground(new Color(255,159,69));
		frame.setBounds(100, 100, 471, 471);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		list = new JList<Song>(dataList);
		list.setBackground(new Color(242, 201, 225));
		list.setBounds(26, 128, 148, 216);
		frame.getContentPane().add(list);
		
		JButton btnAgregarCancion = new JButton("Agregar Cancion");
		btnAgregarCancion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarFile();
			}
		});
		btnAgregarCancion.setBounds(26, 354, 143, 29);
		frame.getContentPane().add(btnAgregarCancion);
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(pausado) {
					
					mreproductor.resume();
					pausado=false;
					enReproduccion=true;
					btnPlay.setLabel("Pause");
					t.start();
				}else if(enReproduccion) {					
					mreproductor.pause();
					enReproduccion=false;
					pausado=true;
					btnPlay.setLabel("Play");
					t.stop();
					
				}else {
					int index = list.getSelectedIndex();
					  if(index >= 0){ 
						  Song cancion = list.getSelectedValue();
						  btnPlay.setLabel("Pausar");						 
						  mreproductor.Play(cancion.getPath());//inicia un thread
						  enReproduccion=true;						 
						  lblReproduciendo.setText(cancion.getNombre());
						  iniciarConteo(cancion.getDuracion()); 
						  t = new Timer(1000, updateClockAction);
						  t.start();
					    }else {
					    	showMessageDialog(null, "Seleccione una canción");
					    }	
				}	
			}
		});
		btnPlay.setBounds(200, 125, 85, 21);
		frame.getContentPane().add(btnPlay);
		
		JButton btnEliminar = new JButton("Eliminar Cancion");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  int index = list.getSelectedIndex();
				    if(index >= 0){ //Remove only if a particular item is selected
				        dataList.removeElementAt(index);
				    }else {
				    	showMessageDialog(null, "Seleccione una canción");
				    }
			}
		});
		btnEliminar.setBounds(26, 393, 143, 29);
		frame.getContentPane().add(btnEliminar);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enReproduccion || pausado) {
					pausado=false;
					enReproduccion=false;
					btnPlay.setLabel("Play");
					mreproductor.Stop();
					t.stop();
					t=null;
					lbltiempo.setText(""); 
					lblReproduciendo.setText("Seleccione una cancion");
					counter=0;
				}else {
					showMessageDialog(null, "No se esta reproduciendo ninguna canción");
				}
			}
		});
		btnStop.setBounds(309, 125, 85, 21);
		frame.getContentPane().add(btnStop);
		
		lblReproduciendo = new JLabel("No hay ninguna pista reproduciendose");
		lblReproduciendo.setForeground(new Color(229, 227, 201));
		lblReproduciendo.setBounds(210, 160, 237, 29);
		frame.getContentPane().add(lblReproduciendo);
		
		lbltiempo = new JLabel("");
		lbltiempo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbltiempo.setForeground(new Color(242, 201, 225));
		lbltiempo.setBounds(205, 199, 213, 29);
		frame.getContentPane().add(lbltiempo);
		
		lblNewLabel = new JLabel("Canciones");
		lblNewLabel.setForeground(new Color(229, 227, 201));
		lblNewLabel.setBounds(41, 91, 99, 13);
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("REPRODUCTOR MP3");
		lblNewLabel_1.setForeground(new Color(236, 236, 236));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(102, 27, 280, 29);
		frame.getContentPane().add(lblNewLabel_1);
	}
}
