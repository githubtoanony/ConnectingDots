package components;

import java.awt.Graphics2D;
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
	
	public Trace ()
	{
		active = false;
		marked = false;
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
	
	public void set_marked (boolean newMarked)
	{
		marked = newMarked;
		return;
	}
	
	public boolean get_marked ()
	{
		return marked;
	}
	
	public void render (Graphics2D graphics)
	{
		if (this.active) graphics.drawImage(image, x, y, null);
		return;
	}
}
