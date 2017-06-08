package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.AudioClip;

public class Ground 
{	
	private int [][] tab;
	
	private int numberOfRow;
	private int numberOfLine;
	
	private int halfWidthOfRow;
	private int halfHeigthOfLine;
	
	public Ground(int numberOfRow, int numberOfLine, int halfWidthOfRow, int halfHeigthOfLine)
	{	
		this.tab=new int [numberOfRow][numberOfLine];
		this.numberOfRow=numberOfRow;
		this.numberOfLine=numberOfLine;
		
		this.halfWidthOfRow=halfWidthOfRow;
		this.halfHeigthOfLine=halfHeigthOfLine;
		
		//--------------------------------------- plateau avec caisses et murs ------------------------------------------------

		for (int i=0;i<numberOfRow;i++)
		{	
			for (int j=0;j<numberOfLine;j++)
			{	
				if ((i==0)||(j==0)||(i==numberOfRow-1)||(j==numberOfLine-1)||((i%2==0)&&(j%2==0)))
					this.tab [i][j]=-1; // mur
				else
					this.tab [i][j]=0; // caisse
			}
		}
		
		//--------------------------------------- espace libre pour le joueur 1 ------------------------------------------------
		this.tab [1][1]=1; // libre
		this.tab [2][1]=1;
		this.tab [3][1]=1;
		this.tab [3][2]=1;
		this.tab [1][2]=1;
		this.tab [1][3]=1;
		this.tab [2][3]=1;
		
		//--------------------------------------- espace libre pour le joueur 2 ------------------------------------------------

		this.tab [numberOfRow-2][numberOfLine-2]=1;
		this.tab [numberOfRow-3][numberOfLine-2]=1;
		this.tab [numberOfRow-4][numberOfLine-2]=1;
		this.tab [numberOfRow-4][numberOfLine-3]=1;
		this.tab [numberOfRow-2][numberOfLine-3]=1;
		this.tab [numberOfRow-2][numberOfLine-4]=1;
		this.tab [numberOfRow-3][numberOfLine-4]=1;
	}

	public int getHalfWidthOfRow()
	{
		return this.halfWidthOfRow;
	}
	
	public int getHalfHeigthOfLine()
	{
		return this.halfHeigthOfLine;
	}
	
	public int getTab(int x, int y)
	{
		return this.tab[x][y];
	}
	
	public void setTab(int x, int y, int value)
	{
		this.tab[x][y]=value;
	}
	
	public int getNumberOfRow()
	{
		return this.numberOfRow;
	}
	
	public int getNumberOfLine()
	{
		return this.numberOfLine;
	}

	public void draw(Player [] joueur)
	{
		String[] filesToDraw = {"", ""}; // tableau regroupant 2 fichiers images qui seront affichées en superposition
		
		for (int i=0;i<this.numberOfRow;i++)
		{	
			for (int j=0;j<this.numberOfLine;j++)
			{	
				filesToDraw[0] = "case_libre.png"; // par défaut la première image dessinée est une case libre (cas le plus récurent)
				filesToDraw[1] = ""; // on donnera la deuxième image en fonction des cas
				
				if (this.tab[i][j]==0)
				{	
					filesToDraw[0] = "case_caisse.png";
				}
				else if (this.tab[i][j]==-1)
				{	
					filesToDraw[0] = "case_mur.png";
				}
				else if (this.tab[i][j]==10)
				{	
					filesToDraw[1] = "bonus_flamme_bleue.png";
				}
				else if (this.tab[i][j]==20)
				{	
					filesToDraw[1] = "bonus_flamme_jaune.png";
				}
				else if (this.tab[i][j]==30)
				{	
					filesToDraw[1] = "bonus_flamme_rouge.png";
				}
				else if (this.tab[i][j]==40)
				{	
					filesToDraw[1] = "bonus_bombe_rouge.png";
				}
				else if (this.tab[i][j]==50)
				{	
					filesToDraw[1] = "bonus_vie.png";
				}
				else if (this.tab[i][j]==60)
				{	
					filesToDraw[1] = "bonus_speed_up.png";
				}
				else if (this.tab[i][j]==70)
				{
					filesToDraw[1] = "bonus_speed_down.png";
				}
				else if (this.tab[i][j]==80)
				{	
					filesToDraw[1] = "bonus_bombe_plus.png";
				}
				else if (this.tab[i][j]==90)
				{	
					filesToDraw[1] = "bonus_bombe_moins.png";
				}
				else if (this.tab[i][j]==100)
				{	
					filesToDraw[1] = "bonus_shield.png";
				}
				else if (this.tab[i][j]==110)
				{	
					filesToDraw[1] = "bonus_flamme_verte.png";
				}
				else if (this.tab[i][j]==120)
				{	
					filesToDraw[1] = "bonus_passe_muraille.png";
				}
				else if (this.tab[i][j] == -99)
				{
					filesToDraw[1] = "bombe.png";
				}
				else if (this.tab[i][j]<=-100)
				{	
					filesToDraw[0] = "Explosion.png";
				}
			
				for (int file = 0 ; file < filesToDraw.length ; file++)
				{
					if(filesToDraw[file]!="") // condition utile pour les cas où il n'y a qu'une image a dessiner
					{
						StdDraw.picture((i*2*this.halfWidthOfRow)+halfWidthOfRow, (j*2*this.halfHeigthOfLine)+halfHeigthOfLine, filesToDraw[file], 50, 50);
					}
				}
			}
		}
		
		// ----------------------------------------------- On dessine les joueurs ------------------------------------------ 
		for (int i=0;i<joueur.length;i++) 
		{
			StdDraw.picture(joueur[i].getPositionX(), joueur[i].getPositionY(), joueur[i].getAvatar(), 33, 50);
		}
		
		// ------------------------------------------- On dessine les stats des joueurs ------------------------------------------ 
		StdDraw.setPenColor(255, 255, 255);
		StdDraw.setPenRadius(200);
		
		// ---------------------------------- On regroupe les différents images associés au stat des joueur  ------------------------------------------ 

		String[] statsFiles = {"NombreVies.png", "NombreBombes.png", "bonus_speed_up.png", "Explosion.png","timer.png", "bonus_passe_muraille.png"};

		// ------------------------------------------- On récupère les différents stats du joueur 1  ------------------------------------------ 
		
		String[] statsP1 = {"X " + joueur[0].getNumberOfLife(), "X " + joueur[0].getNumberOfBomb(), "" + joueur[0].getSpeed(), "" + joueur[0].bombe[0].getPuissance(), "" + joueur[0].bombe[0].getTimeBeforeExplosion()/1000 + " s", ""};

		StdDraw.picture(this.halfWidthOfRow*2*2.1, this.halfHeigthOfLine*2*0.5, "J1.png", 200, 90); // correspond au texte "Joueur 1" en bas à gauche
		
		for(int i = 0 ; i < statsFiles.length ; i++)
		{
			if(statsFiles[i]=="bonus_passe_muraille.png") // condition car pas valeur pour ce stat, juste afficher l'image
			{
				if(joueur[0].canWalkOnBoxAndBomb())
					StdDraw.picture(this.halfWidthOfRow*2*0.5, this.halfHeigthOfLine*2*(2.5+2*i), statsFiles[i], 30, 30);
			}
			else
			{
				StdDraw.picture(this.halfWidthOfRow*2*0.5, this.halfHeigthOfLine*2*(2.5+2*i), statsFiles[i], 30, 30); // on affiche l'image de stat
				StdDraw.text(this.halfWidthOfRow*2*0.5, this.halfHeigthOfLine*2*(1.5+2*i), statsP1[i]); // on affiche la valeur de la stat associée en dessous
			}
		}
		
		// ------------------------------------------- On récupère les différents stats du joueur 2  ------------------------------------------ 

		String[] statsP2 = {"X " + joueur[1].getNumberOfLife(), "X " + joueur[1].getNumberOfBomb(), "" + joueur[1].getSpeed(), "" + joueur[1].bombe[0].getPuissance(), "" + joueur[1].bombe[0].getTimeBeforeExplosion()/1000 + " s", ""};
		
		StdDraw.picture(this.halfWidthOfRow*2*18.9, this.halfHeigthOfLine*2*16.5, "J2.png", 200, 90); // correspond au texte "Joueur 2" en haut à droite
		
		for(int i = 0 ; i < statsFiles.length ; i++)
		{
			if(statsFiles[i]=="bonus_passe_muraille.png")
			{
				if(joueur[1].canWalkOnBoxAndBomb())
					StdDraw.picture(this.halfWidthOfRow*2*20.6, this.halfHeigthOfLine*2*(15.5-2*i), statsFiles[i], 30, 30);
			}
			else
			{
				StdDraw.picture(this.halfWidthOfRow*2*20.6, this.halfHeigthOfLine*2*(15.5-2*i), statsFiles[i], 30, 30);
				StdDraw.text(this.halfWidthOfRow*2*20.6, this.halfHeigthOfLine*2*(14.5-2*i), statsP2[i]);
			}
			
		}
		
		// ------------------------------------------- On rappelle les bonus ------------------------------------------ 
		
		String[] bonusFiles = {"rappel_bonus_bombe_moins.png",
							   "rappel_bonus_bombe_plus.png",
							   "rappel_bonus_bombe_rouge.png",
							   "rappel_bonus_flamme_bleue.png",
							   "rappel_bonus_flamme_rouge.png",
							   "rappel_bonus_flamme_jaune.png",
							   "rappel_bonus_flamme_verte.png"};
		
		for(int i = 0 ; i < bonusFiles.length ; i++)
		{
			StdDraw.picture(this.halfWidthOfRow*2+ (120*i), this.halfHeigthOfLine*2*16.5, bonusFiles[i], 100, 40);
		}
		
	}
	
	public void displayMenu()
	{
		StdDraw.picture(this.halfWidthOfRow*2*10.5, this.halfHeigthOfLine*2*8.5, "menu.png", halfWidthOfRow*numberOfRow*2+halfWidthOfRow*2,halfHeigthOfLine*numberOfLine*2+halfHeigthOfLine+30);
		StdDraw.picture(this.halfWidthOfRow*2*4, this.halfHeigthOfLine*2*7.5, "bouton_jouer.png", 400, 200);
		StdDraw.picture(this.halfWidthOfRow*2*17.5, this.halfHeigthOfLine*2*7, "bouton_commande.png", 400, 200);
		StdDraw.show();
		
		boolean buttonActivate = false;
		while(!buttonActivate)
		{
			if(StdDraw.mousePressed())
			{
				if(StdDraw.mouseX()>this.halfWidthOfRow*2*4-200
				&& StdDraw.mouseX()<this.halfWidthOfRow*2*4+200
				&& StdDraw.mouseY()>this.halfHeigthOfLine*2*7.5-100
				&& StdDraw.mouseY()<this.halfHeigthOfLine*2*7.5+100)	// si le joueur appuie sur jouer on rappel la main
				{
					buttonActivate = true;
					main.main(null);
				}
				
				if(StdDraw.mouseX()>this.halfWidthOfRow*2*17-200
				&& StdDraw.mouseX()<this.halfWidthOfRow*2*17+200
				&& StdDraw.mouseY()>this.halfHeigthOfLine*2*7-100
				&& StdDraw.mouseY()<this.halfHeigthOfLine*2*7+100)		// si le joueur appuie sur commandes on affiche les commandes
				{
					buttonActivate = true;
					displayCommandes();
				}
			}
		}
	}
	
	public void displayCommandes()
	{
		StdDraw.picture(this.halfWidthOfRow*2*10.5, this.halfHeigthOfLine*2*8.5, "commandes.png", halfWidthOfRow*numberOfRow*2+halfWidthOfRow*2,halfHeigthOfLine*numberOfLine*2+halfHeigthOfLine+30);
		StdDraw.picture(this.halfWidthOfRow*2*16, this.halfHeigthOfLine*2*2, "bouton_jouer.png", 400, 200);
		StdDraw.show();
		boolean buttonActivate = false;

		while(!buttonActivate)
		{
			if(StdDraw.mousePressed())
			{
				if(StdDraw.mouseX()>this.halfWidthOfRow*2*16-200
				&& StdDraw.mouseX()<this.halfWidthOfRow*2*16+200
				&& StdDraw.mouseY()>this.halfHeigthOfLine*2*2-100
				&& StdDraw.mouseY()<this.halfHeigthOfLine*2*2+100)	
				{
					buttonActivate = true;
					main.main(null); // si le joueur appuie sur jouer on rappel la main
				}
			}
		}
	}
	
	public void displayGameOver(Player[] joueur)
	{
		int idWinner = (joueur[0].getNumberOfLife()<=0) ? 2 : 1;
		StdDraw.picture(this.halfWidthOfRow*2*11, this.halfHeigthOfLine*2*8, "finJ" + idWinner + ".png", 500, 300);
		StdDraw.picture(this.halfWidthOfRow*2*10, this.halfHeigthOfLine*2*5.8, "bouton_rejouer.png", 100, 50);
		Sound sound = new Sound("musique_fin_joueurGagnant_" + idWinner);
		StdDraw.show();
		
		while(true)
		{
			if(StdDraw.mousePressed())
			{
				if(StdDraw.mouseX()>this.halfWidthOfRow*2*10-50
				&& StdDraw.mouseX()<this.halfWidthOfRow*2*10+50
				&& StdDraw.mouseY()>this.halfHeigthOfLine*2*5.8-25
				&& StdDraw.mouseY()<this.halfHeigthOfLine*2*5.8+25)
				{
					sound.stop();
					main.main(null);
				}
			}
		}
	}
}