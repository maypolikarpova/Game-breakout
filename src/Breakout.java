/*
 * File: Breakout.java
 * -------------------
 * Name: Polikarpova M.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 40;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/**The pause*/	
	private static final double DELAY = 10;


/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		launch();
		play();}

	private void play() {//the part with the methods connected with play part
		GLabel label = new GLabel ("Are you ready to play? Just click!");
		label.setFont("Calibri-20");
		add(label,WIDTH/2-label.getWidth()/2,HEIGHT/2-BALL_RADIUS*1.5);
		waitForClick();
		remove(label);
		getCollidingObject();
		ballMovement();
		waitForClick();
		if (lives!=0){restart();}}

	private void launch() {//the part with the methods connected with the settings
		bricks();
		paddle();
		ball();
		//buttons();
		}

	private void restart() {//the method that restarts the game if the player has the lives left
		while (lives>0){
			remove(ball);
			remove(label1);
			ball();
			play();}}

	private GObject getCollidingObject() {//check for collisions
			while(true){
				collider = getElementAt(ball.getX(),ball.getY());
				if (collider!=null){return collider;}
				else {collider=getElementAt(ball.getX()+BALL_RADIUS*2,ball.getY()+BALL_RADIUS*2);
					if (collider!=null){return collider;}
					else {collider=getElementAt(ball.getX(),ball.getY()+BALL_RADIUS*2);
						if (collider!=null){return collider; }
						else {collider=getElementAt(ball.getX()+BALL_RADIUS*2,ball.getY());
							if (collider!=null){return collider;}
							else{return null;}}}}}}

	private void ballMovement() {//this method describes the movement of the ball
		vy=3;
		vx = rgen.nextDouble(1.0, 3.0); 
		if (rgen.nextBoolean(0.5)) vx = -vx;
		AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");//audio just for fun :)
	
		while (true){
			ball.move(vx, vy);
			pause(DELAY);
			
			if (ball.getX()>=WIDTH-BALL_RADIUS||ball.getX()<=BALL_RADIUS){vx=-vx; touch=false;bounceClip.play();}//change path if it is the left or the right border
			if (ball.getY()<=0){vy=-vy;touch=false;//change path if it is the top
			bounceClip.play();}//the sound
			if (ball.getY()>=HEIGHT-BALL_RADIUS){//the game is over and you lose if it is the bottom
				lives--; 
				label1 = new GLabel("Game over. You have " + lives +" lives left");
				label1.setFont("Calibri-20");
				add(label1,WIDTH/2-label1.getWidth()/2,HEIGHT/2);
				break;}
			
			if (brick_amount==0){//check if there any bricks left, the game is over if isn`t
				GLabel label = new GLabel("You won!");
				label.setFont("Calibri-20");
				add(label,WIDTH/2-label.getWidth()/2,HEIGHT/2);
				lives=0;
				break;}
			
			GObject collider1 = getCollidingObject();
			if (collider1==paddle&&touch==false){vy=-vy; touch=true;}
			else if (collider!=null&&collider!=paddle){vy=-vy; remove(collider);brick_amount--;touch = false; bounceClip.play();}}  }

	private void ball() {//creating the ball 
		ball = new GOval(WIDTH/2-BALL_RADIUS,HEIGHT/2-BALL_RADIUS,BALL_RADIUS*2,BALL_RADIUS*2);
		ball.setFilled(true);
		Color color = rgen.nextColor();
		ball.setColor(color);
		add(ball);}

	private void paddle() {//creating the paddle
		paddle = new GRect(WIDTH/2-PADDLE_WIDTH,HEIGHT-PADDLE_Y_OFFSET,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		add(paddle);}
	
	public void mouseMoved(MouseEvent e){//control the movement of the paddle
		if(e.getX()>0&&e.getX()<=WIDTH-PADDLE_WIDTH){	paddle_x=e.getX();
			paddle.setLocation(paddle_x, HEIGHT-PADDLE_Y_OFFSET);}}
	
	private void bricks() {//creating a block of bricks
		for (int y=BRICK_Y_OFFSET,number=1;y<(BRICK_HEIGHT+BRICK_SEP)*NBRICK_ROWS+BRICK_Y_OFFSET;y+=BRICK_HEIGHT+BRICK_SEP,number++){
			for(int x=BRICK_SEP;x<WIDTH-BRICK_SEP;x+=BRICK_WIDTH+BRICK_SEP){
				GRect brick = new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
				brick.setFilled(true);
				if (number<=2){brick.setColor(Color.RED);}
				else if (number<=4){brick.setColor(Color.ORANGE);}
				else if (number<=6){brick.setColor(Color.YELLOW);}
				else if (number<=8){brick.setColor(Color.GREEN);}
				else {brick.setColor(Color.CYAN);}
				add(brick);}}}
	
	private GRect paddle;//the paddle
	private int paddle_x;//the location of the paddle
	
	private GOval ball;//the ball
	private RandomGenerator rgen = new RandomGenerator();//random generator for random colors of the ball
	private double vx,vy;//the velocity of the ball
	
	private GObject collider;//the paddle or the brick

	private int brick_amount = NBRICK_ROWS*NBRICKS_PER_ROW;//Amount of bricks
	
	private boolean touch = false;//this variable makes sure that the ball won`t cling to the paddle
	
	private int lives = NTURNS;//the amount of gamer`s lives
	private GLabel label1;
}
