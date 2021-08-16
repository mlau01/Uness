package uness.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import uness.Uness;
import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.usql.USQLParams;

public class Users {
	private ArrayList<UData> userList;
	private UData signedUser;
	private final DBManagerInterface acMan;
	private final int verboseLevel = 0;

	public Users(final DBManagerInterface p_acMan) {
		acMan = p_acMan;
	}
	
	
	public void loadUsers() throws DBException {
		userList = acMan.select(TABLE.USER.getFields(), true);
	}
	
	public void addUser(final String fullname) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.USER_fullname, fullname);
		
		acMan.insert(datas.getFields(), false, datas);
	}
	
	
	public void login(final String fullname)
	{
		signedUser = getUserByFullname(fullname);
	}

	public final UData getSignedUser() {
		return signedUser;
	}

	public void logout() {
		signedUser = null;
	}

	public final UData getUserByFullname(String fullName) {
		for (UData user : userList) {
			if (user.getValue(FIELD.USER_fullname).equals(fullName))
				return user;
		}

		return null;
	}
	
	public final ArrayList<UData> getUserList() throws DBException
	{
		loadUsers();
		return userList;
	}
	public final String[] getFullnameList() throws DBException
	{
		loadUsers();
		String[] names = new String[userList.size()];
		for(int i = 0; i < userList.size(); i++)
		{
			UData user = userList.get(i);
			names[i] = (String)user.getValue(FIELD.USER_fullname);
		}
		
		return names;
	}
	
	public void removeUser(final String id) throws DBException
	{
		acMan.delete(FIELD.USER_id, false, new USQLParams(FIELD.USER_id, id));
	}
	
	public void editUser(final String id, final String newName) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.USER_fullname, newName);
		
		USQLParams params = new USQLParams(FIELD.USER_id, id);
		
		acMan.update(datas.getFields(), false, params, datas);
	}
}
