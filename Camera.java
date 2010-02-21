/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;

import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class Camera {
private SimpleUniverse su;
private Sprite character;
public Camera(SimpleUniverse uni, Sprite c) {
    su = uni;
    character = c;
}
public void rotateY(double angle) {
   /* ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup steerTG = vp.getViewPlatformTransform();

    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);
    t3d.lookAt(character.getLocation());
    t3d.invert();
    steerTG.setTransform(t3d);
*/}
}