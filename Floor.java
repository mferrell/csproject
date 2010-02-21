package csproject;

import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.vecmath.*;
import java.util.ArrayList;

public class Floor
{
  private final static int FLOOR_LEN = 20;  

  private final static Color3f blue = new Color3f(0.0f, 0.1f, 0.4f);
  private final static Color3f green = new Color3f(0.0f, 0.5f, 0.1f);
  private final static Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
  private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  private BranchGroup floorBG;


  public Floor()
  {
    ArrayList blueCoords = new ArrayList();
    ArrayList greenCoords = new ArrayList();
    floorBG = new BranchGroup();

    boolean isBlue;
    for(int z=-FLOOR_LEN/2; z <= (FLOOR_LEN/2)-1; z++) {
      isBlue = (z%2 == 0)? true : false; 
      for(int x=-FLOOR_LEN/2; x <= (FLOOR_LEN/2)-1; x++) {
        if (isBlue)
          createCoords(x, z, blueCoords);
        else
          createCoords(x, z, greenCoords);
        isBlue = !isBlue;
      }
    }
    floorBG.addChild( new Tiled(blueCoords, blue) );
    floorBG.addChild( new Tiled(greenCoords, green) );

    addOriginMarker();
  }  


  private void createCoords(int x, int z, ArrayList coords)
  {
    Point3f p1 = new Point3f(x, 0.0f, z+1.0f);
    Point3f p2 = new Point3f(x+1.0f, 0.0f, z+1.0f);
    Point3f p3 = new Point3f(x+1.0f, 0.0f, z);
    Point3f p4 = new Point3f(x, 0.0f, z);
    coords.add(p1); coords.add(p2);
    coords.add(p3); coords.add(p4);
  } 


  private void addOriginMarker()

  { 
    Point3f p1 = new Point3f(-0.25f, 0.01f, 0.25f);
    Point3f p2 = new Point3f(0.25f, 0.01f, 0.25f);
    Point3f p3 = new Point3f(0.25f, 0.01f, -0.25f);
    Point3f p4 = new Point3f(-0.25f, 0.01f, -0.25f);

    ArrayList oCoords = new ArrayList();
    oCoords.add(p1); oCoords.add(p2);
    oCoords.add(p3); oCoords.add(p4);

    floorBG.addChild( new Tiled(oCoords, medRed) );
  } 

  private TransformGroup makeText(Vector3d vertex, String text)
 
  {
    Text2D message = new Text2D(text, white, "SansSerif", 36, Font.BOLD );
    TransformGroup tg = new TransformGroup();
    Transform3D t3d = new Transform3D();
    t3d.setTranslation(vertex);
    tg.setTransform(t3d);
    tg.addChild(message);
    return tg;
  } 

  public BranchGroup getBG()
  {  return floorBG;  }

} 

