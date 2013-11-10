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
public class Button extends Rectangle
{

	private static final long serialVersionUID = 3L;

	/** Vetor para armazenamento das imagens do bot�o*/
	private BufferedImage imagem[];
	
	/** Estado do bot�o. Sendo 0 o estado normal e 1 o estado pressionado. */
	private int estado;
	
	/** Visibilidade de bot�o */
	private boolean visibilidade;
	
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
	}
	
	/**
	 * Este m�todo carrega as imagens do bot�o no vetor.
	 * @param imagem_0 the image[0] to set
	 * @param imagem_1 the image[1] to set
	 */
	public void set_image(File imagem_0, File imagem_1) throws IOException
	{
		this.imagem[0] = ImageIO.read(imagem_0);
		this.imagem[1] = ImageIO.read(imagem_1);
	}
	
	/**
	 * Este m�todo carrega apenas uma imagem do bot�o no vetor.
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
	 * Este m�todo define as coordenadas x e y do bot�o.
	 * @param x the x to set
	 * @param y the y to set
	 */
	public void setCoordinates(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Este m�todo renderiza a imagem do bot�o definida pelo seu estado no buffer.
	 * @param graphics o buffer onde a imagem ser� renderizada.
	 */
	public void render(Graphics graphics)
	{
		graphics.drawImage(imagem[estado], x, y, null);
	}
}
