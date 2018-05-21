package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

import logika.Igra;
import logika.Plosca;
import logika.Polje;
import logika.Stanje;

@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener, MouseMotionListener {
	private GlavnoOkno master;
	
	/**
	 * Relativna širina èrte
	 */
	private final static double LINE_WIDTH = 0.1;
	
	/**
	 * Relativni prostor okoli èrnih in belih žetonov
	 */
	private final static double PADDING = 0.1;
	
	private int[] senca = null;
	
	private final Color belaSenca = new Color(255, 255, 255, 150);
	private final Color crnaSenca = new Color(0, 0, 0, 100);
	private final Color barvaOzadja = new Color(205, 133, 63);
	
	public IgralnoPolje(GlavnoOkno master) {
		super();
		setBackground(barvaOzadja);
		this.master = master;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

	private double squareWidth() {
		return Math.min(getWidth(), getHeight()) / Igra.getN();
	}
	
	private void paintBeli(Graphics2D g2, int i, int j) {
		double w = squareWidth();
		double r = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer žetona
		double x = w * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.drawOval((int)x, (int)y, (int)r , (int)r);
		g2.fillOval((int)x, (int)y, (int)r , (int)r);
	}
	
	private void paintCrni(Graphics2D g2, int i, int j) {
		double w = squareWidth();
		double r = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer žetona
		double x = w * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.drawOval((int)x, (int)y, (int)r , (int)r);
		g2.fillOval((int)x, (int)y, (int)r , (int)r);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		// širina kvadratka
		double w = squareWidth();
		// èrte
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		for (int i = 1; i < Igra.getN() + 1; i++) {
			g2.drawLine((int)(i * w - squareWidth() / 2),
					    (int)(squareWidth() / 2),
					    (int)(i * w - squareWidth() / 2),
					    (int)(Igra.getN() * w - squareWidth() / 2));
			g2.drawLine((int)(squareWidth() / 2),
					    (int)(i * w - squareWidth() / 2),
					    (int)(Igra.getN() * w - squareWidth() / 2),
					    (int)(i * w - squareWidth() / 2));
		}
		
		// žetoni
		Plosca plosca = master.getPlosca();
		if (plosca != null) {
			for (int i = 0; i < Igra.getN(); i++) {
				for (int j = 0; j < Igra.getN(); j++) {
					switch(plosca.element(i, j)) {
					case BELI: paintBeli(g2, i, j); break;
					case CRNI: paintCrni(g2, i, j); break;
					default: break;
					}
				}
			}
		}
		
		// senca
		if (senca != null) {
			int x = senca[0];
			int y = senca[1];
			double r = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
			if (master.igra.stanje() == Stanje.BELI_NA_POTEZI) {
				g2.setColor(belaSenca);
				g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
				g2.fillOval((int)x, (int)y, (int)r , (int)r);
			} else if (master.igra.stanje() == Stanje.CRNI_NA_POTEZI) {
				g2.setColor(crnaSenca);
				g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
				g2.fillOval((int)x, (int)y, (int)r , (int)r);
			} else {
				senca = null;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int w = (int)(squareWidth());
		int i = x / w ;
		double di = (x % w) / squareWidth() ;
		int j = y / w ;
		double dj = (y % w) / squareWidth() ;
		if (0 <= i && i < Igra.getN() &&
		    0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
		    0 <= j && j < Igra.getN() && 
		    0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
			master.klikniPolje(i, j);
			senca = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		if () {
//			senca = null;
//		}
		int x = e.getX();
		int y = e.getY();
		int w = (int)(squareWidth());
		int i = x / w ;
		double di = (x % w) / squareWidth() ;
		int j = y / w ;
		double dj = (y % w) / squareWidth() ;
		if (0 <= i && i < Igra.getN() &&
		    0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
		    0 <= j && j < Igra.getN() && 
		    0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
			Plosca plosca = master.getPlosca();
			if (plosca.element(i, j) == Polje.PRAZNO) {
				senca = new int[] {(int)(w * (i + 0.5 * LINE_WIDTH + PADDING)), (int)(w * (j + 0.5 * LINE_WIDTH + PADDING))};
			} else {
				senca = null;
			}
		}
		repaint();
	}
	
}