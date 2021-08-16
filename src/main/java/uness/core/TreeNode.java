package uness.core;

import java.util.ArrayList;

public class TreeNode {
	
	private ArrayList<TreeNode> children = null;
	private String value;
	
	public TreeNode(final String p_value)
	{
		children = new ArrayList<TreeNode>();
		value = p_value;
	}
	
	public final String getValue()
	{
		return value;
	}
	public void addChild(final TreeNode child)
	{
		children.add(child);
	}
	public final ArrayList<TreeNode> getChildren()
	{
		return children;
	}
	
	public final boolean hasChildren()
	{
		return ! children.isEmpty();
	}

}
