package handVisualizerFinalized;

import java.awt.*;

import java.awt.Graphics;

public class Joint implements Drawable, Actable {
	private int xCoord, yCoord, width, height;

	private String fingerJoint = "";

	private Main gameObject;

	public Joint(int x, int y, int w, int h, String fingerJoint, Main g) {
		xCoord = x;
		yCoord = y;
		
		width = w;
		height = h;

		this.fingerJoint = fingerJoint;

		gameObject = g;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.fillOval(xCoord, yCoord, width, height);
	}

	public void act() {
		int newX = xCoord;
		int newY = yCoord;

		Drawable thingInMyWay = gameObject.moveCollidesWith(this, newX, newY);

		xCoord = newX;
		yCoord = newY;
	}

	public void setX(int x) {
		xCoord = x;
	}

	public void setY(int y) {
		yCoord = y;
	}

	public void setWidth(int w) {
		width = w;
	}

	public void setHeight(int h) {
		height = h;
	}

	public int getX() {
		return xCoord + width / 2;
	}

	public int getY() {
		return yCoord + height / 2;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getFingerJoint() {
		return fingerJoint;
	}

	public boolean equals(Object obj) {
		
		if (!(obj instanceof Joint))
			return false;

		Joint other = (Joint) obj;

		return (xCoord == (other).getX() && yCoord == (other).getY());
	}
}