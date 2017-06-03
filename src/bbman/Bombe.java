package bbman;

import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import edu.princeton.cs.introcs.StdDraw;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;

public class Bombe 
{	
	private boolean isActivated;
	private boolean hasExplosed;

	private int positionX;
	private int positionY;
	
	private long timer;
	private int timeBeforeExplosion;
	private int timeOfExplosion;
		
	private int puissance;

	private int[] arreaExplosedd = {0,0,0,0}; // left, top, right, bot
	
	
	public Bombe ()
	{	
		this.isActivated=false;
		this.hasExplosed=false;
		this.positionX=0;
		this.positionY=0;
		this.timer=0;
		this.timeBeforeExplosion=5000;
		this.timeOfExplosion=1000;
		this.puissance=3;
	}
		
	
	public boolean isActivated()
	{	return this.isActivated;
	}
	
	public int getPuissance()
	{
		return this.puissance;
	}
	
	public void setPuissance(int puissance)
	{
		this.puissance = puissance;
	}
	
	public Terrain activateBomb(int x, int y, Terrain terrain)
	{	
		if (terrain.getTab(x, y)!=665)
		{	
			this.isActivated=true;
			this.positionX=x; 
			this.positionY=y;
			
			this.timer=java.lang.System.currentTimeMillis() ;
			
			terrain.setTab(x,y,665);
			Audio sound = new Audio("Beep2");
		}
		
		return terrain;
	}
	
	public Terrain manage(Terrain terrain, Joueur[] joueur)
	{	
		if ((this.isActivated==true)&&(this.hasExplosed==false))
		{	if (java.lang.System.currentTimeMillis()-this.timer>this.timeBeforeExplosion)
			{ 
				terrain=this.explose(terrain, joueur);
			}
		}
		if ((this.isActivated==true)&&(this.hasExplosed==true))
		{	if (java.lang.System.currentTimeMillis()-this.timer>this.timeOfExplosion)
			{
				terrain=this.endOfEplosion(terrain);
			}
			
		}
		return terrain;
	}

	public Terrain explose(Terrain terrain, Joueur[] joueur)
	{	int test=0;
		int i=1;

		Audio sound = new Audio("explose");
		checkIfPlayerIsHere(terrain, joueur, this.positionX,this.positionY);
		terrain.setTab(this.positionX, this.positionY, 666);
		
		while (test==0)
		{	if (terrain.getTab(this.positionX+i,this.positionY)==1)
				test=1;
			else if (terrain.getTab(this.positionX+i,this.positionY)==2)
			{	terrain.setTab(this.positionX+i,this.positionY,667);
				test=1;
			}
			else if(terrain.getTab(this.positionX+i,this.positionY)==0 )
			{
				terrain.setTab(this.positionX+i,this.positionY,666);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX+i, this.positionY);
			if (i==this.puissance)
				test=1;
		
			if (test==0)
				i=i+1;
		}
		this.arreaExplosedd[2]=i;
		i=1;
		test=0;
		
		while (test==0)
		{	if (terrain.getTab(this.positionX,this.positionY-i)==1)
				test=1;
			else if (terrain.getTab(this.positionX,this.positionY-i)==2)
			{	terrain.setTab(this.positionX,this.positionY-i,667);
				test=1;
			}
			else if(terrain.getTab(this.positionX,this.positionY-i)==0)
			{				
				terrain.setTab(this.positionX,this.positionY-i,666);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX,this.positionY-i);
			if (i==this.puissance)
				test=1;
		
			if (test==0)
				i=i+1;
		}
		this.arreaExplosedd[3]=i;
		i=1;
		test=0;
		
		while (test==0)
		{	if (terrain.getTab(this.positionX-i,this.positionY)==1)
				test=1;
			else if (terrain.getTab(this.positionX-i,this.positionY)==2)
			{	terrain.setTab(this.positionX-i,this.positionY,667);
				test=1;
			}
			else if(terrain.getTab(this.positionX-i,this.positionY)==0)
			{
				terrain.setTab(this.positionX-i,this.positionY,666);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX-i,this.positionY);
			
			if (i==this.puissance)
				test=1;
		
			if (test==0)
				i=i+1;
		}
		this.arreaExplosedd[0]=i;
		i=1;
		test=0;
		
		while (test==0)
		{	if (terrain.getTab(this.positionX,this.positionY+i)==1)
				test=1;
			else if (terrain.getTab(this.positionX,this.positionY+i)==2)
			{	terrain.setTab(this.positionX,this.positionY+i,667);
				test=1;
			}
			else if(terrain.getTab(this.positionX,this.positionY+i)==0)
			{
				terrain.setTab(this.positionX,this.positionY+i,666);
			}
			checkIfPlayerIsHere(terrain, joueur, this.positionX,this.positionY+i);
			if (i==this.puissance)
				test=1;
		
			if (test==0)
				i=i+1;
		}
		this.arreaExplosedd[1]=i;
		i=1;
		test=0;
		
		this.timer=java.lang.System.currentTimeMillis() ;
		this.hasExplosed=true;
		
		return terrain;
	}

	private boolean checkIfPlayerIsHere(Terrain terrain, Joueur[] joueur, int postionExplosionX, int positionExplosionY) 
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
	
	public Terrain endOfEplosion(Terrain terrain)
	{	
		int i=1;
		
		terrain.setTab(this.positionX,this.positionY,0);

		while (i<=this.arreaExplosedd[2])
		{	
			if(terrain.getTab(this.positionX+i,this.positionY)==667)
			{
				terrain.setTab(this.positionX+i,this.positionY,randomBonus());
			}
			else if (terrain.getTab(this.positionX+i,this.positionY)>=666)
				terrain.setTab(this.positionX+i,this.positionY,0);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosedd[3])
		{	
			if(terrain.getTab(this.positionX,this.positionY-i)==667)
			{
				terrain.setTab(this.positionX,this.positionY-i,randomBonus());
			}
			else if (terrain.getTab(this.positionX,this.positionY-i)>=666)
					terrain.setTab(this.positionX,this.positionY-i,0);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosedd[0])
		{	
			if(terrain.getTab(this.positionX-i,this.positionY)==667)
			{
				terrain.setTab(this.positionX-i,this.positionY,randomBonus());
			}
			else if (terrain.getTab(this.positionX-i,this.positionY)>=666)
					terrain.setTab(this.positionX-i,this.positionY,0);
		
			i=i+1;
		}
		i=0;
		
		while (i<=this.arreaExplosedd[1])
		{	
			if(terrain.getTab(this.positionX,this.positionY+i)==667)
			{
				terrain.setTab(this.positionX,this.positionY+i,randomBonus());
			}
			else if (terrain.getTab(this.positionX,this.positionY+i)>=666)
				terrain.setTab(this.positionX,this.positionY+i,0);
		
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
		int value = 0;
		
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
	
	public void draw(Terrain terrain)
	{	
		if ((this.isActivated==true)&&(this.hasExplosed==false)) 
		{			
			int x=this.positionX*terrain.getHalfWidthOfRow()*2+terrain.getHalfWidthOfRow();
			int y=this.positionY*terrain.getHalfHeigthOfLine()*2+terrain.getHalfHeigthOfLine();
			StdDraw.picture(x, y, "Herbe.png", 50, 50);
			StdDraw.picture(x, y, "Bombe.png", 40, 50);
		}
	}
}


