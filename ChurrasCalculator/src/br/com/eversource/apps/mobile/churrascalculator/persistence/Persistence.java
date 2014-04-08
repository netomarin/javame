package br.com.eversource.apps.mobile.churrascalculator.persistence;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;


public abstract class Persistence {
	//Possible operatons in the Persistence
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
	public static final int SAVE = 4;
	public static final int LOAD = 5;

	//This ID is used in controlling the content in and out of
	//the RecordStore
	//WARNING: if this is set to -1, it means the data will be
	//INSERT into the RecordStore
	private int ID = -1;

	/**
	 * Return the ID for this content inside the RecordStore */
	public int getID() {
		return ID;
	}

	/**
	 * Sets the ID for this content inside the RecordStore */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	protected String getRMSName() {
		String className = this.getClass().getName(); 
		return className.substring(className.lastIndexOf('.')+1);
	}

	/**
	 * it returns an array with the content of the object 
	 * */
	public abstract byte[] serialize() throws IOException;

	/**
	 * With an array, it uses to read the content inside a RecordStore */
	public abstract void deserialize(byte[] content) throws IOException;	

	/**
	 * Do several operations with an opened RecordStore
	 * IMPORTANT: if option is SAVE, it's important to know if <code>getID()</code>
	 * is set or not
	 */
	public void persistence(int option, RecordStore rms)
	throws IOException, RecordStoreException, RecordStoreNotOpenException,
	RecordStoreFullException, InvalidRecordIDException {
		byte[] content = ( (option != DELETE) && (option != LOAD) ? serialize() : null);
		int recordID = 0;
		switch(option) {
		case INSERT:
			recordID = rms.addRecord(content, 0, content.length);
			setID(recordID); break;
		case UPDATE: rms.setRecord(getID(), content, 0, content.length); break;
		case DELETE: rms.deleteRecord(getID()); break;
		case SAVE:
			//If getID() is negative, assume it should ADD to RecordStore
			if(getID() < 0) {
				recordID = rms.addRecord(content, 0, content.length);
				setID(recordID);
			}
			//else getID() is greaer than zero, assume already exist
			//and it should be update
			else rms.setRecord(getID(), content, 0, content.length);
			break;
		case LOAD:
			deserialize(rms.getRecord(getID()));
			break;
		}
	}
}
