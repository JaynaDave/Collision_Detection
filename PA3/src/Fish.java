

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Fish
{
  private int fish_object;
  private int tail_object;
  private float scale;
  private float angle;
  private float change = .5f; 
  public double x, y, z;
  
  private boolean wallX, wallY, wallZ = false;

  public Fish( float scale_, double coord_x, double coord_y, double coord_z)
  {
    scale = scale_;
    angle = 5;
    x = coord_x;
    y = coord_y;
    z = coord_z;
    
  }
  
  
  public void init( GL2 gl )
  {

    fish_object = gl.glGenLists(1);
   
    gl.glNewList( fish_object, GL2.GL_COMPILE );
	    // create the body
	    GLUT glut = new GLUT();
	    gl.glPushMatrix();
	   
	    gl.glScalef(0.3f, 0.2f, 0.2f);
	   // gl.glTranslated(1, 1, 1);
	    glut.glutSolidSphere(scale, 25,25);
	    gl.glPopMatrix();
	   
    gl.glEndList();
    
    
    tail_object = gl.glGenLists(1);
    gl.glNewList( tail_object, GL2.GL_COMPILE );
	    // create the fin 
    	gl.glPushMatrix();
       gl.glTranslated(0, 0, -.3);
	    glut.glutSolidCone(.17, .3, 25, 25);
	    gl.glPopMatrix();
	  
    gl.glEndList();
  }
  
  
  
  public void update( GL gl ,double[] gradient)
  {
// move fin back and forth 
	  if(angle > 10) {
	    	change = -0.5f;
	    }else if(angle < -10) {
	    	change = 0.5f;
	    }
	    angle += change;
	    
	    
	    double mag = Math.sqrt(gradient[0]*gradient[0] + gradient[1]*gradient[1] + gradient[2]*gradient[2]);
	    
	    
	 //keeping creature inside the tank 
	if (x>2 || x <-2 ) {
    	wallX = !wallX;
    }

    if(y>2 || y < -2  ) {
    	wallY = !wallY;
    }

    if(z> 2  || z < -2  ) {
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
	    
	    
    x += gradient[0]*0;
    y += gradient[1]*0;
    z += gradient[2]*0;
	    
	
	 //System.out.print(x); System.out.print(y); System.out.print(z); System.out.println();
    
    
  }

  public void draw( GL2 gl , float[] rotMatrix)
  {
	  
	//translation to new position based on gradient 
	gl.glTranslated(x, y, z);
   // gl.glMultMatrixf(rotMatrix, 0);
   
	gl.glPushMatrix();
    gl.glPushAttrib( GL2.GL_CURRENT_BIT );

    gl.glRotatef( 0, 0.0f, 1.0f, 0.0f );
    gl.glColor3f( 0.83f, 0.52f, 0.17f); // Orange
    gl.glCallList( fish_object );
    
    gl.glTranslated(0, 0, 0);
    gl.glRotated(90, 0, 1, 0);
    //gl.glTranslated(-1, 0,0);
    gl.glRotated(angle, 0, 1, 0);
    gl.glScaled(.2, 1, 1);
    
    gl.glCallList(tail_object);
    
    gl.glPopAttrib();
    gl.glPopMatrix();
	  
	
	
  }
}
