package mygame.main;
import mygame.main.Object;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
//////////////////////////////////////////////////////////////////////////////////////////////////////
//A penguin slide n'jump game where the speed and score increases constantly over accumulated time	//
//Good Luck and Have Fun																			//
//By Hongyu Luo															                            s//
//Student# 602581 and 												                                //
//A whoppin 7 - something megabytes and a lot of blood + sweat										//
//not to mention tears																				//
//////////////////////////////////////////////////////////////////////////////////////////////////////
class Animation extends SurfaceView implements SurfaceHolder.Callback {
	class AnimationThread extends Thread {		//animation thread of the program, which basically is multitasking within the program
		Resources res = getResources();			//auto-generated codes that sort the images used in the thread
		Bitmap subject = BitmapFactory.decodeResource(res, R.drawable.pix);	//stating the images 
		Bitmap plat = BitmapFactory.decodeResource(res, R.drawable.plat);
		Bitmap title = BitmapFactory.decodeResource(res, R.drawable.titlepic);
		Bitmap gg = BitmapFactory.decodeResource(res, R.drawable.ggback);
		Bitmap button1 = BitmapFactory.decodeResource(res, R.drawable.startbutton);
		Bitmap button2 = BitmapFactory.decodeResource(res, R.drawable.startbuttonpressed);
		Bitmap back = BitmapFactory.decodeResource(res, R.drawable.background);
		Bitmap subject2 = BitmapFactory.decodeResource(res, R.drawable.pix2);
		private Path path = new Path();//setting the path for the strings displayed
		private Path path2 = new Path();/////////////////////////////////////////////////////////////////////////////////////////////
		View view = new View (getContext());// the class that is responsible for the look of the text component
		ArrayList list = new ArrayList (); //Arraylist to hold the platforms
		private boolean on;				//determines the state of the program (on/off)
		private boolean down = true;	//if penguin is falling
		private long previousTime;      //factors in determining the frame rate *******
		private int diff;				//*******
		private int framesCollected = 0;	//******
		private int frameTime = 0;	//*******
		private int fps = 0;		//*******
		private int count = 0;		//keeps track of the number of jumps
		private float currentX=0; 	//stores the current X position of the subject
		private final float currentY=  600;	//stores the current Y position of the subject
		private float gravity =10;	//gravity that determines the jump velocity of the penguin
		private float velocity =0;	//velocity of the jumps
		private int num=0;		//number of platforms on screen
		private SurfaceHolder surfaceHolder;	//allows you to modify and change the surface image
		private float DecreaseInY=10;		//determines the horizontal movement velocity in relative motion to the platforms
		private Paint text;		//defines colour patterns for 2D operations
		private long score=0;	//stores the accumulated score
		private boolean lose =false;	//determine if player has lost
		private boolean start =false;	//determines if the game has started 
		private boolean menuscreen = true;	//determines if the program proceeds to menuscreen or not
		private static final float TOUCH_TOLERANCE = 4;	//touch tolerance
		
		public AnimationThread(SurfaceHolder sh) { //constructor method
			surfaceHolder = sh;
			setFocusable(true);
			text = new Paint();
			text.setColor (Color.BLACK);
			text.setTextSize(16);
		}
		@Override
		//////////////////////////////////////////////////////////////////////////////////////////////\
		public void run() {//run 

			init ();	//creates setting for beginning
			currentX = 60;	//creates default positio of subject
			while (on) {	//loop while program is on
				Canvas c = null;	//canvas class for drawing images
				try {
					c = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {	//allows for communication between the threads
						if (start == false)	//if the game did not start yet
							menu(c);	//go to menu first
						else {update();	//if the game has started then continues with the game
						if (!lose)	//if the player does not lose
							Act3 (c);	//continues to act3
						else
							gameover (c);	//this would suggest that the player has lost and goes to gameover
						}}
				}finally {	//protection net
					if (c != null) {
						surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}

		private void init ()	//the initial procedure of the program
		{
			Object i =new Object (350,getHeight()-630 );	//constructs 2 platforms
			list.add (i);	//add them to array
			Object i2 =new Object (350,getHeight()-1300 );
			list.add (i2);
			num++;	//increase the count for platforms
			num++;
		}

		public void menu(Canvas canvas){ //menu screen
			canvas.drawBitmap(title, 0,0 , text);	//draws the titlescreen onto the screen
			canvas.drawBitmap(button2, 200,250 , text);	//draws the button onto the screen
		}

		private void gameover (Canvas canvas)	//gameover screen
		{
			/////////////////////////////////////////////////////////////////////////////////////////////
			menuscreen = true; //the menuscreen has been accessed
			path.moveTo(100, 500);	//sets the path for the strings
			path.lineTo (100, 300);
			canvas.drawBitmap(gg, 0,0 , text);	//draws gg image
			canvas.drawPath(path, text);	//draws the path
			canvas.drawTextOnPath("Game Over ", path ,5, 0, text);	//writes the texts along the path
			canvas.drawTextOnPath(score + " points", path, 5, 20, text);	
			canvas.drawTextOnPath("Press anything to restart", path, 5, 60, text);
			/////////////////////////////////////////////////////////////////////////////////////////////
		}

		private void update() { //Algorithm for the frame rate the current frame rate
			long now = System.currentTimeMillis();

			if (previousTime != 0)
			{

				diff = (int) (now - previousTime);
				frameTime += diff;
				framesCollected++;

				if (framesCollected == 10) {

					fps = (int) (10000 / frameTime);

					frameTime = 0;
					framesCollected = 0;
				}
			}
			previousTime= now;
		}

		private void act2 (){//the act of adding more platforms when a platform is deleted
			Object i =new Object (400 -(float)(100 * Math.random()),-180 - (float)(100 * Math.random()));//randomly creates a platform with x y co-ordinates in a range
			list.add (i);//adds it to the array
			num++;	//number of platforms increase
		}

		private void drawItem (Canvas canvas)	//draws the platforms in relation to their current position
		{
			view.draw(canvas);	//initiate the canvas
			if (num!=0)		//if the number of platforms are not 0, which it shouldn't be anyways
			{
				for (int l =0; l<list.size (); l ++)	//for as many times as the number of platforms
				{
					Object temp = (Object) list.get(l);	//gets the platform
					
					if (DecreaseInY!=0)	//if the horizontal velocity is not 0, which it shouldn't be 
					{
						temp.setY (temp.getY()+DecreaseInY*diff/100); //give the platform the new position due to change in position
					}
					canvas.drawBitmap(plat,temp.getX(),temp.getY (), text); //draws the new platform
					if (list.size() < 2)	//if there are less than 2 platforms
					{
						if (temp.getY() >= 400)	//when a platform reaches to that point while having less than 2 platforms in total
							act2();	//then add another platform
					}
					if (currentX >= temp.getX()- 50 && currentX <=temp.getX()&& currentY+40 >= temp.getY() && currentY <= temp.getY()+500) //if the penguin is at that point
					{
						currentX = temp.getX()-50; // this serves as a net at this position for the penguin
						count = 0;	//the number of jumps is refreshed
						velocity = 1;	//the velocity is set to 1 - seemingly motionless
						down = true;	//and the penguin is down on the ground
					} 

					if (temp.getY () > getHeight ()) //when the platform reach a certain point
					{
						list.remove (l);	//the platform is deleted
						num --;	//decreasing the number of total platforms
						l--;	//decreasing the array size, stopping the next 'for' procedure
					}
				}
			}
		}
		private void Act3(Canvas canvas) {	//act of drawing the subject - the penguin
			//////////////////////////////////////////////////////////////////////////////
			canvas.drawBitmap(back, 0,0 , text);			//Android will assign more resources to the program if more resoruces are needed
			canvas.drawBitmap(back, 0,0 , text);			//so by adding a few more meaningless images, the demand for resources increase
			canvas.drawBitmap(back, 0,0 , text);
			canvas.drawBitmap(back, 0,0 , text);
			path2.moveTo(14, 100);	//path settings
			path2.lineTo (14, 10);	
			canvas.drawTextOnPath(fps + " fps",path2, 0, 5, text);	//displays the frame rate on path
			canvas.drawTextOnPath(score + " points",path2, 0, 20, text);	//displays the score on path
			//////////////////////////////////////////////////////////////////////////////
			score ++;	//score increases constantly
			DecreaseInY += 0.005;	//horizontal velocity increases constantly
			if (DecreaseInY >= 100)	//the cap for horizontal velocity is set at 100
				DecreaseInY = 100;	//will stay at 100 if velocity goes greater than 100
			velocity -= (gravity*diff)/100;	//determines the current jump velocity of the penguin
			currentX-= velocity*diff/100;	//determines the current x position of the penguin

			if (currentX>getWidth ())		//if the penguin falls down...
				lose =true;	//you lose
			drawItem(canvas);	//draws the platforms again with their new positions
			if(!down)	//if the penguin is down, the image of it sliding is apparent
				canvas.drawBitmap(subject, currentX ,currentY , text);  	
			else	//or else the jumping image is shown
				canvas.drawBitmap(subject2, currentX ,currentY , text);  

			canvas.restore();            //restores the canvas state
		}


		public void setRunning(boolean b) {	//the game is running or not
			on = b;
		}      

		public void goUp ()	//when the penguin jumps
		{
			velocity =50;	//jump velocity is set at 50
		}
	}


	/** The thread that actually draws the animation */
	private AnimationThread thread;


	public Animation(Context context, AttributeSet attrs) {
		super(context, attrs);


		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// create thread only; it's started in surfaceCreated()
		thread = new AnimationThread(holder);

	}
/////////////////////////////////mouse controls are an option for player
	@Override
	public boolean onTouchEvent(MotionEvent event) {	//the method from Fingerpaint sample for mouse interaction
        float x = event.getX();	//gets the x and y position of the mouse
        float y = event.getY();
        
		switch (event.getAction()) {	
		case MotionEvent.ACTION_DOWN:	//when the mouse is pressed down
			if (!thread.start&& x >210 && x <370 && y > 250 && y < 550)	//on the title screen with the button
			{
				thread.init();	//the game starts if the mouse is pressed down within that parameter
				thread.start = true;	//game started
			}
			else
			{
				if (thread.menuscreen){	//when the player has lost and is at the gameover screen
					thread.menuscreen = false;	//resets everything upon click to restart game
					thread.score =0;
					thread.list=new ArrayList ();
					thread.num=0;
					thread.init();
					thread.lose=false;
					thread.currentX = 60;
					thread.velocity = 0;
					thread.count = 0;
					thread.DecreaseInY = 10;
				}
				else {
					if (thread.count < 2)	//otherwise it acts as the click - to - jump
					{
						thread.goUp ();	//jumps
						thread.down = false;	//not down - on - the - ground
					}
					thread.count ++;
				}
				break;
			}
		}
		return true;
	}	
	/////////////////////////////////Keyboard controls are also an option for player
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) { //when the up key is pressed on keyboard
			if (thread.count < 2)
			{
				thread.goUp ();	//jump
				thread.down = false;
			}
			thread.count ++;
			return (true);
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {	//when down key is pressed
			thread.score =0;	//resets
			thread.list=new ArrayList ();
			thread.num=0;
			thread.init();
			thread.lose=false;
			thread.currentX = 60;
			thread.velocity = 0;
			thread.count = 0;
			return (true);
		}
		return super.onKeyDown(keyCode, msg);
	}




	/**
	 * Obligatory method that belongs to the:implements SurfaceHolder.Callback
	 */

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {	
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
}
