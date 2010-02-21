/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csproject;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.loaders.*;
import ncsa.j3d.*;
import ncsa.j3d.loaders.*;


public class Loader
{
  private static final int X_AXIS = 0;
  private static final int Y_AXIS = 1;
  private static final int Z_AXIS = 2;
  private static final int INCR = 0;
  private static final int DECR = 1;

  private static final double MOVE_INCR = 0.1;   
  private static final double ROT_INCR = 10;     
  private static final double ROT_AMT = Math.toRadians(ROT_INCR);    


  private TransformGroup moveTG, rotTG, scaleTG;
  private Transform3D t3d;          
  private Transform3D chgT3d;      

  private String filename;          
  private double xRot, yRot, zRot;   
  private ArrayList rotInfo;         
  private double scale;             

  private DecimalFormat df;    


  public Loader(String loadFnm, boolean hasCoordsInfo)
  {
    filename = loadFnm;
    xRot = 0.0; yRot = 0.0; zRot = 0.0;   
    rotInfo = new ArrayList();
    scale = 1.0;

    t3d = new Transform3D();     
    chgT3d = new Transform3D();

    df = new DecimalFormat("0.###");  
    loadFile(loadFnm);
    if (hasCoordsInfo)    
      getFileCoords(loadFnm);
  }  



  private void loadFile(String file)
 
  { System.out.println("Loading object file: models/" + file);

	Scene s = null;
    ModelLoader modelLoader = new ModelLoader();
	try {
	  s = modelLoader.load(file);  
	}
	catch (Exception e) {
	  System.err.println(e);
	  System.exit(1);
	}

    BranchGroup sceneGroup = s.getSceneGroup();

    TransformGroup objBoundsTG = new TransformGroup();
    objBoundsTG.addChild( sceneGroup );

    String ext = getExtension(file);
    BoundingSphere objBounds = (BoundingSphere) sceneGroup.getBounds();
    setBSPosn(objBoundsTG, objBounds.getRadius(), ext);

    scaleTG = new TransformGroup();
    scaleTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    scaleTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    scaleTG.addChild( objBoundsTG );

    rotTG = new TransformGroup();
    rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    rotTG.addChild( scaleTG );

    moveTG = new TransformGroup();
    moveTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    moveTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    moveTG.addChild( rotTG );

  } 


  private String getExtension(String fnm)

  {
    int dotposn = fnm.lastIndexOf(".");
    if (dotposn == -1)  
      return "(none)";
    else
      return fnm.substring(dotposn+1).toLowerCase();
  }


  private void setBSPosn(TransformGroup objBoundsTG,
					double radius, String ext)
  {
    Transform3D objectTrans = new Transform3D();
    objBoundsTG.getTransform( objectTrans );


    Transform3D scaleTrans = new Transform3D();
    double scaleFactor = 1.0/radius;
    scaleTrans.setScale( scaleFactor );

    objectTrans.mul(scaleTrans);

    if (ext.equals("3ds")) {   
      Transform3D rotTrans = new Transform3D();
      rotTrans.rotX( -Math.PI/2.0 );   
      objectTrans.mul(rotTrans);
    }

    objBoundsTG.setTransform(objectTrans);
  } 


  public TransformGroup getTG()
  {  return moveTG; }


  private void getFileCoords(String fnm)
  
  {
    String coordFile = getName(fnm) + "Coords.txt";
    try {
      BufferedReader br = new BufferedReader( new FileReader(coordFile));
      br.readLine();    
      String line;
      char ch;
      while((line = br.readLine()) != null) {
        ch = line.charAt(1);
        if (ch == 'p')
          setCurrentPosn(line);
        else if (ch == 'r')
          setCurrentRotation(line);
        else if (ch == 's')
          setCurrentScale(line);
        else
          System.out.println(coordFile + ": did not recognise line: " + line);
      }
      br.close();
      System.out.println("Read in coords file: " + coordFile);
    }
    catch (IOException e)
    { System.out.println("Error reading coords file: " + coordFile);
      System.exit(1);
    }
  }  



  private String getName(String fnm)
  
  {
    int dotposn = fnm.lastIndexOf(".");
    if (dotposn == -1)
      return fnm;
    else
      return fnm.substring(0, dotposn);
  }


  private void setCurrentPosn(String line)
 
  {
    double vals[] = new double[3];           
    vals[0] = 0; vals[1] = 0; vals[2] = 0;   

    StringTokenizer tokens = new StringTokenizer(line);
    String token = tokens.nextToken();  
    int count = 0;
    while (tokens.hasMoreTokens()) {
      token = tokens.nextToken();
      try {
        vals[count] = Double.parseDouble(token);
        count++;
      }
      catch (NumberFormatException ex){
        System.out.println("Incorrect format for position data in coords file");
        break;
      }
    }
    if (count != 3)
      System.out.println("Insufficient position data in coords file");

    doMove( new Vector3d( vals[0], vals[1], vals[2]) );

  } 


  private void setCurrentRotation(String line)
  
  {
    int rotNum;
    StringTokenizer tokens = new StringTokenizer(line);
    String token = tokens.nextToken();   
    if (!tokens.hasMoreTokens())  
      return;
    token = tokens.nextToken();
    for (int i=0; i < token.length(); i++) {
      try {
        rotNum = Character.digit(token.charAt(i),10);
      }
      catch (NumberFormatException ex){
        System.out.println("Incorrect format for rotation data in coords file");
        break;
      }
      if (rotNum == 1)         
        rotate(X_AXIS, INCR);
      else if (rotNum == 2)    
        rotate(X_AXIS, DECR);
      else if (rotNum == 3)    
        rotate(Y_AXIS, INCR);
      else if (rotNum == 4)   
        rotate(Y_AXIS, DECR);
      else if (rotNum == 5)    
        rotate(Z_AXIS, INCR);
      else if (rotNum == 6)    
        rotate(Z_AXIS, DECR);
      else
        System.out.println("Did not recognise the rotation info in the coords file");
    }
  } 




  private void setCurrentScale(String line)
  

  {
    StringTokenizer tokens = new StringTokenizer(line);
    String token = tokens.nextToken();    
    double startScale;

    token = tokens.nextToken();   
    try {
      startScale = Double.parseDouble(token);
    }
    catch (NumberFormatException ex){
      System.out.println("Incorrect format for scale data in coords file");
      startScale = 1.0;
    }
    if (startScale != 1.0) {
      scale(startScale);
    }
  } 

  public void move(int axis, int change)
  {
    double moveStep = (change == INCR) ? MOVE_INCR : -MOVE_INCR ;
    Vector3d moveVec;
    if (axis == X_AXIS)
      moveVec = new Vector3d(moveStep,0,0);
    else if (axis == Y_AXIS)
      moveVec = new Vector3d(0,moveStep,0);
    else   
      moveVec = new Vector3d(0,0,moveStep);
    doMove( moveVec );
  }  


  private void doMove(Vector3d theMove)
  {
    moveTG.getTransform(t3d);        
    chgT3d.setIdentity();            
    chgT3d.setTranslation(theMove);  
    t3d.mul(chgT3d);                 
    moveTG.setTransform(t3d);      
  }



  public void rotate(int axis, int change)
  {
    doRotate(axis, change);
    storeRotate(axis, change);
  } 


  private void doRotate(int axis, int change)
  {
    double radians = (change == INCR) ? ROT_AMT : -ROT_AMT;
    rotTG.getTransform(t3d);    
    chgT3d.setIdentity();        
    switch (axis) {             
      case X_AXIS: chgT3d.rotX(radians); break;
      case Y_AXIS: chgT3d.rotY(radians); break;
      case Z_AXIS: chgT3d.rotZ(radians); break;
      default: System.out.println("Unknown axis of rotation"); break;
    }
    t3d.mul(chgT3d);     
    rotTG.setTransform(t3d);     
  }  

  private void storeRotate(int axis, int change)
  {
    double degrees = (change == INCR) ? ROT_INCR : -ROT_INCR;
    switch (axis) {
      case X_AXIS: storeRotateX(degrees); break;
      case Y_AXIS: storeRotateY(degrees); break;
      case Z_AXIS: storeRotateZ(degrees); break;
      default: System.out.println("Unknown storage axis of rotation"); break;
    }
  }  


  private void storeRotateX(double degrees)

  {
    xRot = (xRot+degrees)%360;   
    if (degrees == ROT_INCR)
      rotInfo.add(new Integer(1)); 
    else if (degrees == -ROT_INCR)
       rotInfo.add(new Integer(2));
    else
      System.out.println("No X-axis rotation number for " + degrees);
  }


  private void storeRotateY(double degrees)
  {
    yRot = (yRot+degrees)%360;   
    if (degrees == ROT_INCR)
      rotInfo.add(new Integer(3));   
    else if (degrees == -ROT_INCR)
       rotInfo.add(new Integer(4));
    else
      System.out.println("No Y-axis rotation number for " + degrees);
  } // end of storeRotateY()


  private void storeRotateZ(double degrees)
  // record the z-axis rotation
  {
    zRot = (zRot+degrees)%360;   // update z-axis total rotation
    if (degrees == ROT_INCR)
      rotInfo.add(new Integer(5));  // rotation number
    else if (degrees == -ROT_INCR)
       rotInfo.add(new Integer(6));
    else
      System.out.println("No Z-axis rotation number for " + degrees);
  } // end of storeRotateZ()


  public void scale(double d)
  // Scale the object by d units
  {
    scaleTG.getTransform(t3d);    // get current scale from TG
    chgT3d.setIdentity();         // reset change Trans
    chgT3d.setScale(d);           // set up new scale
    t3d.mul(chgT3d);              // multiply new scale to current one
	scaleTG.setTransform(t3d);    // update the TG

    scale *= d;    // update scale variable
  }  // end of scale()



  // ----------------------------------------------------------
  // return current position/rotation/scale information
  // Used by the GUI interface


  public Vector3d getLoc()
  {
    moveTG.getTransform(t3d);
    Vector3d trans = new Vector3d();
    t3d.get(trans);
    // printTuple(trans, "getLoc");
    return trans;
  } // end of getLoc()


  public Point3d getRotations()
  {  return new Point3d(xRot, yRot, zRot);  }

  public double getScale()
  {  return scale;  }


  // ------------------------------ storing ---------------------------


  public void saveCoordFile()
  // create a coords file for this object
  {
    String coordFnm = "models/" + getName(filename) + "Coords.txt";
    try {
      PrintWriter out = new PrintWriter( new FileWriter(coordFnm));

      out.println(filename);     // object filename
      Vector3d currLoc = getLoc();
      out.println("-p " + df.format(currLoc.x) + " " + df.format(currLoc.y) +
						" " + df.format(currLoc.z) );
      out.print("-r ");
      for (int i=0; i < rotInfo.size(); i++)
         out.print( ""+((Integer) rotInfo.get(i)).intValue() );
      out.println("");

      out.println("-s " + df.format(scale) );

      out.close();
      System.out.println("Saved to coord file: " + coordFnm);
    }
    catch(IOException e)
    { System.out.println("Error writing to coord file: " + coordFnm); }
  }  // end of saveCoordFile()



  // --------------------- methods used for debugging --------------------------


  private void printTG(TransformGroup tg, String id)
  // print the translation stored in tg
  {
    Transform3D currt3d = new Transform3D( );
    tg.getTransform( currt3d );
    Vector3d currTrans = new Vector3d( );
    currt3d.get( currTrans );
    printTuple( currTrans, id);
  }  // end of printTG()


  private void printTuple(Tuple3d t, String id)
  // used to print Vector3d, Point3d objects
  {
    System.out.println(id + " x: " + df.format(t.x) +
				", " + id + " y: " + df.format(t.y) +
				", " + id + " z: " + df.format(t.z));
  }  // end of printTuple()


}  // end of PropManager class
