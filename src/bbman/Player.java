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
		
	private int speed;
	private boolean hasAShield;
	private boolean canWalkOnBoxAndBomb;
	private String avatar;

	public Player (Ground ground, int id, int positionX, int positionY)
	{	
		this.id=id;
		this.positionX=positionX;
		this.positionY=positionY;
		this.numberOfLife=3;
		this.numberOfBomb=3;
		
		for (int i=0; i<this.bombe.length; i++)
		{	
			bombe[i]=new Bomb();
		}
		
		this.speed=3;
		this.hasAShield = false;
		this.canWalkOnBoxAndBomb = false;
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
	public void setAvatar(String newAvatar)
	{
		this.avatar = newAvatar;
	}
	
	public boolean hasAShield()
	{
		return this.hasAShield;
	}
	public void setShield(boolean trueOrFalse)
	{
		this.hasAShield=trueOrFalse;
	}
	
	public Bomb[] getBombs()
	{
		return this.bombe;
	}
	
	public boolean canWalkOnBoxAndBomb()
	{
		return this.canWalkOnBoxAndBomb;
	}
	
	public Ground dropBomb(Ground ground)
	{
		boolean noBombAvailableFound = true;
		
		int positionX=this.positionX/(ground.getHalfWidthOfRow()*2); // positionX dans la table 
		int positionY=this.positionY/(ground.getHalfHeigthOfLine()*2); // positionY dans la table
		
		for (int i = 0 ; i < this.numberOfBomb && noBombAvailableFound ; i++)
		{
			if (this.bombe[i].isActivated()==false) // on entre dans la boucle à la première bombe disponible
			{	
				ground=this.bombe[i].activateBomb(positionX, positionY, ground); // on active la bombe avec commme position la position du joueur
				noBombAvailableFound=false; // on modifie la variable pour sortir de la boucle
			}
		}
		return ground;
	}
		
	public Ground getBonus(Ground ground)
	{
		int playerPositionXInTab=this.positionX/(ground.getHalfWidthOfRow()*2);
		int playerPositionYInTab=this.positionY/(ground.getHalfHeigthOfLine()*2);
		
		if ((ground.getTab(playerPositionXInTab, playerPositionYInTab)>=10)) // si le joueur est sur une case bonus
		{
			int bonusValue = ground.getTab(playerPositionXInTab, playerPositionYInTab); // on récupère la valeur de la case
			Sound sound;
			if(bonusValue==10 && this.bombe[0].getPuissance()>1) // bonus flamme bleue (reduit portée des bombes)
			{
				for(int i = 0 ; i < this.bombe.length ; i++)
				{
					this.bombe[i].setPuissance(bombe[i].getPuissance()-1);
				}
			}
			else if(bonusValue==20 && this.bombe[0].getPuissance()<10) // bonus flamme jaune (augmente portée des bombes)
			{
				for(int i = 0 ; i < this.bombe.length ; i++)
				{
					this.bombe[i].setPuissance(bombe[i].getPuissance()+1);
				}
			}
			else if(bonusValue == 30) // bonus flamme rouge (augmente à 10 la portée des bombes)
			{
				for(int i = 0 ; i < this.bombe.length ; i++)
				{
					this.bombe[i].setPuissance(10);
				}
			}
			else if(bonusValue == 40) // bonus bombe rouge (permet aux bombes d'exploser à travers les murs
			{
				for (int i = 0 ; i < 10 ; i++)
				{
					this.bombe[i].setCanOvercomeWalls(true);
				}
			}
			else if(bonusValue == 50) // bonus vie (donne une vie supplémentaire au joueur)
			{
				this.numberOfLife++;
				sound = new Sound("bonus_vie");
			}
			else if(bonusValue == 60) // bonus speed up (augmente la vitesse du joueur)
			{
				this.speed++;
				
			}
			else if(bonusValue==70 && this.speed>1) // bonus speed down (diminue la vitesse du joueur)
			{
				this.speed--;
			}
			else if (bonusValue==80) // bonus bombe + (augmente de 2 le nombre de bombes du joueur)
			{
				this.numberOfBomb=this.numberOfBomb+2;
				if (this.numberOfBomb>7)
					this.numberOfBomb=7;
			}
			else if(bonusValue == 90) // bonus bombe - (diminue de 2 le nombre de bombes du joueur)
			{
				this.numberOfBomb=this.numberOfBomb-2;
				if (this.numberOfBomb<2)
					this.numberOfBomb=2;
			}
			else if(bonusValue == 100) // bonus bouclier (rend le joueur invincible à la prochaine bombe)
			{
				this.hasAShield=true;
			}
			else if(bonusValue == 110) // bonus flamme verte (diminue le temps de déclenchement et augmente la portée)
			{
				if (this.bombe[0].getTimeBeforeExplosion()>=4000)
				{
					for (int i = 0 ; i < 10 ; i ++)
					{
						this.bombe[i].setTimeBeforExplosion(bombe[i].getTimeBeforeExplosion()-1000);
					}
				}
				if(this.bombe[0].getPuissance()<10)
				{
					for (int i = 0 ; i < 10 ; i ++)
					{
						this.bombe[i].setPuissance(bombe[i].getPuissance()+1);
					}
				}
			}
			else if(bonusValue == 120) // bonus passe muraille (permet au joueur de se déplacer sur les caisses et les bombes)
			{
				this.canWalkOnBoxAndBomb=true;
			}
			ground.setTab(playerPositionXInTab, playerPositionYInTab, 1); // On retire le bonus au ground
		}
		
		return ground;
	}
	
	public void moveTo(String move, Ground ground)
	{	
		String shield = (this.hasAShield==true) ? "_shield" : ""; // on regarde si le joueur a un bouclier (pour l'avatar)
		
		if (move=="up" && noObstacle("up", ground)) // on verifie qu'il n'y a pas d'obstacle
		{	
			this.positionY=this.positionY+this.speed;
			this.avatar="player_" + (this.id+1) + "_back_profile" + shield + ".png";
		}
		else if (move=="left" && noObstacle("left", ground))
		{	
			this.positionX=this.positionX-this.speed;
			this.avatar="player_" + (this.id+1) + "_left_profile" + shield + ".png";
		}
		else if (move=="down" && noObstacle("down", ground))
		{	
			this.positionY=this.positionY-this.speed;
			this.avatar="player_" + (this.id+1) + "_front_profile" + shield + ".png";
		}
		else if (move=="right" && noObstacle("right", ground))
		{	
			this.positionX=this.positionX+this.speed;
			this.avatar="player_" + (this.id+1) + "_right_profile" + shield + ".png";
		}
	
	}

	public boolean noObstacle(String move, Ground ground) // on vérifie que le joueur peut bien se déplacer sur cette case
	{
		int spaceAllowBeforeColision = 0;
		int casePositionToCheck = 0;
		int caseValueToCheck = 0;
		int playerPositionXInTab = this.positionX/(ground.getHalfWidthOfRow()*2);
		int playerPositionYInTab = this.positionY/(ground.getHalfHeigthOfLine()*2);

		if (move == "up")
		{
			spaceAllowBeforeColision=ground.getHalfHeigthOfLine();
			casePositionToCheck = (this.positionY+spaceAllowBeforeColision)/(ground.getHalfHeigthOfLine()*2); // la position de la case à regarder
			caseValueToCheck = ground.getTab(playerPositionXInTab, casePositionToCheck); // la valeur de la case
			
			if((caseValueToCheck==0 && this.canWalkOnBoxAndBomb==false)	// Si la case est une caisse et si le joueur n'a pas le bonus passe muraille
			 ||caseValueToCheck==-1 // ou si la case est un mur
			 ||(caseValueToCheck == -99 && playerPositionYInTab!=casePositionToCheck  && this.canWalkOnBoxAndBomb==false)) // ou si la case est une bombe et le joueur n'a pas le bonus passe muraille
			{
				return false;	// alors on renvoit faux et le joueur ne se déplace pas
			}
		}
		
		else if(move == "left")
		{
			spaceAllowBeforeColision=2*ground.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX-spaceAllowBeforeColision)/(ground.getHalfWidthOfRow()*2);
			caseValueToCheck = ground.getTab(casePositionToCheck, playerPositionYInTab);

			if((caseValueToCheck==0 && this.canWalkOnBoxAndBomb==false)
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionXInTab!=casePositionToCheck  && this.canWalkOnBoxAndBomb==false))
			{
				return false;
			}

		}
		
		else if(move == "right")
		{
			spaceAllowBeforeColision=2*ground.getHalfWidthOfRow()/3;
			casePositionToCheck = (this.positionX+spaceAllowBeforeColision)/(ground.getHalfWidthOfRow()*2);
			caseValueToCheck = ground.getTab(casePositionToCheck, playerPositionYInTab);
			
			if((caseValueToCheck==0 && this.canWalkOnBoxAndBomb==false)
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionXInTab!=casePositionToCheck  && this.canWalkOnBoxAndBomb==false))
			{
				return false;
			}
		}
		
		else if(move == "down")
		{
			spaceAllowBeforeColision=ground.getHalfHeigthOfLine();
			casePositionToCheck = (this.positionY-spaceAllowBeforeColision)/(ground.getHalfHeigthOfLine()*2);
			caseValueToCheck = ground.getTab(playerPositionXInTab, casePositionToCheck);

			if((caseValueToCheck==0 && this.canWalkOnBoxAndBomb==false)
			 ||caseValueToCheck==-1
			 ||(caseValueToCheck == -99 && playerPositionYInTab!=casePositionToCheck  && this.canWalkOnBoxAndBomb==false))
			{
				return false;
			}
		}
		return true;
	}
}