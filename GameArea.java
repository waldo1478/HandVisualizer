package handVisualizerFinalized;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class GameArea extends JPanel {
	public static final int WIDTH = 1300, HEIGHT = 800;
	private ArrayList<Shape> list2;
	private float stroke = 10;
	private Main gameObject;

	public GameArea(Main m) {
		list2 = new ArrayList<Shape>();
		setBackground(Color.white);
		gameObject = m;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(stroke, // Line width
				BasicStroke.CAP_ROUND,        // End-cap style
				BasicStroke.JOIN_ROUND));     // Vertex join style

		// determines whether lines connecting joints are shown or not
		boolean showLines = true; 
		
		if (showLines) {
			for (int pos = 0; pos < list2.size(); pos++) {
				Shape s = list2.get(pos);
				if (s != null) {
					g2d.fill(s);
					g2d.draw(s);
				}
			}
		} 
		else 
		{
			ArrayList<Drawable> drawableList = gameObject.getDrawableList();
			for (int num = 0; num < drawableList.size(); num++)
				drawableList.get(num).draw(g2d);
		}
	}

	public void addShape(Shape s) {
		list2.add(s);
	}

	public void clearList2() {
		list2.clear();
	}

	public void setStroke(int i) {
		stroke = (float) i;
	}

	public Dimension getSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	public Dimension getMinimumSize() {
		return getSize();
	}

	public Dimension getMaximumSize() {
		return getSize();
	}

	public Dimension getPreferredSize() {
		return getSize();
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
}