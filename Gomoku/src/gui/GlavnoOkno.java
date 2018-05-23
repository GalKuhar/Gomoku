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
import logika.Igralec;
import logika.Peterica;
import logika.Plosca;
import logika.Poteza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	/**
	 * JPanel, v katerega rišemo crne in bele žetone
	 */
	private IgralnoPolje polje;

	/**
	 * Statusna vrstica v spodnjem delu okna
	 */
	private JLabel status;

	
	/**
	 * Logika igre, null ce se igra trenutno ne igra
	 */
	protected Igra igra;
	
	/**
	 * Strateg, ki vlece poteze belega
	 */
	private Strateg strategBeli;

	/**
	 * Strateg, ki vlece poteze crnega
	 */
	private Strateg strategCrni;
	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;

	public GlavnoOkno() {
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		// menu
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Igra");
		menu_bar.add(igra_menu);
		
		igraClovekRacunalnik = new JMenuItem("Clovek : racunalnik");
		igra_menu.add(igraClovekRacunalnik);
		igraClovekRacunalnik.addActionListener(this);
	
		igraRacunalnikClovek = new JMenuItem("Racunalnik : clovek");
		igra_menu.add(igraRacunalnikClovek);
		igraRacunalnikClovek.addActionListener(this);

		igraRacunalnikRacunalnik = new JMenuItem("Racunalnik : racunalnik");
		igra_menu.add(igraRacunalnikRacunalnik);
		igraRacunalnikRacunalnik.addActionListener(this);

		igraClovekClovek = new JMenuItem("Clovek : clovek");
		igra_menu.add(igraClovekClovek);
		igraClovekClovek.addActionListener(this);

		// igralno polje
		polje = new IgralnoPolje(this);
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout);
		
		// statusna vrstica za sporocila
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		// zacnemo novo igro
		novaIgra(new Clovek(this, Igralec.CRNI),
				new Racunalnik(this, Igralec.BELI));
	}
	
	/**
	 * @return trenutna igralna plosca, ali null, ce igra ni aktivna
	 */
	public Plosca getPlosca() {
		return (igra == null ? null : igra.getPlosca());
	}
	
	public void novaIgra(Strateg noviSrategCRNI, Strateg noviStrategBELI) {
		if (strategCrni != null) { strategCrni.prekini(); }
		if (strategBeli != null) { strategBeli.prekini(); }
		
		this.igra = new Igra();
		
		strategCrni = noviSrategCRNI;
		strategBeli = noviStrategBELI;
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
		if (e.getSource() == igraClovekRacunalnik) {
			novaIgra(new Clovek(this, Igralec.CRNI),
					  new Racunalnik(this, Igralec.BELI));
		}
		else if (e.getSource() == igraRacunalnikClovek) {
			novaIgra(new Racunalnik(this, Igralec.CRNI),
					  new Clovek(this, Igralec.BELI));
		}
		else if (e.getSource() == igraRacunalnikRacunalnik) {
			novaIgra(new Racunalnik(this, Igralec.CRNI),
					  new Racunalnik(this, Igralec.BELI));
		}
		else if (e.getSource() == igraClovekClovek) {
			novaIgra(new Clovek(this, Igralec.CRNI),
			          new Clovek(this, Igralec.BELI));
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
			case CRNI_NA_POTEZI: status.setText("Na potezi je crni"); break;
			case BELI_NA_POTEZI: status.setText("Na potezi je beli"); break;
			case CRNI_ZMAGA: status.setText("Zmagal je crni"); break;
			case BELI_ZMAGA: status.setText("Zmagal je beli"); break;
			case NEODLOCENO: status.setText("Neodloceno!"); break;
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
	
	public Poteza getZadnjaPoteza() {
		return igra.getZadnjaPoteza();
	}
	
	public Peterica getZmagovalnaPeterica() {
		return igra.getZmagovalnaPeterica();
	}
}