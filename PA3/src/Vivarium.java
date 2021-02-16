

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium
{
  private Tank tank;
  public Dolphin dolphin;
  public Fish fish;
  //public Fish fish2;
  public boolean collide = false;
  private float[] current_vec = new float[] {1, 0, 0};
  private float[] current_vec2 = new float[] {1, 0, 0};
  
 
  

  public Vivarium()
  {
    tank = new Tank( 4.0f, 4.0f, 4.0f );
    dolphin = new Dolphin( 0.75f, 0, 0 , 0);
    fish = new Fish(0.75f, 1, 1, 0);
   // fish2 = new Fish(0.75f, -1.5, -1.5, 0);
    
    
  }
  

  public void init( GL2 gl )
  {
	 
	  
    tank.init( gl );
    dolphin.init( gl );
    fish.init(gl);
    //fish2.init(gl);
  }


 //calculate gradient for the prey movement 
public double[] getGradient(Fish prey) {
	  
	
	  double norm = Math.pow(prey.x - dolphin.x, 2) +  Math.pow(prey.y - dolphin.y, 2) + Math.pow(prey.z - dolphin.z, 2);
	  
	  double df_dx = 2 * Math.exp(-(norm))*(prey.x - dolphin.x);
	  double df_dy = 2 * Math.exp(-(norm))*(prey.y - dolphin.y);
	  double df_dz = 2 * Math.exp(-(norm))*(prey.z - dolphin.z);
	  
	  //double mag = Math.sqrt(df_dx*df_dx + df_dy*df_dy + df_dz*df_dz);
	  
	  double arr[] = new double[] {.07*df_dx, .07*df_dy ,.07*df_dz};
	  
	  return arr;
  }
  
//calucalte gradient for predator movemnt 
 public double[] getGradientPredator(Fish prey) {
	  
	  double norm = Math.pow(prey.x - dolphin.x, 2) +  Math.pow(prey.y - dolphin.y, 2) + Math.pow(prey.z - dolphin.z, 2);
	  
	  double df_dx = -2 * Math.exp(-(norm))*(dolphin.x - prey.x);
	  double df_dy = -2 * Math.exp(-(norm))*(dolphin.y - prey.y);
	  double df_dz = -2 * Math.exp(-(norm))*(dolphin.z - prey.z);
	  
	  //double mag = Math.sqrt(df_dx*df_dx + df_dy*df_dy + df_dz*df_dz);
	  
	  double arr[] = new double[] {df_dx, df_dy ,df_dz};
	  
	  return arr;
 }
 
 //gradient so creature avoids walls 
 public double[] getWallGradient(Fish prey, double[] wall) {
	  
	  double norm = Math.pow(prey.x - wall[0], 2) +  Math.pow(prey.y - wall[1], 2) + Math.pow(prey.z - wall[2], 2);
	  
	  double df_dx = .1 * Math.exp(-(norm))*(prey.x - wall[0]);
	  double df_dy = .1 * Math.exp(-(norm))*(prey.y - wall[1]);
	  double df_dz = .1 * Math.exp(-(norm))*(prey.z - wall[2]);
	  
	  //double mag = Math.sqrt(df_dx*df_dx + df_dy*df_dy + df_dz*df_dz);
	  
	  double arr[] = new double[] {df_dx, df_dy ,df_dz};
	  
	  return arr;
}
 
 
 //quaternion rotation matrix 
 public float[] rotMatrix(Fish prey){
	 
	
	
	 double[] new_orientation = getGradient(fish);
	 
	 double mag1 = Math.sqrt(current_vec2[0]*current_vec2[0] + current_vec2[1]*current_vec2[1]+ current_vec2[2]*current_vec2[2]);
	 double mag2 = Math.sqrt(new_orientation[0]*new_orientation[0] + new_orientation[1]*new_orientation[1]+ new_orientation[2]*new_orientation[2]);
	 
	 
	 float cos = (float)((new_orientation[0]*current_vec2[0] + new_orientation[1]*current_vec2[1] + new_orientation[2]*current_vec2[2])/(mag1*mag2) );
	 
	 double[] orth_vec = new double[] {(current_vec2[1]*new_orientation[2] - current_vec2[2]*new_orientation[1]), (current_vec2[2]*new_orientation[0] - current_vec2[0]*new_orientation[2]),  (current_vec2[0]*new_orientation[1] - current_vec2[1]*new_orientation[0])};
	 
	 double mag_orth = Math.sqrt(orth_vec[0]*orth_vec[0] + orth_vec[1]*orth_vec[1]+ orth_vec[2]*orth_vec[2]);
	 
	 double theta = Math.acos(cos);
	 
	 float v0 = (float)(Math.sin(theta)*(orth_vec[0]/mag_orth));
	 float v1 = (float)(Math.sin(theta)*(orth_vec[1]/mag_orth));
	 float v2 = (float)(Math.sin(theta)*(orth_vec[2]/mag_orth));
	 
	 Quaternion rot = new Quaternion(cos, v0, v1, v2) {};
	 
	 current_vec2[0] = (float)new_orientation[0];
	 current_vec2[1] = (float)new_orientation[1];
	 current_vec2[2] = (float)new_orientation[2];
	 
	 return rot.to_matrix();
	 
	 
	 
 }

//quaternion rotation matrix 
 public float[] rotMatrixPredator(Dolphin dolphin){
	 
		
		
	 double[] new_orientation = getGradientPredator(fish);
	 
	 double mag1 = Math.sqrt(current_vec[0]*current_vec[0] + current_vec[1]*current_vec[1]+ current_vec[2]*current_vec[2]);
	 double mag2 = Math.sqrt(new_orientation[0]*new_orientation[0] + new_orientation[1]*new_orientation[1]+ new_orientation[2]*new_orientation[2]);
	 
	 
	 float cos = (float)((new_orientation[0]*current_vec[0] + new_orientation[1]*current_vec[1] + new_orientation[2]*current_vec[2])/(mag1*mag2) );
	 
	 double[] orth_vec = new double[] {(current_vec[1]*new_orientation[2] - current_vec[2]*new_orientation[1]), (current_vec[2]*new_orientation[0] - current_vec[0]*new_orientation[2]),  (current_vec[0]*new_orientation[1] - current_vec[1]*new_orientation[0])};
	 
	 double mag_orth = Math.sqrt(orth_vec[0]*orth_vec[0] + orth_vec[1]*orth_vec[1]+ orth_vec[2]*orth_vec[2]);
	 
	 double theta = Math.acos(cos);
	 
	 float v0 = (float)(Math.sin(theta)*(orth_vec[0]/mag_orth));
	 float v1 = (float)(Math.sin(theta)*(orth_vec[1]/mag_orth));
	 float v2 = (float)(Math.sin(theta)*(orth_vec[2]/mag_orth));
	 
	 Quaternion rot = new Quaternion(cos, v0, v1, v2) {};
	 
	 current_vec[0] = (float)new_orientation[0];
	 current_vec[1] = (float)new_orientation[1];
	 current_vec[2] = (float)new_orientation[2];
	 
	 return rot.to_matrix();
	 
	 
	 
 }
 
  public void update( GL2 gl )
  {
	  
	  double[] posXWall_prey = getWallGradient(fish, new double[] {2 ,fish.y, fish.z});
	  double[] posYWall_prey = getWallGradient(fish, new double[] {fish.x ,2, fish.z});
	  double[] posZWall_prey = getWallGradient(fish, new double[] {fish.x ,fish.y, 2});
	  double[] negXWall_prey = getWallGradient(fish, new double[] {-2 ,fish.y, fish.z});
	  double[] negYWall_prey = getWallGradient(fish, new double[] {fish.x ,-2, fish.z});
	  double[] negZWall_prey = getWallGradient(fish, new double[] {fish.x ,fish.y, -2});
	
	  
	 
	  
	 double[] gradientprey = getGradient(fish);
	 double arr[] = new double[] { gradientprey[0] + posXWall_prey[0] + negXWall_prey[0],
			 gradientprey[1] + posYWall_prey[1] + negYWall_prey[1],
		     gradientprey[2] + posZWall_prey[2] + negZWall_prey[2]};
	 
	 double[] no_move = new double[] {0, 0, 0};
	 
	 double[] gradientPredaotr_1 = getGradientPredator(fish);

	 //double[] gradientPredaotr_2 = getGradientPredator(fish2);
	 
	 //double[] predatorGradient = new double[] {gradientPredaotr_1[0]+gradientPredaotr_2[0], gradientPredaotr_1[1]+gradientPredaotr_2[1], gradientPredaotr_1[2]+gradientPredaotr_2[2]};
	
	 tank.update( gl );
	 
	 dolphin.update( gl, getGradientPredator(fish));
	 
	 
	 if(collide) {
		 dolphin.update(gl, no_move);
		 
	 }else {
		 dolphin.update( gl, getGradientPredator(fish));
	 }
		 
	 fish.update(gl, getGradient(fish));
	// fish2.update(gl, getGradient(fish2));
	 
 }
	 
	
	
	


  public void draw( GL2 gl )
  {
	  
	  
	double distance = Math.sqrt((fish.x - dolphin.x)*(fish.x - dolphin.x) + (fish.y - dolphin.y)*(fish.y - dolphin.y) + (fish.z - dolphin.z)*(fish.z - dolphin.z));

	if(distance < (.01)) {collide = true;}
		 
	if (collide) {
			tank.draw( gl );
			dolphin.draw( gl ,rotMatrixPredator(dolphin));
			}
	else {
			
			tank.draw( gl );
			dolphin.draw( gl , rotMatrixPredator(dolphin));
			fish.draw(gl, rotMatrix(fish));
	}
	//tank.draw( gl );
	//dolphin.draw( gl , rotMatrixPredator(dolphin));
	//fish.draw(gl, rotMatrix(fish));
	//fish2.draw(gl,rotMatrix(fish));
	
}
}
