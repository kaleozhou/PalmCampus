/**  
* @Title: JsonToUser.java 
* @Package com.org.palmcampus.oa.pojo.wraper 
* @Description: TODO 
* @author kaleo  
* @date 2014-8-18 下午5:34:14 
* @version V1.0  
*/ 

package com.org.palmcampus.oa.pojo.wraper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.impl.Log4JLogger;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.org.palmcampus.oa.pojo.ErpgongGao;
import com.org.palmcampus.oa.pojo.ErplanEmail;
import com.org.palmcampus.oa.pojo.ErpnetEmail;
import com.org.palmcampus.oa.pojo.Erpuser;

/** 
 * @ClassName: JsonToUser 
 * @Description:  
 * @author: kaleo 
 * @date 2014-8-18 下午5:34:14 
 *  
 */

public class JsonToPojo {
	private String tag="解析Json";
	
	/** 
	* @Title: toErpuser 
	* @Description: TODO(对返回的json对象解析出ErpUser) 
	* @param @param obj
	* @param @return    Erpuser对象
	* @return Erpuser    返回类型
	* @throws 
	*/ 
	public Erpuser toErpuser(JSONObject obj){
		Erpuser user=new Erpuser();
		try {
			user.setUserName(obj.getString("userName"));
			user.setUserPwd(obj.getString("userPwd"));
			user.setTrueName(obj.getString("trueName"));
			user.setEmailStr(obj.getString("emailstr"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(tag, "解析User失败");
		}
		return user;
		
	}
	/** 
	* @Title: toErpgongGao 
	* @Description: TODO(将Json解析成ErpgongGao对象) 
	* @param @param obj
	* @param @return    Erpgonggao对象
	* @return ErpgongGao    返回类型
	* @throws 
	*/ 
	public ErpgongGao toErpgongGao(JSONObject obj){
		ErpgongGao gonggao=new ErpgongGao();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
		try {
			gonggao.setChuanYueYiJian(obj.getString("chuanYueYiJian"));
			gonggao.setContentStr(obj.getString("contentStr"));
			gonggao.setId(obj.getInt("id"));
			Timestamp now = new Timestamp(obj.getJSONObject("timeStr").getLong("time"));
			gonggao.setTimeStr(df.format(now));
			gonggao.setUserName(obj.getString("userName"));
			gonggao.setTitleStr(obj.getString("titleStr"));
			gonggao.setIsTop(obj.getString("isTop"));
			gonggao.setTypeStr(obj.getString("typeStr"));
			gonggao.setUserBuMen(obj.getString("userBuMen"));
			gonggao.setYiJieShouRen(obj.getString("yiJieShouRen"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(tag, "解析gonggao失败");
		}
		
		return gonggao;
		
	}
	/** 
	* @Title: toErplanEmail 
	* @Description: TODO(将Json解析成ErplanEmail对象) 
	* @param @param obj
	* @param @return    设定文件
	* @return ErplanEmail    返回类型
	* @throws 
	*/ 
	public ErplanEmail toErplanEmail(JSONObject obj){
		ErplanEmail lanemail=new ErplanEmail();
		try {
			lanemail.setId(obj.getInt("id"));
			lanemail.setSendToList(obj.getString("sendToList"));
			lanemail.setFromUser(obj.getString("fromUser"));
			lanemail.setToUser(obj.getString("toUser"));
			lanemail.setEmailContent(obj.getString("emailContent"));
			lanemail.setTimeStr(new Timestamp(obj.getJSONObject("timeStr").getLong("time")));
			lanemail.setEmailState(obj.getString("emailState"));
			lanemail.setBcc(obj.getString("bcc"));
			lanemail.setReceipt(obj.getString("receipt"));
			lanemail.setEmailTitle(obj.getString("emailTitle"));
			lanemail.setCc(obj.getString("cc"));
			lanemail.setFuJian(obj.getString("fuJian"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(tag, "解析lanEmail失败");
		}
		return lanemail;
		
	}
	/** 
	* @Title: toErpnetEmail 
	* @Description: TODO(将Json解析成ErpnetEmail对象) 
	* @param @param obj
	* @param @return    ErpnetEmail对象
	* @return ErpnetEmail    返回类型
	* @throws 
	*/ 
	public ErpnetEmail toErpnetEmail(JSONObject obj){
		ErpnetEmail netemail=new ErpnetEmail();
		try {
			netemail.setEmailContent(obj.getString("emailContent"));
			netemail.setEmailState(obj.getString("emailState"));
			netemail.setEmailTitle(obj.getString("emailTitle"));
			netemail.setFromUser(obj.getString("fromUser"));
			netemail.setFuJian(obj.getString("fuJian"));
			netemail.setId(obj.getInt("id"));
			netemail.setToUser(obj.getString("toUser"));
			netemail.setTimeStr(new Timestamp(obj.getLong("timeStr")));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(tag, "解析netemail失败");
		}
		
		return netemail;
	}
	

}
