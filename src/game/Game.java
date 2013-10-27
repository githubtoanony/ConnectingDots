package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game extends Frame implements Runnable, MouseListener
{
	// Definition
	public final int offsetY = 30;
	public final int offsetX = 3;
	
	// Definition
	public final int WIDTH = (960 + (2 * offsetX));
	
	// Definition
	public final int HEIGHT = (720 + offsetY + offsetX);
	
	// Definition
	public boolean running = false;
	
	// Used during deserialization to verify.
	private static final long serialVersionUID = 1L;
	
	// Definition   ?? Not in use
	//private JFrame frame;
	
	// Definition
	private BufferedImage buffer;
	
	// Definition
	private Graphics2D graphics;
	
	// Definition
	private Graphics2D g2d;
	
	// Game's icon image
	private Image icon;
	
	// Cursor coordinates
	private int cursorX;
	private int cursorY;
	
	// Window coordinates
	private int windowX;
	private int windowY;
	
	// Flag that warns when the mouse was clicked.
	private boolean clicked;
	
	// Counts the updates.
	private int ticks;
			
	// Counts the frames.
	private int frames;
	
	// Main menu in game.
	Menu menu;
	
	// Definition
	GameBoard gameBoard;
	
	/*
	 * Class constructor
	 */
	public Game() throws IOException 
	{  
		menu = new Menu(offsetX, offsetY);
		menu.set_active(true);
		
		gameBoard = new GameBoard(offsetX, offsetY);
		gameBoard.set_active(false);
		
		windowX = getX();
		windowY = getY();
		
		setTitle("Connecting Dots");
	    setSize(WIDTH, HEIGHT);
	    setVisible(true);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = buffer.createGraphics();
		
	    addWindowListener(new CloseWindow());
	    
	    // Load an image for the icon.
	    icon = ImageIO.read(new File("res/General/icon.png"));
	    
	    // Set the custom icon.
	    setIconImage(icon);
	    
	    // Get the default toolkit.
	    Toolkit toolkit = Toolkit.getDefaultToolkit();  
	    
	    // Load an image for the cursor.
	    Image cursor_image = toolkit.getImage("res/General/cursor_p1.png");
	    
	    // Create the hotspot for the cursor.
	    Point hotSpot = new Point(0,0);  
	    
	    // Create the custom cursor.
	    Cursor cursor = toolkit.createCustomCursor(cursor_image, hotSpot, "Pencil"); 
	    
	    // Set the custom cursor.
	    setCursor(cursor);   
	    
	    // Adding the mouse listener.
	    addMouseListener(this);
	    
	    clicked = false;
	}
	
	/*
	 * Main function.
	 */
	public static void main (String[] args) throws IOException
	{
		Game game = new Game();
		game.start();
	}
	
	/*
	 * Starting game method.
	 */
	public synchronized void start()
	{
		// Setting running state to true.
		running = true;
		
		// Creating a thread that is an instance of Runnable, running automatically the run method.
		new Thread(this).start();
	}
	
	/*
	 * Stopping game method.
	 */
	public synchronized void stop()
	{
		// Setting running state to false.
		running = false;
		return;
	}

	/*
	 * Definition
	 */
	public void paint(Graphics g)
	{
		g2d = (Graphics2D) g;
		return;
	}
	
	/*
	 * Definition
	 */
	public void run() 
	{
		// Gets the current time within nano precision.
		long last_time = System.nanoTime();
		
		// Variable that stores how many nano-seconds are in one tick.
		double ns_per_tick = (1000000000D/60D);
		
		// Last time measured.
		long last_timer = System.currentTimeMillis();
		
		// How many unprocessed nanoseconds had gone by so far.
		double delta = 0;
		
		ticks = 0;
		frames = 0;
		
		while(running)
		{
			// The current time checked against the last time.
			long now = System.nanoTime();
			
			// Indicates weather or not the frame should be rendered.
			boolean ready_to_render = false;
			
			delta += ((now - last_time) / ns_per_tick);
			last_time = now;
			
			// Making magic to keep a maximum of 60 fps.
			while(delta >= 1)
			{
				ticks++;
				try { tick(); } catch (IOException e) { e.printStackTrace(); }
				delta -= 1;
				ready_to_render = true;
			}
			
			// If it's time to render then update the frame counter and render.
			if (ready_to_render)
			{
				frames++;
				render();
			}
			
			// After one second reset the counters.
			if ((System.currentTimeMillis() - last_timer) >= 1000)
			{
				//System.out.println("Frames: " + frames + " Updates: " + ticks);
				setTitle("Connecting Dots | fps: " + frames + " ups: " + ticks);
				last_timer += 1000;
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	/*
	 * Definition
	 */
	public void update(Graphics g)
	{
		g2d = (Graphics2D) g;
		g2d.drawImage(buffer, 0, 0, null);
	}
	
	/*
	 * This function will update the logic of the game.
	 */
	public void tick () throws IOException
	{
		// Updating the window's coordinates.
		windowX = getX();
		windowY = getY();
		
		// Updating the mouse's coordinates.
		cursorX = (MouseInfo.getPointerInfo().getLocation().x - windowX);         
		cursorY = (MouseInfo.getPointerInfo().getLocation().y) - windowY;
		
		System.out.println("X: " + cursorX + " Y: " + cursorY);
		
		// Update menu screen if active.
		if (menu.get_active()) 
		{
			// Select a new screen to show based on chosen option in menu screen.
			if (menu.update(cursorX, cursorY, clicked) == 1) gameBoard.set_active(true);		// Start game
			else if (menu.update(cursorX, cursorY, clicked) == 2) gameBoard.set_active(true);	// Start multiplayer game
			//else if (menu.update(cursorX, cursorY, clicked) == 3) records.set_active(true);	// Show records
			//else if (menu.update(cursorX, cursorY, clicked) == 4) settings.set_active(true);	// Show settings
		}
		
		// Update in game screen if active.
		if (gameBoard.get_active()) 
			if (gameBoard.update(cursorX, cursorY, clicked) == 1) 
			{
				menu.set_active(true);
				gameBoard = new GameBoard(offsetX, offsetY);
			}
		
		clicked = false;
	}
	
	/*
	 * This function will print out the frames.
	 */
	public void render()
	{	    
		graphics.setPaint(Color.WHITE);
		
		// Render menu screen if active.
		if (menu.get_active()) menu.render(graphics);
		
		// Render in game screen if active.
		if (gameBoard.get_active()) gameBoard.render(graphics);
		
		this.repaint();
	}

	/*
	 * Java Mouse Events
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) { clicked = true; }

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
