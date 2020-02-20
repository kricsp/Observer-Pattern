package loadbalancer.entities;

import loadbalancer.observer.ServiceManager;

public class Trie {
	// Code for Trie data structure.
	// Used as a key value store.
	// Key is the service name. Value is an instance of ServiceManager.
	// Rest of the code.

	private static final int R = 256;
	private TrieNode root = new TrieNode();


	private static class TrieNode{
		private ServiceManager serviceManager = null;
		private TrieNode[] next = new TrieNode[R];
		boolean isLeafNode;


		public void unMarkAsLeaf(){
			this.isLeafNode = false;
		}
	}

	public void put(String key, ServiceManager serviceManagerIn){
		root = put(root, key, serviceManagerIn, 0); 
	}

	private TrieNode put(TrieNode node, String key, ServiceManager serviceManagerIn, int d){

		if (node == null) {
			node = new TrieNode();
		}
		if (d == key.length()) {
			node.serviceManager = serviceManagerIn; 
			return node; 
		}

		char c = key.charAt(d);
		node.next[c] = put(node.next[c], key, serviceManagerIn, d+1);
		return node;
	}

	public boolean contains(String key){ 
		return get(key) != null; 
	}

	public ServiceManager get(String key){

		TrieNode node = get(root, key, 0);

		if (node == null) 
			return null;

		return (ServiceManager) node.serviceManager;
	}

	private TrieNode get(TrieNode node, String key, int d){

		if (node == null) 
			return null;
		if (d == key.length()) 
			return node;
		char c = key.charAt(d);

		return get(node.next[c], key, d+1);
	}

	public void delete(String key){	
		if ((root == null) || (key == null)){
			return;
		}

		delete(key, root, key.length(), 0);
		return;
	}

	private boolean delete(String key, TrieNode currentNode, int length, int level)
	{
		boolean deletedSelf = false;
		if (currentNode == null){
			return deletedSelf;
		}

		if (level == length){
			if (hasNoChildren(currentNode)){

				currentNode = null;
				deletedSelf = true;
			}else{
				currentNode.unMarkAsLeaf();
				deletedSelf = false;
			}
		}else{	
			TrieNode childNode = currentNode.next[key.charAt(level)];
			boolean childDeleted = delete(key, childNode, length, level + 1);

			if (childDeleted){
				currentNode.next[key.charAt(level)] = null;
				if (currentNode.isLeafNode)
					deletedSelf = false;
				else if (!hasNoChildren(currentNode))
					deletedSelf = false;
				else{
					currentNode = null;
					deletedSelf = true;
				}
			}else
				deletedSelf = false;
		}

		return deletedSelf;
	}

	private boolean hasNoChildren(TrieNode currentNode){	
		for (int i = 0; i < currentNode.next.length; i++){
			if ((currentNode.next[i]) != null)
				return false;
		}
		return true;
	}
}

