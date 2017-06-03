package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

public class main 
{	
	public static void main(String[] args) 
	{	
		int numberOfRow=21;
		int numberOfLine=17;
		int halfWidthOfRow=25;
		int halfHeigthOfLine=25;
		int numberOfPlayers=2;
		int i=0;
		int j=0;

		StdDraw.setCanvasSize(halfWidthOfRow*numberOfRow*2,halfHeigthOfLine*numberOfLine*2);
	
		StdDraw.setXscale(0, halfWidthOfRow*numberOfRow*2);
		StdDraw.setYscale(0, halfHeigthOfLine*numberOfLine*2);
		
		StdDraw.show(0);

		Terrain terrain=new Terrain(numberOfRow,numberOfLine,halfWidthOfRow,halfHeigthOfLine);
		Joueur [] joueur=new Joueur [numberOfPlayers] ;
		
		int idJoueur;
		int positionX;
		int positionY;
		for (i=0;i<numberOfPlayers;i++)
		{	
			idJoueur = i;
			positionX=0;
			positionY=0;
			if(idJoueur==0)
			{
				positionX=3*halfWidthOfRow;
				positionY=3*halfHeigthOfLine;
			}
			else if(idJoueur==1)
			{
				positionX=(numberOfRow*(halfWidthOfRow*2))-(3*halfWidthOfRow);
				positionY=(numberOfLine*(halfHeigthOfLine*2))-(3*halfHeigthOfLine);
			}
			joueur [i]=new Joueur(terrain,idJoueur, positionX, positionY);
		}
		
		terrain.draw(joueur);

		while (noPlayerIsDead(joueur))
		{	
			i=0;
			j=0;
		
			listenToPlayersAction(joueur, terrain);
			
			for (i=0; i<numberOfPlayers; i++)
			{	
				terrain=joueur[i].getBonus(terrain);
				for (j=0; j<joueur[i].getNumberOfBomb();j++)
				{
					terrain=joueur[i].bombe[j].manage(terrain, joueur);
				}
			}
		
			terrain.draw(joueur);
			StdDraw.show();
			pause(5);
		}
		displayGameOver(joueur, terrain);
	}

	public static void listenToPlayersAction(Joueur [] joueur, Terrain terrain)
	{	
		if (StdDraw.isKeyPressed(KeyEvent.VK_Z))
		{	
			joueur[0].moveTo("up",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_Q))
		{	
			joueur[0].moveTo("left",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_S))
		{	
			joueur[0].moveTo("down",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_D))
		{	
			joueur[0].moveTo("right",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_A))
		{	
			terrain=joueur[0].dropBomb(terrain);	
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
		{	
			joueur[1].moveTo("up",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
		{	
			joueur[1].moveTo("left",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
		{	
			joueur[1].moveTo("down",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
		{	
			joueur[1].moveTo("right",terrain);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER))
		{	
			terrain=joueur[1].dropBomb(terrain);
		}
	}
	
	public static void pause(int mili)
	{	
		long time=java.lang.System.currentTimeMillis();
		while (java.lang.System.currentTimeMillis()-time<mili);
	}

	public static boolean noPlayerIsDead(Joueur[] joueur)
	{
		for (int i = 0 ; i < joueur.length ; i++)
		{
			if(joueur[i].getNumberOfLife()<=0)
			{
				return false;
			}
		}
		return true;
	}
	
	public static void displayGameOver(Joueur[] joueur, Terrain terrain)
	{
		String joueurGagnant;
		if(joueur[0].getNumberOfLife()<=0)
		{
			joueurGagnant = "Joueur2" ;
		}
		else
		{
			joueurGagnant="Joueur1";

		}
		
		if (joueurGagnant=="Joueur1")
		{
			StdDraw.picture(terrain.getHalfWidthOfRow()*2*11, terrain.getHalfHeigthOfLine()*2*8, "FinJ1.png", 500, 300);
			StdDraw.picture(terrain.getHalfWidthOfRow()*2*10, terrain.getHalfHeigthOfLine()*2*5.8, "Rejouer.png", 100, 50);
			Audio sound = new Audio("Violin");
		}
		else if (joueurGagnant=="Joueur2")
		{
			StdDraw.picture(terrain.getHalfWidthOfRow()*2*11, terrain.getHalfHeigthOfLine()*2*8, "FinJ2.png", 500, 300);
			StdDraw.picture(terrain.getHalfWidthOfRow()*2*10, terrain.getHalfHeigthOfLine()*2*5.8, "Rejouer.png", 100, 50);
			Audio sound = new Audio("Hello");
		}
		
		
		StdDraw.show();
		
		while(true)
		{
			if(StdDraw.mousePressed())
			{
				if(StdDraw.mouseX()>terrain.getHalfWidthOfRow()*2*10-100
				&& StdDraw.mouseX()<terrain.getHalfWidthOfRow()*2*10+100
				&& StdDraw.mouseY()>terrain.getHalfHeigthOfLine()*2*5.8-50
				&& StdDraw.mouseY()<terrain.getHalfHeigthOfLine()*2*5.8+50)
					
				{
					main(null);
				}
			}
		}
		

	}
}