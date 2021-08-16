package unessdb;

import java.util.ArrayList;

public enum TABLE {
	// ---- Enum ----
	USER("User", "U"),
	ENTERPRISE("Enterprise", "E"),
	LOCATION("Location", "L"),
	MODEL("Model", "M"),
	DEVICE("Device", "D"), 
	LOG("Log", "L"),  
	RMA("RMA", "R"),
	GROUP("Group", "G");

	private String accessName;
	private String accessAlias;
	
	// ---- Constructor ----

	TABLE(String p_accessName, String p_accessAlias) {
		accessName = p_accessName;
		accessAlias = p_accessAlias;
	}

	// ---- Setters ----
	
	// ---- Getters ----
	public final String getAccessName() {
		return accessName;
	}

	public final String getAccessAlias() {
		return accessAlias;
	}
	
	public final FIELD[] getFields()
	{
		final FIELD[] fields = FIELD.values();
		final ArrayList<FIELD> tableFields = new ArrayList<FIELD>();
		for(final FIELD f : fields)
		{
			
			if(f.getTable() == this)
			{
				tableFields.add(f);
			}
		}
		
		final FIELD[] retVal = tableFields.toArray(new FIELD[]{});
		
		return retVal;
	}
	
	
}
