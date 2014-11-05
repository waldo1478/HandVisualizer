package handVisualizerFinalized;

import java.awt.Graphics;

public interface Drawable {
	public void draw(Graphics g);

	public void setX(int x);

	public void setY(int y);

	public void setWidth(int w);

	public void setHeight(int h);

	public int getX();

	public int getY();

	public int getWidth();

	public int getHeight();

	public String getFingerJoint();
}
