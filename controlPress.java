/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import java.awt.AWTEvent;
import java.awt.event.*;
import java.awt.AWTEvent.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.text.DecimalFormat;

public class controlPress extends Behavior {
private Sprite character;
private TransformGroup camTG;
private Transform3D trans;
private Transform3D move;
private Transform3D rotate;
private Point3d charLoc;
private WakeupCondition keyPress, keyRelease;
private Mover mover;
private Point3d camSpot;
private Camera cam;

public controlPress(Sprite c, TransformGroup tg, Mover m, Camera orb){
    cam = orb;
    character = c;
    charLoc = character.getLocation();
    camTG = tg;
    mover = m;
    trans = new Transform3D();
    move = new Transform3D();
    rotate = new Transform3D();
    keyPress = new WakeupOnAWTEvent( KeyEvent.KEY_PRESSED);
}
    public void initialize() {
        wakeupOn(keyPress);
    }

    @Override
    public void processStimulus(Enumeration enmrtn) {
        WakeupCriterion wake;
        AWTEvent[] event;
        while(enmrtn.hasMoreElements()) {
            wake = (WakeupCriterion) enmrtn.nextElement();
            if(wake instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent)wake).getAWTEvent();
                for(int i = 0; i < event.length; i++) {
                       if(event[i].getID() == KeyEvent.KEY_PRESSED)
                           processKeyEvent((KeyEvent)event[i]);
                }
            }
        }
        wakeupOn(keyPress);
    }
    private void moveCam() {
        Point3d loc = character.getLocation();
        Vector3d vec = new Vector3d(loc.x - charLoc.x, 0, loc.z - charLoc.z);
        camTG.getTransform(trans);
        move.setTranslation(vec);
        trans.mul(move);
        camTG.setTransform(trans);
        charLoc = loc;
    }
    private void rotateCam(double angle) {
        camTG.getTransform(trans);
        rotate.rotY(angle);
        trans.mul(rotate);
        camTG.setTransform(trans);

    }
    private void zoomCam(double z) {
        Vector3d vec = new Vector3d(0,0,z);
        camTG.getTransform(trans);
        move.setTranslation(vec);
        trans.mul(move);
        camTG.setTransform(trans);
    }
    private void moveCam(double x, double y, double z) {
        Vector3d vec = new Vector3d(x, y, z);
        camTG.getTransform(trans);
        move.setTranslation(vec);
        trans.mul(move);
        camTG.setTransform(trans);
    }
    private void processKeyEvent(KeyEvent keyEvent) {
     int key = keyEvent.getKeyCode();
     double temp = 0.1;
     cam.rotateY(temp);
     if(key == KeyEvent.VK_UP) {
        mover.setForward(true);
     }
    if(key == KeyEvent.VK_DOWN) {
        mover.setBackward(true);
    }
    if(key == KeyEvent.VK_RIGHT) {
        mover.setTurnRight(true);
    }
    if(key == KeyEvent.VK_LEFT) {
        mover.setTurnLeft(true);
    }
    if(key == KeyEvent.VK_SPACE) {
        mover.setJump(true);
    }
    if(key == KeyEvent.VK_L) {
        character.leanDown();
    }
    if(key == KeyEvent.VK_K)
        character.leanUp();

    if(key == KeyEvent.VK_I) {
        zoomCam(-5);
    }
    if(key == KeyEvent.VK_O) {
        zoomCam(5.0);
    }
    }
}


