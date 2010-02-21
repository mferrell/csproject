/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;

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
public class MainLoop extends Behavior {
private Sprite character;
private TransformGroup camTG;
private Transform3D trans;
private Transform3D move;
private Point3d charLoc;
private WakeupCondition wakeUp;

public MainLoop(Sprite c, TransformGroup tg){
    character = c;
    camTG = tg;
    trans = new Transform3D();
    move = new Transform3D();
    wakeUp = new WakeupOnElapsedTime(1);
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
                System.out.println("1 second");
            }
        }
        wakeupOn(wakeUp);
    }

}
