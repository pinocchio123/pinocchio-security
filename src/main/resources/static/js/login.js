/**
 * 登录
 */
 $(function(){
     layui.use(['form' ,'layer'], function() {
         var form = layui.form;
         var layer = layui.layer;
         //监控提交
         form.on("submit(sendMsg)",function (data) {
             //sendMsg();
             var flag=checkParams();
             if(flag!=false){
                 send(this,true);
             }
             return false;
         });
         form.on("submit(login)",function () {
             login();
             return false;
         });
         var path=window.location.href;
//    	 console.info("==请求的uri:"+path);
         if(path.indexOf("kickout")>0){
             layer.alert("您的账号已在别处登录；若不是您本人操作，请立即修改密码！",function(){
                 window.location.href="/login";
             });
         }
     })
 })


function login(){
    $.post("sys/login",$("#useLogin").serialize(),function(data){
        console.log("data:"+data)
        if(data.code=="200"){
            layer.alert("登录成功",function () {
                window.location.href="index";
            });
        }else{
            $("#code").val("");
            $("#smsCode").val("");
            layer.alert(data.msg,function(){
                layer.closeAll();//关闭所有弹框
            });
        }
    });
}


