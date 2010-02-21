/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;
import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Sprite {
private TransformGroup spriteTG;
private Transform3D rotate, move, trans;
private Switch transparent;
private double radius;
private boolean active;

public Sprite(String file)
{
        Loader load = new Loader(file, false);
        radius = load.getScale();
        transparent = new Switch();
        transparent.setCapability(Switch.ALLOW_SWITCH_WRITE);
        transparent.addChild(load.getTG());
        transparent.setWhichChild(Switch.CHILD_ALL);

        spriteTG = new TransformGroup();
        spriteTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        spriteTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        spriteTG.addChild(transparent);

        trans = new Transform3D();
        move = new Transform3D();
        rotate = new Transform3D();
        active = true;
}
public void moveDef(double x, double y, double z)
{
        Point3d here = getLocation();
        double x1 = x - here.x;
        double y1 = x - here.y;
        double z1 = x - here.z;
        moveRel(x1, y1, z1);
}
public void rotate(double angle, int axis) {
        spriteTG.getTransform(trans);
        if(axis == 0)
               rotate.rotX(angle);
        if(axis == 1)
                rotate.rotY(angle);
        if(axis == 2)
                rotate.rotZ(angle);
        trans.mul(rotate);
        spriteTG.setTransform(trans);
}

public Point3d moveTest(Vector3d vec) {
        spriteTG.getTransform(trans);
        move.setTranslation(vec);
        trans.mul(move);
        Vector3d vector = new Vector3d();
        trans.get(vector);
        return new Point3d(vector.x, vector.y, vector.z);
}

public void moveRel(double x, double y, double z)
{
     Vector3d to = new Vector3d(x, y, z);
     spriteTG.getTransform(trans);
     move.setTranslation(to);
     trans.mul(move);
     spriteTG.setTransform(trans);
}
public boolean getActive() { return active; }

public Point3d getLocation()
{
    spriteTG.getTransform(trans);
    Vector3d vec = new Vector3d();
    trans.get(vec);
    return new Point3d(vec.x, vec.y, vec.z);
}
public double getX() { spriteTG.getTransform(trans);
                     Vector3d vec = new Vector3d();
                     trans.get(vec);
                     return vec.x;
}
public double getY() { spriteTG.getTransform(trans);
                     Vector3d vec = new Vector3d();
                     trans.get(vec);
                     return vec.y;
}
public double getZ() { spriteTG.getTransform(trans);
                     Vector3d vec = new Vector3d();
                     trans.get(vec);
                     return vec.z;
}

public void jump(double dist) {
       moveRel(0.0, dist, 0.0);
}
public void moveForward() {
       moveRel(0.0, 0.0, 0.1);
}
public void moveBackward() {
        moveRel(0.0, 0.0, -0.1);
}
public void leanDown() {
        rotate((Math.PI / 18) , 0);
}
public void leanUp() {
        rotate((Math.PI / 18), 0);
}
public void turnLeft() {
        rotate((Math.PI / 18), 1);
}
public void turnRight() {
        rotate((Math.PI / -18), 1);
}
public TransformGroup getTG() {   
    return spriteTG; 
}
}

//public double getX() { spriteTG.getTransform(trans);
 //                    Vector3d vec = new Vector3d();
   //                  trans.get(vec);
 ///                    return vec.x;
//}