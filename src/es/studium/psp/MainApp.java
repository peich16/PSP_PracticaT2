package es.studium.psp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
	private JFrame frame;
	private JList<String> list; // mostrar una lista de elementos
	private DefaultListModel<String> listModel; // lista predeterminada
	private MusicPlayer player;

	// metodo principal y Runable
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MainApp window = new MainApp();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public MainApp() {
		iniciar(); //iniciacion de componentes
		player = new MusicPlayer(); // inicio del reproductor
		buscarArchivosAudio(); // búsqueda de ficheros
	}

	private void iniciar() {
		frame = new JFrame();
		frame.setTitle("Mi Música");
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		listModel = new DefaultListModel<>();
		list = new JList<>(listModel); // componente Jlist para mostrar lista por pantalla
		frame.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);

		JButton btnPlay = new JButton("Reproducir");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedFile = list.getSelectedValue(); //// Obtiene el archivo seleccionado en la lista
				if (selectedFile != null) {
					player.play(selectedFile);
				}
			}
		});
		panel.add(btnPlay);

		JButton btnStop = new JButton("Parar");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedFile = list.getSelectedValue(); // para el archivo seleccionado
				if (selectedFile != null) {
					player.stop(selectedFile);
				}
			}
		});
		panel.add(btnStop);

		// Añadir MouseListener para reproducir el archivo al hacer doble clic
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				//doble click en el ratón
				if (evt.getClickCount() == 2) {
					String selectedFile = list.getSelectedValue(); // obtiene el archivo seleccionado
					if (selectedFile != null) {
						player.play(selectedFile);
					}
				}
			}
		});
	}

	private void buscarArchivosAudio() {
		File[] roots = File.listRoots(); // Obtiene los roots del sistema de archivos
		List<File> audioFiles = new ArrayList<>(); // Lista para almacenar los archivos de audio encontrados 
		for (File root : roots) {
			buscarArchivosRecursivos(root, audioFiles); //  Busca archivos de audio de forma recursiva en cada root 
		}
		for (File audioFile : audioFiles) {
			listModel.addElement(audioFile.getAbsolutePath()); //  Añade los archivos encontrados al modelo de la lista
		}
	}

	private void buscarArchivosRecursivos(File file, List<File> audioFiles) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					buscarArchivosRecursivos(f, audioFiles);// Obtiene los archivos y directorios dentro del directorio actual
				}
			}
		} else {
			if (isAudioFile(file)) {
				audioFiles.add(file);
			}
		}
	}

	private boolean isAudioFile(File file) {
		String[] audioExtensions = { "mp3", "wav" }; 
		String fileName = file.getName().toLowerCase();
		for (String ext : audioExtensions) {
			if (fileName.endsWith("." + ext)) { // Verifica si el archivo termina con una de las extensiones de audio permitidas return true;
				return true;
			}
		}
		return false;
	}
}
