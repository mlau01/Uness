package uness.gui.common;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

public class ComboUtils {
	
	public static void selectComboItem(Control control, String item)
	{
		Combo combo = (Combo)control;
		int itemCount = combo.getItemCount();
		String[] items = combo.getItems();
		for(int i = 0; i < itemCount; i++)
		{
			if(items[i].equals(item))
			{
				combo.select(i);
				return;
			}
		}
	}
	
	public static final boolean valueNotFound(Control control)
	{
		Combo combo = (Combo)control;
		int itemCount = combo.getItemCount();
		String text = combo.getText();
		String[] items = combo.getItems();
		for(int i = 0; i < itemCount; i++)
		{
			if(items[i].equals(text)) return false;
		}
		
		return true;
	}

}
