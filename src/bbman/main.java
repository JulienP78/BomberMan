package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

public class main 
{	
	static boolean isFirstGame = true;
	
	public static void main(String[] args) 
	{	
		Ground ground = createGround();
		
		Player[] players = createPlayers(ground);
		
		if(isFirstGame)
		{
			isFirstGame=false;
			ground.displayMenu();
		}
		
		while (noPlayerIsDead(players))
		{
			play(ground, players);
		}
		
		ground.displayGameOver(players, ground);
	}
	
	public static Ground createGround()
	{
		int numberOfRow=21;
		int numberOfLine=17;
		int halfWidthOfRow=25;
		int halfHeigthOfLine=25;

		StdDraw.setCanvasSize(halfWidthOfRow*numberOfRow*2,halfHeigthOfLine*numberOfLine*2);
	
		StdDraw.setXscale(0, halfWidthOfRow*numberOfRow*2);
		StdDraw.setYscale(0, halfHeigthOfLine*numberOfLine*2);
		
		StdDraw.show(0);

		Ground ground=new Ground(numberOfRow,numberOfLine,halfWidthOfRow,halfHeigthOfLine); // instanciation du ground
		return ground;
	}
	
	public static Player[] createPlayers(Ground ground)
	{
		int numberOfPlayers=2;
		Player [] players=new Player [numberOfPlayers] ;
		
		int idPlayer;
		int positionX;
		int positionY;
		
		for (int i=0;i<numberOfPlayers;i++)
		{	
			idPlayer = i;
			positionX=0;
			positionY=0;
			if(idPlayer==0)
			{
				positionX=3*ground.getHalfWidthOfRow();
				positionY=3*ground.getHalfHeigthOfLine();
			}
			else if(idPlayer==1)
			{
				positionX=(ground.getNumberOfRow()*(ground.getHalfWidthOfRow()*2))-(3*ground.getHalfWidthOfRow());
				positionY=(ground.getNumberOfLine()*(ground.getHalfHeigthOfLine()*2))-(3*ground.getHalfHeigthOfLine());
			}
			players[i]=new Player(ground,idPlayer, positionX, positionY);	// création des joueurs
		}
		
		ground.draw(players);	// on dessine le début de partie
		return players;
	}
	
	public static void play(Ground ground, Player[] players)
	{
		while (noPlayerIsDead(players))	// si aucun joueur n'est mort
		{	
			listenToPlayersAction(players, ground); // on écoute les saisis des deux joueurs
			
			for (int i=0; i<players.length; i++)
			{	
				ground=players[i].getBonus(ground); // on regarde si le joueur est sur une case avec un bonus
				for (int j=0; j<players[i].getNumberOfBomb();j++)
				{
					ground=players[i].bombe[j].manage(ground, players); // on gère les bombes
				}
			}
			ground.draw(players); // on dessine le tout
			StdDraw.show();
			pause(5);
		}
	}
	
	public static void listenToPlayersAction(Player [] players, Ground ground)
	{	
		if (StdDraw.isKeyPressed(KeyEvent.VK_Z))
		{	
			players[0].moveTo("up",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_Q))
		{	
			players[0].moveTo("left",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_S))
		{	
			players[0].moveTo("down",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_D))
		{	
			players[0].moveTo("right",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_A))
		{	
			ground=players[0].dropBomb(ground);	
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
		{	
			players[1].moveTo("up",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
		{	
			players[1].moveTo("left",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
		{	
			players[1].moveTo("down",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
		{	
			players[1].moveTo("right",ground);
		}
		if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER))
		{	
			ground=players[1].dropBomb(ground);
		}
	}
	
	public static void pause(int mili)
	{	
		long time=java.lang.System.currentTimeMillis();
		while (java.lang.System.currentTimeMillis()-time<mili);
	}

	public static boolean noPlayerIsDead(Player[] players)
	{
		for (int i = 0 ; i < players.length ; i++)
		{
			if(players[i].getNumberOfLife()<=0)
			{
				return false;
			}
		}
		return true;
	}
}