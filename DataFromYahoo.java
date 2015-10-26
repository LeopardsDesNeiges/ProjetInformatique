import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class DataFromYahoo {
	public static final String YAHOO_FINANCE_URL="http://table.finance.yahoo.com/table.csv?";
	public static final String YAHOO_FINANCE_URL_TODAY = "http://download.finance.yahoo.com/d/quotes.csv?";
	
	public static List<StockData> getStockData(String StockName,String FromDate,String ToDate){
		List<StockData> list=new ArrayList<StockData>();
		String[] DateFromInfo=FromDate.split("-");   //将日期用—分隔的方式显示
		String[] ToDateInfo=ToDate.split("-");  
		String code=StockName.substring(0,6);    //将股票编码存入字符串
		
		String a=(Integer.valueOf(DateFromInfo[1])-1)+""; //起始月,将string转换成int
		String b=DateFromInfo[2];
		String c=DateFromInfo[0];
		String d=(Integer.valueOf(ToDateInfo[1])-1)+"";
		String e=ToDateInfo[2];
		String f=ToDateInfo[0];
		
		String params="&a="+a+"&b="+b+"&c"+c+ "&d=" + d + "&e="+e+"&f="+f;
		String url=YAHOO_FINANCE_URL+"s="+StockName+params;
		
		URL MyURL=null;
		URLConnection con=null;
		InputStreamReader ins=null;
		BufferedReader in=null;
		try{
			MyURL=new URL(url);
			con=MyURL.openConnection();
			ins=new InputStreamReader(con.getInputStream(),"UTF-8");
			in=new BufferedReader(ins);
			String newline=in.readLine();
			
			while ((newline = in.readLine()) != null) {
	                String StockInfo[] = newline.trim().split(",");
	                StockData sd = new StockData();
	                sd.setCode(code);
	                sd.setDate(StockInfo[0]);
	                sd.setOpen(Float.valueOf(StockInfo[1]));
	                sd.setHigh(Float.valueOf(StockInfo[2]));
	                sd.setLow(Float.valueOf(StockInfo[3]));
	                sd.setClose(Float.valueOf(StockInfo[4]));
	                sd.setVolume(Float.valueOf(StockInfo[5]));
	                sd.setAdj(Float.valueOf(StockInfo[6]));
	                list.add(sd);
	            }
			} catch(Exception ex){
				return null;
		    } 
		finally {
			if(in!=null){
				try {
					in.close();
				}catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
		return list;
	}
	
	 public static StockData getStockData(String stockName, String date){
	        List<StockData> list = getStockData(stockName,date,date);
	        return ((list.size()>0)?list.get(0):null);
	    }
	 
	 public static StockData getStockData(String stockName){
	        String date = String.format("%1$tF", new Date());
	        List<StockData> list = getStockData(stockName,date,date);
	        return ((list!=null&&list.size()>0)?list.get(0):null);
	    }

}
