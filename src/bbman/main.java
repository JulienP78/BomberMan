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

		StdDraw.setCanvasSize(halfWidthOfRow*numberOfRow*2,halfHeigthOfLine*numberOfLine*2);
	
		StdDraw.setXscale(0, halfWidthOfRow*numberOfRow*2);
		StdDraw.setYscale(0, halfHeigthOfLine*numberOfLine*2);
		
		StdDraw.show(0);

		Ground ground=new Ground(numberOfRow,numberOfLine,halfWidthOfRow,halfHeigthOfLine); // instanciation du ground
		Player [] joueur=new Player [numberOfPlayers] ;
		
		int idJoueur;
		int positionX;
		int positionY;
		
		for (int i=0;i<numberOfPlayers;i++)
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
			joueur[i]=new Player(ground,idJoueur, positionX, positionY);	// création des joueurs
		}
		
		ground.draw(joueur); // on dessine le début de partie

		while (noPlayerIsDead(joueur)) // si aucun joueur n'est mort
		{	
			listenToPlayersAction(joueur, ground); // on écoute les saisis des deux joueurs
			
			for (int i=0; i<numberOfPlayers; i++)
			{	
				ground=joueur[i].getBonus(ground); // on regarde si le joueur est sur une case avec un bonus
				for (int j=0; j<joueur[i].getNumberOfBomb();j++)
				{
					ground=joueur[i].bombe[j].manage(ground, joueur); // on gère les bombes
				}
			}
		
			ground.draw(joueur); // on dessine le tout
			StdDraw.show();
			pause(5);
		}
		ground.displayGameOver(joueur, ground); // si un joueur est mort on affiche l'écran de fin
	}

	public static void listenToPlayersAction(Player [] joueur, Ground ground)
	{	
		if (StdDraw.isKeyPressed(KeyEvent.VK_Z))
		{	
			joueur[0].moveTo("up",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_Q))
		{	
			joueur[0].moveTo("left",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_S))
		{	
			joueur[0].moveTo("down",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_D))
		{	
			joueur[0].moveTo("right",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_A))
		{	
			ground=joueur[0].dropBomb(ground);	
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
		{	
			joueur[1].moveTo("up",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
		{	
			joueur[1].moveTo("left",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
		{	
			joueur[1].moveTo("down",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
		{	
			joueur[1].moveTo("right",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER))
		{	
			ground=joueur[1].dropBomb(ground);
		}
	}
	
	public static void pause(int mili)
	{	
		long time=java.lang.System.currentTimeMillis();
		while (java.lang.System.currentTimeMillis()-time<mili);
	}

	public static boolean noPlayerIsDead(Player[] joueur)
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
}