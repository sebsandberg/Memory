import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
/**@assignment lab 4
 * @author Sebastian Sandberg & Erik Risfelt
 * @groupnumber Labbgrupp 55
 */
public class Memory extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int p1Score; // Score of player 1
	private int p2Score; // Score of player 2
	private Color p1Color = Color.YELLOW;
	private Color p2Color = Color.lightGray;
	private Kort[] k;
	private Kort[] activeCards;
	private Kort currentCard;
	private int height = 0;
	private int width = 0;
	private JPanel cards = new JPanel();
	private JButton stop;
	private JButton newGame;
	private int nbrOfActiveCards;
	private Kort lastCard;
	private JPanel p1panel;
	private JLabel p1ScorePanel;
	private JPanel p2panel;
	private JLabel p2ScorePanel;
	private boolean disable = false;
	private Timer timer;
	private int n;
	
	public Memory(){
		//Create an array of Kort containing images
		File bildmapp = new File("bildmapp");
		File[] bilder = bildmapp.listFiles();
		k = new Kort[bilder.length];
		for(int i = 0; i < bilder.length; i++){
			ImageIcon icon = new ImageIcon(bilder[i].getPath());
			k[i] = new Kort(icon, Kort.Status.DOLT);
		}
		//Mainframe
		setTitle("Memory");
		setBackground(Color.lightGray);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//PlayerPanel
		JPanel players = new JPanel();
		add(players, BorderLayout.WEST);
		players.setLayout(new GridLayout(2,1));
		players.setBackground(Color.GRAY);	
		
		//Player 1 Panel
		p1panel = new JPanel();
		players.add(p1panel);
		p1panel.setLayout(new GridLayout(2,1));
		p1panel.setBackground(p1Color);
		JLabel p1 = new JLabel("Spelare 1", JLabel.CENTER);
		p1.setFont(new Font("Serif", Font.BOLD, 30));
		p1panel.add(p1);
		p1ScorePanel = new JLabel("" + p1Score, JLabel.CENTER);
		p1ScorePanel.setFont(new Font("Serif", Font.BOLD, 30));
		p1panel.add(p1ScorePanel);
		
		//Player 2
		p2panel = new JPanel();
		players.add(p2panel, BorderLayout.SOUTH);
		p2panel.setLayout(new GridLayout(2,1));
		p2panel.setBackground(p2Color);
		JLabel p2 = new JLabel("Spelare 2", JLabel.CENTER);
		p2.setFont(new Font("Serif", Font.BOLD, 30));
		p2panel.add(p2);
		p2ScorePanel = new JLabel("" + p2Score, JLabel.CENTER);
		p2ScorePanel.setFont(new Font("Serif", Font.BOLD, 30));
		p2panel.add(p2ScorePanel);
		
		
		//Menupanel
		JPanel menu = new JPanel();
		add(menu, BorderLayout.SOUTH);
		menu.setLayout(new FlowLayout());
		
		//Buttons
		newGame = new JButton("Nytt spel");
		menu.add(newGame);
		
		stop = new JButton("Avsluta");
		menu.add(stop);
		
		
	
		//CardsPanel
		//JPanel cards = new JPanel();
		add(cards, BorderLayout.CENTER);
		cards.setBackground(Color.white);
		
		//ActionListeners
		newGame.addActionListener( this );
		stop.addActionListener( this );
		//newGame
		nyttSpel();
	}

	/**
	 * Creates a new game. 
	 */
	public void nyttSpel (){
		while(cards.getComponentCount()>0){
			cards.removeAll();
		}
		String inputHeight = JOptionPane.showInputDialog("Please input height between 2 and 6");
		String inputWidth = JOptionPane.showInputDialog("Please input width between 2 and 6"); 	
		/*
		 * Check if the input is numbers only, 
		 * if not, then give an error message and then call nyttSpel() again.
		 */
		try {
			height = Integer.parseInt(inputHeight);
			width = Integer.parseInt(inputWidth);
		} catch (NumberFormatException e){
			JOptionPane.showMessageDialog(null,
					"Please insert numbers only",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			nyttSpel();
		}
		
		height = Integer.parseInt(inputHeight);
		width = Integer.parseInt(inputWidth);
		cards.setLayout(new GridLayout(height, width));
		n = height * width;
		/*
		 * Check if the input is numbers between 2 and 6 only, 
		 * if not, then give an error message and then call nyttSpel() again.
		 */
		if ( n > 36 || n < 4 ) {
			JOptionPane.showMessageDialog(null,
					"Please insert numbers between 2 and 6 only",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			nyttSpel();//popup med error, samt restart-listener
		} else if ( n%2 != 0 ){ //Checks if the n is not even, if its not, then remove one.
			n = n-1;
		}
		//creates a new array with the n numbers of spaces
		activeCards = new Kort[n];
		Verktyg.slumpOrdning(k);
		for ( int i = 0; i <= (n/2); i++){
			activeCards[i] = k[i];
			activeCards[n-i-1] = activeCards[i].copy();
		}
		Verktyg.slumpOrdning(activeCards);
		for (int i = 0; i < n; i++){
			activeCards[i].setStatus(Kort.Status.DOLT);
			activeCards[i].setPreferredSize(new Dimension(100, 100));
			cards.add(activeCards[i]);
			activeCards[i].addActionListener( this );
		}
		repaint();
		cards.revalidate();
		//Reset players score
		p1Score = 0;
		p2Score = 0;
		p1Color = Color.YELLOW;
		p2Color = Color.lightGray;
		p1ScorePanel.setText("" + p1Score);
		p2ScorePanel.setText("" + p2Score);
		p1ScorePanel.repaint();
		p2ScorePanel.repaint();
		p1panel.setBackground(p1Color);
		p2panel.setBackground(p2Color);
		p1panel.repaint();
		p2panel.repaint();
		pack();
	}
	/**
	 * A timer set to 1.5 seconds. Used to block user input
	 */
	public void delay(){
		timer = new Timer(1500,  this);
		timer.setRepeats(false);
		timer.start();
	}
	/**
	 * Checks if the two cards are the same. If true, then add a score to the current player, else change player.
	 */
	public void checkCards(){
		if(currentCard.sammaBild(lastCard)){
			currentCard.setEnabled(false);
			currentCard.setStatus(Kort.Status.SAKNAS);
			currentCard.setEnabled(true);
			lastCard.setEnabled(false);
			lastCard.setStatus(Kort.Status.SAKNAS);
			lastCard.setEnabled(true);
			// Check for which player is active
			if(p1Color == Color.YELLOW){
				p1Score = p1Score +1;
				p1ScorePanel.setText("" + p1Score);
				p1ScorePanel.repaint();
			} else if (p2Color == Color.YELLOW){
				p2Score = p2Score +1;
				p2ScorePanel.setText("" + p2Score);
				p2ScorePanel.repaint();
			}
			repaint();
		} else{
			currentCard.setEnabled(false);
			currentCard.setStatus(Kort.Status.DOLT);
			currentCard.setEnabled(true);
			lastCard.setEnabled(false);
			lastCard.setStatus(Kort.Status.DOLT);
			lastCard.setEnabled(true);
			if(p1Color == Color.YELLOW){
				p1Color = Color.lightGray;
				p2Color = Color.YELLOW;
				p1panel.setBackground(p1Color);
				p2panel.setBackground(p2Color);
				p1panel.repaint();
				p2panel.repaint();
			} else if (p2Color == Color.YELLOW){
				p1Color = Color.YELLOW;
				p2Color = Color.lightGray;
				p1panel.setBackground(p1Color);
				p2panel.setBackground(p2Color);
				p2panel.repaint();
			}
		}
		nbrOfActiveCards = 0;
		scoreCheck();
	}
	/**
	 * Checks if the final score has been reached.
	 * If true, then show an option to play again, or exit.
	 */
	public void scoreCheck(){
		int end = (n/2);
		if(end == p1Score + p2Score){
			String message = "Vill du spela igen?";
			String title = "Spelet är över";
			int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION){
				nyttSpel();
			} else if (reply == JOptionPane.NO_OPTION){
				System.exit(0);
			}
		}
	}
	/**
	 * Checks for which button that has been pressed and act accordingly.
	 */
	public void actionPerformed(ActionEvent e){
		// if "Avsluta" has been clicked, quit.
		if (e.getSource() == stop && !disable){
			 System.exit(0);
		}
		// if "Nytt spel" has been clicked, start a new game by calling nyttSpel()
		else if (e.getSource() == newGame && !disable){
			nyttSpel();
		}
		else if (e.getSource() == timer){
			disable = false;
			timer.stop();
			checkCards();
		}
		else if(!disable){
			Object o = e.getSource();
			for(int i=0; i<activeCards.length; i++){
				if (o.equals(activeCards[i])){
					//Checks so that the card is not set to SAKNAS and is not the same card that was clicked.
					if(activeCards[i].getStatus() != Kort.Status.SAKNAS && !activeCards[i].equals(lastCard)){
						//Make the card SYNLIGT
						activeCards[i].setEnabled(false);
						activeCards[i].setStatus(Kort.Status.SYNLIGT);
						activeCards[i].setEnabled(true);
						nbrOfActiveCards = nbrOfActiveCards + 1;
						if(nbrOfActiveCards == 1){
							lastCard = activeCards[i];
						} else if (nbrOfActiveCards==2){
							disable = true;
							currentCard = activeCards[i];
							delay();
						}
						repaint();
					}
				}
			}
		 }	  
	 }
	private static void newMemory(){
		new Memory();
	}
	public static void main(String[] args){
		newMemory();
	}
}
