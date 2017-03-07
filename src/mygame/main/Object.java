package mygame.main;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Object {	//the class that defines the platforms
        private float x;	//x positions
        private float y;	//y positions

        public Object() 	//constructor for a new object
        {
                x = (float) (480 * Math.random()-50) ;
                y = 0;
        }
        
        public Object(float newx, float newy) 	//constructor for a new object with defined positions
        {
                x = newx;
                y = newy;
        }
        
        public void setY (float newy)	//sets the new y position
        {	//no setting x position because the x position does not change from its initial value
                y=newy;
        }
        
        public float getX ()	//gets the current x position
        {
                return x;
        }

        public float getY ()	//gets the current y position
        {
                return y;
        }
}
