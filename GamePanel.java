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
public class GamePanel extends JPanel
{
  private static final int PWIDTH = 1024;
  private static final int PHEIGHT = 1024;

  private static final int BOUNDSIZE = 1000;

  private static final Point3d USERPOSN = new Point3d(0,5,20);
  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;  
  private int circX = 0;
  private Sprite sprite;
  private OrbitBehavior orbit;
  public GamePanel()
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    setFocusable(true);
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true);    
    canvas3D.requestFocus();

    su = new SimpleUniverse(canvas3D);
    su.getViewer().getView().setSceneAntialiasingEnable(true);
    createSceneGraph();
    initUserPosition();        
    orbitControls(canvas3D);   
    su.addBranchGraph( sceneBG );
    
    
  } 

  private void createSceneGraph()
  {
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);
    lightScene();         
    addBackground();     
    sceneBG.addChild( new Floor().getBG() );
    addSprite("src/csproject/resources/models/robot.3ds");
    sceneBG.compile();  
  }
  private void lightScene()
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    AmbientLight ambientLightNode = new AmbientLight(white);
   ambientLightNode.setInfluencingBounds(bounds);
    sceneBG.addChild(ambientLightNode);

    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);

    DirectionalLight light1 = new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 =
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneBG.addChild(light2);
  }  
  private void addBackground()
  { Background back = new Background();
    back.setApplicationBounds( bounds );
    back.setColor(0.50f, 0.20f, 0.00f);    
    sceneBG.addChild( back );
  }  
  private void orbitControls(Canvas3D c)
  {
    orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
    orbit.setSchedulingBounds(bounds);
    orbit.setTranslateEnable(false);

    ViewingPlatform vp = su.getViewingPlatform();
    vp.setViewPlatformBehavior(orbit);
  }  
  private void initUserPosition()
  {
    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup steerTG = vp.getViewPlatformTransform();

    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);

    t3d.lookAt( USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
    t3d.invert();

    steerTG.setTransform(t3d);
  }
  private Sprite addSprite(String file) {
       sprite = new Sprite(file);
       sceneBG.addChild(sprite.getTG());
       ViewingPlatform vp = su.getViewingPlatform();
       TransformGroup vTG = vp.getViewPlatformTransform();
       Mover mover = new Mover(sprite, vTG);
       controlPress cp = new controlPress(sprite, vTG, mover, new Camera(su, sprite));
       cp.setSchedulingBounds(bounds);
       sceneBG.addChild(cp);
       controlRelease cr = new controlRelease(sprite, vTG, mover);
       cr.setSchedulingBounds(bounds);
       sceneBG.addChild(cr);
       mover.setSchedulingBounds(bounds);
       sceneBG.addChild(mover);
       sprite.moveRel(0.0, 1.5, 0.0);
       return sprite;

  }

  private void floatingCone(int x, int y, int z)
  {
        Color3f white = new Color3f(0.9f, 0.9f, 0.9f);
        Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Material mat = new Material(white, blue, white, black, 25.0f);

        mat.setLightingEnable(true);
        Appearance matApp = new Appearance();
        matApp.setMaterial(mat);

    Transform3D t3d = new Transform3D();
    t3d.set( new Vector3f(x,y,z));
    TransformGroup tg = new TransformGroup(t3d);
    tg.addChild(new Cone(2.0f, 2.0f, matApp));  

    sceneBG.addChild(tg);

  }
  private void floatingSphere(int x, int y, int z)
  {
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
    Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);

    Material blueMat= new Material(blue, black, blue, specular, 25.0f);
    blueMat.setLightingEnable(true);

    Appearance blueApp = new Appearance();
    blueApp.setMaterial(blueMat);

    Transform3D t3d = new Transform3D();
    t3d.set( new Vector3f(x,y,z));
    TransformGroup tg = new TransformGroup(t3d);
    tg.addChild(new Sphere(2.0f, blueApp)); 

    sceneBG.addChild(tg);
  }  

}
