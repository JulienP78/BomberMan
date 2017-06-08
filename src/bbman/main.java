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
		Player [] player=new Player [numberOfPlayers] ;
		
		int idplayer;
		int positionX;
		int positionY;
		
		for (int i=0;i<numberOfPlayers;i++)
		{	
			idplayer = i;
			positionX=0;
			positionY=0;
			if(idplayer==0)
			{
				positionX=3*halfWidthOfRow;
				positionY=3*halfHeigthOfLine;
			}
			else if(idplayer==1)
			{
				positionX=(numberOfRow*(halfWidthOfRow*2))-(3*halfWidthOfRow);
				positionY=(numberOfLine*(halfHeigthOfLine*2))-(3*halfHeigthOfLine);
			}
			player[i]=new Player(ground,idplayer, positionX, positionY);	// création des players
		}
		
		ground.draw(player); // on dessine le début de partie

		while (noPlayerIsDead(player)) // si aucun player n'est mort
		{	
			listenToPlayersAction(player, ground); // on écoute les saisis des deux players
			
			for (int i=0; i<numberOfPlayers; i++)
			{	
				ground=player[i].getBonus(ground); // on regarde si le player est sur une case avec un bonus
				for (int j=0; j<player[i].getNumberOfBomb();j++)
				{
					ground=player[i].bombe[j].manage(ground, player); // on gère les bombes
				}
			}
		
			ground.draw(player); // on dessine le tout
			StdDraw.show();
			pause(5);
		}
		ground.displayGameOver(player, ground); // si un player est mort on affiche l'écran de fin
	}

	public static void listenToPlayersAction(Player [] player, Ground ground)
	{	
		if (StdDraw.isKeyPressed(KeyEvent.VK_Z))
		{	
			player[0].moveTo("up",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_Q))
		{	
			player[0].moveTo("left",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_S))
		{	
			player[0].moveTo("down",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_D))
		{	
			player[0].moveTo("right",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_A))
		{	
			ground=player[0].dropBomb(ground);	
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
		{	
			player[1].moveTo("up",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
		{	
			player[1].moveTo("left",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
		{	
			player[1].moveTo("down",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
		{	
			player[1].moveTo("right",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER))
		{	
			ground=player[1].dropBomb(ground);
		}
	}
	
	public static void pause(int mili)
	{	
		long time=java.lang.System.currentTimeMillis();
		while (java.lang.System.currentTimeMillis()-time<mili);
	}

	public static boolean noPlayerIsDead(Player[] player)
	{
		for (int i = 0 ; i < player.length ; i++)
		{
			if(player[i].getNumberOfLife()<=0)
			{
				return false;
			}
		}
		return true;
	}
}