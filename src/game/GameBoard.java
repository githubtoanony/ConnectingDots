package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameBoard 
{
	// Variable that control the time the menu shall be the active screen.
	private boolean active;
	
	// Window offset in pixels
	public final int offsetX;
	public final int offsetY;
		
	// Background images.
	private BufferedImage background;
	private BufferedImage dots;
	private BufferedImage grid[];
	
	// Warning images
	private Button warning_menu;
	
	// Buttons.
	private Button menu;
	private Button options;
	private Button continue_button;
	private Button cancel;
	
	// Blocks in the game
	private Block block[][];
	
	// Definition.
	private Rectangle vert_coll_detector[][];
	private Trace vertical_trace[][];
	private Rectangle hori_coll_detector[][];
	private Trace horizontal_trace[][];
	
	// Special points
	private Point last_selection;
	private Point current_selection;
	
	//Players
	private Player player[];
	
	// Defines who is the current player to play.
	private int current_player;
	
	/*
	 * Class constructor.
	 */
	public GameBoard(int newOffsetX, int newOffsetY) throws IOException
	{		
		active = false;
		
		offsetX = newOffsetX;
		offsetY = newOffsetY;
		
		background = ImageIO.read(new File("res/InGame/background.png"));
		dots = ImageIO.read(new File("res/InGame/dots.png"));
		grid = new BufferedImage[5];
		grid[0] = ImageIO.read(new File("res/InGame/grid_p0.png"));
		grid[1] = ImageIO.read(new File("res/InGame/grid_p1.png"));
		grid[2] = ImageIO.read(new File("res/InGame/grid_p2.png"));
		grid[3] = ImageIO.read(new File("res/InGame/grid_p3.png"));
		grid[4] = ImageIO.read(new File("res/InGame/grid_p4.png"));
		
		menu = new Button(offsetX, offsetY, 150, 30, true);
		menu.set_image(new File("res/InGame/menu.png"), new File("res/InGame/menu_pressed.png"));
		
		options = new Button((801 + offsetX), offsetY, 150, 30, true);
		options.set_image(new File("res/InGame/options.png"), new File("res/InGame/options_pressed.png"));
		
		warning_menu = new Button((330 + offsetX), (260 + offsetY), 320, 160, false);
		warning_menu.set_image(new File("res/InGame/Warning_menu.png"));
				
		continue_button = new Button((350 + offsetX), (385 + offsetY), 120,25, true);
		continue_button.set_image(new File("res/InGame/continue_button.png"), new File("res/InGame/continue_button_pressed.png"));
		
		cancel = new Button((510 + offsetX), (385 + offsetY), 120, 25, true);
		cancel.set_image(new File("res/InGame/cancel_button.png"), new File("res/InGame/cancel_button_pressed.png"));
		
		block = new Block[16][12];
		for (int i = 0; i < 16; i++) for (int j = 0; j < 12; j++) 
		{
			block[i][j] = new Block();
			block[i][j].set_coordinates((123 + (45 * i)), (115 + (45 *j)));
		}
		
		vertical_trace = new Trace[17][12];
		for (int R = 0; R < 17; R++) for (int C = 0; C < 12; C++) vertical_trace[R][C] = new Trace(); 
		
		vert_coll_detector = new Rectangle[17][12];
		for (int R = 0; R < 17; R++) { for (int C = 0; C < 12; C++) vert_coll_detector[R][C] = new Rectangle((115 + (45 * R)), (110 + 16 +(45 * C)), 17, 30); }
		
		horizontal_trace = new Trace[16][13];
		for (int R = 0; R < 16; R++) for (int C = 0; C < 13; C++) horizontal_trace[R][C] = new Trace();
		
		hori_coll_detector = new Rectangle[16][13];
		for (int R = 0; R < 16; R++) { for (int C = 0; C < 13; C++) hori_coll_detector[R][C] = new Rectangle((115 +16 + (45 * R)), (110 + (45 * C)), 30, 17); }
		
		player = new Player[4];
		for (int player_number = 0; player_number < 4; player_number++) player[player_number] = new Player(player_number);
		
		last_selection = new Point(0);
		current_selection = new Point(1);
		current_player = 0;
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
	public int update (int cursorX, int cursorY, boolean clicked) throws IOException
	{
		// Game over situation. *Need to fix draw situation*
		if ((player[0].get_score() + player[1].get_score() + player[2].get_score() + player[3].get_score()) == 192)
		{
			int winner = 1;
			for (int i = 1; i < 4; i++)
			{
				if (player[i].get_score() > player[i -1].get_score()) winner = (i + 1);
			}
			System.out.println("Game Over!\n Player " + winner + " won.");
			
		}
		
		// Number of player selection screen, after that start the game.
		if (current_player == 0 ) 
		{
			current_player = 1;
			return 0;
		}
		
		// Searching for mouse position when a warning is active
		if (warning_menu.get_visible()) return check_warnings(cursorX, cursorY, clicked);
		
		// Searching for mouse position inside the game board.
		if (cursorX >= 105 && cursorX <= 860 && cursorY >= 105 && cursorY <= 675) return check_inside(cursorX, cursorY, clicked);
		
		// Searching for mouse position outside the game board.
		if (cursorY <= 100) return check_outside(cursorX, cursorY, clicked);
		
		// Returning 0 means that it has to keep updating.
		return 0;
	}
	
	/*
	 * Searching for mouse position when a warning is active
	 */
	private int check_warnings(int cursorX, int cursorY, boolean clicked)
	{
		// If the cursor is hovering continue button
		if (cursorX >= continue_button.start_x && cursorX < continue_button.end_x && cursorY >= continue_button.start_y && cursorY < continue_button.end_y)
		{
			continue_button.set_state(1);
			cancel.set_state(0);
						
			if (clicked == true)
			{
				this.active = false;
				return 1;
			}
		}
		else if (cursorX >= cancel.start_x && cursorX < cancel.end_x && cursorY >= cancel.start_y && cursorY < cancel.end_y)
		{
			continue_button.set_state(0);
			cancel.set_state(1);
						
			if (clicked == true)
			{
				warning_menu.set_visible(false);
			}
		}
		else
		{
			continue_button.set_state(0);
			cancel.set_state(0);
		}
					
		return 0;
	}
	
	/*
	 * Searching for mouse position outside the game board.
	 */
	private int check_outside(int cursorX, int cursorY, boolean clicked)
	{
		current_selection.set_active(false);
		
		// If mouse hovers on menu button.
		if((cursorX >= menu.start_x) && (cursorX <= menu.end_x) && (cursorY >= menu.start_y) && (cursorY <= menu.end_y)) 
		{
			menu.set_state(1);
			options.set_state(0);
					
			if (clicked == true)
			{
				warning_menu.set_visible(true);
				//this.active = false;
				//return 1;
			}
		}
		// If mouse hovers on options button.
		else if((cursorX >= options.start_x) && (cursorX <= options.end_x) && (cursorY >= options.start_y) && (cursorY <= options.end_y)) 
		{
			options.set_state(1);
			menu.set_state(0);
					
			if (clicked == true)
			{
				// Show options menu
			}
		}
		else
		{
			menu.set_state(0);
			options.set_state(0);
		}
		
		return 0;
	}
	
	/*
	 * Searching for mouse position inside the game board.
	 */
	private int check_inside(int cursorX, int cursorY, boolean clicked) throws IOException
	{
		boolean block_closed = false;
		
		// If the cursor is inside the game board.
		if (cursorX >= 113 && cursorX < 850 && cursorY >= 108 && cursorY < 670)
		{
			int row, column;
			int orientation = 0;
							
			// Finding out the row and column.
			row = ((cursorX - 113) / 45);
			column = ((cursorY - 108) / 45);
							
			// Testing collision with the boundaries.
			if(row < 16 && column < 12)
			{
				if (vert_coll_detector[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 1);
					orientation = 1;
				}
				else if (hori_coll_detector[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 0);
					orientation = 0;
				}
				else current_selection.set_active(false);
			}
			else if (row == 16 && column < 12)
			{
				if (vert_coll_detector[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 1);
					orientation = 1;
				}
				else current_selection.set_active(false);
			}
			else if (row < 16 && column == 12)
			{
				if (hori_coll_detector[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 0);
					orientation = 0;
				}
				else current_selection.set_active(false);
			}
						
			// Finding out the blocks
			
			// If clicked
			if (clicked == true && current_selection.get_active() && current_player != 0)
			{
				if (orientation == 0)
				{
					if (!horizontal_trace[row][column].get_marked())
					{
						// Updating the trace.
						horizontal_trace[row][column].set_marked(true);
						switch (current_player)
						{
							case 1: horizontal_trace[row][column].set_image(new File("res/InGame/bar_hor_1.png")); break;
							case 2: horizontal_trace[row][column].set_image(new File("res/InGame/bar_hor_2.png")); break;
							case 3: horizontal_trace[row][column].set_image(new File("res/InGame/bar_hor_3.png")); break;
							case 4: horizontal_trace[row][column].set_image(new File("res/InGame/bar_hor_4.png")); break;
						}
						horizontal_trace[row][column].set_active(true);
						horizontal_trace[row][column].set_coordinates((119 + (45 * row)), (114 + (45 * column)));
						
						// Updating the block.
						if(row >= 0 && row < 16 && column > 0 && column < 12)
						{
							if (block[row][column].set_trace(2, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed |= true;
							}
							
							if (block[row][column - 1].set_trace(1, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed |= true;
							}
						}
						else if(row < 16 && column == 0 && column < 12)
						{
							if (block[row][column].set_trace(2, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed = true;	
							}
						}
						else if (row == 0 && column == 0)
						{
							if (block[row][column].set_trace(2, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed = true;
							}
						}
						else if (row < 16 && column == 12)
						{
							if (block[row][column - 1].set_trace(1, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed = true;
							}
						}
					}
					else return 0;
				}
				else if (orientation == 1)
				{
					if (!vertical_trace[row][column].get_marked())
					{
						// Updating the trace.
						vertical_trace[row][column].set_marked(true);
						switch (current_player)
						{
							case 1: vertical_trace[row][column].set_image(new File("res/InGame/bar_ver_1.png")); break;
							case 2: vertical_trace[row][column].set_image(new File("res/InGame/bar_ver_2.png")); break;
							case 3: vertical_trace[row][column].set_image(new File("res/InGame/bar_ver_3.png")); break;
							case 4: vertical_trace[row][column].set_image(new File("res/InGame/bar_ver_4.png")); break;
						}
						vertical_trace[row][column].set_active(true);
						vertical_trace[row][column].set_coordinates((119 + (45 * row)), (116 +(45 * column)));
						
						// Updating the block.
						if(row > 0 && row < 16 && column >= 0 && column < 12)
						{
							if (block[row][column].set_trace(3, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed |= true;
							}
							
							if (block[row - 1][column].set_trace(4, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed |= true;
							}
						}
						else if(row == 0 && row < 16 && column < 12)
						{
							if (block[row][column].set_trace(3, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed = true;
							}
						}
						else if (row == 0 && column == 0)
						{
							if (block[row][column].set_trace(3, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed = true;
							}
						}
						else if (row == 16 && column < 12)
						{
							if (block[row - 1][column].set_trace(4, current_player))
							{
								player[current_player - 1].set_score(1);
								block_closed = true;
							}
						}
					}
					else return 0;
			}
			
				// After the click, change the player turn.
				current_selection.set_active(false);
				
				if (!last_selection.get_active()) last_selection.set_active(true);
				last_selection.set_coordinates(row, column, orientation);
				
				if (block_closed == true) current_player--;
							
				current_player = ((current_player) % 4) + 1;
				current_selection.set_state(current_player);
			}
		}
		else // Otherwise, if the cursor is outside the game board.
		{
			current_selection.set_active(false);
		}
		
		return 0;
	}
	
	/*
	 * This function will render all images within the main menu respecting printing order.
	 */
	public void render (Graphics2D graphics)
	{
		// Render the background.
		graphics.drawImage(background, offsetX, offsetY, null);
		
		// Render the players.
		for (int i = 0; i < 4; i++) player[i].render(graphics);
		
		//Render the blocks.
		for (int R = 0; R < 16; R++) for (int C = 0; C < 12; C++) block[R][C].render(graphics);
		
		// Render the traces.
		for (int R = 0; R < 16; R++) for (int C = 0; C < 13; C++) horizontal_trace[R][C].render(graphics);
		for (int R = 0; R < 17; R++) for (int C = 0; C < 12; C++) vertical_trace[R][C].render(graphics);
		
		// Render the dots on the board.
		graphics.drawImage(dots, (96 + offsetX), (68 + offsetY), null);
		
		// Render the grid related to the current player.
		graphics.drawImage(grid[current_player], (96 + offsetX), (68 + offsetY), null);
		
		// Render the buttons.
		menu.render(graphics);
		options.render(graphics);
		
		// Render the last points of the last trace made.
		if(last_selection.get_active()) last_selection.render(graphics);
		
		// Render the points related to the current selection.
		if (current_selection.get_active()) current_selection.render(graphics);
		
		// Render the warning window if necessary.
		if (warning_menu.get_visible())
		{
			warning_menu.render(graphics);
			continue_button.render(graphics);
			cancel.render(graphics);
		}
		
		return;
	}
}
