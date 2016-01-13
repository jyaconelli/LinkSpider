package com.josephyaconelli.linkspider;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class MainFrame extends JFrame {

	private final JButton _startButton, _analyzeLinksButton;
	private final JTextField _urlField;
	private final JTextArea _resultField;
	private final ScrollPane _resultContainer;
	private final JPanel _controls;
	private ArrayList<String> _links;
	private ArrayList<String> _secondLevelLinks = new ArrayList<>();

	public MainFrame() {
		super("Link Spider");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(500, 500);

		// Start Button
		_startButton = new JButton("Start!");
		_startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					_links = GrabHTML.Connect(_urlField.getText(), "href=\"/view_video.php?", "href=\"", "\" title");
					_resultField.setText("");
					for (String link : _links) {
						_resultField.setText(_resultField.getText() + "\n" + link);
					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		// Analyze Links Button

		_analyzeLinksButton = new JButton("Analyze!");
		_analyzeLinksButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<ArrayList<String>, Integer> analyzeWorker = new SwingWorker<ArrayList<String>, Integer>() {

					ArrayList<String> videos = new ArrayList<>();
					
					@Override
					protected ArrayList<String> doInBackground() throws Exception {
						
						_resultField.setText("Analyzing...");
						int processed = 0;
						try {
							for (String link : _links) {
								videos.addAll((GrabHTML.Connect(link, "<meta name=\"twitter:player:stream\"", "content=\"", "\">")));
								processed++;
								publish(processed);
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return videos;
					}
					
					

					@Override
					protected void process(List<Integer> chunks) {
						int totalProcessed = chunks.get(chunks.size() - 1);
						_resultField.setText("Analyzing..." + totalProcessed + "/" + _links.size());
					}



					protected void done() {
						
						try {
							_secondLevelLinks.clear();
							_secondLevelLinks = new ArrayList<String>(videos.size());
							_resultField.setText("Done!");
							_secondLevelLinks = get();
							for (String footage : _secondLevelLinks) {
								_resultField.setText(_resultField.getText() + "\n" + footage);
							}
						} catch (InterruptedException | ExecutionException e) {
							_resultField.setText("Uh oh! Something went wrong!\n" + e.getMessage());
							e.printStackTrace();
						}
					}

				};
				analyzeWorker.execute();
				

			}
			
		

		});

		// URL Field
		_urlField = new JTextField("http://website.com");

		// Result Label
		_resultField = new JTextArea("You're links will end up here!");

		// controls pane
		_controls = new JPanel();
		_controls.setLayout(new GridLayout(1, 3));
		_controls.add(_startButton);
		_controls.add(_urlField);
		_controls.add(_analyzeLinksButton);
		add(_controls, BorderLayout.NORTH);

		// ScrollPane
		_resultContainer = new ScrollPane();
		_resultContainer.add(_resultField, _resultContainer);

		add(_resultContainer, BorderLayout.CENTER);

	}

}
