import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.CORBA.SetOverrideType;
import org.w3c.dom.Text;

public class Hangman extends JFrame {

	public final int WIDTH = 768;
	public final int HEIGHT = 524;
	public HangmanPanel panel;
	
	public static void main(String[] args) {
		new Hangman();
	}
	
	public Hangman() {
		panel = new HangmanPanel(this);
		setTitle("Hangman");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		add(panel);
	}
	
}

class HangmanPanel extends JPanel{
	
	public Hangman frame;
	public JTextField input = new JTextField();
	public JTextArea dispText = new JTextArea();
	public ManPanel panelMan;
	public String text = "";
	public boolean wordComplete = true;
	
	public HangmanPanel(Hangman frame) {
		this.frame = frame;
		setLayout( );
		setup();
	}
	
	public void setup() {
		//System.out.print("vibugefburgrfreigu");
		dispText.setText("Enter \nWord(s)");
		dispText.setBounds(25 , 25, frame.WIDTH/2 + 10, frame.HEIGHT-200);
		dispText.setEditable(false);
		dispText.setBackground(Color.GREEN);
		dispText.setForeground(Color.RED);
		dispText.setFont(new Font("Arial", Font.BOLD, 55));
		dispText.setLineWrap(true);
		dispText.setWrapStyleWord(true);
		add(dispText);
		
		panelMan = new ManPanel(frame);
		add(panelMan);
		
		input.setText("");
		input.setBounds(20, frame.HEIGHT - 130, frame.WIDTH - 50, 80);
		input.setFont(new Font("Arial", Font.BOLD, 35));
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setText(input.getText());
				input.setText("");
			}	
		});
		add(input);
		
		new Thread(new textChecker(this, panelMan)).start();
	}
	
	public void setText(String s) {
		this.text = s;
	}
	
}

class ManPanel extends JPanel{
	
	Hangman frame;
	int lives = 6;
	
	public ManPanel(Hangman frame) {
		this.frame = frame;
		setBounds(frame.WIDTH/2 + 35, 25, frame.WIDTH/2 - 70, frame.HEIGHT-200);
		setBackground(frame.getBackground());
	}
	
	public void displayMan(int lives) {
		this.lives = lives;
		repaint();
	}
	public void paint(Graphics g) {
		System.out.println(lives + "   " + (6-lives));
		g.setColor(Color.BLACK);
		g.drawLine(0, 300, 300, 300);
		g.drawLine(75, 300, 75, 50);
		g.drawLine(75, 50, 200, 50);
		g.drawLine(200, 50, 200, 100);
		for(int i = 6 - lives; i > 0; i--) {
			switch(i) {
			case 1:
				g.drawOval(200 - 30, 100, 60, 60);
				break;
			}
		}
	}
	
}

class textChecker extends Thread {
	
	HangmanPanel panel;
	ManPanel panelMan;
	public boolean stillPlaying = true;
	public int lives = 6;
	
	public String word;
	public char[] wordChars;
	public boolean[] wordBools;
	
	public textChecker(HangmanPanel panel, ManPanel panelMan) {
		this.panel = panel;
		this.panelMan = panelMan;
	}
	
	public void run() {
		while(stillPlaying) {
			if(panel.wordComplete) {
				if(!panel.text.equals("")) {
					word = panel.text;
					wordChars = word.toCharArray();
					wordBools = new boolean[wordChars.length];
					panel.text = "";
					displayText();
					panelMan.displayMan(lives);
					panel.wordComplete = false;
				}
			}
			
			else if(!panel.text.equals("")) {
				boolean inWord = false;
				for(int i = 0; i < wordChars.length; i++) {
					if(panel.text.equals(word.substring(i, i+1))) {
						wordBools[i] = true;
						inWord = true;
					}
				}
				lives -= (inWord) ? 0 : 1;
				displayText();
				panelMan.displayMan(lives);
				System.out.println(panel.dispText.getText());
				panel.text = "";
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
	
	public void displayText() {
		panel.dispText.setText(buildString());
	}
	
	public String buildString() {
		String builder = "";
		for(int i = 0; i < wordBools.length; i++) {
			String printChar = word.substring(i, i+1);
			builder += (wordBools[i]) ? (!printChar.equals(" ")) ? printChar : " " : "°";
			//builder += " ";
		}
		return builder;
	}
	
}