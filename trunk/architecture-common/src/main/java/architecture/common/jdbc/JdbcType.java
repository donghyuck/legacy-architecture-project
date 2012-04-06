package architecture.common.jdbc;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author   donghyuck
 */
public enum JdbcType {
	/*
	 * This is added to enable basic support for the ARRAY data type - but a
	 * custom type handler is still required
	 */
	
	/**
	 * @uml.property  name="aRRAY"
	 * @uml.associationEnd  
	 */
	ARRAY(Types.ARRAY), 
	/**
	 * @uml.property  name="bIT"
	 * @uml.associationEnd  
	 */
	BIT(Types.BIT), 
	/**
	 * @uml.property  name="tINYINT"
	 * @uml.associationEnd  
	 */
	TINYINT(Types.TINYINT), 
	/**
	 * @uml.property  name="sMALLINT"
	 * @uml.associationEnd  
	 */
	SMALLINT(Types.SMALLINT), 
	/**
	 * @uml.property  name="iNTEGER"
	 * @uml.associationEnd  
	 */
	INTEGER(Types.INTEGER), 
	/**
	 * @uml.property  name="bIGINT"
	 * @uml.associationEnd  
	 */
	BIGINT(Types.BIGINT), 
	/**
	 * @uml.property  name="fLOAT"
	 * @uml.associationEnd  
	 */
	FLOAT(Types.FLOAT), 
	/**
	 * @uml.property  name="rEAL"
	 * @uml.associationEnd  
	 */
	REAL(Types.REAL), 
	/**
	 * @uml.property  name="dOUBLE"
	 * @uml.associationEnd  
	 */
	DOUBLE(Types.DOUBLE), 
	/**
	 * @uml.property  name="nUMERIC"
	 * @uml.associationEnd  
	 */
	NUMERIC(Types.NUMERIC), 
	/**
	 * @uml.property  name="dECIMAL"
	 * @uml.associationEnd  
	 */
	DECIMAL(Types.DECIMAL), 
	/**
	 * @uml.property  name="cHAR"
	 * @uml.associationEnd  
	 */
	CHAR(Types.CHAR), 
	/**
	 * @uml.property  name="vARCHAR"
	 * @uml.associationEnd  
	 */
	VARCHAR(Types.VARCHAR), 
	/**
	 * @uml.property  name="lONGVARCHAR"
	 * @uml.associationEnd  
	 */
	LONGVARCHAR(Types.LONGVARCHAR), 
	/**
	 * @uml.property  name="dATE"
	 * @uml.associationEnd  
	 */
	DATE(Types.DATE), 
	/**
	 * @uml.property  name="tIME"
	 * @uml.associationEnd  
	 */
	TIME(Types.TIME), 
	/**
	 * @uml.property  name="tIMESTAMP"
	 * @uml.associationEnd  
	 */
	TIMESTAMP(Types.TIMESTAMP), 
	/**
	 * @uml.property  name="bINARY"
	 * @uml.associationEnd  
	 */
	BINARY(Types.BINARY), 
	/**
	 * @uml.property  name="vARBINARY"
	 * @uml.associationEnd  
	 */
	VARBINARY(Types.VARBINARY), 
	/**
	 * @uml.property  name="lONGVARBINARY"
	 * @uml.associationEnd  
	 */
	LONGVARBINARY(Types.LONGVARBINARY), 
	/**
	 * @uml.property  name="nULL"
	 * @uml.associationEnd  
	 */
	NULL(Types.NULL), 
	/**
	 * @uml.property  name="oTHER"
	 * @uml.associationEnd  
	 */
	OTHER(Types.OTHER), 
	/**
	 * @uml.property  name="bLOB"
	 * @uml.associationEnd  
	 */
	BLOB(Types.BLOB), 
	/**
	 * @uml.property  name="cLOB"
	 * @uml.associationEnd  
	 */
	CLOB(Types.CLOB), 
	/**
	 * @uml.property  name="bOOLEAN"
	 * @uml.associationEnd  
	 */
	BOOLEAN(Types.BOOLEAN), 
	/**
	 * @uml.property  name="cURSOR"
	 * @uml.associationEnd  
	 */
	CURSOR(-10), // Oracle
	/**
	 * @uml.property  name="uNDEFINED"
	 * @uml.associationEnd  
	 */
	UNDEFINED(Integer.MIN_VALUE + 1000), 
	/**
	 * @uml.property  name="nVARCHAR"
	 * @uml.associationEnd  
	 */
	NVARCHAR(-9), // JDK6
	/**
	 * @uml.property  name="nCHAR"
	 * @uml.associationEnd  
	 */
	NCHAR(-15), // JDK6
	/**
	 * @uml.property  name="nCLOB"
	 * @uml.associationEnd  
	 */
	NCLOB(2011), // JDK6
	/**
	 * @uml.property  name="sTRUCT"
	 * @uml.associationEnd  
	 */
	STRUCT(Types.STRUCT);

	public final int TYPE_CODE;
	
	private static Map<Integer, JdbcType> codeLookup = new HashMap<Integer, JdbcType>();

	static {
		for (JdbcType type : JdbcType.values()) {
			codeLookup.put(type.TYPE_CODE, type);
		}
	}

	JdbcType(int code) {
		this.TYPE_CODE = code;
	}

	public static JdbcType forCode(int code) {
		return codeLookup.get(code);
	}

}