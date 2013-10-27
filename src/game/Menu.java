package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Menu
{
	// Variable that control the time the menu shall be the active screen.
	private boolean active;
	
	// Definition
	public final int offsetX;
	public final int offsetY;
	
	// Background image.
	private BufferedImage background;
	
	// Buttons images.
	private Button start;
	private Button multiplayer;
	private Button records;
	private Button settings;
		
	/*
	 * Class constructor.
	 */
	public Menu(int newOffsetX, int newOffsetY) throws IOException
	{
		active = false;
		
		offsetX = newOffsetX;
		offsetY = newOffsetY;
		
		background = ImageIO.read(new File("res/menu/background.png"));
		
		start = new Button((350 + offsetX), (205 + offsetY), 260, 100, true);
		start.set_image(new File("res/menu/start.png"), new File("res/menu/start_pressed.png"));

		multiplayer = new Button((350 + offsetX), (325 + offsetY), 260, 100, true);
		multiplayer.set_image(new File("res/menu/multiplayer.png"), new File("res/menu/multiplayer_pressed.png"));

		records = new Button((350 + offsetX), (445 + offsetY), 260, 100, true);
		records.set_image(new File("res/menu/records.png"), new File("res/menu/records_pressed.png"));
		
		settings = new Button((350 + offsetX), (565 + offsetY), 260, 100, true);
		settings.set_image(new File("res/menu/settings.png"), new File("res/menu/settings_pressed.png"));
	}
	
	/*
	 *  Sets a new value to the active variable.
	 */
	public void set_active (boolean state)
	{
		active = state;
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
	 * This function will update the logic game within the main menu.
	 */
	public int update (int cursorX, int cursorY, boolean clicked)
	{
		
		// Searching for mouse position.
		if((cursorX >= start.start_x) && (cursorX <= start.end_x) && (cursorY >= start.start_y) && (cursorY <= start.end_y))
		{ 
			start.set_state(1);
			multiplayer.set_state(0);
			records.set_state(0);
			settings.set_state(0);
			
			if (clicked == true)
			{
				this.active = false;
				return 1;
			}
		}
		else if((cursorX >= multiplayer.start_x) && (cursorX <= multiplayer.end_x) && (cursorY >= multiplayer.start_y) && (cursorY <= multiplayer.end_y))
		{
			start.set_state(0);
			multiplayer.set_state(1);
			records.set_state(0);
			settings.set_state(0);
			
			if (clicked == true)
			{
				this.active = false;
				return 2;
			}
		}
		else if((cursorX >= records.start_x) && (cursorX <= records.end_x) && (cursorY >= records.start_y) && (cursorY <= records.end_y))
		{
			start.set_state(0);
			multiplayer.set_state(0);
			records.set_state(1);
			settings.set_state(0);
			
			if (clicked == true)
			{
				this.active = false;
				return 3;
			}
		}
		else if((cursorX >= settings.start_x) && (cursorX <= settings.end_x) && (cursorY >= settings.start_y) && (cursorY <= settings.end_y))
		{
			start.set_state(0);
			multiplayer.set_state(0);
			records.set_state(0);
			settings.set_state(1);
			
			if (clicked == true)
			{
				this.active = false;
				return 4;
			}
		}
		else
		{
			start.set_state(0);
			multiplayer.set_state(0);
			records.set_state(0);
			settings.set_state(0);
		}
		
		return 0;
	}
	
	/*
	 * This function will render all images within the main menu respecting printing order.
	 */
	public void render (Graphics2D graphics)
	{
		graphics.drawImage(background, offsetX, offsetY, null);
		start.render(graphics);
		multiplayer.render(graphics);
		records.render(graphics);
		settings.render(graphics);
		
		return;
	}
}
