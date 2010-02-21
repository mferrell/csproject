/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;
import java.awt.AWTEvent;
import java.awt.event.*;
import java.awt.AWTEvent.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.text.DecimalFormat;

public class controlRelease extends Behavior {
private Sprite character;
private TransformGroup camTG;
private Transform3D trans;
private Transform3D move;
private Point3d charLoc;
private WakeupCondition keyRelease;
private Mover mover;

public controlRelease(Sprite c, TransformGroup tg, Mover m){
    character = c;
    camTG = tg;
    mover = m;
    trans = new Transform3D();
    move = new Transform3D();
    keyRelease = new WakeupOnAWTEvent( KeyEvent.KEY_RELEASED);
}
    public void initialize() {
        wakeupOn(keyRelease);
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
                       if(event[i].getID() == KeyEvent.KEY_RELEASED)
                           processKeyEvent((KeyEvent)event[i]);
                }
            }
        }
        wakeupOn(keyRelease);
    }

    private void processKeyEvent(KeyEvent keyEvent) {
     int key = keyEvent.getKeyCode();
     if(key == KeyEvent.VK_UP) {
        System.out.println("forward press");
        mover.setForward(false);
     }
    if(key == KeyEvent.VK_DOWN) {
        mover.setBackward(false);
    }
    if(key == KeyEvent.VK_RIGHT) {
        mover.setTurnRight(false);
    }
    if(key == KeyEvent.VK_LEFT) {
        mover.setTurnLeft(false);
    }
    }
}


