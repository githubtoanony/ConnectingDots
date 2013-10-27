package game;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Button {
	private BufferedImage image[];
	private int state;
	private boolean visible;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public int start_x;
	public int start_y;
	public int end_x;
	public int end_y;
	
	/*
	 * Class constructor.
	 */
	public Button (int newX, int newY, int newWidth, int newHeight, boolean newVisible)
	{
		image = new BufferedImage[2];
		state = 0;
		visible = newVisible;
		x = newX;
		y = newY;
		width = newWidth;
		height = newHeight;
		
		start_x = x;
		start_y = y;
		end_x =  x + width;
		end_y = y + height;
	}
	
	/*
	 * Set the image(s) of the button.
	 */
	public void set_image(File newImage0, File newImage1) throws IOException
	{
		image[0] = ImageIO.read(newImage0);
		image[1] = ImageIO.read(newImage1);
		return;
	}
	
	public void set_image(File newImage) throws IOException
	{
		image[0] = ImageIO.read(newImage);
		return;
	}
	
	/*
	 *  Sets a new value of the x and y coordinate variables.
	 */
	public void set_coordinates(int newX, int newY)
	{
		x = newX;
		y = newY;
		return;
	}
	
	/*
	 *  Sets a new value of the x coordinate variable.
	 */
	public void set_x (int newX)
	{
		x = newX;
		return;
	}
	
	/*
	 *  Gets the current value of the x coordinate variable.
	 */
	public int get_x()
	{
		return x;
	}
	
	/*
	 *  Sets a new value of the y coordinate variable.
	 */
	public void set_y(int newY)
	{
		y = newY;
		return;
	}
	
	/*
	 *  Gets the current value of the x coordinate variable.
	 */
	public int get_y()
	{
		return y;
		
	}
	
	/*
	 *  Sets a new value of the width variable.
	 */
	public void set_width (int newWidth)
	{
		width = newWidth;
		return;
	}
	
	/*
	 *  Gets the current value of the width variable.
	 */
	public int get_width()
	{
		return width;
	}
	
	/*
	 *  Sets a new value of the height variable.
	 */
	public void set_height (int newHeight)
	{
		height = newHeight;
		return;
	}
	
	/*
	 *  Gets the current value of the height variable.
	 */
	public int get_height()
	{
		return height;
	}
	

	/*
	 *  Sets a new value of the state variable.
	 */
	public void set_state(int newState)
	{
		state = newState;
		return;
	}
	
	
	/*
	 *  Gets the current value of the state variable.
	 */
	public int get_state()
	{
		return state;
	}
	
	public void set_visible(boolean newVisible)
	{
		visible = newVisible;
		return;
	}
	
	public boolean get_visible()
	{
		return visible;
	}
	
	/*
	 * This function will render all images within the main menu respecting printing order.
	 */
	public void render (Graphics2D graphics)
	{
		graphics.drawImage(image[state], x, y, null);
		return;
	}
}
