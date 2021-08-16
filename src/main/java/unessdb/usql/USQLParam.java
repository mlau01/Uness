package unessdb.usql;

import unessdb.FIELD;
import unessdb.usql.USQL.OPERATOR;

public class USQLParam {
	
	private FIELD field;
	private Object value;
	private OPERATOR operator;
	private boolean strict;
	
	public USQLParam(FIELD p_field, Object p_value, boolean p_strict)
	{
		setField(p_field);
		setValue(p_value);
		setStrict(p_strict);
		setOperator(OPERATOR.LIKE);
	}
	
	public USQLParam(FIELD p_field, OPERATOR p_op, Object p_value, boolean p_strict)
	{
		setField(p_field);
		setValue(p_value);
		setStrict(p_strict);
		setOperator(p_op);
	}
	


	/**
	 * @return the field
	 */
	public FIELD getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(FIELD field) {
		this.field = field;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the operator
	 */
	public OPERATOR getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(OPERATOR operator) {
		this.operator = operator;
	}

	/**
	 * @return the strict
	 */
	public boolean isStrict() {
		return strict;
	}

	/**
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}

}
