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

/**
 *   Esta é a classe principal do jogo. Suas atribuições principais são à inicialização
 * do programa pela classe main, e a execução do loop de lógica e loop de renderização.
 * 
 * @author      <a href="mailto:marceloatg@outlook.com"> Marcelo A. T. Gomes</a>
 * @version     1.0.0               
 * @since       2013-10-23  
 *
 */
public class Game extends Frame implements Runnable, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	/** Ofsset horizontal da janela do programa. */
	public static final int offsetX = 3;
	
	/** Ofsset vertical da janela do programa. */
	public static final int offsetY = 30;
	
	/** Largura da janela do programa. */
	public static final int WIDTH = (960 + (2 * offsetX));
	
	/** Altura da janela do programa. */
	public static final int HEIGHT = (720 + offsetY + offsetX);
	
	/** Estado de execução do programa. */
	public boolean running = false;
	
	/** Buffer onde os elementos gráficos são preparados para renderização. */
	private BufferedImage buffer;
	
	/** Elemento gráfico onde o buffer será renderizado. */
	private Graphics2D graphics;
	
	/** Elemento gráfico auxiliar. */
	private Graphics2D g2d;
	
	/** Imagem contendo ícone do programa. */
	private Image icon;
	
	/** Armazena posição horizontal do cursor. */
	private int cursorX;
	
	/** Armazena posição vertical do cursor. */
	private int cursorY;
	
	/** Armazena posição horizontal da janela do programa. */
	private int windowX;
	
	/** Armazena posição vertical da janela do programa. */
	private int windowY;
	
	/** Flag que simboliza o clique do mouse*/
	private boolean clicked;
	
	/** Armazena a quantidade de atualizações feitas. */
	private int ticks;
			
	/** Armazena a quantidade de frames renderizados. */
	private int frames;
	
	/** Menu principal do jogo. */
	private Menu menu;
	
	/** Tabuleiro do jogo. */
	private GameBoard gameBoard;
	
	/**
	 * Construtor da classe
	 */
	public Game() throws IOException 
	{  
		// Instanciando menu principal.
		menu = new Menu(offsetX, offsetY);
		menu.set_active(true);
		
		// Instanciando tabuleiro.
		gameBoard = new GameBoard(offsetX, offsetY);
		gameBoard.set_active(false);
		
		// Recuperando coordenadas da janela do programa.
		windowX = getX();
		windowY = getY();
		
		
		// Definindo propriedades do frame do programa.
		setTitle("Connecting Dots");
	    setSize(WIDTH, HEIGHT);
	    setVisible(true);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    addWindowListener(new CloseWindow());
	    
	    // Instanciando variáveis gráficas.
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = buffer.createGraphics();
	    
	    // Criando ícone personalizado
	    icon = ImageIO.read(new File("res/General/icon.png"));
	    setIconImage(icon);
	    
	    // Criando cursor personalizado.
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image cursor_image = toolkit.getImage("res/General/cursor_p1.png");
	    Point hotSpot = new Point(0,0);
	    Cursor cursor = toolkit.createCustomCursor(cursor_image, hotSpot, "Pencil");
	    setCursor(cursor);   
	    
	    // Adicionando o mouse listener.
	    addMouseListener(this);
	    
	    // Inicializando a variável clicked.
	    clicked = false;
	}
	
	 /**
	  * Método de inicialização da thread.
	  */
	public synchronized void start()
	{
		// Testando estado do programa.
	 	if (running) return;
	 	
	 	// Definindo o estado de running como verdadeiro.
	 	running = true;
	 	
	 	// Instanciando e inicializando o objeto thread.
	 	new Thread(this).start();
	 	
	 	// Mensagem de controle.
	 	System.out.println("Started.");
	}
	
	/**
	 * Método de encerramento da thread.
	 */
	public synchronized void stop() throws InterruptedException
	{
		// Testando estado do programa.
		if (!running) return;
		
		// Definindo o estado de running como falso.
		running = false;
	  
		//Mensagem de controle.
		System.out.println("Stoped.");
	}

	/*
	 * Método gráfico obrigatório.
	 */
	public void paint(Graphics g)
	{
		g2d = (Graphics2D) g;
	}
	
	/*
     * Método de execução dos loops principais do programa.
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
	 * Método gráfico obrigatório.
	 */
	public void update(Graphics g)
	{
		g2d = (Graphics2D) g;
		g2d.drawImage(buffer, 0, 0, null);
	}
	
	/**
     * Método de atualização da lógica do jogo.
     */ 
	void tick () throws IOException
	{
		// Updating the window's coordinates.
		windowX = getX();
		windowY = getY();
		
		// Updating the mouse's coordinates.
		cursorX = (MouseInfo.getPointerInfo().getLocation().x - windowX);         
		cursorY = (MouseInfo.getPointerInfo().getLocation().y) - windowY;
		
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
	
	/**
	 *  Método de renderização do jogo.
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
	
	/**
	 * Função Main.
	 */
	public static void main (String[] args) throws IOException
	{
		Game game = new Game();
		game.start();
	}
}
