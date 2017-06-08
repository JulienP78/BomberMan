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
		if (ground.getTab(x, y)!=-99) // on verifi qu'il n'y a pas déjà une bombe de posée
		{	
			this.isActivated=true;
			this.positionX=x; 
			this.positionY=y;
			
			this.timer=java.lang.System.currentTimeMillis(); // on lance le compte à rebourt
			
			ground.setTab(x,y,-99);
			Sound sound = new Sound("drop_bomb");
		}
		return ground;
	}
	
	public Ground manage(Ground ground, Player[] joueur) // c'est ici qu'on gère les bombes en fonctions des compte à rebourt
	{	
		if ((this.isActivated==true)&&(this.hasExplosed==false)) // si la bombe est posée sur le terrain mais n'a pas encore explosée
		{	
			if (java.lang.System.currentTimeMillis()-this.timer>this.timeBeforeExplosion) // on regarde si le compte à rebourt a dépassé le temps de déclenchement défini
			{ 
				ground=this.explose(ground, joueur); // si c'est le cas on lance l'explosion
			}
		}
		if ((this.isActivated==true)&&(this.hasExplosed==true)) // si la bombe est en cour d'explosion
		{	
			if (java.lang.System.currentTimeMillis()-this.timer>this.timeOfExplosion) // on regarde si le compte à rebours a dépassé le temps d'explosion défini
			{
				ground=this.endOfEplosion(ground); // si c'est le cas on arrête l'explosion
			}	
		}
		return ground;
	}

	public Ground explose(Ground ground, Player[] joueur)
	{	
		Sound sound = new Sound("boum");
		checkIfPlayerIsHere(ground, joueur, this.positionX,this.positionY); // on regarde si un joueur doit perdre une vie		
		
		// -------------------------------------- On fait exploser au centre -----------------------------------------------
		
		ground.setTab(this.positionX, this.positionY, -100);
		
		// -------------------------------------- On fait exploser à droite -----------------------------------------------

		boolean wallOrBoxFound = false;
		
		for (int i = 0 ; i <= this.puissance && !wallOrBoxFound && (this.positionX+i<ground.getNumberOfRow()); i++)
		{	
			if (ground.getTab(this.positionX+i,this.positionY)==-1 && this.canOvercomeWalls==false) // si la case parcourue est un mur et que le joueur n'a pas le bonus bombe rouge alors on arrête
				wallOrBoxFound=true;
			else if (ground.getTab(this.positionX+i,this.positionY)==0) // si la case est une caisse alors on l'a fait exploser et on arrête
			{	
				ground.setTab(this.positionX+i,this.positionY,-101);
				wallOrBoxFound=true;
			}
			else if(ground.getTab(this.positionX+i,this.positionY)==1) // si la case est une case libre alors on l'a fait exploser
			{
				ground.setTab(this.positionX+i,this.positionY,-100);
			}
			else if(ground.getTab(this.positionX+i,this.positionY)==-99) // si la case est uen bombe alors on fait exploser la bombe correspondante à son tour
			{
				makeTheBombAtXYExplosed(this.positionX+i,this.positionY, joueur, ground);
			}
			checkIfPlayerIsHere(ground, joueur, this.positionX+i, this.positionY); // on regarde si un joueur doit perdre une vie	
			if (this.positionX+(i+1) > ground.getNumberOfRow()-1) // si l'on s'apprete à sortir du terrain
				wallOrBoxFound=true;
			if(wallOrBoxFound || i == this.puissance || (this.positionX+(i+1)>=ground.getNumberOfRow())) // quand l'explosion s'arrete
				this.arreaExplosed[2]=i; // on recupere le nombre de case explosée dans l'attribut arreaExplosed propre à la bombe ( int [left, top, right, bot] )
		}
		
		// -------------------------------------- On fait exploser en bas -----------------------------------------------

		wallOrBoxFound=false;
		
		for (int i = 0 ; i <= this.puissance && !wallOrBoxFound && (this.positionY-i>=0); i++)
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
			if (this.positionY-(i+1) < 0)
				wallOrBoxFound=true;
			if(wallOrBoxFound || i == this.puissance || (this.positionY-(i+1)<0))
				this.arreaExplosed[3]=i;
		}
		
		// -------------------------------------- On fait exploser à gauche -----------------------------------------------

		wallOrBoxFound=false;
		
		for (int i = 0 ; i <= this.puissance && !wallOrBoxFound && (this.positionX-i>=0); i++)
		{	
			

			if (ground.getTab(this.positionX-i,this.positionY)==-1 && this.canOvercomeWalls==false)
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
			
			if (this.positionX-(i+1) < 0)
				wallOrBoxFound=true;
			if(wallOrBoxFound || i == this.puissance || (this.positionX-(i+1)<0))
				this.arreaExplosed[0]=i;
		}
		
		// -------------------------------------- On fait exploser en haut -----------------------------------------------

		wallOrBoxFound=false;
		
		for (int i = 0 ; i <= this.puissance && !wallOrBoxFound && (this.positionY+i < ground.getNumberOfLine()); i++)
		{	
			if (ground.getTab(this.positionX,this.positionY+i)==-1 && this.canOvercomeWalls==false)
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
			if (this.positionY+(i+1) > ground.getNumberOfLine()-1)
				wallOrBoxFound=true;
			if(wallOrBoxFound || i == this.puissance || (this.positionY+(i+1) >= ground.getNumberOfLine()))
				this.arreaExplosed[1]=i;
		}
		
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
			positionPlayerX = joueur[i].getPositionX()/(ground.getHalfWidthOfRow()*2); // on calcul la position du joueur dans le tableau
			positionPlayerY = joueur[i].getPositionY()/(ground.getHalfHeigthOfLine()*2);
			
			if(positionPlayerX == postionExplosionX && positionPlayerY == positionExplosionY) // si le joueur se trouve sur la case de l'explosion
			{
				playerIsHere=true;
				if(joueur[i].hasAShield()) // si le joueur a un bouclier
				{
					joueur[i].setShield(false); // on retire son bouclier et il ne perd pas de vie
				}
				else // sinon
				{
					joueur[i].setNumberOfLife(joueur[i].getNumberOfLife()-1); // le joueur perd une vie
					Sound sound = new Sound("ouch");
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
				if(player[i].getBombs()[j].positionX == positionX && player[i].getBombs()[j].positionY == positionY) // si les coordonnées de la bombe parcourure correspondent bien aux coordonnées de la bombe à faire exploser
				{
					player[i].getBombs()[j].explose(ground, player); // alors on l'a fait exploser
				}
			}
		}
	}
	
	public Ground endOfEplosion(Ground ground)
	{	
		
		ground.setTab(this.positionX,this.positionY,1); // la case où était posée la bombe redevient une case libre
		
		// ------------------------------------------------------------ on parcourt les cases explosées à droite -------------------------------------------------------------
		for (int i = 0 ; i<=this.arreaExplosed[2] ; i++)
		{	
			if(ground.getTab(this.positionX+i,this.positionY)==-101) // Si la case en explosion était une caisse
			{
				ground.setTab(this.positionX+i,this.positionY,randomBonus()); // On ajoute un bonus aléatoire
			}
			else if (ground.getTab(this.positionX+i,this.positionY)==-100) // Si la case était une case libre
				ground.setTab(this.positionX+i,this.positionY,1);	// On la fait redevenir libre
		}
		
		for (int i = 0 ; i<=this.arreaExplosed[3] ; i++)
		{	
			if(ground.getTab(this.positionX,this.positionY-i)==-101)
			{
				ground.setTab(this.positionX,this.positionY-i,randomBonus());
			}
			else if (ground.getTab(this.positionX,this.positionY-i)==-100)
					ground.setTab(this.positionX,this.positionY-i,1);
		}
		
		for (int i = 0 ; i<=this.arreaExplosed[0] ; i++)
		{	
			if(ground.getTab(this.positionX-i,this.positionY)==-101)
			{
				ground.setTab(this.positionX-i,this.positionY,randomBonus());
			}
			else if (ground.getTab(this.positionX-i,this.positionY)==-100)
					ground.setTab(this.positionX-i,this.positionY,1);
		}
		
		for (int i = 0 ; i<=this.arreaExplosed[1] ; i++)
		{	
			if(ground.getTab(this.positionX,this.positionY+i)==-101)
			{
				ground.setTab(this.positionX,this.positionY+i,randomBonus());
			}
			else if (ground.getTab(this.positionX,this.positionY+i)==-100)
				ground.setTab(this.positionX,this.positionY+i,1);
		}
		
		this.isActivated=false;
		this.hasExplosed=false;
		
		return ground;
	}
	
	public int randomBonus() // fonction qui nous permet, lors d'une caisse explosée, de définir la case soit comme libre soit comme étant un nouveau bonus
	{	
		int test=(int)(Math.random()*100);
		int newTest=(int)(Math.random()*12);
		int value = 1; // par défaut on dit que la case sera une case libre vierge
		
		if (test<20) // on a une chance sur 5 d'entrer dans cette boucle et donc d'avoir un nouveau bonus
		{
			switch(newTest) // test qui nous permet de choisir aléatoirement un bonus
			{
				case 0:
					value=10; // bonus flamme bleue (reduit portée des bombes)
					break;
				
				case 1:
					value=20; // bonus flamme jaune (augmente portée des bombes)
					break;
				
				case 2:
					value=30; // bonus flamme rouge (augmente à 10 la portée des bombes)
					break;
					
				case 3:
					value=40; // bonus bombe rouge (permet aux bombes d'exploser à travers les murs
					break;
					
				case 4:
					value=50; // bonus vie (donne une vie supplémentaire au joueur)
					break; 
					
				case 5:
					value=60; // bonus speed up (augmente la vitesse du joueur)
					break;
					
				case 6:
					value=70; // bonus speed down (diminue la vitesse du joueur)
					break;
					
				case 7:
					value=80;  // bonus bombe + (augmente de 2 le nombre de bombes du joueur)
					break;
					
				case 8:
					value=90; // bonus bombe - (diminue de 2 le nombre de bombes du joueur)
					break;	
				
				case 9:
					value=100; // bonus bouclier (rend le joueur invincible à la prochaine bombe)
					break;
					
				case 10:
					value=110; // bonus flamme verte (diminue le temps de déclenchement et augmente la portée)
					break;
				
				case 11:
					value=120; // bonus passe muraille (permet au joueur de se déplacer sur les caisses et les bombes)
					break; 
			}
		}
		if (value!=1)
		{
			Sound sound = new Sound("new_bonus");
		}
		return 40;
	}
}