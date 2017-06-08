package bbman;

import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import edu.princeton.cs.introcs.StdDraw;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;

public class Bomb 
{	
	private boolean isActivated;
	private boolean hasExplosed;

	private int positionX;
	private int positionY;
	
	private long timer;
	private int timeBeforeExplosion;
	private int timeOfExplosion;
		
	private int puissance;
	private boolean canOvercomeWalls;

	private int[] arreaExplosed = {0,0,0,0}; // left, top, right, bot
	
	
	public Bomb()
	{	
		this.isActivated=false;
		this.hasExplosed=false;
		this.positionX=0;
		this.positionY=0;
		this.timer=0;
		this.timeBeforeExplosion=5000;
		this.timeOfExplosion=1000;
		this.puissance=3;
		this.canOvercomeWalls=false;
	}
		
	
	public boolean isActivated()
	{
		return this.isActivated;
	}
	
	public int getPuissance()
	{
		return this.puissance;
	}
	
	public void setPuissance(int puissance)
	{
		this.puissance = puissance;
	}
	
	public boolean canOvercomeWalls()
	{
		return this.canOvercomeWalls;
	}
	
	public void setCanOvercomeWalls(boolean trueOrFalse)
	{
		this.canOvercomeWalls = trueOrFalse;
	}
	
	public int getTimeBeforeExplosion()
	{
		return this.timeBeforeExplosion;
	}
	
	public void setTimeBeforExplosion(int newTime)
	{
		this.timeBeforeExplosion = newTime;
	}
	
	public Ground activateBomb(int x, int y, Ground ground)
	{	
		if (ground.getTab(x, y)!=-99)
		{	
			this.isActivated=true;
			this.positionX=x; 
			this.positionY=y;
			
			this.timer=java.lang.System.currentTimeMillis() ;
			
			ground.setTab(x,y,-99);
			Sound sound = new Sound("Beep2");
		}
		return ground;
	}
	
	public Ground manage(Ground ground, Player[] joueur)
	{	
		if ((this.isActivated==true)&&(this.hasExplosed==false))
		{	
			if (java.lang.System.currentTimeMillis()-this.timer>this.timeBeforeExplosion)
			{ 
				ground=this.explose(ground, joueur);
			}
		}
		if ((this.isActivated==true)&&(this.hasExplosed==true))
		{	
			if (java.lang.System.currentTimeMillis()-this.timer>this.timeOfExplosion)
			{
				ground=this.endOfEplosion(ground);
			}	
		}
		return ground;
	}

	public Ground explose(Ground ground, Player[] joueur)
	{	
		
		Sound sound = new Sound("boum");
		checkIfPlayerIsHere(ground, joueur, this.positionX,this.positionY);
		ground.setTab(this.positionX, this.positionY, -100);
		
		
		// -------------------------------------- On fait exploser au centre -----------------------------------------------

		
		ground.setTab(this.positionX, this.positionY, -100);
		
		// -------------------------------------- On fait exploser à droite -----------------------------------------------

		boolean wallOrBoxFound = false;
		int i=1;
		
		while (!wallOrBoxFound)
		{	
			if (ground.getTab(this.positionX+i,this.positionY)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (ground.getTab(this.positionX+i,this.positionY)==0)
			{	
				ground.setTab(this.positionX+i,this.positionY,-101);
				wallOrBoxFound=true;
			}
			else if(ground.getTab(this.positionX+i,this.positionY)==1)
			{
				ground.setTab(this.positionX+i,this.positionY,-100);
			}
			else if(ground.getTab(this.positionX+i,this.positionY)==-99)
			{
				makeTheBombAtXYExplosed(this.positionX+i,this.positionY, joueur, ground);
			}
			checkIfPlayerIsHere(ground, joueur, this.positionX+i, this.positionY);
			if (i==this.puissance || this.positionX+(i+1) > ground.getNumberOfRow()-1)
				wallOrBoxFound=true;
		
			if (!wallOrBoxFound)
				i=i+1;
		}
		this.arreaExplosed[2]=i;
		
		// -------------------------------------- On fait exploser en bas -----------------------------------------------

		i=1;
		wallOrBoxFound=false;
		
		while (!wallOrBoxFound)
		{	
			if (ground.getTab(this.positionX,this.positionY-i)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (ground.getTab(this.positionX,this.positionY-i)==0)
			{	
				ground.setTab(this.positionX,this.positionY-i,-101);
				wallOrBoxFound=true;
			}
			else if(ground.getTab(this.positionX,this.positionY-i)==1)
			{				
				ground.setTab(this.positionX,this.positionY-i,-100);
			}
			else if(ground.getTab(this.positionX,this.positionY-i)==-99)
			{
				makeTheBombAtXYExplosed(this.positionX,this.positionY-i, joueur, ground);
			}
			checkIfPlayerIsHere(ground, joueur, this.positionX,this.positionY-i);
			if (i==this.puissance || this.positionY-(i+1) < 0)
				wallOrBoxFound=true;
		
			if (!wallOrBoxFound)
				i=i+1;
		}
		this.arreaExplosed[3]=i;
		
		// -------------------------------------- On fait exploser à gauche -----------------------------------------------

		i=1;
		wallOrBoxFound=false;
		
		while (!wallOrBoxFound)
		{	if (ground.getTab(this.positionX-i,this.positionY)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (ground.getTab(this.positionX-i,this.positionY)==0)
			{	ground.setTab(this.positionX-i,this.positionY,-101);
				wallOrBoxFound=true;
			}
			else if(ground.getTab(this.positionX-i,this.positionY)==1)
			{
				ground.setTab(this.positionX-i,this.positionY,-100);
			}
			else if(ground.getTab(this.positionX-i,this.positionY)==-99)
			{
				makeTheBombAtXYExplosed(this.positionX-i,this.positionY, joueur,ground);
			}
			checkIfPlayerIsHere(ground, joueur, this.positionX-i,this.positionY);
			
			if (i==this.puissance || this.positionX-(i+1) < 0)
				wallOrBoxFound=true;
		
			if (!wallOrBoxFound)
				i=i+1;
		}
		this.arreaExplosed[0]=i;
		i=1;
		
		// -------------------------------------- On fait exploser en haut -----------------------------------------------

		wallOrBoxFound=false;
		
		while (!wallOrBoxFound)
		{	if (ground.getTab(this.positionX,this.positionY+i)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (ground.getTab(this.positionX,this.positionY+i)==0)
			{	ground.setTab(this.positionX,this.positionY+i,-101);
				wallOrBoxFound=true;
			}
			else if(ground.getTab(this.positionX,this.positionY+i)==1)
			{
				ground.setTab(this.positionX,this.positionY+i,-100);
			}
			else if(ground.getTab(this.positionX,this.positionY+i)==-99)
			{
				makeTheBombAtXYExplosed(this.positionX,this.positionY+i, joueur, ground);
			}
			checkIfPlayerIsHere(ground, joueur, this.positionX,this.positionY+i);
			if (i==this.puissance || this.positionY+(i+1) > ground.getNumberOfLine()-1)
				wallOrBoxFound=true;
		
			if (!wallOrBoxFound)
				i=i+1;
		}
		
		this.arreaExplosed[1]=i;
		this.timer=java.lang.System.currentTimeMillis() ;
		this.hasExplosed=true;
		return ground;
	}

	public boolean checkIfPlayerIsHere(Ground ground, Player[] joueur, int postionExplosionX, int positionExplosionY) 
	{
		boolean playerIsHere = false;
		int positionPlayerX = 0;
		int positionPlayerY = 0;
		
		for (int i = 0 ; i < joueur.length ; i++)
		{
			positionPlayerX = joueur[i].getPositionX()/(ground.getHalfWidthOfRow()*2);
			positionPlayerY = joueur[i].getPositionY()/(ground.getHalfHeigthOfLine()*2);
			
			if(positionPlayerX == postionExplosionX && positionPlayerY == positionExplosionY)
			{
				playerIsHere=true;
				if(joueur[i].hasAShield()) // si le joueur a un bouclier
				{
					joueur[i].setShield(false); // on retire son bouclier
				}
				else // sinon
				{
					joueur[i].setNumberOfLife(joueur[i].getNumberOfLife()-1);
				}
			}
		}
		return playerIsHere;
	}
	
	public void makeTheBombAtXYExplosed(int positionX, int positionY, Player[] player, Ground ground)
	{
		for (int i = 0 ; i < player.length ; i++)
		{
			for (int j = 0; j < player[i].getBombs().length ; j++)
			{
				if(player[i].getBombs()[j].positionX == positionX && player[i].getBombs()[j].positionY == positionY)
				{
					player[i].getBombs()[j].explose(ground, player);
				}
			}
		}
	}
	
	public Ground endOfEplosion(Ground ground)
	{	
		int i=1;
		
		ground.setTab(this.positionX,this.positionY,1);

		while (i<=this.arreaExplosed[2])
		{	
			if(ground.getTab(this.positionX+i,this.positionY)==-101) // Si la case en explosion est une caisse
			{
				ground.setTab(this.positionX+i,this.positionY,randomBonus()); // On ajoute un bonus aléatoire
			}
			else if (ground.getTab(this.positionX+i,this.positionY)==-100) // Si la case était une case libre
				ground.setTab(this.positionX+i,this.positionY,1);	// On la fait redevenir libre
			
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosed[3])
		{	
			if(ground.getTab(this.positionX,this.positionY-i)==-101)
			{
				ground.setTab(this.positionX,this.positionY-i,randomBonus());
			}
			else if (ground.getTab(this.positionX,this.positionY-i)==-100)
					ground.setTab(this.positionX,this.positionY-i,1);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosed[0])
		{	
			if(ground.getTab(this.positionX-i,this.positionY)==-101)
			{
				ground.setTab(this.positionX-i,this.positionY,randomBonus());
			}
			else if (ground.getTab(this.positionX-i,this.positionY)==-100)
					ground.setTab(this.positionX-i,this.positionY,1);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosed[1])
		{	
			if(ground.getTab(this.positionX,this.positionY+i)==-101)
			{
				ground.setTab(this.positionX,this.positionY+i,randomBonus());
			}
			else if (ground.getTab(this.positionX,this.positionY+i)==-100)
				ground.setTab(this.positionX,this.positionY+i,1);
		
			i=i+1;
		}
		
		this.isActivated=false;
		this.hasExplosed=false;
		
		return ground;
	}
	
	public int randomBonus()
	{	
		int test=(int)(Math.random()*100);
		int newTest=(int)(Math.random()*12);
		int value = 1;
		
		if (test<20)
		{
			switch(newTest)
			{
				case 0:
					value=10;
					break;
				
				case 1:
					value=20;
					break;
				
				case 2:
					value=30;
					break;
					
				case 3:
					value=40;
					break;
					
				case 4:
					value=50;
					break;
					
				case 5:
					value=60;
					break;
					
				case 6:
					value=70;
					break;
					
				case 7:
					value=80;
					break;
					
				case 8:
					value=90;
					break;	
				
				case 9:
					value=100;
					break;
					
				case 10:
					value=110;
					break;
				
				case 11:
					value=120;
					break;
			}
		}
		return value;
	}
}


