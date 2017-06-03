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
	
	private int hitx;
	private int hity;
	
	private int life;
	
	private int speed;
		
	private int nb_bomb;
	Bombe [] bombe=new Bombe [10] ;
	
	private int bb_bef;
	private int bb_exp;
	
	private long timer;
	private int ttm;
	private int inv;
	
	

	public Joueur (Terrain terrain, int id, int positionX, int positionY)
	{	this.life=3;
		this.speed=3;
		this.nb_bomb=3;
		this.timer=0;
		this.inv=0;
		this.ttm=2000;
		this.sens="front_profile";
		
		this.id=id;
		this.positionX=positionX;
		this.positionY=positionY;
		
		this.hitx=3*terrain.getwidth()/4;
		this.hity=3*terrain.getwidth()/4;
		
		int i=0;
		
		for (i=0; i<10; i++)
		{	bombe [i]=new Bombe();
		}
		
		this.bb_bef=5000;
		this.bb_exp=1000;
	}
	
	public int getPositionX()
	{
		return this.positionX;
	}
	
	public int getPositionY()
	{
		return this.positionY;
	}
	public int getnbbombe ()
	{	
		return this.nb_bomb;
	}
	
	public int getlife ()
	{	
		return this.life;
	}
	public int getnbbomb ()
	{	
		return this.nb_bomb;
	}
	public Bombe getbomb (int num)
	{	
		return this.bombe[num];
	}
	
	public void setLife(int life)
	{
		this.life = life;
	}
		
	public Terrain put_bombe(Terrain terrain)
	{
		int i=0;
		int test=0;
		
		int sx=this.positionX/(terrain.getwidth()*2);
		int sy=this.positionY/(terrain.getheigth()*2);
		
		while (test==0)
		{	if (i==this.nb_bomb)
			{	test=1;
			}
			else
			{	if (this.bombe[i].getactivate()==0)
				{	
					terrain=this.bombe[i].put_bombe(this.bb_bef, this.bb_exp, bombe[i].getPuissance(), sx, sy, terrain);
					test=1;
				}
				else
					i=i+1;
			}
		}
		
		return terrain;
	}
	
	public void setbon (int val)
	{
		if(val==10 && this.bombe[0].getPuissance()>1)
		{
			System.out.println("Bonus 1");
			for(int i = 0 ; i < this.bombe.length ; i++)
			{
				this.bombe[i].setPuissance(bombe[i].getPuissance()-1);
			}
		}
		else if(val==20 && this.bombe[0].getPuissance()<10)
		{
			System.out.println("Bonus 2");

			for(int i = 0 ; i < this.bombe.length ; i++)
			{
				this.bombe[i].setPuissance(bombe[i].getPuissance()+1);
			}
		}
		else if(val == 30)
		{
			System.out.println("Bonus 3");

			for(int i = 0 ; i < this.bombe.length ; i++)
			{
				this.bombe[i].setPuissance(10);
			}
		}
		else if(val == 40)
		{
			System.out.println("Bonus 4");

		}
		else if(val == 50)
		{
			System.out.println("Bonus 5");

			this.life++;
		}
		else if(val == 60)
		{
			System.out.println("Bonus 6");

			this.speed++;
			
		}
		else if(val==70 && this.speed>1)
		{
			System.out.println("Bonus 7");

			this.speed--;
		}
		else if (val==80)
		{
			System.out.println("Bonus 8");

			Audio sound3 = new Audio("Bonus");
			this.nb_bomb=this.nb_bomb+2;
			if (this.nb_bomb>7)
				this.nb_bomb=7;
		}
		else if(val == 90)
		{
			System.out.println("Bonus 9");

			this.nb_bomb=this.nb_bomb-2;
			if (this.nb_bomb<2)
				this.nb_bomb=2;
		}
	
	}
	
	
	public Terrain bon_deg(Terrain terrain)
	{
		int sx=this.positionX/(terrain.getwidth()*2);
		int sy=this.positionY/(terrain.getheigth()*2);
		
		if ((terrain.gettab(sx, sy)>=10)&&(terrain.gettab(sx, sy)<=90))
		{
			this.setbon(terrain.gettab(sx, sy));
			terrain.set(sx, sy, 0);
		}
		/*--------------------------------------------------------------------------------------------------------------------------*/
		if (this.positionX+this.hitx>(sx+1)*2*terrain.getwidth())
		{

			
			if ((terrain.gettab(sx+1, sy)>=10)&&(terrain.gettab(sx+1, sy)<=90))
			{
				this.setbon(terrain.gettab(sx+1, sy));
				terrain.set(sx+1, sy, 0);
			}
		}
		if (this.positionX-this.hitx<(sx)*2*terrain.getwidth())
		{

			
			if ((terrain.gettab(sx-1, sy)>=10)&&(terrain.gettab(sx-1, sy)<=90))
			{
				this.setbon(terrain.gettab(sx-1, sy));
				terrain.set(sx-1, sy, 0);
			}
		}
		if (this.positionY+this.hity>(sy+1)*2*terrain.getheigth())
		{

			
			if ((terrain.gettab(sx, sy+1)>=10)&&(terrain.gettab(sx, sy+1)<=90))
			{
				this.setbon(terrain.gettab(sx, sy+1));
				terrain.set(sx, sy+1, 0);
			}
		}
		if (this.positionY-this.hity<(sy)*2*terrain.getheigth())
		{	

			
			if ((terrain.gettab(sx, sy-1)>=10)&&(terrain.gettab(sx, sy-1)<=90))
			{
				this.setbon(terrain.gettab(sx, sy-1));
				terrain.set(sx, sy-1, 0);
			}
		}
		
		return terrain;
	}
	
	public void  move(String move, Terrain terrain)
	{	
		int mem_x=this.positionX;
		int mem_y=this.positionY;
		
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
	
	public void draw()
	{	
		String playerImage = "player_" + (this.id+1) + "_" + this.sens + ".png";
		StdDraw.picture(positionX, positionY, playerImage, 33, 50);
	}

	public boolean noObstacle(String move, Terrain terrain)
	{
		int spaceAllow = 0;
		int casePositionToCheck=0;
		int playerPositionXInTab = this.positionX/(terrain.getwidth()*2);
		int playerPositionYInTab = this.positionY/(terrain.getheigth()*2);

		if (move == "up")
		{
			spaceAllow=2*terrain.getheigth()/3;
			casePositionToCheck = (this.positionY+spaceAllow)/(terrain.getheigth()*2);

			if(terrain.gettab(playerPositionXInTab, casePositionToCheck)==1
			 ||terrain.gettab(playerPositionXInTab, casePositionToCheck)==2)
			{
				return false;
			}
		}
		else if(move == "left")
		{
			spaceAllow=2*terrain.getwidth()/3;
			casePositionToCheck = (this.positionX-spaceAllow)/(terrain.getwidth()*2);

			if(terrain.gettab(casePositionToCheck, playerPositionYInTab)==1
			 ||terrain.gettab(casePositionToCheck, playerPositionYInTab)==2)
			{
				return false;
			}

		}
		else if(move == "right")
		{
			spaceAllow=2*terrain.getwidth()/3;
			casePositionToCheck = (this.positionX+spaceAllow)/(terrain.getwidth()*2);

			if(terrain.gettab(casePositionToCheck, playerPositionYInTab)==1
			 ||terrain.gettab(casePositionToCheck, playerPositionYInTab)==2)
			{
				return false;
			}
		}
		else if(move == "down")
		{
			spaceAllow=2*terrain.getheigth()/3;
			casePositionToCheck = (this.positionY-spaceAllow)/(terrain.getheigth()*2);

			if(terrain.gettab(playerPositionXInTab, casePositionToCheck)==1
			 ||terrain.gettab(playerPositionXInTab, casePositionToCheck)==2)
			{
				return false;
			}
		}
		return true;
	}
}
