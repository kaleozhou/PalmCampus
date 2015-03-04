package com.org.palmcampus.oa.custom;
//方法名称，常量定义
public class MethodName {
	//显示员工信息
    public static final int SHOWEMPLOYEE=1000;
    //显示通知公告
    public static final int SHOWNEWS=1001;
    //修改员工信息
    public static final int UPDATEEMPLOYEE=1002;
    //添加客户信息
    public static final int ADDCUSTOMER=1003;
    //为新闻添加阅读留言
    public static final int ADDADVICE=1004;
    //显示客户列表
    public static final int SHOWCUSTOMER=1005;
    //上传客户证件照片
    public static final int UPLOADCUSTOMERFILE=1006;
    //上传客户证件照片（新修改）
    public static final int UPLOADCUSTOMERFILE2=1016;
    //发送邮件
    public static final int SENDEMAIL=1007;
    //接收所有邮件
    public static final int GETALLEMAIL=1008;
    //接收所有员工为邮件
    public static final int GETALLEEMPLOYEE=1009;
    //修改密码
    public static final int CHANGEPASSWORD=1010;
    //获取贷款前的客户
    public static final int SHOWlOANCUSTOMER=1011;
    //删除邮件
    public static final int DELETEMAIL=1012;
    
	public static final int DOLOGIN = 1;
	public static final int GETALLFLOWS = 2;
	public static final int GETTODOMSGS = 3;//获取代办提醒
	public static final int GETFLOWSBYID = 4;//获取所有流程列表
	public static final int STARTFLOW = 5;//能否创建新流程
	public static final int GETWORKDRTAIL = 6;//获取流程表单详细信息
	public static final int GETWORKUSERTREE = 7;//获取人员树
	public static final int GETDICDATA = 8;//获取字典列表
	public static final int PREPARE2SUBMITWORK = 9;//获取路由列表
	public static final int SUBMITROUTESELECTOVER = 10;//获取人员列表
	public static final int SUBMITUSERSELECTOVER = 11;//获取信息提示列表
	public static final int SUBMITWORK = 12;//正式提交公文
	public static final int SUBMITRECORD = 13;//预提交公文
	public static final int GETSERIAL = 14;//获取编号
	public static final int GETKEYWORK = 15;//获取主题词
	public static final int GETOPINIONLIST = 16;//获取意见列表
	public static final int GETOPINIONBYUSERLOG = 17;//新建意见
	public static final int SUBMITOPINION = 18;//上传意见
	public static final int GETTRACES = 19;//获取审批记录
	public static final int SUBMITFILE = 20;//上传附件
	public static final int GETFILELIST = 21;//获取附件列表
	public static final int GETFILEBYID = 22;//获取详细附件
	public static final int INIWORK = 23;//初始化文件
	public static final int GETWORD = 24;//获取word文件
	
	public static final int GETVIEWFIELDSBYNO = 25;//获取列表视图
	public static final int GETFORMFIELDS = 26;//获取明细
	
	public static final int GETRCGLVALUES = 27;//获取日程日历
	public static final int GETRCGLLIST = 28;//获取某日日程列表
	public static final int GETRCGL = 29;//获取日程明细
	public static final int GETTITLENEWS = 30;//获取主题新闻
	
	public static final int FLOW_LIST = 101;//获取流程列表
	public static final int ANGENCY_LIST = 102;//获取代办列表
	
}
