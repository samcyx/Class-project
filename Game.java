import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Game extends JPanel {
    // initialize the variables
    private int netYA = 5;// net speed
    private User user;
    private static Game game;
    private static boolean restart = false;// restart variable

    private Obstacle[] entities = new Obstacle[1000];// array of all possible obstacles
    private boolean firstGame;// boolean of whether or not the current game class is the first one ever
    private static int sW = 1020;
    private static int sH = 640;
    private int screenWidth;
    private int screenHeight;
    private int laneNum;
    private int laneSpace;
    private int laneWidth;
    private int[] laneX;// holds all the x values for laness

    private int score;
    private int highScore = readHighScore();
    private int backgroundScrollSpeed;// how fast the backgrounds scroll
    private int background1y;// y position of backgrounds
    private int background2y;

    private boolean collided;// holds whether or not the game ends, i.e collision occured

    private int gameOverWidth = 780;// how wide the gameover screen is
    private boolean pause;
    private boolean started = false;// whether or not the game is started past the cover
    private boolean enter = false;// if the enter key is pressed
    private static boolean exit;
    // load images
    private BufferedImage cover = null;
    private BufferedImage pauseScreen = null;
    private BufferedImage gameOver = null;
    private BufferedImage background1 = null;
    private BufferedImage background2 = null;
    private boolean released = true;// released this key for firing the net
    private Controller c = new Controller(this);

    public Game(int laneSpace, int laneNum, int screenWidth, boolean firstGame) {
        this.netYA = 5;
        this.backgroundScrollSpeed = 2;
        this.firstGame = firstGame;
        this.pause = firstGame;// if first game, then pause it
        this.started = !firstGame;// make sure that the game starts immediately if the game is not a first game
        // import images
        try {
            this.background1 = ImageIO.read(new File("Background-1.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND background 1");
        }
        try {
            this.background2 = ImageIO.read(new File("Background-2.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND background 2");
        }

        // reads in the intro page, pause page, and restart/gameover page
        try {
            this.cover = ImageIO.read(new File("Cover.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND cover");
        }
        try {
            this.pauseScreen = ImageIO.read(new File("Pause Screen.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND pause");
        }
        try {
            this.gameOver = ImageIO.read(new File("End Screen.png"));
        } catch (IOException e) {
            System.out.println("NO IMAGE FOUND gameOver");
        }

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && started && !pause)
                    released = true;// set released to true, allowing the player to fire again
                game.keyReleased(e);// pass keyReleased to the game
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // user.keyPressed(e);
                game.keyPressed(e);
                if (started && !pause) {

                    user.keyPressed(e);// if only the game is started and not paused can the user move, same with net
                                       // below
                }
                if (started && !pause) {

                    if (e.getKeyCode() == KeyEvent.VK_SPACE && released == true) {
                        c.addNet(new Net(user.getX() + ((user.getWidth() / 2) - 4), user.getY() + 4, c.game, netYA));// add
                                                                                                                     // a
                                                                                                                     // new
                                                                                                                     // net
                                                                                                                     // to
                                                                                                                     // the
                                                                                                                     // controller
                                                                                                                     // object
                        released = false;// set released to false
                    }

                }
            }
        });
        setFocusable(true);
        this.laneNum = 3;
        this.laneX = new int[laneNum];

        this.screenWidth = 1020;
        this.screenHeight = 640;
        this.background1y = 0;
        this.background2y = -this.screenHeight;
        this.laneSpace = 1020;
        this.laneWidth = this.laneSpace / this.laneNum;
        this.laneX[0] = (this.screenWidth - this.laneSpace) / 2;// gets the leftmost border
        this.score = 0;
        for (int i = 1; i < laneNum; i++) {
            this.laneX[i] = laneX[i - 1] + laneWidth;
        }
        this.user = new User(373, 380, 200, 166, laneX[0], laneX[1], laneX[2]);

        entities[0] = new Animal(laneX[0], -150, 100, 150, this.laneWidth, 5);
        // initialize an animal at the start of the game for all the others to be
        // generated in reference to
        for (int i = 1; i < entities.length; i++) {// for the entities
            if ((int) (Math.random() * (4 - 1) + 1) == 1) {// if hits 1/3 chance, generate obstacle
                this.entities[i] = new Obstacle(laneX[(int) (Math.random() * 3)],
                        (entities[i - 1].getY() + (int) (-((Math.random() * (700 - 400) + 400)))), 80, 80,
                        this.laneWidth, 2);// randomly make the obstacle between 700 and 300 away from the adjacent
                                           // entity
            }

            else {// if hits 2/3 chance, generate animal
                this.entities[i] = new Animal(laneX[(int) (Math.random() * 3)],
                        (entities[i - 1].getY() + (int) (-((Math.random() * (600 - 300) + 300)))), 100, 150,
                        this.laneWidth, 5);// randomly make the animal between 600 and 300 away from the adjacent entity
            }

        }

    }

    private int readHighScore() {// read the high score file for the highscore variable
        try {
            FileReader fileReader = new FileReader("highScore.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String highScoreString = bufferedReader.readLine();
            bufferedReader.close();
            return Integer.parseInt(highScoreString);
        } catch (IOException | NumberFormatException e) {
            // Error reading the high score, handle it as needed
            return 0; // Return a default high score of 0
        }
    }

    private void writeHighScore(int score) {// write a new score for the high score
        try {
            FileWriter fileWriter = new FileWriter("highScore.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(Integer.toString(score));
            bufferedWriter.close();
        } catch (IOException e) {
            // Error writing the high score
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {// handles keyevents

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {// if escape, then user wants to exit
            this.exit = true;
            System.out.println("SCORE: " + score);
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {// pause the game if the user presses p
            this.pause = pause ? false : true;// reverses the boolean
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (collided) {// if the game has ended, then restart is true
                restart = true;
                this.user.setCollided(false);
            } else if (started && !pause)// if the game has started and is not paused then do a reset
            {
                this.collided = true;
                this.user.setCollided(true);
            } else if (pause && started) {
                pause = !pause;// extra function because some users may want to unpause using enter as habit
            }
            this.started = true;// set the started variable to true

            this.enter = enter ? false : true;// reverse the previous enter, equal to this.enter = !enter;
        }

    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // scrolling code

        g2d.drawImage(background1, 0, background1y, 1020, 650, null);
        g2d.drawImage(background2, 0, background2y, 1020, 650, null);
        // draw backgrounds at their specificed valued, with extra tolerance coded in
        for (int i = 0; i < entities.length; i++) {// paint all entities
            entities[i].paint(g2d);
        }

        user.paint(g2d);// paint user
        c.paint(g2d, this.user);// paint controller
        if (!collided) {// if the game isn't over
            if (!started && firstGame) {// draws the cover art
                g2d.drawImage(cover, 0, -50, 1020, 651, null);

            } else if (!started && firstGame && pause) {// draws the pauseScreen.
                g2d.drawImage(pauseScreen, 0, 0, 1020, 640, null);
            } else if (pause) {// draws the pause screen but for a different set of conditions.
                g2d.drawImage(pauseScreen, 0, 0, 1020, 608, null);
            }

            else {// else draw the high score stuff in top left corner
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(0, 0, 120, 60);
                g2d.setColor(Color.BLACK);
                g2d.drawString("SCORE: " + String.valueOf(score), 0, 15);
                g2d.drawString("HIGH SCORE: " + String.valueOf(highScore), 0, 40);
            }
        }

        else if (collided) {// if collided then draw the game over stuff and center the high score over the
                            // things
            g.drawImage(gameOver, this.screenWidth / 2 - this.gameOverWidth / 2, 0, this.gameOverWidth,
                    this.screenHeight, null);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(this.screenWidth / 2 - 140 / 2, 0, 140, 80);
            g2d.setColor(Color.BLACK);
            g2d.drawString("SCORE: " + String.valueOf(score), this.screenWidth / 2 - 140 / 2, 15);
            g2d.drawString("HIGH SCORE: " + String.valueOf(highScore), this.screenWidth / 2 - 140 / 2, 40);
        }
    }

    public static boolean getRestart() {
        return restart;// return the restart value
    }

    public void move() {

        if (!collided && started && !pause) {// only animate if not paused, if started, and if not collided i.e game
                                             // overed
            // move the nets/user
            user.move();
            c.move();
            // scroll the backgrounds down
            background1y += backgroundScrollSpeed;
            background2y += backgroundScrollSpeed;
            if (background1y > this.screenHeight) {// if it is below, make it above the screen
                background1y = -this.screenHeight;
            }
            if (background2y > this.screenHeight) {// ^^^ same as above, but for the second background picture
                background2y = -this.screenHeight;
            }
            for (int i = 0; i < entities.length; i++) {
                // essentially loops through and animates the entities
                // below if statements make the game move faster if the score has reached a
                // certain number
                if (this.score == 25)// checks if the score reached the increment number
                {
                    entities[i].setYA(3);
                    this.backgroundScrollSpeed = 3;
                    netYA = 6;
                } else if (this.score == 50) {
                    entities[i].setYA(4);
                    this.backgroundScrollSpeed = 4;
                    netYA = 7;
                } else if (this.score == 80) {
                    entities[i].setYA(6);
                    this.backgroundScrollSpeed = 6;
                    netYA = 8;
                }

                if (entities[i].isAnimal())// if it is an animal, then check for collision with net
                {
                    c.collision(entities[i]);
                    if (entities[i].acollided) {
                        this.score += entities[i].getScore();// add score if they collide
                        entities[i].capture();// capture the animal

                        System.out.println("Shots fired!!!11!!11!");

                        entities[i].acollided = false;// sets that entity's collided to false
                    }
                }
                entities[i].move();

                user.collision(entities[i]);// check for collision between entitity and user
                collided = user.getCollided();// make collided field set to the collided calculated from earlier, if
                                              // applicable

                if (collided)// if collided, then that entity is set to crashed
                {
                    entities[i].setCrashed();
                }
            }

        }

    }

    public void checkHighScore() {// replace the highscore if it had been surpassed
        if (score > highScore) {
            highScore = score;
            writeHighScore(highScore);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Dian Fossey's Safari Adventure");
        game = new Game(1020, 3, 1020, true);
        frame.add(game);
        frame.setSize(sW, sH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// initialize the frame

        while (true) {
            game.move();
            game.repaint();
            Thread.sleep(10);

            if (exit) {// exiting the game
                game.checkHighScore();// check the high score
                frame.dispose();// dispose of the frame
                break;// get out of the for loop
            }

            else if (game.getRestart()) {// if the user decides to restart
                // check the high score
                game.checkHighScore();
                frame.dispose(); // Destroy the JFrame object
                restart = false;
                frame = new JFrame("Dian Fossey's Safari Adventure");
                System.out.println("RESTARTING");
                // Above code essentially recreates a new frame after disposing of the last one
                game = new Game(1020, 3, 1020, false);// new game that is identical to last, except firstGame is false
                frame.add(game);// do frame operations
                frame.setSize(sW, sH);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }
}
