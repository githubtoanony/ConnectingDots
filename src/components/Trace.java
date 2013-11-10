package components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Trace {
	private boolean active;
	private boolean marked;
	private BufferedImage image;
	private int x;
	private int y;
	private int orientation;
	private Rectangle collision;

	public Trace (int orientation, int row, int column)
	{
		active = false;
		marked = false;
		this.orientation = orientation;
		
		if (orientation == 0)
		{
			x = (119 + (45 * row));
			y = (114 + (45 * column));
		}
		else
		{
			x = (119 + (45 * row));
			y = (116 +(45 * column));
		}
		
		if (orientation == 0)
		{
			collision = new Rectangle((131 + (45 * row)), (110 + (45 * column)), 30, 17);
		}
		else
		{
			collision = new Rectangle((115 + (45 * row)), (126 + (45 * column)), 17, 30);
		}
	}
	
	public void set_image (File newImage) throws IOException
	{
		image = ImageIO.read(newImage);
		return;
	}
	
	public void set_coordinates (int newX, int newY)
	{
		x = newX;
		y = newY;
		return;
	}
	
	public void set_active (boolean newActive)
	{
		active = newActive;
		return;
	}
	
	public boolean get_active ()
	{
		return active;
	}
	
	public void set_marked (int current_player) throws IOException
	{
		marked = true;
		
		active = true;
		
		if (orientation == 0)
		{
			switch (current_player)
			{
				case 1: image = ImageIO.read(new File("res/InGame/bar_hor_1.png")); break;
				case 2: image = ImageIO.read(new File("res/InGame/bar_hor_2.png")); break;
				case 3: image = ImageIO.read(new File("res/InGame/bar_hor_3.png")); break;
				case 4: image = ImageIO.read(new File("res/InGame/bar_hor_4.png")); break;
			}
		}
		else
		{
			switch (current_player)
			{
				case 1: image = ImageIO.read(new File("res/InGame/bar_ver_1.png")); break;
				case 2: image = ImageIO.read(new File("res/InGame/bar_ver_2.png")); break;
				case 3: image = ImageIO.read(new File("res/InGame/bar_ver_3.png")); break;
				case 4: image = ImageIO.read(new File("res/InGame/bar_ver_4.png")); break;
			}
		}
		
		return;
	}
	
	public boolean get_marked()
	{
		return marked;
	}
	
	public boolean contains(int cursorX, int cursorY)
	{
		return collision.contains(cursorX, cursorY);
	}
	
	public void render(Graphics2D graphics)
	{
		if (this.active) graphics.drawImage(image, x, y, null);
		return;
	}
}
