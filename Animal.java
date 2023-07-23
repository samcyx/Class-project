import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

public class Animal extends Obstacle {

    private int score;
    private int tick;
    private BufferedImage finalImage1;
    private boolean isCaptured;
    private BufferedImage finalImage2;
    private BufferedImage finalImage;
    private int height1;
    private int height2;


    public Animal(int x, int y, int width, int height, int laneWidth, int score) {
        super(x, y, width, height, laneWidth, 2);
        // this.laneWidth = laneWidth;
        this.height1 = height;
        this.height2 = 136;

        this.score = score;
        this.tick = 0;

        this.isCaptured = false;
//read in animals
        try {
            this.finalImage1 = ImageIO.read(new File("Animal-1Crop.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND animal 1");
        }
        try {
            this.finalImage2 = ImageIO.read(new File("Animal-2Crop.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND animal 2");
        }
    }
   
    public boolean isAnimal() {
        return true;
    }

    public int getScore() {
        if (scored)
            return 0;
        return score;
    }

    public void paint(Graphics g) {
        super.paint(g, finalImage, isCaptured);//refer to the parent's paint
    }

    public void move() {
        if (this.captured)//don't draw if captured
            this.finalImage = null;
        else if (this.tick % 100 == 0) //ticks essentially control animation so it doesn't flicker between them rapidly
        {
            this.finalImage = finalImage2;
            this.height = height2;
        } 
        else if (this.tick % 50 == 0) {
            
        
            this.height = height1;
            this.finalImage = finalImage1;
            

        }
        this.y += ya;
        tick++;

        if (this.tick > 400) {//reset tick at this number
            this.tick = 0;
        }
    }

    public boolean isCaught() {
        return isCaptured;
    }

}
