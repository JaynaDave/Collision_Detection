

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Food
{
  private int food_object;
  private float scale;
  public float x, y, z;
  

  public Food( float scale_, int coord_x, int coord_y, int coord_z)
  {
    scale = scale_;
    x = coord_x;
    y = coord_y;
    z = coord_z;
    
  }

  public void init( GL2 gl )
  {

    food_object = gl.glGenLists(1);
   
    gl.glNewList( food_object, GL2.GL_COMPILE );
	    // create the body
	    GLUT glut = new GLUT();
	    gl.glPushMatrix();
	   
	   // gl.glTranslated(1, 1, 1);
	    glut.glutSolidSphere(scale, 25,25);
	    gl.glPopMatrix();
	   
    gl.glEndList();
    

  }

  public void update( GL gl )
  {
    
    
  }

  public void draw( GL2 gl )
  {
	  
	gl.glTranslated(x, y, z);
	gl.glPushMatrix();
    gl.glPushAttrib( GL2.GL_CURRENT_BIT );
    //gl.glRotatef( 0, 0.0f, 1.0f, 0.0f );
    gl.glColor3f( 0.83f, 0.52f, 0.17f); // Orange
    gl.glCallList( food_object );
    
    
    
  
    
    gl.glPopAttrib();
    gl.glPopMatrix();
	  
	
	
  }
}
