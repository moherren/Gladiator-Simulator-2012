package graphics;

public class Screen extends Render{
	public Render2D render;
	public static int centerX,centerY,width,height;
	public Screen(int width, int height){
		super(width,height);
		render=new Render2D(width,height);
		centerX=width/2;
		centerY=height/2;
		Screen.width=width;
		Screen.height=height;
	}
	public void render(){
		render.render();
		draw(render,0,0);
	}
}
