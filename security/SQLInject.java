package com.kayakwise.gray.api.util.security;

import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kayakwise.gray.api.code.ErrorCode;
import com.kayakwise.gray.api.exception.BizException;

public class SQLInject {
	
	private static String noPassChar = "";
	//白名单
	public static String passChar = "";
	
	private static SQLInject instance=null;
	
	private static final Logger log = LoggerFactory.getLogger(SQLInject.class);
	
	public SQLInject(){
		
	}
	
	public synchronized static SQLInject getSQLCheck(){
		if (instance==null){
			instance= new SQLInject();
		}
		return instance;
	}
	
	/**
	 * SQL注入校验，目前只支持map对象的json字符串
	 * @param str json 字符串，只能校验如下类型 {"aa":"xx","bb":"xx"}
	 * @return
	 * @throws TransErrorException 
	 */
	public boolean check(String str) throws BizException{
		HashMap headMap =JSON.parseObject(str, HashMap.class);  
		Iterator it = headMap.keySet().iterator();  
        while (it.hasNext()) {
        	String key = it.next() +"";
            String dd = headMap.get(key) + "";
            if("backURL".equalsIgnoreCase(key)){
            	continue;
            }
            if(!passValidate(key)){
            	continue;
            }
            String flag = sqlValidate(dd);
    		if(!("1".equals(flag))){
    			throw new BizException(ErrorCode.SYS1008_SQL注入检查不通过);
    		}
        }
		return true;
	}
	
	/**
	 * 白名单过滤
	 * @param str
	 * @return
	 */
	public static boolean passValidate(String str){
		
        String[] badStrs = passChar.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if(str.equals(badStrs[i]+"")){
            	return false;
            }
        }
		return true;
	}
	/**
	 * 关键字检查
	 * @param str
	 * @return
	 */
	private static String sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String[] badStrs = noPassChar.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return badStrs[i]+"";
            }
        }
        return "1";
    }
	
}
