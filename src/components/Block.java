package components;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Block {
	
	private BufferedImage image;
	private int owner;
	private int traces;
	private int x;
	private int y;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	
	/*
	 * Class constructor.
	 */
	public Block ()
	{
		owner = 0;
		traces = 0;
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	public void set_coordinates (int newX, int newY)
	{
		x = newX;
		y = newY;
		return;
	}
	
	public boolean set_trace (int newTrace, int player) throws IOException
	{
		boolean block_closed = false;
		traces++;
		
		switch(newTrace)
		{
		case 1: down = true; break;
		case 2: up = true; break;
		case 3: left = true; break;
		case 4: right = true; break;
		}
		
		if (traces == 4)
		{
			owner = player;
			block_closed = true;
			
			switch (owner)
			{
			case 1: image = ImageIO.read(new File("res/InGame/block_1.png")); break;
			case 2: image = ImageIO.read(new File("res/InGame/block_2.png")); break;
			case 3: image = ImageIO.read(new File("res/InGame/block_3.png")); break;
			case 4: image = ImageIO.read(new File("res/InGame/block_4.png")); break;
			}
		}
		
		return block_closed;
	}
	
	public int get_traces ()
	{
		return traces;
	}
	
	public boolean get_up () { return up; }
	public boolean get_down () { return down; }
	public boolean get_left () { return left; }
	public boolean get_right () { return right; }
	
	/*
	 * This function will render all images within the main menu respecting printing order.
	 */
	public void render (Graphics2D graphics)
	{
		// Render the background.
		if (owner != 0) graphics.drawImage(image, x, y, null);
		
		return;
	}
}
