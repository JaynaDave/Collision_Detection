

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Dolphin
{
  private int dolphin_object;
  private int tail_object;
  private float scale;
  private float angle;
  private float change = .5f; 
  public double x, y, z;
  
  private boolean wallX, wallY, wallZ = false;
  

  public Dolphin( float scale_, double coord_x, double coord_y, double coord_z)
  {
    scale = scale_;
    angle = 180;
    x = coord_x;
    y = coord_y;
    z = coord_z;
  }

  public void init( GL2 gl )
  {
	  
    dolphin_object = gl.glGenLists(1);
    //cat_object = gl.glGenLists(1);
	
    gl.glNewList( dolphin_object, GL2.GL_COMPILE );
	    // create the dolphin body
	    GLUT glut = new GLUT();

	    gl.glPushMatrix();
	    gl.glScalef(0.9f, 0.3f, 0.3f);
	    glut.glutSolidSphere(scale, 25,25);
	    gl.glScalef(0.6f, 0.4f, 0.6f);
	    gl.glTranslated(.9, -.5, 0);
	    glut.glutSolidSphere(scale, 25,25);
	    gl.glTranslated(-1.75, 0, 0);
	    gl.glRotated(-90, 0, 1, 0);
	    gl.glRotated(20, 1, 0, 0);
	    glut.glutSolidCone(1, 1, 25, 25);
	    gl.glRotated(-100, 1, 0, 0);
	    gl.glTranslated(0, 1.5, .8);
	    gl.glScalef(.5f, .5f, 1.1f);
	    glut.glutSolidCone(1, 2, 25, 25);
	    gl.glRotated(90, 0, 1, 0);
	    gl.glRotated(15, 1, 0, 0);
	    gl.glScalef(1.1f, .5f, 2f);
	    gl.glTranslated(.70, -.5, .5);
	    glut.glutSolidCone(1, 2, 25, 25);
	   
	    gl.glRotated(-180, 0, 1, 0);
	    gl.glRotated(40, 1, 0, 0);
	    //gl.glRotated(180, 0, 1, 1);
	    //gl.glRotated(180, 1, 0, 0);
	    //gl.glRotated(180, 0, 0, 1);
	    gl.glTranslated(-.1, -.4, .7);
	    glut.glutSolidCone(.9, 4, 25, 25);
	    gl.glPopMatrix();
	   
	    
    gl.glEndList();
    
    
    tail_object = gl.glGenLists(1);
    gl.glNewList( tail_object, GL2.GL_COMPILE );
	    // create the teapot triangles 
	    //gl.glScalef(4.5f, 1, 2);
	    //gl.glRotated(-20, 0, 1, 1);
    	gl.glPushMatrix();
    	//gl.glRotated(90, 1, 1, 1);
	    glut.glutSolidCone(.3, .75, 25, 25);
	    gl.glPopMatrix();
	   
    gl.glEndList();
  }

  public void update( GL g, double[] gradient )
  {
		 
	 //fin going back and forth 
    if(angle > 190) {
    	change = -0.5f;
    }else if(angle < 182) {
    	change = 0.5f;
    }
    angle += change;
    
  //move the other way if creature hits a wall
    
    if (x>2 || x < -2) {
    	wallX = !wallX;
    }

    if(y>2 || y < -2) {
    	wallY = !wallY;
    }

    if(z>2 || z < -2) {
    	wallZ = !wallZ;
    }

  
  	if(wallX) {
  		gradient[0] = -gradient[0];
    }
    
    if(wallY) {
    	gradient[1] = -gradient[1];
    }

    if(wallZ) {
    	gradient[2] = -gradient[2];
    }
   	
    x += gradient[0]*.05;
    y += gradient[1]*.05;
    z += gradient[2]*.05;
	
	 //System.out.print(x); System.out.print(y); System.out.print(z); System.out.println();
  }

  public void draw( GL2 gl, float[] rotMatrix)
  {
	//translation based on the gradient 
	gl.glTranslated(x, y, z);
	//gl.glMultMatrixf(rotMatrix, 0);
	gl.glPushMatrix();

    gl.glPushAttrib( GL2.GL_CURRENT_BIT );
 
 
    
    gl.glScaled(.5f, .5f, .5f);
    gl.glRotatef( 0, 0.0f, 1.0f, 0.0f );
    gl.glColor3f( 0.48f, 0.65f, 0.80f); // light blue
    gl.glCallList( dolphin_object );
  
    gl.glScaled(.8f, .15f, 1);
    gl.glRotated(270, 0, 1, 0);
    gl.glRotated(angle, 1, 0, 0);
    gl.glTranslated(0, .5, -1.8);
    gl.glCallList(tail_object);
    

    
    gl.glPopAttrib();
    gl.glPopMatrix();
	  
	
	
  }
}
