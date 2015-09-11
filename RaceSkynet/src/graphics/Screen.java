package graphics;

import java.util.Arrays;

public class Screen extends Render{
	public Render2D render;
	public Screen(int width, int height){
		super(width,height);
		render=new Render2D(width,height);
	}
	public void render(){
		render.render();
		draw(render,0,0);
	}
}
