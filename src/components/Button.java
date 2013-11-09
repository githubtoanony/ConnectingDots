package components;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Marcelo A. T. Gomes
 *
 */
public class Button {

	/** Vetor para armazenamento das imagens do botão*/
	private BufferedImage imagem[];
	
	/** Estado do botão. Sendo 0 o estado normal e 1 o estado pressionado. */
	private int estado;
	
	/** Visibilidade de botão */
	private boolean visibilidade;
	
	/** Coordenada horizontal */ 
	private int x;
	
	/** Coordenada vertical */
	private int y;
	
	/** Largura da imagem */
	private int width;
	
	/** Altura da imagem */
	private int height;
	
	/** Retângulo usado para o cálculo de colisão */
	private Rectangle retangulo;
	
	/**
	 * Construtor da classe.
	 */
	public Button (int x, int y, int width, int height, boolean visibilidade)
	{
		// Atribuindo valores iniciais ao objeto.
		imagem = new BufferedImage[2];
		estado = 0;
		
		// Atribuindo valores recebidos externamente.
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.visibilidade = visibilidade;
		
		retangulo = new Rectangle(x, y, width, height);
	}
	
	/**
	 * Este método carrega as imagens do botão no vetor.
	 * @param imagem_0 the image[0] to set
	 * @param imagem_1 the image[1] to set
	 */
	public void set_image(File imagem_0, File imagem_1) throws IOException
	{
		this.imagem[0] = ImageIO.read(imagem_0);
		this.imagem[1] = ImageIO.read(imagem_1);
	}
	
	/**
	 * Este método carrega apenas uma imagem do botão no vetor.
	 * @param imagem the image[0] to set
	 */
	public void set_image(File imagem) throws IOException
	{
		this.imagem[0] = ImageIO.read(imagem);
	}
	
	/**
	 * @return the state
	 */
	public int getState() 
	{
		return estado;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(int estado) 
	{
		this.estado = estado;
	}
	
	/**
	 * @return the visibilidade
	 */
	public boolean isVisible() 
	{
		return visibilidade;
	}
	
	/**
	 * @param visibilidade the visibilidade to set
	 */
	public void setVisible(boolean visibilidade) 
	{
		this.visibilidade = visibilidade;
	}
	
	/**
	 * @return the x
	 */
	public int getX() 
	{
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) 
	{
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() 
	{
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY(int y) 
	{
		this.y = y;
	}
	
	/**
	 * Este método define as coordenadas x e y do botão.
	 * @param x the x to set
	 * @param y the y to set
	 */
	public void setCoordinates(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() 
	{
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) 
	{
		this.height = height;
	}

	/**
	 * Este método renderiza a imagem do botão definida pelo seu estado no buffer.
	 * @param graphics o buffer onde a imagem será renderizada.
	 */
	public void render(Graphics graphics)
	{
		graphics.drawImage(imagem[estado], x, y, null);
		return;
	}
	
	/**
	 * Este método retorna veradadeiro caso o ponto indicado esteja contido dentro do
	 * botão e falso caso contrário.
	 * @param x é a coordenada horizontal do ponto a ser verificado
	 * @param y é a coordenada vertical do ponto a ser verificado
	 */
	public boolean contem(int cursorX, int cursorY)
	{
		return retangulo.contains(cursorX, cursorY);
	}
}
