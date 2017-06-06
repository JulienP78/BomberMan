package bbman;

import java.awt.event.KeyEvent;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.AudioClip;

public class Ground 
{	private int [][] tab;
	
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
	
		int i=0;
		int j=0;
		
		for (i=0;i<numberOfRow;i++)
		{	
			for (j=0;j<numberOfLine;j++)
			{	
				if ((i==0)||(j==0)||(i==numberOfRow-1)||(j==numberOfLine-1)||((i%2==0)&&(j%2==0)))
					this.tab [i][j]=-1;
				else
					this.tab [i][j]=0;
			}
		}
		
		this.tab [1][1]=1;
		this.tab [2][1]=1;
		this.tab [3][1]=1;
		this.tab [3][2]=1;
		this.tab [1][2]=1;
		this.tab [1][3]=1;
		this.tab [2][3]=1;
		
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

	public void draw(Player [] joueur)
	{
		String[] filesToDraw = {"", ""};
		
		for (int i=0;i<this.numberOfRow;i++)
		{	
			for (int j=0;j<this.numberOfLine;j++)
			{	
				filesToDraw[0] = "Herbe.png";
				filesToDraw[1] = "";
				
				if (this.tab[i][j]==0)
				{	
					filesToDraw[0] = "Caisse.png";
				}
				else if (this.tab[i][j]==-1)
				{	
					filesToDraw[0] = "Pelouse.png";
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
				else if (this.tab[i][j]<=-100)
				{	
					filesToDraw[0] = "Explosion.png";
				}
			
			for (int file = 0 ; file < filesToDraw.length ; file++)
			{
				if(filesToDraw[file]!="")
				{
					StdDraw.picture((i*2*this.halfWidthOfRow)+halfWidthOfRow, (j*2*this.halfHeigthOfLine)+halfHeigthOfLine, filesToDraw[file], 50, 50);
				}
			}
			}
		}
		StdDraw.setPenColor(255, 255, 255);
		StdDraw.setPenRadius(200);
		StdDraw.picture(this.getHalfWidthOfRow()*2*18.9, this.getHalfHeigthOfLine()*2*16.5, "J2.png", 200, 90);
		StdDraw.picture(this.getHalfWidthOfRow()*2*20.6, this.getHalfHeigthOfLine()*2*15.5, "NombreVies.png", 30, 30);
		StdDraw.text(this.getHalfWidthOfRow()*2*20.6, this.getHalfHeigthOfLine()*2*14.5, "X " + joueur[1].getNumberOfLife());
		StdDraw.picture(this.getHalfWidthOfRow()*2*20.6, this.getHalfHeigthOfLine()*2*13.5, "NombreBombes.png", 30, 30);
		StdDraw.text(this.getHalfWidthOfRow()*2*20.6, this.getHalfHeigthOfLine()*2*12.5, "X " + joueur[1].getNumberOfBomb());
		
		StdDraw.picture(this.getHalfWidthOfRow()*2*2.1, this.getHalfHeigthOfLine()*2*0.5, "J1.png", 200, 90);
		StdDraw.picture(this.getHalfWidthOfRow()*2*0.5, this.getHalfHeigthOfLine()*2*2.5, "NombreVies.png", 30, 30);
		StdDraw.text(this.getHalfWidthOfRow()*2*0.5, this.getHalfHeigthOfLine()*2*1.5, "X " + joueur[0].getNumberOfLife());
		StdDraw.picture(this.getHalfWidthOfRow()*2*0.5, this.getHalfHeigthOfLine()*2*4.5, "NombreBombes.png", 30, 30);
		StdDraw.text(this.getHalfWidthOfRow()*2*0.5, this.getHalfHeigthOfLine()*2*3.5, "X " + joueur[0].getNumberOfBomb());
		
		for (int i=0;i<joueur.length;i++)
		{
			for (int j=0; j<joueur[i].getNumberOfBomb();j++) // On dessine les bombes
				joueur[i].bombe[j].draw(this);
		}
		
		for (int i=0;i<joueur.length;i++)
			joueur[i].draw();	// on dessine les joueurs
	}

	
}