package handVisualizerFinalized;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Gesture.Type;

public class AnimationManager implements Runnable {
	private Main gameObject; // store a reference to the data holder

	private long timeAtEndOfLoop = 0, timeAtBeginningOfLoop = 0, pause = 0;
	private final long DESIRED_DELAY = 40;

	private Controller leapMotionController;
	private Frame currFrame;

	private double multiplier = 0;

	public AnimationManager(Main p, JFrame mw) {
		gameObject = p;
		leapMotionController = new Controller();
		// you must enable gestures
		leapMotionController.enableGesture(Type.TYPE_SCREEN_TAP);
		leapMotionController.enableGesture(Type.TYPE_SWIPE);
	}

	public void run() {

		while (true) {
			ArrayList<Drawable> list = gameObject.getDrawableList();

			for (int pos = 0; pos < list.size(); pos++) { // call act method on all drawables
				Drawable d = list.get(pos);
				if (d instanceof Actable) {
					((Actable) d).act();
				}
				gameObject.repaint();
			}

			timeAtBeginningOfLoop = System.currentTimeMillis();

			currFrame = leapMotionController.frame(); // accesses the leap
			HandList handsInFrame = currFrame.hands(); // gets hands
			Hand hand = handsInFrame.get(0); //gets right hand
			
			
			// clears all stored joints before new joints are created
			gameObject.clearJoints(); 
			
			/*
			 * creates all new 'joints'
			 * the joints are numbered as such:
			 * fingers are numbered from 0 (thumb) to pinky (4)
			 * joints on each finger are numbered from 0 (base) to 3 (tip)
			 * from this, a "fingerJoint" string is formed with a 
			 * combination of the finger number followed by the joint number
			 *                     Visual Example
			 *                    ----------------
			 *          (index)   (middle)   (ring)   (pinky)
			 *  (thumb)                       
			 *            13         23        33        43
			 *             |          |         |        |
			 *    03       |          |         |        |
			 *     |      12         22        32        42
			 *     |       |          |         |        |
			 *    02        \         |         |       /
			 *     |        11       21        31      41
			 *     |         |        |         |      |
			 *    01          \       |        /      /
			 *     \          10     20     30      40
			 *      \     ----------------------------
			 *       00 /         	     		      \
			 *         /                               \
			 */
			int fingerNum = 0;
			for (Finger finger : hand.fingers()) {
				int jointNum = 0;
				for (Bone.Type boneType : Bone.Type.values()) {
					Bone bone = finger.bone(boneType);
					Vector currJoint = bone.nextJoint();
					String fingerJoint = fingerNum + "" + jointNum; // creates 'fingerJoint' string
					
					int displayCenterX = gameObject.getWidth()/2; // center of screenX
					int displayCenterY = gameObject.getHeight()/2; // center of screenY
					
				    // creates a multiplier that is used to create the distance affect 
					multiplier = (currJoint.getY() + 200 / currJoint.getY()) / 100;
					gameObject.getGameArea().setStroke((int) (10 * multiplier));
					int newJointX = (int) (displayCenterX + (int)currJoint.getX() * multiplier);
					int newJointY = (int) (displayCenterY + (int)currJoint.getZ() * multiplier);
					int newJointWidth = 0;
					if (((int) currJoint.getY() / 15) < 3)       // minimum width
						newJointWidth = 3;
					else if (((int) currJoint.getY() / 15) > 20) // maximum width
						newJointWidth = 20;
					else                                         // in between width
						newJointWidth = (int) currJoint.getY() / 15; 
					
					// creates new joint
					gameObject.newJoint(newJointX, newJointY, newJointWidth,
							newJointWidth, fingerJoint);
					jointNum++;
				}
				fingerNum++;
			}

			// list of joints that were just created 
			ArrayList<Drawable> joints = gameObject.getJoints();

			// creates lines that connect the joints 
			if (joints.size() == 20) {
				gameObject.getGameArea().clearList2();
				// lines connecting each finger
				for (int fing = 0; fing < 5; fing++) {      //fing = finger
					for (int join = 0; join < 3; join++) {  //join = joint
						String s = fing + "" + join;
						String s2 = fing + "" + (join + 1);
						Joint jointMeh = gameObject.getJoint(s);
						Joint jointMeh2 = gameObject.getJoint(s2);
						int joint3X = jointMeh.getX();
						int joint3Y = jointMeh.getY();
						int joint4X = jointMeh2.getX();
						int joint4Y = jointMeh2.getY();

						// draws a line connecting each joint on each finger
						Shape a = new Line2D.Double(joint3X, joint3Y, joint4X,
								joint4Y);
						gameObject.getGameArea().addShape(a);
					}
				}
				// lines connecting the base of each finger
				for (int bases = 1; bases < 4; bases++) {
					String s = bases + "" + 0;
					String s2 = (bases + 1) + "" + 0;
					Joint jointMeh = gameObject.getJoint(s);
					Joint jointMeh2 = gameObject.getJoint(s2);

					int joint3X = jointMeh.getX();
					int joint3Y = jointMeh.getY();
					int joint4X = jointMeh2.getX();
					int joint4Y = jointMeh2.getY();

					Shape a = new Line2D.Double(joint3X, joint3Y, joint4X,
							joint4Y);
					gameObject.getGameArea().addShape(a);
				}
				
				// line connects the bottom of thumb to the bottom of the pinky
				String s = 4 + "" + 0;
				String s2 = 0 + "" + 0;
				Joint jointMeh = gameObject.getJoint(s);
				Joint jointMeh2 = gameObject.getJoint(s2);

				int joint3X = jointMeh.getX();
				int joint3Y = jointMeh.getY();
				int joint4X = jointMeh2.getX();
				int joint4Y = jointMeh2.getY();

				Shape a = new Line2D.Double(joint3X, joint3Y, joint4X, joint4Y);
				gameObject.getGameArea().addShape(a);

				// line connects the second joint in thumb to the bottom of the index
				String as = 0 + "" + 1;
				String as2 = 1 + "" + 0;
				Joint ajointMeh = gameObject.getJoint(as);
				Joint ajointMeh2 = gameObject.getJoint(as2);

				int ajoint3X = ajointMeh.getX();
				int ajoint3Y = ajointMeh.getY();
				int ajoint4X = ajointMeh2.getX();
				int ajoint4Y = ajointMeh2.getY();

				Shape aa = new Line2D.Double(ajoint3X, ajoint3Y, ajoint4X,
						ajoint4Y);
				gameObject.getGameArea().addShape(aa);
			}

			// code for detecting gestures
			GestureList gestures = currFrame.gestures();
			Iterator<Gesture> gestIter = gestures.iterator();

			while (gestIter.hasNext()) {
				Gesture currGesture = gestIter.next();
				if (currGesture.type() == Type.TYPE_SWIPE) {
					// complete action
					// this is here so you see how gestures work, try it!
					System.out.println("Swipe"); 
					break;
				}
			}

			timeAtEndOfLoop = System.currentTimeMillis();
			pause = DESIRED_DELAY - (timeAtEndOfLoop - timeAtBeginningOfLoop);
			if (pause < 0)
				pause = 1;
			try {
				Thread.sleep(pause);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public double getMultiplier() {
		return multiplier;
	}
}
