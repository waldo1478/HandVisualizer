package handVisualizerFinalized;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Shape;

import javax.swing.Box;
import javax.swing.JFrame;

public class Main extends JFrame {
	private ArrayList<Drawable> drawableList;
	private ArrayList<Drawable> joints;

	private GameArea gameArea;

	private Thread animationThread;
	private AnimationManager aniManager;

	private static GraphicsDevice graphicsDevice = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public Main() {
		super("Hand Visualizer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 800); // use for not full screen
//		graphicsDevice.setFullScreenWindow(this); // use for full screen

		drawableList = new ArrayList<Drawable>();

		gameArea = new GameArea(this);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(gameArea, BorderLayout.CENTER);

		aniManager = new AnimationManager(this, this);
		animationThread = new Thread(aniManager);
		animationThread.start();

		setVisible(true);
	}

	public ArrayList<Drawable> getDrawableList() {
		return drawableList;
	}

	public void removeFromDrawableList(Drawable b) {
		drawableList.remove(b);
	}

	public void clearDrawableList() {
		drawableList.clear();
	}

	public ArrayList<Drawable> getJoints() {
		joints = new ArrayList<Drawable>();
		for (int i = 0; i < drawableList.size(); i++) {
			if (drawableList.get(i) instanceof Joint)
				joints.add(drawableList.get(i));
		}
		return joints;
	}

	public Joint getJoint(String s) {
		Joint joint = null;
		for (int i = 0; i < joints.size(); i++) {
			if (joints.get(i).getFingerJoint().equals(s))
				joint = (Joint) joints.get(i);
		}
		return joint;
	}

	public void clearJoints() {
		ArrayList<Drawable> joints = new ArrayList<Drawable>();
		for (int i = 0; i < drawableList.size(); i++) {
			if (drawableList.get(i) instanceof Joint)
				drawableList.remove(i);
			i--;
		}
	}

	public void newJoint(int x, int y, int w, int h, String fJ) {
		drawableList.add(new Joint(x, y, w, h, fJ, this));
	}

	public GameArea getGameArea() {
		return gameArea;
	}

	public static void main(String[] args) {
		Main iJustLostTheGame = new Main();
	}

	// collision detection
	public Drawable moveCollidesWith(Drawable mover, int newX, int newY) {

		Drawable collidedWith = null; // if we find an intersection, we'll
										// change this

		Rectangle moverRect = new Rectangle(newX, newY, mover.getWidth(),
				mover.getHeight());
		// int curr = 0; curr <= drawableList.size(); curr++
		for (int pos = 0; pos < drawableList.size(); pos++) {
			Drawable curr = drawableList.get(pos);
			// System.out.println(curr + " is curr and " + mover + " is mover");
			if (curr.equals(mover) == false) // only check for collisions with
												// other Drawables
			{
				Rectangle currRect = new Rectangle(curr.getX(), curr.getY(),
						curr.getWidth(), curr.getHeight());

				if (moverRect.intersects(currRect)) {
					collidedWith = curr; // we collided, so set collidedWith and
											// stop looking
					break;
				}
			}
		}
		return collidedWith;
	}
}