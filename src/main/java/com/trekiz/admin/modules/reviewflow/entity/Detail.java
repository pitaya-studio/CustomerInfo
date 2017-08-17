package com.trekiz.admin.modules.reviewflow.entity;



public class Detail {
	
   
	private String key; 
    private String value;
    
    public Detail(){}
    public Detail(String key,String value){
      this.key=key;
      this.value=value;
    }      
	
    public String getKey(){
        return this.key;
    }
	
    public String getValue(){
        return this.value;
    }
	
	public void setKey(String key){
        this.key = key;
    }		
	
	public void setValue(String value){
        this.value = value ;
    }
	
	 @Override
		public String toString() {
			return "key="+this.key+",value="+this.value;
		}

}
