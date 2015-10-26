
public class StockData {
	private String code;
	private String name;
	private String date;
	private double open=0.0;
	private double high=0.0;
	private double low=0.0;
	private double close=0.0;
	private double volume=0.0;
	private double adj=0.0;
	
	public void setCode(String c){
		this.code=c;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setDate(String d){
		this.date=d;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setOpen(double o){
		this.open=o;
	}
	
	public double getOpen(){
		return this.open;
	}
	
	public void setHigh(double h){
		this.open=h;
	}
	
	public double getHigh(){
		return this.high;
	}
	
	public void setLow(double l){
		this.open=l;
	}
	
	public double getLow(){
		return this.low;
	}
	
	public void setClose(double cl){
		this.open=cl;
	}
	
	public double getClose(){
		return this.close;
	}
	
	public void setVolume(double v){
		this.open=v;
	}
	
	public double getVolume(){
		return this.volume;
	}
	
	public void setAdj(double a){
		this.open=a;
	}
	
	public double getAdj(){
		return this.adj;
	}

}
