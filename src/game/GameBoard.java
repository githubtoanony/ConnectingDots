package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;
import javax.imageio.ImageIO;
import components.Block;
import components.Block_Storage;
import components.Button;
import components.Player;
import components.Point;
import components.Trace;

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
	
	// Number of players selection
	private Button player_selection_background;
	private Button _2players;
	private Button _3players;
	private Button _4players;
	
	// Player image
	private Button player_3;
	private Button player_4;
	
	// Warning images
	private Button warning_menu;
	
	// Buttons.
	private Button menu;
	private Button options;
	private Button continue_button;
	private Button cancel;
	
	// Blocks in the game
	private Block block[][];
	
	// List of block storages
	private Vector<LinkedList <Block_Storage>> traces_list;
	
	// Definition.
	private Trace horizontal_trace[][];
	private Trace vertical_trace[][];
	
	// Special points
	private Point last_selection;
	private Point current_selection;
	
	//Players
	private Player player[];
	private int number_of_players;
	
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
		
		player_selection_background = new Button(offsetX, offsetY, 960, 720, false); 
		player_selection_background.set_image(new File("res/InGame/player_number.png"));
		
		_2players = new Button((25 +offsetX), (600 + offsetY), 283, 108, false);
		_2players.set_image(new File("res/InGame/2p.png"), new File("res/InGame/2ps.png"));
		
		_3players = new Button((342 + offsetX), (600 + offsetY), 283, 108, false);
		_3players.set_image(new File("res/InGame/3p.png"), new File("res/InGame/3ps.png"));
		
		_4players = new Button((655 + offsetX), (600 + offsetY), 283, 108, false);
		_4players.set_image(new File("res/InGame/4p.png"), new File("res/InGame/4ps.png"));
		
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
		
		traces_list = new Vector<LinkedList<Block_Storage>>();
		for (int i = 0; i < 5; i++) traces_list.add(i, new LinkedList<Block_Storage>());
		for (int i = 0; i < 192; i++) traces_list.get(0).add(new Block_Storage());
		
		int aux = 0;
		for (int j = 0; j < 16; j++) 
			for (int k = 0; k < 12; k++)
			{
				traces_list.get(0).set(aux, new Block_Storage(j, k));
				aux++;
			}

		block = new Block[16][12];
		for (int i = 0; i < 16; i++) for (int j = 0; j < 12; j++) block[i][j] = new Block(i, j);
		
		horizontal_trace = new Trace[16][13];
		for (int R = 0; R < 16; R++) for (int C = 0; C < 13; C++) horizontal_trace[R][C] = new Trace(0, R, C);
		
		vertical_trace = new Trace[17][12];
		for (int R = 0; R < 17; R++) for (int C = 0; C < 12; C++) vertical_trace[R][C] = new Trace(1, R, C);
		
		number_of_players = 0;
		
		player_3 = new Button(offsetX, (95 + offsetY), 0, 0, false);
		player_3.set_image(new File("res/InGame/P3.png"));
		
		player_4 = new Button((889 + offsetX), (100 + offsetY), 0, 0, false);
		player_4.set_image(new File("res/InGame/P4.png"));
		
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
		// Number of player selection screen, skip one frame and then start the game.
		if (number_of_players == 0 ) return set_number_players(cursorX, cursorY, clicked);

		// If the player is not human execute the AI
		if (!player[current_player - 1].get_human_player()) return AI_move();
				
		// Searching for mouse position when a warning is active
		if (warning_menu.isVisible()) return check_warnings(cursorX, cursorY, clicked);
		
		// Searching for mouse position inside the game board.
		if (cursorX >= 105 && cursorX <= 860 && cursorY >= 105 && cursorY <= 675) return check_inside(cursorX, cursorY, clicked);
		
		// Searching for mouse position outside the game board.
		else return check_outside(cursorX, cursorY, clicked);
	}
	
	/*
	 * Select the number of players and instantiates the players variable
	 */
	private int set_number_players (int cursorX, int cursorY, boolean clicked) throws IOException
	{
		current_player = 1;
		player_selection_background.setVisible(true);
		
		if (_2players.contains(cursorX, cursorY))
		{
			_2players.setState(1);
			_3players.setState(0);
			_4players.setState(0);
			
			if (clicked)
			{
				number_of_players = 2;
				player = new Player[number_of_players];
				for (int player_number = 0; player_number < number_of_players; player_number++) player[player_number] = new Player(player_number);
				for (int aux = 1; aux < number_of_players; aux++) player[aux].set_human_player(false);
				player_selection_background.setVisible(false);
				
				return 0;
			}
		}
		else if (_3players.contains(cursorX, cursorY))
		{
			_2players.setState(0);
			_3players.setState(1);
			_4players.setState(0);
			
			if (clicked)
			{
				player_3.setVisible(true);
				
				number_of_players = 3;
				player = new Player[number_of_players];
				for (int player_number = 0; player_number < number_of_players; player_number++) player[player_number] = new Player(player_number);
				for (int aux = 1; aux < number_of_players; aux++) player[aux].set_human_player(false);
				player_selection_background.setVisible(false);
				
				return 0;
			}
		}
		else if (_4players.contains(cursorX, cursorY))
		{
			_2players.setState(0);
			_3players.setState(0);
			_4players.setState(1);
			
			if (clicked)
			{
				player_3.setVisible(true);
				player_4.setVisible(true);
				
				number_of_players = 4;
				player = new Player[number_of_players];
				for (int player_number = 0; player_number < number_of_players; player_number++) player[player_number] = new Player(player_number);
				for (int aux = 0; aux < number_of_players; aux++) player[aux].set_human_player(false);
				player_selection_background.setVisible(false);
				
				return 0;
			}
		}
		else
		{
			_2players.setState(0);
			_3players.setState(0);
			_4players.setState(0);
			return 0;
		}
		
		return 0;
	}
	
	/*
	 * Searching for mouse position when a warning is active
	 */
	private int check_warnings(int cursorX, int cursorY, boolean clicked)
	{
		// If the cursor is hovering continue button
		if (continue_button.contains(cursorX, cursorY))
		{
			continue_button.setState(1);
			cancel.setState(0);
						
			if (clicked == true)
			{
				this.active = false;
				return 1;
			}
		}
		else if (cancel.contains(cursorX, cursorY))
		{
			continue_button.setState(0);
			cancel.setState(1);
						
			if (clicked == true)
			{
				warning_menu.setVisible(false);
			}
		}
		else
		{
			continue_button.setState(0);
			cancel.setState(0);
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
		if(menu.contains(cursorX, cursorY)) 
		{
			menu.setState(1);
			options.setState(0);
					
			if (clicked == true)
			{
				warning_menu.setVisible(true);
				//this.active = false;
				//return 1;
			}
		}
		// If mouse hovers on options button.
		else if(options.contains(cursorX, cursorY)) 
		{
			options.setState(1);
			menu.setState(0);
					
			if (clicked == true)
			{
				// Show options menu
			}
		}
		else
		{
			menu.setState(0);
			options.setState(0);
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
				if (vertical_trace[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 1);
					orientation = 1;
				}
				else if (horizontal_trace[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 0);
					orientation = 0;
				}
				else current_selection.set_active(false);
			}
			else if (row == 16 && column < 12)
			{
				if (vertical_trace[row][column].contains(cursorX, cursorY)) 
				{
					current_selection.set_active(true);
					current_selection.set_coordinates(row, column, 1);
					orientation = 1;
				}
				else current_selection.set_active(false);
			}
			else if (row < 16 && column == 12)
			{
				if (horizontal_trace[row][column].contains(cursorX, cursorY)) 
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
						horizontal_trace[row][column].set_marked(current_player);
						
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
						vertical_trace[row][column].set_marked(current_player);
						
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
							
				current_player = ((current_player) % number_of_players) + 1;
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
	 * Movement of artificial intelligence 
	 */
	private int AI_move () throws IOException
	{
		int row = 0;
		int column = 0;
		int orientation = -1;
		boolean block_closed = false;
		Block_Storage storage = new Block_Storage();
		
		// Updating the lists
		for (int alpha = 3; alpha >= 0; alpha--)
		{
			if (!traces_list.get(alpha).isEmpty()) 
			{
				int size = traces_list.get(alpha).size();
				for (int i = 0; i < size; i++)
				{
					storage = traces_list.get(alpha).get(i);
					row = storage.row;
					column = storage.collum;
					
					if (block[row][column].get_traces() == (alpha + 1)) 
					{
						traces_list.get(alpha).remove(storage);
						traces_list.get(alpha + 1).add(storage);
						i--;
						size--;
					}
				}
			}
		}
	
		// Choosing the block to mark
		if (!traces_list.get(3).isEmpty()) 
		{
			storage = traces_list.get(3).get(0);
			row = storage.row;
			column = storage.collum;
		}
		
		
		if (!last_selection.get_active()) last_selection.set_active(true);
		last_selection.set_coordinates(row, column, orientation);
		
		if (block_closed == true) current_player--;
					
		System.out.println("I choose block[" + row + "][" + column + "]");
			
		current_player = ((current_player) % number_of_players) + 1;
		current_selection.set_state(current_player);
		
		System.out.println("0: " + traces_list.get(0).size() + " 1: "+ traces_list.get(1).size() + " 2: "+ traces_list.get(2).size() + " 3: " + traces_list.get(3).size() + " 4: " + traces_list.get(4).size());
		
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
		if (player_3.isVisible()) player_3.render(graphics);
		if (player_4.isVisible()) player_4.render(graphics);
		for (int i = 0; i < number_of_players; i++) player[i].render(graphics);
		
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
		if (warning_menu.isVisible())
		{
			warning_menu.render(graphics);
			continue_button.render(graphics);
			cancel.render(graphics);
		}
		
		// Render number of players selection screen if necessary
		if (player_selection_background.isVisible()) 
		{
			player_selection_background.render(graphics);
			_2players.render(graphics);
			_3players.render(graphics);
			_4players.render(graphics);
		}
		
		return;
	}
}
