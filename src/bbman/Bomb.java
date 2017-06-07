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
	
	public Ground activateBomb(int x, int y, Ground terrain)
	{	
		if (terrain.getTab(x, y)!=-99)
		{	
			this.isActivated=true;
			this.positionX=x; 
			this.positionY=y;
			
			this.timer=java.lang.System.currentTimeMillis() ;
			
			terrain.setTab(x,y,-99);
			Sound sound = new Sound("Beep2");
		}
		return terrain;
	}
	
	public Ground manage(Ground terrain, Player[] joueur)
	{	
		if ((this.isActivated==true)&&(this.hasExplosed==false))
		{	
			if (java.lang.System.currentTimeMillis()-this.timer>this.timeBeforeExplosion)
			{ 
				terrain=this.explose(terrain, joueur);
			}
		}
		if ((this.isActivated==true)&&(this.hasExplosed==true))
		{	
			if (java.lang.System.currentTimeMillis()-this.timer>this.timeOfExplosion)
			{
				terrain=this.endOfEplosion(terrain);
			}	
		}
		return terrain;
	}

	public Ground explose(Ground terrain, Player[] joueur)
	{	
		
		Sound sound = new Sound("boum");
		checkIfPlayerIsHere(terrain, joueur, this.positionX,this.positionY);
		terrain.setTab(this.positionX, this.positionY, -100);
		
		
		// -------------------------------------- On fait exploser au centre -----------------------------------------------

		
		terrain.setTab(this.positionX, this.positionY, -100);
		
		// -------------------------------------- On fait exploser à droite -----------------------------------------------

		boolean wallOrBoxFound = false;
		int i=1;
		
		while (!wallOrBoxFound)
		{	
			if (terrain.getTab(this.positionX+i,this.positionY)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (terrain.getTab(this.positionX+i,this.positionY)==0)
			{	
				terrain.setTab(this.positionX+i,this.positionY,-101);
				wallOrBoxFound=true;
			}
			else if(terrain.getTab(this.positionX+i,this.positionY)==1)
			{
				terrain.setTab(this.positionX+i,this.positionY,-100);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX+i, this.positionY);
			if (i==this.puissance || this.positionX+(i+1) > terrain.getNumberOfRow()-1)
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
			if (terrain.getTab(this.positionX,this.positionY-i)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (terrain.getTab(this.positionX,this.positionY-i)==0)
			{	
				terrain.setTab(this.positionX,this.positionY-i,-101);
				wallOrBoxFound=true;
			}
			else if(terrain.getTab(this.positionX,this.positionY-i)==1)
			{				
				terrain.setTab(this.positionX,this.positionY-i,-100);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX,this.positionY-i);
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
		{	if (terrain.getTab(this.positionX-i,this.positionY)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (terrain.getTab(this.positionX-i,this.positionY)==0)
			{	terrain.setTab(this.positionX-i,this.positionY,-101);
				wallOrBoxFound=true;
			}
			else if(terrain.getTab(this.positionX-i,this.positionY)==1)
			{
				terrain.setTab(this.positionX-i,this.positionY,-100);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX-i,this.positionY);
			
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
		{	if (terrain.getTab(this.positionX,this.positionY+i)==-1 && this.canOvercomeWalls==false)
				wallOrBoxFound=true;
			else if (terrain.getTab(this.positionX,this.positionY+i)==0)
			{	terrain.setTab(this.positionX,this.positionY+i,-101);
				wallOrBoxFound=true;
			}
			else if(terrain.getTab(this.positionX,this.positionY+i)==1)
			{
				terrain.setTab(this.positionX,this.positionY+i,-100);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX,this.positionY+i);
			if (i==this.puissance || this.positionY+(i+1) > terrain.getNumberOfLine()-1)
				wallOrBoxFound=true;
		
			if (!wallOrBoxFound)
				i=i+1;
		}
		
		this.arreaExplosed[1]=i;
		this.timer=java.lang.System.currentTimeMillis() ;
		this.hasExplosed=true;
		return terrain;
	}

	private boolean checkIfPlayerIsHere(Ground terrain, Player[] joueur, int postionExplosionX, int positionExplosionY) 
	{
		boolean playerIsHere = false;
		int positionPlayerX = 0;
		int positionPlayerY = 0;
		
		for (int i = 0 ; i < joueur.length ; i++)
		{
			positionPlayerX = joueur[i].getPositionX()/(terrain.getHalfWidthOfRow()*2);
			positionPlayerY = joueur[i].getPositionY()/(terrain.getHalfHeigthOfLine()*2);
			
			if(positionPlayerX == postionExplosionX && positionPlayerY == positionExplosionY)
			{
				playerIsHere=true;
				joueur[i].setNumberOfLife(joueur[i].getNumberOfLife()-1);
			}
		}
		return playerIsHere;
	}
	
	public Ground endOfEplosion(Ground terrain)
	{	
		int i=1;
		
		terrain.setTab(this.positionX,this.positionY,1);

		while (i<=this.arreaExplosed[2])
		{	
			if(terrain.getTab(this.positionX+i,this.positionY)==-101) // Si la case en explosion est une caisse
			{
				terrain.setTab(this.positionX+i,this.positionY,randomBonus()); // On ajoute un bonus aléatoire
			}
			else if (terrain.getTab(this.positionX+i,this.positionY)==-100) // Si la case était une case libre
				terrain.setTab(this.positionX+i,this.positionY,1);	// On la fait redevenir libre
			
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosed[3])
		{	
			if(terrain.getTab(this.positionX,this.positionY-i)==-101)
			{
				terrain.setTab(this.positionX,this.positionY-i,randomBonus());
			}
			else if (terrain.getTab(this.positionX,this.positionY-i)==-100)
					terrain.setTab(this.positionX,this.positionY-i,1);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosed[0])
		{	
			if(terrain.getTab(this.positionX-i,this.positionY)==-101)
			{
				terrain.setTab(this.positionX-i,this.positionY,randomBonus());
			}
			else if (terrain.getTab(this.positionX-i,this.positionY)==-100)
					terrain.setTab(this.positionX-i,this.positionY,1);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosed[1])
		{	
			if(terrain.getTab(this.positionX,this.positionY+i)==-101)
			{
				terrain.setTab(this.positionX,this.positionY+i,randomBonus());
			}
			else if (terrain.getTab(this.positionX,this.positionY+i)==-100)
				terrain.setTab(this.positionX,this.positionY+i,1);
		
			i=i+1;
		}
		
		this.isActivated=false;
		this.hasExplosed=false;
		
		return terrain;
	}
	
	public int randomBonus()
	{	
		int test=(int)(Math.random()*100);
		int newTest=(int)(Math.random()*9);
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
			}
		}
		return value;
	}
}


