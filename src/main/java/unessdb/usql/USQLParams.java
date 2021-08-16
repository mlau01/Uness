package unessdb.usql;

import java.util.ArrayList;

import unessdb.FIELD;
import unessdb.usql.USQL.OPERATOR;

public class USQLParams {
	
	ArrayList<USQLParam> params;
	
	public USQLParams()
	{
		params = new ArrayList<USQLParam>();
	}
	
	public USQLParams(FIELD p_field, Object p_value)
	{
		params = new ArrayList<USQLParam>();
		params.add(new USQLParam(p_field, p_value, true));
	}
	
	public void add(FIELD p_field, Object p_value, boolean p_strict)
	{
		params.add(new USQLParam(p_field, p_value, p_strict));
	}
	
	public void add(FIELD p_field, OPERATOR p_op, Object p_value, boolean p_strict)
	{
		params.add(new USQLParam(p_field, p_op, p_value, p_strict));
	}
	
	public ArrayList<USQLParam> getList()
	{
		return params;
	}
	
	public String toString()
	{
		String out = "";
		for(USQLParam param : params)
		{
			if( ! out.isEmpty()) out += "\n";
			out += param.getField().toString() + " "+ param.getOperator().toString() + param.getValue();
		}
		
		return out;
	}

}
