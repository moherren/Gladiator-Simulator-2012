package com.mime.evolve.graphics;

import java.util.Arrays;

import com.mime.evolve.Controller;
import com.mime.evolve.Game;
import com.mime.evolve.Victory;
import com.mime.evolve.projectiles.Projectile;

public class Screen extends Render{
	private Render2D render;
	public Screen(int width, int height){
		super(width,height);
		render=new Render2D(width,height);
	}
	public void render(Controller game){
		Arrays.fill(render.depthMap, 0);
		render.Arena();
		
		render.Player(game.player1);
		render.Player(game.player2);
		if(game.execusioner!=null)
			render.Player(game.execusioner);
		
		for(Projectile p:game.projectiles){
			render.drawProjectile(p);
		}
		
		render.displayFightInformation(game);
		
		draw(render,0,0);
	}
	public void renderLoadingScreen(Game game,double loadingAmount){
		render.background();
		render.loadingBar(loadingAmount);
		draw(render,0,0);
	}
	public void renderWinningScreen(Victory vic){
		render.winScreen(vic);
		draw(render,0,0);
	}
}
