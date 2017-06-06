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
		int i=0;
		int j=0;
		
		int rayon;
		
		if (this.halfWidthOfRow>this.halfHeigthOfLine)
			rayon=this.halfHeigthOfLine;
		else
			rayon=this.halfWidthOfRow;
		
		int orgx=this.halfWidthOfRow;
		int orgy=this.halfHeigthOfLine;

		for (i=0;i<this.numberOfRow;i++)
		{	
			for (j=0;j<this.numberOfLine;j++)
			{	
				if (this.tab[i][j]==1)
				{
					StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				}
				if (this.tab[i][j]==0)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Caisse.png", 50, 50);
				}
				else if (this.tab[i][j]==-1)
				{	
					StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Pelouse.png", 50, 50);
				}
				else if (this.tab[i][j]>=666)
				{	
					StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Explosion.png", 50, 50);
				}
				else if (this.tab[i][j]==10)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_flamme_bleue.png", 35, 33);
				}
				else if (this.tab[i][j]==20)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_flamme_jaune.png", 35, 33);
				}
				else if (this.tab[i][j]==30)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_flamme_rouge.png", 35, 33);
				}
				else if (this.tab[i][j]==40)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_bombe_rouge.png", 35, 33);
				}
				else if (this.tab[i][j]==50)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_vie.png", 35, 33);
				}
				else if (this.tab[i][j]==60)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_speed_up.png", 35, 33);
				}
				else if (this.tab[i][j]==70)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_speed_down.png", 35, 33);
				}
				else if (this.tab[i][j]==80)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_bombe_plus.png", 35, 33);
				}
				else if (this.tab[i][j]==90)
				{	StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "Herbe.png", 50, 50);
				StdDraw.picture((i*2*this.halfWidthOfRow)+orgx, (j*2*this.halfHeigthOfLine)+orgy, "bonus_bombe_moins.png", 35, 33);
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
		
		for (i=0;i<joueur.length;i++)
		{
			for (j=0; j<joueur[i].getNumberOfBomb();j++) // On dessine les bombes
				joueur[i].bombe[j].draw(this);
		}
		
		for (i=0;i<joueur.length;i++)
			joueur[i].draw();	// on dessine les joueurs
	}

	
}