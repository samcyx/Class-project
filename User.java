import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.event.KeyEvent;

public class User {
	private int x;
	private int y;
	private int width;
	private int height;

	private int xa;

	private boolean right = false;
	private boolean left = false;
	boolean spaceCar = false;

	private boolean isInLeft = false;
	private boolean isInMiddle = true;
	private boolean isInRight = false;

	private int xLeft;
	private int xMiddle;
	private int xRight;

	private int tick;

	private int lane = 2;

	private boolean collided = false;

	private BufferedImage img = null;
	private BufferedImage img2 = null;
	private boolean released = false;

	public User(int x, int y, int width, int height, int xLeft, int xMiddle, int xRight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.xLeft = xLeft + 65;
		this.xMiddle = xMiddle + 70;
		this.xRight = xRight + 75;

		this.xa = xMiddle + 70;

		this.tick = 0;

		try {
			img = ImageIO.read(new File("jeep.png"));
			img2 = ImageIO.read(new File("jeepShoot.png"));
		} catch (IOException e) {
			System.out.println("No Image");
		}
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

	public boolean getCollided() {
		return this.collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

	public void keyPressed(KeyEvent e) {
		if (!this.getCollided()) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				left = true;
				move();
				left = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = true;
				move();
				right = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				spaceCar = true;
			}
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (spaceCar) {
			paintCarShoot(g);
		} else {
			paintCar(g);
		}
	}

	public void paintCar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(this.img, this.x, this.y, this.width, this.height, null);
	}

	public void paintCarShoot(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(this.img2, this.x, this.y + 10, this.width, 173, null);

		if (this.tick % 100 == 0) {
			spaceCar = false;
			this.tick = 0;
		}

		tick++;

		if (this.tick > 10) {
			this.tick = 0;
		}
	}

	public void collision(Obstacle b) {
		if (!b.getCaptured()) {
			int front = this.y;
			int animalFront = b.getY() + b.getHeight();

			int back = this.y + height;
			int animalBack = b.getY();

			int animalLane = 0;

			if (b.getX() == 0) {
				animalLane = 1;
			} else if (b.getX() == 340) {
				animalLane = 2;
			} else if (b.getX() == 680) {
				animalLane = 3;
			}

			if ((front <= animalFront) && back >= animalBack && animalLane == lane) {
				System.out.println("Game over");
				collided = true;
			}
		}
	}

	public void move() {
		if (isInLeft) {
			if (right) {
				isInLeft = false;
				right = false;
				xa = xMiddle;
				// System.out.println(x);
				isInMiddle = true;
				lane = 2;
			}
		}
		if (isInMiddle) {
			if (left) {
				isInMiddle = false;
				xa = xLeft;
				// System.out.println(x);
				isInLeft = true;
				lane = 1;
			}
			if (right) {
				isInMiddle = false;
				xa = xRight;
				// System.out.println(x);
				isInRight = true;
				lane = 3;
			}
		}
		if (isInRight) {
			if (left) {
				isInRight = false;
				xa = xMiddle;
				// System.out.println(x);
				isInMiddle = true;
				lane = 2;
			}
		}
		x = xa;
	}
}
