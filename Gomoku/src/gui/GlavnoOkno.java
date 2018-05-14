package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igra;
import logika.Plosca;
import logika.Poteza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	/**
	 * JPanel, v katerega rišemo èrne in bele žetone
	 */
	private IgralnoPolje polje;

	/**
	 * Statusna vrstica v spodnjem delu okna
	 */
	private JLabel status;

	
	/**
	 * Logika igre, null èe se igra trenutno ne igra
	 */
	protected Igra igra;
	
	/**
	 * Strateg, ki vleèe poteze belega
	 */
	private Strateg strategBeli;

	/**
	 * Strateg, ki vleèe poteze èrnega
	 */
	private Strateg strategCrni;
	
	// Izbire v menujih
	private JMenuItem nova_igra;

	public GlavnoOkno() {
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		// menu
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Igra");
		menu_bar.add(igra_menu);
		nova_igra = new JMenuItem("Nova igra");
		igra_menu.add(nova_igra);
		nova_igra.addActionListener(this);
	
		// igralno polje
		polje = new IgralnoPolje(this);
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout);
		
		// statusna vrstica za sporoèila
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		// zaènemo novo igro
		nova_igra();
	}
	
	/**
	 * @return trenutna igralna plosèa, ali null, èe igra ni aktivna
	 */
	public Plosca getPlosca() {
		return (igra == null ? null : igra.getPlosca());
	}
	
	public void nova_igra() {
		if (strategCrni != null) { strategCrni.prekini(); }
		if (strategBeli != null) { strategBeli.prekini(); }
		this.igra = new Igra();
		strategCrni = new Clovek(this);
		strategBeli = new Clovek(this);
		// Tistemu, ki je na potezi, to povemo
		switch (igra.stanje()) {
		case CRNI_NA_POTEZI: strategCrni.na_potezi(); break;
		case BELI_NA_POTEZI: strategBeli.na_potezi(); break;
		default: break;
		}
		osveziGUI();
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nova_igra) {
			nova_igra();
		}
	}

	public void odigraj(Poteza p) {
		igra.odigrajPotezo(p);
		osveziGUI();
		switch (igra.stanje()) {
		case CRNI_NA_POTEZI: strategCrni.na_potezi(); break;
		case BELI_NA_POTEZI: strategBeli.na_potezi(); break;
		case CRNI_ZMAGA: break;
		case BELI_ZMAGA: break;
		case NEODLOCENO: break;
		case IGRA_NI_VELJAVNA: break;
		}
	}
	
	public void osveziGUI() {
		if (igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(igra.stanje()) {
			case CRNI_NA_POTEZI: status.setText("Na potezi je èrni"); break;
			case BELI_NA_POTEZI: status.setText("Na potezi je beli"); break;
			case CRNI_ZMAGA: status.setText("Zmagal je èrni"); break;
			case BELI_ZMAGA: status.setText("Zmagal je beli"); break;
			case NEODLOCENO: status.setText("Neodloèeno!"); break;
			case IGRA_NI_VELJAVNA: break;
			}
		}
		polje.repaint();
	}
	
	public void klikniPolje(int i, int j) {
		if (igra != null) {
			switch (igra.stanje()) {
			case BELI_NA_POTEZI:
				strategBeli.klik(i, j);
				break;
			case CRNI_NA_POTEZI:
				strategCrni.klik(i, j);
				break;
			default:
				break;
			}
		}		
	}

	/**
	 * @return kopija trenutne igre
	 */
	public Igra copyIgra() {
		return new Igra(igra);
	}
}