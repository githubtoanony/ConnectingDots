package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {

	private BufferedImage score_ten_image;
	private BufferedImage score_unit_image;
	private int number;
	private int score;
	private int score_ten_x;
	private int score_ten_y;
	private int score_unit_x;
	private int score_unit_y;
	
	/*
	 * Class constructor.
	 */
	public Player (int player_number) throws IOException
	{
		number = player_number;
		this.set_score(0);
	}

	/*
	 * This function sets the new score and updates the score's image.
	 */
	public void set_score (int newScore) throws IOException
	{
		score += newScore;
		
		if (score > 9)
		{
			switch(score % 10)
			{
			case 0: score_unit_image = ImageIO.read(new File("res/InGame/00.png")); break;
			case 1: score_unit_image = ImageIO.read(new File("res/InGame/01.png")); break;
			case 2: score_unit_image = ImageIO.read(new File("res/InGame/02.png")); break;
			case 3: score_unit_image = ImageIO.read(new File("res/InGame/03.png")); break;
			case 4: score_unit_image = ImageIO.read(new File("res/InGame/04.png")); break;
			case 5: score_unit_image = ImageIO.read(new File("res/InGame/05.png")); break;
			case 6: score_unit_image = ImageIO.read(new File("res/InGame/06.png")); break;
			case 7: score_unit_image = ImageIO.read(new File("res/InGame/07.png")); break;
			case 8: score_unit_image = ImageIO.read(new File("res/InGame/08.png")); break;
			case 9: score_unit_image = ImageIO.read(new File("res/InGame/09.png")); break;
			}
			
			switch(score / 10)
			{
			case 1: score_ten_image = ImageIO.read(new File("res/InGame/01.png")); break;
			case 2: score_ten_image = ImageIO.read(new File("res/InGame/02.png")); break;
			case 3: score_ten_image = ImageIO.read(new File("res/InGame/03.png")); break;
			case 4: score_ten_image = ImageIO.read(new File("res/InGame/04.png")); break;
			case 5: score_ten_image = ImageIO.read(new File("res/InGame/05.png")); break;
			case 6: score_ten_image = ImageIO.read(new File("res/InGame/06.png")); break;
			case 7: score_ten_image = ImageIO.read(new File("res/InGame/07.png")); break;
			case 8: score_ten_image = ImageIO.read(new File("res/InGame/08.png")); break;
			case 9: score_ten_image = ImageIO.read(new File("res/InGame/09.png")); break;
			}
		}
		else
		{
			switch(score)
			{
			case 0: score_unit_image = ImageIO.read(new File("res/InGame/00.png")); break;
			case 1: score_unit_image = ImageIO.read(new File("res/InGame/01.png")); break;
			case 2: score_unit_image = ImageIO.read(new File("res/InGame/02.png")); break;
			case 3: score_unit_image = ImageIO.read(new File("res/InGame/03.png")); break;
			case 4: score_unit_image = ImageIO.read(new File("res/InGame/04.png")); break;
			case 5: score_unit_image = ImageIO.read(new File("res/InGame/05.png")); break;
			case 6: score_unit_image = ImageIO.read(new File("res/InGame/06.png")); break;
			case 7: score_unit_image = ImageIO.read(new File("res/InGame/07.png")); break;
			case 8: score_unit_image = ImageIO.read(new File("res/InGame/08.png")); break;
			case 9: score_unit_image = ImageIO.read(new File("res/InGame/09.png")); break;
			}
		}
		
		if (score == 10)
		{
			switch (number)
			{
			case 0: score_ten_x = 394; score_ten_y = 705; score_unit_x = 427; score_unit_y = 705; break;
			case 1: score_ten_x = 443; score_ten_y =  29; score_unit_x = 477; score_unit_y =  29; break;
			case 2: score_ten_x = -20; score_ten_y = 325; score_unit_x = -20; score_unit_y = 365; break;
			case 3: score_ten_x = 895; score_ten_y = 372; score_unit_x = 895; score_unit_y = 412; break;
			}
		}
		else if (score == 0)
		{
			switch (number)
			{
			case 0: score_unit_x = 412; score_unit_y = 705; break;
			case 1: score_unit_x = 459; score_unit_y =  29; break;
			case 2: score_unit_x = -20; score_unit_y = 345; break;
			case 3: score_unit_x = 895; score_unit_y = 392; break;
			}
		}
		
		return;
	}

	/*
	 * This function will return the score value.
	 */
	public int get_score ()
	{
		return score;
	}
	
	/*
	 * This function will render all images within the main menu respecting printing order.
	 */
	public void render (Graphics2D graphics)
	{
		// Render the background.
		if (score > 9) 
		{
			graphics.drawImage(score_ten_image, score_ten_x, score_ten_y, null);
			graphics.drawImage(score_unit_image, score_unit_x, score_unit_y, null);
		}
		else
		{
			graphics.drawImage(score_unit_image, score_unit_x, score_unit_y, null);
		}
		
		return;
	}
}
