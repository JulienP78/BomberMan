package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

public class Player 
{	
	private int id;

	private int positionX;
	private int positionY;
		
	private int numberOfLife;
	
	private int numberOfBomb;
	Bomb [] bombe=new Bomb [10];
	
	private String avatar;
	
	private int speed;

	public Player (Ground ground, int id, int positionX, int positionY)
	{	
		this.id=id;
		this.positionX=positionX;
		this.positionY=positionY;
		this.numberOfLife=3;
		this.numberOfBomb=3;
		this.speed=3;
		
		for (int i=0; i<this.bombe.length; i++)
		{	
			bombe[i]=new Bomb();
		}
		
		this.avatar = "player_" + (id+1) + "_front_profile.png";
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
	
	public int getSpeed()
	{
		return this.speed;
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	public void setAvater(String newAvatar)
	{
		this.avatar = newAvatar;
	}
	
	public Ground dropBomb(Ground ground)
	{
		boolean keepOn = true;
		
		int positionX=this.positionX/(ground.getHalfWidthOfRow()*2); // positionX dans la table 
		int positionY=this.positionY/(ground.getHalfHeigthOfLine()*2); // positionY dans la table
		
		for (int i = 0 ; i < this.numberOfBomb && keepOn ; i++)
		{
			if (this.bombe[i].isActivated()==false) // on entre dans la boucle à la première bombe non activée
			{	
				ground=this.bombe[i].activateBomb(positionX, positionY, ground); // on active la première bombe non activée avec commme position la position du joueur
				keepOn=false;
			}
		}
		return ground;
	}
		
	public Ground getBonus(Ground ground)
	{
		int playerPositionXInTab=this.positionX/(ground.getHalfWidthOfRow()*2);
		int playerPositionYInTab=this.positionY/(ground.getHalfHeigthOfLine()*2);
		
		if ((ground.getTab(playerPositionXInTab, playerPositionYInTab)>=10)&&(ground.getTab(playerPositionXInTab, playerPositionYInTab)<=90)) // Si le joueur est sur une case bonus
		{
			int bonusValue = ground.getTab(playerPositionXInTab, playerPositionYInTab);
			
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
			ground.setTab(playerPositionXInTab, playerPositionYInTab, 1); // On retire le bonus au ground
		}
		
		return ground;
	}
	
	public void moveTo(String move, Ground ground)
	{	
		if (move=="up" && noObstacle("up", ground)) // on verifie qu'il n'y a pas d'obstacle
		{	
			this.positionY=this.positionY+this.speed;
			this.avatar="player_" + (this.id+1) + "_back_profile.png";
		}
		else if (move=="left" && noObstacle("left", ground))
		{	
			this.positionX=this.positionX-this.speed;
			this.avatar="player_" + (this.id+1) + "_left_profile.png";
		}
		else if (move=="down" && noObstacle("down", ground))
		{	
			this.positionY=this.positionY-this.speed;
			this.avatar="player_" + (this.id+1) + "_front_profile.png";
		}
		else if (move=="right" && noObstacle("right", ground))
		{	
			this.positionX=this.positionX+this.speed;
			this.avatar="player_" + (this.id+1) + "_right_profile.png";
		}
	
	}

	public boolean noObstacle(String move, Ground ground) // On vérifie que le joueur peut bien se déplacer sur cette case
	{
		int spaceAllow = 0;
		int casePositionToCheck=0;
		int caseValueToCheck=0;
		int playerPositionXInTab = this.positionX/(ground.getHalfWidthOfRow()*2);
		int playerPositionYInTab = this.positionY/(ground.getHalfHeigthOfLine()*2);

		if (move == "up")
		{
			spaceAllow=2*ground.getHalfHeigthOfLine()/3;
			casePositionToCheck = (this.positionY+spaceAllow)/(ground.getHalfHeigthOfLine()*2); // La case à regarder
			caseValueToCheck = ground.getTab(playerPositionXInTab, casePositionToCheck);
			
			if(caseValueToCheck==0	// Si la case est une caisse
			 ||caseValueToCheck==-1 // ou si la case est un mur
			 ||(caseValueToCheck == -99 && playerPositionYInTab!=casePositionToCheck)) // ou si la case est une bombe
			{
				return false;	// alors on renvoit faux et le joueur ne se déplace pas
			}
		}
		
		else if(move == "left")
		{
			spaceAllow=2*ground.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX-spaceAllow)/(ground.getHalfWidthOfRow()*2);
			caseValueToCheck = ground.getTab(casePositionToCheck, playerPositionYInTab);

			if(caseValueToCheck==0
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionXInTab!=casePositionToCheck))
			{
				return false;
			}

		}
		
		else if(move == "right")
		{
			spaceAllow=2*ground.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX+spaceAllow)/(ground.getHalfWidthOfRow()*2);
			caseValueToCheck = ground.getTab(casePositionToCheck, playerPositionYInTab);
			
			if(caseValueToCheck==0
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionXInTab!=casePositionToCheck))
			{
				return false;
			}
		}
		
		else if(move == "down")
		{
			spaceAllow=2*ground.getHalfHeigthOfLine()/3;
			casePositionToCheck = (this.positionY-spaceAllow)/(ground.getHalfHeigthOfLine()*2);
			caseValueToCheck = ground.getTab(playerPositionXInTab, casePositionToCheck);

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
