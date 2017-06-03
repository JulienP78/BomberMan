package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

public class Joueur 
{	
	private int id;

	private int positionX;
	private int positionY;
	
	private String sens;
	
	private int numberOfLife;
	
	private int speed;
		
	private int numberOfBomb;
	Bombe [] bombe=new Bombe [10] ;
	

	public Joueur (Terrain terrain, int id, int positionX, int positionY)
	{	
		this.id=id;
		this.positionX=positionX;
		this.positionY=positionY;
		this.sens="front_profile";
		this.numberOfLife=3;
		this.speed=3;
		this.numberOfBomb=3;
		
		for (int i=0; i<this.bombe.length; i++)
		{	
			bombe[i]=new Bombe();
		}
	}
	
	public int getPositionX()
	{
		return this.positionX;
	}
	
	public int getPositionY()
	{
		return this.positionY;
	}
	public int getNumberOfBomb()
	{	
		return this.numberOfBomb;
	}
	
	public int getNumberOfLife()
	{	
		return this.numberOfLife;
	}
	public void setNumberOfLife(int newNumberOfLife)
	{
		this.numberOfLife = newNumberOfLife;
	}
		
	public Terrain dropBomb(Terrain terrain)
	{
		boolean keepOn = true;
		
		int positionX=this.positionX/(terrain.getHalfWidthOfRow()*2);
		int positionY=this.positionY/(terrain.getHalfHeigthOfLine()*2);
		
		for (int i = 0 ; i < this.numberOfBomb && keepOn ; i++)
		{
			if (this.bombe[i].isActivated()==false)
			{	
				terrain=this.bombe[i].activateBomb(positionX, positionY, terrain);
				keepOn=false;
			}
		}
		return terrain;
	}
		
	public Terrain getBonus(Terrain terrain)
	{
		int playerPositionXInTab=this.positionX/(terrain.getHalfWidthOfRow()*2);
		int playerPositionYInTab=this.positionY/(terrain.getHalfHeigthOfLine()*2);
		
		if ((terrain.getTab(playerPositionXInTab, playerPositionYInTab)>=10)&&(terrain.getTab(playerPositionXInTab, playerPositionYInTab)<=90))
		{
			int bonusValue = terrain.getTab(playerPositionXInTab, playerPositionYInTab);
			
			if(bonusValue==10 && this.bombe[0].getPuissance()>1)
			{
				for(int i = 0 ; i < this.bombe.length ; i++)
				{
					this.bombe[i].setPuissance(bombe[i].getPuissance()-1);
				}
			}
			else if(bonusValue==20 && this.bombe[0].getPuissance()<10)
			{
				for(int i = 0 ; i < this.bombe.length ; i++)
				{
					this.bombe[i].setPuissance(bombe[i].getPuissance()+1);
				}
			}
			else if(bonusValue == 30)
			{
				for(int i = 0 ; i < this.bombe.length ; i++)
				{
					this.bombe[i].setPuissance(10);
				}
			}
			else if(bonusValue == 40)
			{
			}
			else if(bonusValue == 50)
			{
				this.numberOfLife++;
			}
			else if(bonusValue == 60)
			{
				this.speed++;
				
			}
			else if(bonusValue==70 && this.speed>1)
			{
				this.speed--;
			}
			else if (bonusValue==80)
			{
				Audio sound3 = new Audio("Bonus");
				this.numberOfBomb=this.numberOfBomb+2;
				if (this.numberOfBomb>7)
					this.numberOfBomb=7;
			}
			else if(bonusValue == 90)
			{
				this.numberOfBomb=this.numberOfBomb-2;
				if (this.numberOfBomb<2)
					this.numberOfBomb=2;
			}
			terrain.setTab(playerPositionXInTab, playerPositionYInTab, 0);
		}
		
		return terrain;
	}
	
	public void moveTo(String move, Terrain terrain)
	{	
		if (move=="up" && noObstacle("up", terrain))
		{	
			this.positionY=this.positionY+this.speed;
			this.sens="back_profile";
		}
		else if (move=="left" && noObstacle("left", terrain))
		{	
			this.positionX=this.positionX-this.speed;
			this.sens="left_profile";
		}
		else if (move=="down" && noObstacle("down", terrain))
		{	
			this.positionY=this.positionY-this.speed;
			this.sens="front_profile";
		}
		else if (move=="right" && noObstacle("right", terrain))
		{	
			this.positionX=this.positionX+this.speed;
			this.sens="right_profile";
		}
	
	}

	public boolean noObstacle(String move, Terrain terrain)
	{
		int spaceAllow = 0;
		int casePositionToCheck=0;
		int playerPositionXInTab = this.positionX/(terrain.getHalfWidthOfRow()*2);
		int playerPositionYInTab = this.positionY/(terrain.getHalfHeigthOfLine()*2);

		if (move == "up")
		{
			spaceAllow=2*terrain.getHalfHeigthOfLine()/3;
			casePositionToCheck = (this.positionY+spaceAllow)/(terrain.getHalfHeigthOfLine()*2);

			if(terrain.getTab(playerPositionXInTab, casePositionToCheck)==1
			 ||terrain.getTab(playerPositionXInTab, casePositionToCheck)==2)
			{
				return false;
			}
		}
		else if(move == "left")
		{
			spaceAllow=2*terrain.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX-spaceAllow)/(terrain.getHalfWidthOfRow()*2);

			if(terrain.getTab(casePositionToCheck, playerPositionYInTab)==1
			 ||terrain.getTab(casePositionToCheck, playerPositionYInTab)==2)
			{
				return false;
			}

		}
		else if(move == "right")
		{
			spaceAllow=2*terrain.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX+spaceAllow)/(terrain.getHalfWidthOfRow()*2);

			if(terrain.getTab(casePositionToCheck, playerPositionYInTab)==1
			 ||terrain.getTab(casePositionToCheck, playerPositionYInTab)==2)
			{
				return false;
			}
		}
		else if(move == "down")
		{
			spaceAllow=2*terrain.getHalfHeigthOfLine()/3;
			casePositionToCheck = (this.positionY-spaceAllow)/(terrain.getHalfHeigthOfLine()*2);

			if(terrain.getTab(playerPositionXInTab, casePositionToCheck)==1
			 ||terrain.getTab(playerPositionXInTab, casePositionToCheck)==2)
			{
				return false;
			}
		}
		return true;
	}
	
	public void draw()
	{	
		String playerImage = "player_" + (this.id+1) + "_" + this.sens + ".png";
		StdDraw.picture(positionX, positionY, playerImage, 33, 50);
	}
}
