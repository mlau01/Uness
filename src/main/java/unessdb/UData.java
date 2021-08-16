package unessdb;

import java.util.AbstractMap.SimpleEntry;

import unessdb.usql.USQLParam;
import unessdb.usql.USQLParams;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class UData {

	
	private final boolean verbose = false;
	private ArrayList<SimpleEntry<FIELD, Object>> datas;
	 
	
	//----- Constructors -----
	public UData()
	{
		datas = new ArrayList<SimpleEntry<FIELD, Object>>();
	}
	
	
	public UData(final FIELD[] p_fields, final Object[] p_values)
	{
		datas = new ArrayList<SimpleEntry<FIELD, Object>>();
		for(int i = 0; i < p_fields.length; i++)
		{
			put(p_fields[i], p_values[i]);
		}
	}
	// ---- Setters ----

	public void put(final FIELD f, Object s)
	{
		//null value replaced by empty value
		if(s == null)
			s = "";
		
		SimpleEntry<FIELD, Object> entry = new SimpleEntry<FIELD, Object>(f, s);
		
		datas.add(entry);
		if(verbose)
			System.out.println(this.getClass().getName() + " -> function put(final FIELD f, Object s) > putting: " + f + "(" + (String)s + ")");
	}
	
	/**
	 * 
	 * @param wanted field
	 * @return The value of the field if present, null otherwise
	 */
	public final Object getValue(final FIELD f)
	{
		for(final SimpleEntry<FIELD, Object> e : datas)
		{
			if(e.getKey() == f)
			{
				return e.getValue();
			}
		}
		
		return null;
	}

	public final ArrayList<SimpleEntry<FIELD, Object>> getValues(){
		return datas;
	}
	public final FIELD[] getFields()
	{
		FIELD[] array = new FIELD[datas.size()];
		
		for(int i = 0; i < datas.size(); i++)
		{
			array[i] = datas.get(i).getKey();
		}
		
		return array;
	}
	
	
	public final FIELD getIdField() throws UDataException
	{
		for(final SimpleEntry<FIELD, Object> e : datas)
		{
			if(e.getKey().getFieldNumber() == 1)
				return e.getKey();
		}
		
		return null;
	}
	
	
	public final Object getIdValue()
	{
		for(final SimpleEntry<FIELD, Object> e : datas)
		{
			if(e.getKey().getFieldNumber() == 1)
				return e.getValue();
		}
		
		return null;
	}
	
	public final SimpleEntry<FIELD, Object> getEntry(FIELD f){
		for(SimpleEntry<FIELD, Object> e : datas){
			if(e.getKey() == f) return e;
		}
		
		return null;
	}
	
	/**
	 * Method to edit a field already in the list
	 * @param field Field name
	 * @param value Value to set
	 * @throws UDataException if the field is not found
	 */
	public void set(final FIELD field, final Object value) throws UDataException
	{
		for(final SimpleEntry<FIELD, Object> e : datas)
		{
			if(e.getKey() == field)
			{
				e.setValue(value);
				return;
			}
		}
		
		throw new UDataException("Field: " + field + " not found");
	}
	
	public void add(final FIELD field, final Object value)
	{
		datas.add(new SimpleEntry<FIELD, Object>(field, value));
	}
	
	public final String toString()
	{
		String str = "";
		for(final SimpleEntry<FIELD, Object> e : datas)
		{
			if( ! str.isEmpty())
				str += ", ";
			
			str += e.getKey() + ": " + e.getValue();
		}
		
		return str;
	}
	
	public final String getCommaSeparatedValues()
	{
		String str = "";
		for(final SimpleEntry<FIELD, Object> e : datas)
		{
			if( ! str.isEmpty())
				str += ",";
			
			str += e.getValue();
		}
		
		return str;
	}
	
	public final UData update(UData updateDatas)
	{
		for(SimpleEntry<FIELD, Object> e : updateDatas.getValues()){
			getEntry(e.getKey()).setValue(e.getValue());
		}
		
		return this;
	}
	
	public final boolean isEmpty()
	{
		return datas.isEmpty();
	}
	
	public final int getSize()
	{
		return datas.size();
	}
	
	//------ Static ------
	
	public static final UData concat(final UData data1, final UData data2)
	{
		UData newDatas = new UData();
		for(FIELD field : data1.getFields())
		{
			newDatas.put(field, data1.getValue(field));
		}
		for(FIELD field : data2.getFields())
		{
			newDatas.put(field, data2.getValue(field));
		}
		
		return newDatas;
	}
	
	public static final UData concat(final UData data1, final USQLParams params)
	{
		UData newDatas = new UData();
		for(FIELD field : data1.getFields())
		{
			newDatas.put(field, data1.getValue(field));
		}
		for(USQLParam p : params.getList())
		{
			newDatas.put(p.getField(), p.getValue());
		}
		
		return newDatas;
	}
	
	public static final UData copy(final UData src)
	{
		UData datas = new UData();
		for(FIELD field : src.getFields())
		{
			Object value = src.getValue(field);
			
			datas.put(field, value);
		}
		
		return datas;
	}
	
}
