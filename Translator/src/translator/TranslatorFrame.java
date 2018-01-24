package translator;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class TranslatorFrame extends JFrame {

	private Translator translator;
	private String lastFile = "";
	private JTextArea log;
	
	public TranslatorFrame()  {
		setMinimumSize(new Dimension(500, 450));
		setResizable(false);
		setTitle("Translator");
		getContentPane().setLayout(null);
		
		JTextArea output = new JTextArea();
		output.setText("Some output...");
		output.setFont(new Font("Arial", Font.PLAIN, 13));
		output.setEditable(false);
		output.setBounds(10, 271, 474, 133);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		getContentPane().add(output);
		
		JTextArea input = new JTextArea();
		input.setFont(new Font("Arial", Font.PLAIN, 13));
		input.setText("Please enter input here...");
		input.setBounds(10, 93, 474, 133);
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		getContentPane().add(input);
		
		JButton translate = new JButton("Translate");
		translate.setBounds(190, 237, 118, 23);
		getContentPane().add(translate);
		
		log = new JTextArea();
		log.setFont(new Font("Consolas", Font.PLAIN, 11));
		log.setEditable(false);
		log.setBounds(215, 35, 269, 47);
		getContentPane().add(log);
		
		JScrollPane scrollPane = new JScrollPane(log);
		scrollPane.setBounds(215, 35, 269, 47);
		getContentPane().add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JScrollPane scrollPane_1 = new JScrollPane(input);
		scrollPane_1.setBounds(10, 93, 474, 133);
		getContentPane().add(scrollPane_1);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JScrollPane scrollPane_2 = new JScrollPane(output);
		scrollPane_2.setBounds(10, 271, 474, 133);
		getContentPane().add(scrollPane_2);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JButton clear = new JButton("Clear");
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				input.setText("");
				output.setText("");
				log.setText("");
			}
		});
		clear.setBounds(381, 237, 89, 23);
		getContentPane().add(clear);
		
		JButton fileInput = new JButton("Use Input File");
		fileInput.setBounds(10, 237, 118, 23);
		getContentPane().add(fileInput);
		
		JLabel ruleLabel = new JLabel("Rule File:");
		ruleLabel.setBounds(10, 11, 474, 23);
		getContentPane().add(ruleLabel);
		
		JButton loadRuleFile = new JButton("Load Rule File");
		loadRuleFile.setBounds(10, 35, 133, 23);
		getContentPane().add(loadRuleFile);
		
		JButton reloadRuleFile = new JButton("Reload Rule File");
		reloadRuleFile.setBounds(10, 60, 133, 23);
		getContentPane().add(reloadRuleFile);
		
		JButton helpButton = new JButton("Help");
		helpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// have a separate window?
				log.setText("No help option yet. Please stand by");
			}
		});
		helpButton.setBounds(148, 35, 64, 47);
		getContentPane().add(helpButton);

		translator = null;
		try {
			log.setText("Creating Translator from file morseCode.txt");
			translator = new Translator("src/translator/morseCode.txt");
			ruleLabel.setText("Rule File: " + translator.getRuleFilePath());
			lastFile = translator.getRuleFile().getParent();
			log.append("\n" + "Rule file loading successful");
		} catch (TranslatorException | CodeException e1) {
			log.append("\n" + "Error reading default file morseCode.txt.\n" + e1.getMessage());
		}

		translate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				log.setText("Translating...");
				try {
					if (translator != null) {
						String i = input.getText();
						String o = translator.translate(i);
						output.setText(o);
						
						for(int j = 0; j < translator.getPrintCount(); j++) {
							log.append(translator.getPrint(j));
						}
						
						log.append("\n" + "Translation successful");
					}
					else {
						log.append("\n" + "Translator object not initialised.\n"
								+ "Please select a rule file.");
					}
				} catch (TranslatorException | CodeException e1) {
					log.append("\n" + "Error translating input.\n" + e1.getMessage());
				}
			}
		});
		reloadRuleFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (translator == null) {
					log.setText("No initialised translator, please select a rule file for loading");
					return;
				}
				log.setText("Reloading rule file " + translator.getRuleFilePath());
				try {
					translator.reload();
					log.append("\n" + "Rule file reloading successful");
				} catch (TranslatorException | CodeException e1) {
					translator = null;
					log.append("\n" + "Error reading rule file.\n" + e1.getMessage() + "\nPlease select a new one.");
					ruleLabel.setText("Rule File: <NULL>");
				}
			}
		});
		loadRuleFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				log.setText("Reading rule file");
				try {
					JFileChooser chooser;
					chooser = new JFileChooser(lastFile);
					FileNameExtensionFilter filter = new FileNameExtensionFilter ("Rule files", "txt");
					chooser.setFileFilter(filter); 
					int returnVal  = chooser.showOpenDialog(getParent()); 
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						translator = new Translator(chooser.getSelectedFile().getAbsolutePath());
						ruleLabel.setText("Rule File: " + translator.getRuleFilePath());
						lastFile = translator.getRuleFile().getParent();
						log.append("\n" + "Rule file loading successful");
					} 
					else if (returnVal == JFileChooser.CANCEL_OPTION) {
						log.setText("");
					}
				} catch (TranslatorException | CodeException e1) {
					translator = null;
					log.append("\n" + "Error reading rule file.\n" + e1.getMessage());
					ruleLabel.setText("Rule File: <NULL>");
				}
			}
		});
		fileInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				log.setText("Reading input from file");
				StringBuilder contentBuilder = new StringBuilder();
				JFileChooser chooser = new JFileChooser();
				int returnVal  = chooser.showOpenDialog(getParent()); 
				if (returnVal == JFileChooser.APPROVE_OPTION) { 
					try (Stream<String> stream = Files.lines(chooser.getSelectedFile().toPath(), StandardCharsets.UTF_8)) {
					    stream.forEach(s -> contentBuilder.append(s).append("\n"));
					}
					catch (IOException e1) {
						log.append("\n" + "Error reading input file.\n" + e1.getMessage());
					}
					input.setText(contentBuilder.toString()); 
				} 
			}
		});
		
		this.setVisible(true);
	}
}
