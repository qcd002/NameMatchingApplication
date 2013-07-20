package org.apache.commons.codec.language;
public class FirstName {
        private int prior;
	private String name;
	private String code;
        
	
	public FirstName(){
                prior = 0;
		name = "";
		code = "";
	}
	
	public FirstName (int prior,String name, String code){
                this.prior = prior;
		this.name = name;
		this.code = code;
	}
	

        public int getPrior(){
                return prior;
	}
	public String getName(){
		return name;
	}

	public String getCode(){
		return code;
	}
}
