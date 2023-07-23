import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

public class Net
{
		private int ya;
	    private double x;
	    private int y;
	    private int width;
	    private double length;
	    
	    boolean collided;
	    
	    private BufferedImage net;
	    
	    public Net(int x, int y, Game game, int ya)
	    {
			this.ya = ya;
	    	this.x = x;
	    	this.y = y;
	    	
	    	this.width = 12;
	    	this.length = 10;
	        
	        this.net = null;
	        try {
	            this.net = ImageIO.read(new File("Net.png"));
	        } catch (IOException e) {
	            System.out.println("NO IMAGE FOUND");
	        }
	    }
	    
	    public boolean getCollided()
	    {
	    	return this.collided;
	    }	    
	    public int getLane()
	    {
			 if (this.x >= 0 && this.x < 340 && (this.y + this.length) > 0)
			 {
				 return 1;
			 }
			 else if (this.x >= 340 && this.x < 680 && (this.y + this.length) > 0)
			 {
				 return 2;
			 }
			 else if (this.x > 680 && this.x <= 1020 && (this.y + this.length) > 0)
			 {
				 return 3;
			 }
			 return 0;
	    }
	    
	    public void collision(Obstacle b)
	    {	    	
			 if(!b.getCaptured())
			 {
			 double front = this.y + (this.length / 5);
			 double back = this.y + this.length;
			 
			 int animalFront = b.getY() + b.getHeight();
			 int animalBack = b.getY();
			 
			 int animalLane = 4;
			 
			 if (b.getX() == 0)
			 {
				 animalLane = 1;
			 }
			 else if (b.getX() == 340)
			 {
				 animalLane = 2;
			 }
			 else if (b.getX() == 680)
			 {
				 animalLane = 3;
			 }
			 
			 //System.out.println(getLane());

			 //if ((front <= animalBack) && animalLane == getLane())
			 if ((back <= animalFront) && animalLane == getLane() && (back) > 0)
			 {
				 				 
				 collided = true;
				 
				 b.acollided = true;
				 
				 //System.out.println("Lane: " + getLane() + ", y: " + this.y);
			 }
			 }
	    }
	    public void paint(Graphics g)
	    {
	    	Graphics2D g2d = (Graphics2D) g;
	    	
	    	g.drawImage(this.net, (int) (this.x -= 1), this.y, this.width, (int) this.length, null);
	    }

	    public void move()
	    {
	    	if (!((y + length) <= 0))
	    	{
	    	y -= this.ya;
	    	}
	    	
	    	
	    	if (!(width >= 340))
	    	{
	    	width += 2;
	    	}
	    	
	    	if (!(length >= 340))
	    	{
	    	length += 2;
	    	}
	    	
	    	//x -= 1;
	    }
}
