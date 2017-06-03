package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

public class main 
{	
	public static void main(String[] args) 
	{	
		int nb_col=21;
		int nb_line=17;
		int size_x=25;
		int size_y=25;
		int nb_joueur=2;
		
		int i=0;
		int j=0;
		
		

		StdDraw.setCanvasSize (size_x*nb_col*2,size_y*nb_line*2);
	
		StdDraw.setXscale(0, size_x*nb_col*2);
		StdDraw.setYscale(0, size_y*nb_line*2);
		
		StdDraw.show(0);

		Terrain terrain=new Terrain(nb_col,nb_line,size_x,size_y);
		Joueur [] joueur=new Joueur [nb_joueur] ;
		
		int idJoueur;
		int positionX;
		int positionY;
		for (i=0;i<nb_joueur;i++)
		{	
			idJoueur = i;
			positionX=0;
			positionY=0;
			if(idJoueur==0)
			{
				positionX=3*size_x;
				positionY=3*size_y;
			}
			else if(idJoueur==1)
			{
				positionX=(nb_col*2*size_x)-(3*size_x);
				positionY=(nb_line*2*size_y)-(3*size_y);
			}
			joueur [i]=new Joueur(terrain,idJoueur, positionX, positionY);
		}
		
		terrain.draw_all(joueur);

		while (noPlayerIsDead(joueur))
		{	
			i=0;
			j=0;
		
			move2 (joueur, terrain);
			
			
			for (i=0; i<nb_joueur; i++)
			{	terrain=joueur[i].bon_deg(terrain);
				for (j=0; j<joueur[i].getnbbombe();j++)
					terrain=joueur[i].bombe[j].gestion(terrain);
			}
		
			terrain.draw_all (joueur);
			
			
			StdDraw.show();
			sleep (5);
		}
		
		displayGameOver(joueur, terrain);
	}

	
	public static void sleep (int mili)
	{	long time=java.lang.System.currentTimeMillis() ;
	
		while (java.lang.System.currentTimeMillis()-time<mili);
	}
	

	public static void move2 (Joueur [] joueur, Terrain terrain)
	{	
	
		if (StdDraw.isKeyPressed(KeyEvent.VK_Z))
		{	joueur[0].move("up",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_Q))
		{	joueur[0].move("left",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_S))
		{	joueur[0].move("down",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_D))
		{	joueur[0].move("right",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_A))
		{	terrain=joueur[0].put_bombe(terrain);
			
		}
		
		if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
		{	joueur[1].move("up",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
		{	joueur[1].move("left",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
		{	joueur[1].move("down",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
		{	joueur[1].move("right",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER))
		{	
			terrain=joueur[1].put_bombe(terrain);
		}
	}

	
	public static boolean noPlayerIsDead(Joueur[] joueur)
	{
		for (int i = 0 ; i < joueur.length ; i++)
		{
			if(joueur[i].getlife()<=0)
			{
				return false;
			}
		}
		return true;
	}
	
	public static void displayGameOver(Joueur[] joueur, Terrain terrain)
	{
		String joueurGagnant;
		if(joueur[0].getlife()<=0)
		{
			joueurGagnant = "Joueur2" ;
		}
		else
		{
			joueurGagnant="Joueur1";

		}
		
		if (joueurGagnant=="Joueur1")
		{
			StdDraw.picture(terrain.getwidth()*2*11, terrain.getheigth()*2*8, "FinJ1.png", 500, 300);
			StdDraw.picture(terrain.getwidth()*2*10, terrain.getheigth()*2*5.8, "Rejouer.png", 100, 50);
			Audio sound = new Audio("Violin");
		}
		else if (joueurGagnant=="Joueur2")
		{
			
			StdDraw.picture(terrain.getwidth()*2*11, terrain.getheigth()*2*8, "FinJ2.png", 500, 300);
			StdDraw.picture(terrain.getwidth()*2*10, terrain.getheigth()*2*5.8, "Rejouer.png", 100, 50);
			Audio sound = new Audio("Hello");
			
		}
		
		
		StdDraw.show();
		
		while(true)
		{
			if(StdDraw.mousePressed())
			{
				if(StdDraw.mouseX()>terrain.getwidth()*2*10-100
				&& StdDraw.mouseX()<terrain.getwidth()*2*10+100
				&& StdDraw.mouseY()>terrain.getheigth()*2*5.8-50
				&& StdDraw.mouseY()<terrain.getheigth()*2*5.8+50)
					
				{
					System.out.println("Clicked");
					main(null);
				}
			}
		}
		

	}
}