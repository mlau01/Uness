package unessdb.usql;

public class USQL {
	
	public enum OPERATOR {
		LIKE("LIKE"),
		EQ("="),
		BET("><"),
		GT(">"),
		LT("<"),
		GTE(">="),
		LTE("<=");
		
		private String op;
		OPERATOR(String p_op)
		{
			op = p_op;
		}
		
		public final String get(){
			return op;
		}
		
		public static final OPERATOR get(String token)
		{
			for(OPERATOR op : OPERATOR.values())
			{
				if(op.get().equals(token)) return op;
			}
			
			return null;
		}
	}
	
	public enum SYNTAX {
		SQL, TSQL
	};
	public enum ACTION {
		SELECT, INSERT, UPDATE, DELETE, MAX, COUNT
	};
	
	public enum FIELD_TYPE {
		TEXT,
		DIGIT,
		DATE
	}
	
	public enum DB {
		access,
		mysql,
		mssql
	}

}
