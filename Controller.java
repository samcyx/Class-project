import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Controller
{
	LinkedList<Net> b = new LinkedList<Net>();
	
	Net TempNet;
	
	Game game;
	
	public Controller(Game game)
	{
		this.game = game;
	}
	
	public void collision(Obstacle a)
	{	
		for (int i = 0; i < b.size(); i++)
		{
			TempNet = b.get(i);
			
			TempNet.collision(a);
			
			if (TempNet.collided)
			{
				removeNet(TempNet);
			}
		}
	}
	
	public void paint (Graphics g, User u)
	{
	Graphics2D g2d = (Graphics2D) g;
	
	for (int i = 0; i < b.size(); i++)
	{
		TempNet = b.get(i);
		
		if (!TempNet.collided && !u.getCollided())
		{
		TempNet.paint(g);
		}
	}
	}
	
	public void move()
	{
		for (int i = 0; i < b.size(); i++)
		{
			TempNet = b.get(i);
			
			if (!TempNet.collided)
			{
			TempNet.move();
			}	
		}
	}
	
	public void addNet(Net block)
	{
		b.add(block);
	}

	public void removeNet(Net block)
	{
		b.remove(block);
	}
}
