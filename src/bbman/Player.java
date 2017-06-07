package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

public class Player 
{	
	private int id;

	private int positionX;
	private int positionY;
	
	private String sens;
	
	private int numberOfLife;
	
	private int numberOfBomb;
	Bomb [] bombe=new Bomb [10];
	
	private int speed;

	private boolean isBigBoss;
	private String image;

	public Player (Ground terrain, int id, int positionX, int positionY)
	{	
		this.id=id;
		this.positionX=positionX;
		this.positionY=positionY;
		this.sens="front_profile";
		this.numberOfLife=3;
		this.numberOfBomb=3;
		this.speed=3;
		this.isBigBoss=false;
		
		for (int i=0; i<this.bombe.length; i++)
		{	
			bombe[i]=new Bomb();
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
	
	public String getSens()
	{
		return this.sens;
	}
	
	public boolean getIsBigBoss()
	{
		return this.isBigBoss;
	}
	
	public Ground dropBomb(Ground terrain)
	{
		boolean keepOn = true;
		
		int positionX=this.positionX/(terrain.getHalfWidthOfRow()*2);
		int positionY=this.positionY/(terrain.getHalfHeigthOfLine()*2);
		
		for (int i = 0 ; i < this.numberOfBomb && keepOn ; i++)
		{
			if (this.bombe[i].isActivated()==false) // on parcourt les bombes tant qu'elles sont déjà activiées
			{	
				terrain=this.bombe[i].activateBomb(positionX, positionY, terrain); // on active la première bombe non activé avec commme position la position du joueur
				keepOn=false;
			}
		}
		return terrain;
	}
		
	public Ground getBonus(Ground terrain)
	{
		int playerPositionXInTab=this.positionX/(terrain.getHalfWidthOfRow()*2);
		int playerPositionYInTab=this.positionY/(terrain.getHalfHeigthOfLine()*2);
		
		if ((terrain.getTab(playerPositionXInTab, playerPositionYInTab)>=10)&&(terrain.getTab(playerPositionXInTab, playerPositionYInTab)<=100)) // Si le joueur est sur une case bonus
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
				for (int i = 0 ; i < 10 ; i++)
				{
					this.bombe[i].setCanOvercomeWalls(true);
				}
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
				Sound sound3 = new Sound("Bonus");
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
			else if(bonusValue == 100)
			{
				this.isBigBoss = true;
				this.numberOfLife=100;
			}
			terrain.setTab(playerPositionXInTab, playerPositionYInTab, 1); // On retire le bonus au terrain
		}
		
		return terrain;
	}
	
	public void moveTo(String move, Ground terrain)
	{	
		if (move=="up" && noObstacle("up", terrain)) // on verifie qu'il n'y a pas d'obstacle
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

	public boolean noObstacle(String move, Ground terrain) // On vérifie que le joueur peut bien se déplacer sur cette case
	{
		int spaceAllow = 0;
		int casePositionToCheck=0;
		int caseValueToCheck=0;
		int playerPositionXInTab = this.positionX/(terrain.getHalfWidthOfRow()*2);
		int playerPositionYInTab = this.positionY/(terrain.getHalfHeigthOfLine()*2);

		if (move == "up")
		{
			spaceAllow=2*terrain.getHalfHeigthOfLine()/3;
			casePositionToCheck = (this.positionY+spaceAllow)/(terrain.getHalfHeigthOfLine()*2); // La case à regarder
			caseValueToCheck = terrain.getTab(playerPositionXInTab, casePositionToCheck);
			
			if(caseValueToCheck==0	// Si la case est une caisse
			 ||caseValueToCheck==-1 // ou si la case est un mur
			 ||(caseValueToCheck == -99 && playerPositionYInTab!=casePositionToCheck)) // ou si la case est une bombe
			{
				return false;	// alors on renvoit faux et le joueur ne se déplace pas
			}
		}
		
		else if(move == "left")
		{
			spaceAllow=2*terrain.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX-spaceAllow)/(terrain.getHalfWidthOfRow()*2);
			caseValueToCheck = terrain.getTab(casePositionToCheck, playerPositionYInTab);

			if(caseValueToCheck==0
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionXInTab!=casePositionToCheck))
			{
				return false;
			}

		}
		
		else if(move == "right")
		{
			spaceAllow=2*terrain.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX+spaceAllow)/(terrain.getHalfWidthOfRow()*2);
			caseValueToCheck = terrain.getTab(casePositionToCheck, playerPositionYInTab);
			
			if(caseValueToCheck==0
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionXInTab!=casePositionToCheck))
			{
				return false;
			}
		}
		
		else if(move == "down")
		{
			spaceAllow=2*terrain.getHalfHeigthOfLine()/3;
			casePositionToCheck = (this.positionY-spaceAllow)/(terrain.getHalfHeigthOfLine()*2);
			caseValueToCheck = terrain.getTab(playerPositionXInTab, casePositionToCheck);

			if(caseValueToCheck==0
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionYInTab!=casePositionToCheck))
			{
				return false;
			}
		}
		return true;
	}
}
