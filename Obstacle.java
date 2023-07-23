import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.Graphics;

public class Obstacle
{
	protected boolean scored = false;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int ya;
	protected BufferedImage finalImage1;
	protected BufferedImage finalImage2;
	protected BufferedImage finalImage;
	protected boolean crashed;
	protected int laneWidth;
	protected boolean captured = false;
	protected int score;
	
	boolean acollided;
	
	public Obstacle(int x, int y, int width, int height, int laneWidth, int ya) {
		this.crashed = false;
		this.laneWidth = laneWidth;
		this.finalImage1 = null;
		this.finalImage2 = null;
		this.finalImage = null;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.ya = ya;
		try {
			finalImage1 = ImageIO.read(new File("rock-1.png"));
			finalImage2 = ImageIO.read(new File("rock-2.png"));
		} catch (IOException e) {
			System.out.println("No Image Rock");
		}
		this.finalImage = this.finalImage1;
	}
	public void setYA(int ya)
    {
        this.ya = ya;
    }
	public void setCrashed() {
		this.crashed = true;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getYa() {
		return this.ya;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int ya) {
		this.y += ya;
	}

	public void paint(Graphics g) {

		if (crashed){//animate the crash
			finalImage = finalImage2;
			ya = 0;
		}
		//draw the rock centered
		g.drawImage(finalImage, this.laneWidth / 2 + this.x - this.width / 2, this.y, this.width, this.height, null);

	}
	public void paint(Graphics g, BufferedImage img, boolean captured) {//overloaded method for the subclass Animal to use
		
		if(captured)
		{
			ya = 0;
			this.captured = true;
		}
		else if (crashed){
			finalImage = finalImage2;
			ya = 0;
		}
		g.drawImage(img, this.laneWidth / 2 + this.x - this.width / 2, this.y, this.width, this.height, null);//draw the image

	}
	public void move() {
		y += ya;
	}
	public boolean getCaptured()
	{
		return this.captured;
	}
	public boolean isAnimal()
	{
		return false;
	}
	public void capture()
	{//capture this obstacle
		this.captured = true;
		this.scored = true;
	}
	public int getScore(){
		if(scored)//dont return a score if the animal's already been score
			return 0;
		return this.score;
	}
}
