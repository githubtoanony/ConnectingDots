package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Point {

	private BufferedImage image[];
	private boolean active;
	private int state;
	private int x_A;
	private int x_B;
	private int y_A;
	private int y_B;
	private int row;
	private int column;
	private int orientation;

	/*
	 * Class constructor.
	 */
	public Point () throws IOException
	{
		active = false;
		image = new BufferedImage[5];
		image[0] = ImageIO.read(new File("res/InGame/dot_p0.png"));
		image[1] = ImageIO.read(new File("res/InGame/dot_p1.png"));
		image[2] = ImageIO.read(new File("res/InGame/dot_p2.png"));
		image[3] = ImageIO.read(new File("res/InGame/dot_p3.png"));
		image[4] = ImageIO.read(new File("res/InGame/dot_p4.png"));
		state = 0;
	}
	
	public Point (int newState) throws IOException
	{
		active = false;
		image = new BufferedImage[5];
		image[0] = ImageIO.read(new File("res/InGame/dot_p0.png"));
		image[1] = ImageIO.read(new File("res/InGame/dot_p1.png"));
		image[2] = ImageIO.read(new File("res/InGame/dot_p2.png"));
		image[3] = ImageIO.read(new File("res/InGame/dot_p3.png"));
		image[4] = ImageIO.read(new File("res/InGame/dot_p4.png"));
		state = newState;
	}
	
	/*
	 *  Sets a new value to the active variable.
	 */
	public void set_active (boolean newActive)
	{
		active = newActive;
		return;
	}
	
	/*
	 *  Gets the current state of the active variable.
	 */
	public boolean get_active ()
	{
		return active;
	}
	
	/*
	 *  Sets a new value to the state variable.
	 */
	public void set_coordinates (int newRow, int newColumn, int newOrientation)
	{
		row = newRow;
		column = newColumn;
		orientation = newOrientation;
		
		if (orientation == 0)
		{
			x_A = (row * 45) + 113;
			y_A = (column * 45) + 108;
			
			x_B = ((row + 1) * 45) + 113;
			y_B = (column * 45) + 108;
		}
		else
		{
			x_A = (row * 45) + 113;
			y_A = (column * 45) + 108;
			
			x_B = (row * 45) + 113;
			y_B = ((column + 1) * 45) + 108;
		}
			
		return;
	}

	/*
	 *  Sets a new value to the state variable.
	 */
	public void set_state (int newState)
	{
		state = newState;
		return;
	}
	
	/*
	 *  Gets the current state of the state variable.
	 */
	public int get_state ()
	{
		return state;
	}
	
	/*
	 * This function will render all images within the main menu respecting printing order.
	 */
	public void render (Graphics2D graphics)
	{
		graphics.drawImage(image[state], x_A, y_A, null);
		graphics.drawImage(image[state], x_B, y_B, null);
		return;
	}
}