package uness.gui.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import uness.core.Device;
import uness.core.Manage;
import uness.core.TreeNode;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.interfaces.Prefillable;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.usql.USQLParam;
import unessdb.usql.USQLParams;

public class BrowsePanel implements Prefillable{
	
	private final UWin uWin;
	
	private Composite mainContainer;
	private final UResource resourceManager;
	private final Device deviceManager;
	private final Manage manageManager;
	private Group details;
	private final int CONTAINER_STYLE = SWT.BORDER;
	
	private Tree browse;
	
	public BrowsePanel(final UWin p_uWin)
	{
		uWin = p_uWin;
		deviceManager = uWin.getCorma().getDeviceManager();
		manageManager = uWin.getCorma().getManageManager();
		resourceManager = uWin.getResourceManager();
	}
	
	public Composite create(TabFolder tabFolder, int compositeStyle) {
		mainContainer = new Composite(tabFolder, compositeStyle);
		{
			mainContainer.setLayout(new GridLayout(2, false));
			
			browse = new Tree(mainContainer, SWT.BORDER);
			browse.addListener(SWT.Selection, e -> action_select_treeitem());
			browse.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, true));
			
			details = new Group(mainContainer, CONTAINER_STYLE);
			details.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			{
				details.setLayout(new GridLayout());
				
			}
		}
		
		return mainContainer;
	}
	
	private void action_create_device(UData device)
	{
		Composite cdevice = new Composite(details, CONTAINER_STYLE);
		{
			cdevice.setLayout(new RowLayout(SWT.HORIZONTAL));
			
			new Label(cdevice, SWT.NONE).setText(String.valueOf(device.getValue(FIELD.DEVICE_model)));
			String hostname = String.valueOf(device.getValue(FIELD.DEVICE_networkName));
			
			if(hostname != null && ! hostname.isEmpty() &&  ! hostname.equals("null")) {
				Button vnc = new Button(cdevice, SWT.PUSH);
				vnc.setImage(resourceManager.get_image(UResource.IMG_VNC));
				vnc.addListener(SWT.Selection, e -> action_open_vnc(hostname));
				
				Button explorer = new Button(cdevice, SWT.PUSH);
				explorer.setImage(resourceManager.get_image(UResource.IMG_EXPLORER));
				explorer.addListener(SWT.Selection, e -> action_open_explorer(hostname));
			}
		}
		
		//devices.layout(true, true);
		details.pack();
	}
	
	private void action_create_monitoredArea(String area,ArrayList<UData> devices, Hashtable<String, ArrayList<UData>> modelTable)
	{
		Group carea = new Group(details, CONTAINER_STYLE);
		carea.setText(area);
		{
			carea.setLayout(new GridLayout(2, true));
			
			Composite device = new Composite(carea, CONTAINER_STYLE);
			{
				device.setLayout(new GridLayout());
			
				for(UData dev : devices) {
					String location = String.valueOf(dev.getValue(FIELD.DEVICE_location));
					String networkName = String.valueOf(dev.getValue(FIELD.DEVICE_networkName));
					new Label(device, SWT.NONE).setText(networkName);
				}
			}
			
			Composite modelStat = new Composite(carea, CONTAINER_STYLE);
			{
				modelStat.setLayout(new GridLayout());
				for(String model : modelTable.keySet())
				{
					new Label(modelStat, SWT.NONE).setText(model + ": " + modelTable.get(model).size());
					
				}
			}

		}
		
		details.pack();
	}
	
	private void action_open_vnc(String hostname)
	{
		Process process = null;
		try {
			process = new ProcessBuilder("vncviewer.exe","" , hostname + ":5900 -password RESA").start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void action_open_explorer(String hostname)
	{
		try {
			Process process = new ProcessBuilder("\\\\" + hostname + "\\c$\\").start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void action_select_treeitem()
	{
		action_clear_locationDisplay();
		TreeItem selection = browse.getSelection()[0];
		details.setText(selection.getText());
		try {
			if(isTreeItemLocation(selection)) action_load_locationDisplay(selection);
			else action_load_groupDisplay(selection);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private void action_clear_locationDisplay()
	{
		details.setText("");
		for(Control c : details.getChildren())
			c.dispose();
		
		details.pack();
	}
	
	private void action_load_groupDisplay(TreeItem selection) throws DBException
	{
		ArrayList<UData> locations = null;
		ArrayList<UData> networkDevices = new ArrayList<UData>();
		Hashtable<String, ArrayList<UData>> modelTable = new Hashtable<String, ArrayList<UData>>();
	
		locations = uWin.getCorma().getManageManager().getLocations(new USQLParams(FIELD.LOCATION_group, selection.getText()), null, null);
		if(locations == null) return;
		
		for(UData loc : locations) {
			String locationName = String.valueOf(loc.getValue(FIELD.LOCATION_location));
			ArrayList<UData> locDevice = deviceManager.getDevices(new USQLParams(FIELD.DEVICE_location, locationName));
			for(UData device : locDevice) {
				String networkName = String.valueOf(device.getValue(FIELD.DEVICE_networkName));
				String model = String.valueOf(device.getValue(FIELD.DEVICE_model));
				boolean found = false;
				
				if(networkName != null && ! networkName.isEmpty() && ! networkName.equals("null"))
					networkDevices.add(device);
				
				for(String exist_model : modelTable.keySet())
				{
					if(model.equals(exist_model))
					{
						modelTable.get(exist_model).add(device);
						found = true;
						break;
					}
				}
				if(! found) {
					modelTable.put(model, new ArrayList<UData>());
					modelTable.get(model).add(device);
				}
				
			}
		}
		
		action_create_monitoredArea(selection.getText(), networkDevices, modelTable);
	}
	private void action_load_locationDisplay(TreeItem selection) throws DBException{
		
		ArrayList<UData> datas = deviceManager.getDevices(new USQLParams(FIELD.DEVICE_location, selection.getText()));
		for(UData device : datas)
			action_create_device(device);
	}
	
	public void fillDatas()
	{
		browse.removeAll();
		ArrayList<UData> locations = null;
		ArrayList<UData> groups = null;
		try {
			locations = uWin.getCorma().getManageManager().getLocations(null,null, null);
			groups = uWin.getCorma().getManageManager().getGroups();
		} catch (DBException e) {
			e.printStackTrace();
			uWin.showError(e.getClass().getName(), e.getMessage());
		}
		
		//Search root node
		TreeItem root = null;
		for(final UData g : groups)
		{
			if( ((String)g.getValue(FIELD.GROUP_parentgroup)).equals(""))
			{	
				root = new TreeItem(browse, SWT.NONE);
				root.setText((String)g.getValue(FIELD.GROUP_group));
				root.setData(false);
				break;
			}
		}
		getChild(root, groups, locations);
		
		browse.setSelection(root);
		browse.pack(true);
		mainContainer.layout(true, true);
		

	}
	
	private void getChild(TreeItem parent, final ArrayList<UData> groups, final ArrayList<UData> locations)
	{
		for(UData group : groups)
		{
			if(((String)group.getValue(FIELD.GROUP_parentgroup)).equals(parent.getText())){
				TreeItem child = new TreeItem(parent, SWT.NONE);
				child.setData(false);
				child.setText((String)group.getValue(FIELD.GROUP_group));
				for(UData location : locations)
				{
					if(((String)location.getValue(FIELD.LOCATION_group)).equals(child.getText())){
						TreeItem item = new TreeItem(child, SWT.NONE);
						item.setData(true);
						item.setText((String)location.getValue(FIELD.LOCATION_location));
						browse.showItem(item);
					}
				}
				getChild(child, groups, locations);
			}
		}

	}
	
	private boolean isTreeItemLocation(TreeItem item)
	{
		return (boolean)item.getData();
	}
	
}
