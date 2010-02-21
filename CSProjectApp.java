 /*
 * DTestApp.java
 */

package csproject;


// Checkers3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A simple basic world consisting of a checkboard floor,
   with a red center square, and labelled XZ axes.

   A floating, shiny blue sphere is placed at the center.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class CSProjectApp extends JFrame
{
  public CSProjectApp()
  {

    super("CSProjectApp");

    Container c = getContentPane();
    c.setLayout( new BorderLayout() );
    GamePanel game = new GamePanel();     // panel holding the 3D canvas
    c.add(game, BorderLayout.CENTER);

    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    pack();
    setResizable(false);    // fixed size display
    setVisible(true);
  } // end of Checkers3D()


// -----------------------------------------

  public static void main(String[] args)
  { 
      new CSProjectApp(); }

} // end of Checkers3D class
