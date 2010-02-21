/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Enumeration;
import javax.media.j3d.Behavior;import java.awt.AWTEvent;
import java.awt.event.*;
import java.awt.AWTEvent.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 *
 * @author mferrell
 */
public class Mover extends Behavior {
private Sprite character;
private TransformGroup camTG;
private Transform3D trans;
private Transform3D move;
private Point3d charLoc;
private WakeupCondition wakeUp;
private Boolean backward, forward, turnLeft, turnRight, jumpOn;
private double jumpHeight;
private int jumpStep;
public Mover(Sprite c, TransformGroup tg){
    character = c;
    camTG = tg;
    trans = new Transform3D();
    move = new Transform3D();
    wakeUp = new WakeupOnElapsedTime(1);
    forward = false; backward = false; turnLeft = false; turnRight = false; jumpOn = false;
    jumpHeight = 0;
    jumpStep = 0;
}
public void initialize() {
        wakeupOn(wakeUp);
}

public void processStimulus(Enumeration enmrtn) {
        WakeupCriterion wake;
        AWTEvent[] event;
        while(enmrtn.hasMoreElements()) {
            wake = (WakeupCriterion) enmrtn.nextElement();
            if(wake instanceof WakeupOnElapsedTime) {
                makeMove();
            }
        }
        wakeupOn(wakeUp);
    }
public void setForward(boolean dir) { forward = dir; }
public void setBackward(boolean dir) { backward = dir; }
public void setTurnRight(boolean turn) { turnRight = turn; }
public void setTurnLeft(boolean turn) { turnLeft = turn; }
public void setJump(boolean jump) { jumpOn = true; }
public void makeMove() {
    if(forward) { character.moveForward(); }
    if(backward){ character.moveBackward(); }
    if(turnRight) { character.turnRight(); }
    if(turnLeft)  { character.turnLeft(); }
    if(jumpOn) { character.jump(5); }
}
}