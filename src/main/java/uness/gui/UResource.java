package uness.gui;

import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

import uness.Uness;

public class UResource {
	
	private final UWin uwin;
	private final Display display;
	private final Resource[] resources;
	
	public static final int COLOR_RED = 0;
	public static final int COLOR_PURPLE = 1;
	public static final int COLOR_YELLOW = 2;
	public static final int COLOR_GREEN = 3;
	public static final int COLOR_BLUE = 4;
	public static final int COLOR_MARINE = 5;
	public static final int COLOR_GREY = 6;
	public static final int COLOR_WHITE = 7;
	public static final int COLOR_BACKGROUND = 8;
	public static final int IMG_VNC = 9;
	public static final int IMG_EXPLORER = 10;
	public static final int IMG_SEARCH = 11;
	public static final int IMG_SWAP = 12;
	public static final int IMG_RMA = 13;
	public static final int IMG_NEWDEV = 14;
	public static final int IMG_EDIT = 15;
	public static final int IMG_BOMB = 16;
	public static final int IMG_NOTE = 17;
	public static final int IMG_PRINT = 18;
	public static final int IMG_UNESS = 19;
	private int RES_SIZE = 20;
	
	public UResource(UWin p_uwin)
	{
		uwin = p_uwin;
		display = uwin.getDisplay();
		resources = new Resource[RES_SIZE];
		
		resources[COLOR_RED] = new Color(display, 200, 150, 150);
		resources[COLOR_PURPLE] = new Color(display, 245, 240, 255);
		resources[COLOR_YELLOW] = new Color(display, 200, 200, 50);
		resources[COLOR_GREEN] = new Color(display, 150, 200, 150);
		resources[COLOR_BLUE] = new Color(display, 230, 240, 255);
		resources[COLOR_MARINE] = new Color(display, 160, 190, 220);
		resources[COLOR_GREY] = new Color(display, 208, 208, 208);
		resources[COLOR_WHITE] = new Color(display, 255, 255, 255);
		resources[COLOR_BACKGROUND] = new Color(display, 230, 230, 230);
		resources[IMG_VNC] = load_image("vnc.png");
		
		resources[IMG_EXPLORER] = load_image("explorer.png");
		resources[IMG_SEARCH] = load_image("scope_24x24.png");
		resources[IMG_SWAP] = load_image("Retweet_24x24.png");
		resources[IMG_RMA] = load_image("Transport_32x32.png");
		resources[IMG_NEWDEV] = load_image("Chip_32x32.png");
		resources[IMG_EDIT] = load_image("Account_card_32x32.png");
		resources[IMG_BOMB] = load_image("Nuclear_explosion_32x32.png");
		resources[IMG_NOTE] = load_image("Notepad_32x32.png");
		resources[IMG_PRINT] = load_image("Typewriter_32x32.png");
		resources[IMG_UNESS] = load_image("Www_48x48.png");
	}

	public Color get_color(int color)
	{
		return (Color)resources[color];
	}
	
	private Image load_image(String imgName)
	{
		String imgPath = Uness.iconsPath + imgName;
		InputStream imgIs = this.getClass().getResourceAsStream(imgPath);
		if (imgIs == null) System.out.println("Cannot get image: " + imgPath);
		Image img = new Image(uwin.getDisplay(), imgIs);
		
		return img;
	}
	
	public Image get_image(int image)
	{
		return (Image)resources[image];
	}
	
	public void dispose()
	{
		for(Resource r : resources)
		{
			if(r != null) r.dispose();
		}
			
	}

}
