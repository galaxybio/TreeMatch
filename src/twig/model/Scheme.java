package twig.model;

import java.util.ArrayList;



public class Scheme {
	private ArrayList<Integer>  exteneddewey;
	public Scheme()
	{
		exteneddewey = new ArrayList<Integer>();
	}
	public Scheme(Scheme parent,int index){
		exteneddewey = new ArrayList<Integer>();
		for(Integer i : parent.getExtendDewey())
		{
			exteneddewey.add(i);
		}
		exteneddewey.add(index);
	}

	public ArrayList<Integer> getExtendDewey(){
		return exteneddewey;
	}

	public Integer getLastComponent()
	{
		if(exteneddewey.size()>0)
			return exteneddewey.get(exteneddewey.size()-1);
		else {
			return -2;
		}
	}
	public boolean isParentOf(Scheme scheme){
		if(exteneddewey.size() + 1 == scheme.exteneddewey.size()){
			int extenddewey_num = exteneddewey.size();
			for (int i = 0; i < extenddewey_num; i++) {
				if(exteneddewey.get(i) != scheme.exteneddewey.get(i)){
					return false;
				}
			}
			return true;		
		}else{
			return false;
		}
	}
	public boolean isAncestorOf(Scheme scheme){
		if(exteneddewey.size() >scheme.exteneddewey.size()){
			return false;		
		}else{
			int extenddewey_num = exteneddewey.size();
			for (int i = 0; i < extenddewey_num; i++) {
				if(exteneddewey.get(i) != scheme.exteneddewey.get(i)){
					return false;
				}
			}
			return true;
		}
	}

	public Scheme getParentScheme()
	{
		Scheme scheme = new Scheme();
		ArrayList<Integer> dewey = new ArrayList<Integer>();
		int extenddewey_num = exteneddewey.size();
		for (int i = 0; i < extenddewey_num - 1; i++) {
			dewey.add(exteneddewey.get(i));
		}
		scheme.setExtendedDewey(dewey);
		return scheme;
	}


	public void setExtendedDewey(ArrayList<Integer> dewey) {
		this.exteneddewey = dewey;
	}



	public boolean isGreater(Scheme scheme)
	{
		int scheme_extenddewey_num = scheme.exteneddewey.size();
		int extenddewey_num = this.exteneddewey.size();
		if(extenddewey_num>scheme_extenddewey_num)
		{
			for(int i = 0 ;i < scheme_extenddewey_num;i++)
			{
				
				if(this.exteneddewey.get(i)>scheme.exteneddewey.get(i))
					return true;
				else if (this.exteneddewey.get(i)==scheme.exteneddewey.get(i))
					continue;
				else 
					return false;		
			}
			return true;
		}
		else 
		{
			for(int i = 0;i<extenddewey_num;i++)
			{
				if(this.exteneddewey.get(i)>scheme.exteneddewey.get(i))
					return true;
				else if (this.exteneddewey.get(i)==scheme.exteneddewey.get(i))
					continue;
				else 
					return false;	
			}
			return false;
		}			
	}
	public String toString(){		
		return this.exteneddewey.toString();
	}
}
