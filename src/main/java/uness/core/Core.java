package uness.core;

import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQLManager;

public class Core {
	
	private final DBManagerInterface acMan;
	private final Option opMan;
	private final Control contMan;
	private final Device devMan;
	private final Log logMan;
	private final Rma rmaHandlMan;
	private final Swap swaMan;
	private final Users userMan;
	private final Import importMan;
	private final Export export;
	private final Manage manage;
	private final Model model;
	
	public Core() throws OptionException
	{
		opMan = new Option();
		acMan = new USQLManager();
		
		contMan = new Control(acMan);
		devMan = new Device(this, acMan);
		logMan = new Log(acMan);
		rmaHandlMan = new Rma(this, acMan);
		swaMan = new Swap(acMan);
		userMan = new Users(acMan);
		importMan = new Import();
		manage = new Manage(acMan);
		export = new Export();
		model = new Model(acMan);
	}
	
	// ---- Manager Getters ----
	public final Import getImportManager()
	{
		return importMan;
	}
	
	public final DBManagerInterface getAccessManager()
	{
		return acMan;
	}
	public final Device getDeviceManager()
	{
		return devMan;
	}
	public final Control getControlManager()
	{
		return contMan;
	}
	public final Option getOptionManager()
	{
		return opMan;
	}

	public final Log getLogMan()
	{
		return logMan;
	}
	public final Rma getRmaManager()
	{
		return rmaHandlMan;
	}

	public final Swap getSwapManager()
	{
		return swaMan;
	}
	public final Users getUsersManager()
	{
		return userMan;
	}
	
	public final Manage getManageManager()
	{
		return manage;
	}

	public final Export getExportManager()
	{
		return export;
	}
	
	public final Model getModelManager()
	{
		return model;
	}

}
