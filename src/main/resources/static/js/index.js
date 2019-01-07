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
            window.location.href="index";
        }else{
            $("#code").val("");
            $("#smsCode").val("");
            layer.alert("登录出错",function(){
                layer.closeAll();//关闭所有弹框
            });
        }
    });
}

/*$(document).on('click', '#logoutBtn', function() {
    /!*    $.get("logout",function(data){

        });*!/
    layer.open({
        type: 1,
        content: '传入任意的文本或html' //这里content是一个普通的String
    });
});*/

 function logout() {
     layer.config({
         anim: 5, //默认动画风格
         skin: 'layui-layer-molv' //默认皮肤
 });
     layer.confirm('确定退出吗？', function(index){
         $.get("logout",function(data){
             window.location.href="login";
         });
         layer.close(index);
     });
 }




/**
 * 菜单
 * */
//获取路径uri
var pathUri=window.location.href;

// console.log("pathUrl:"+pathUri);
$(function(){
    layui.use('element', function(){
        var element = layui.element;
        // 左侧导航区域（可配合layui已有的垂直导航）
        $.get("menu/MenuList",function(data){
            if(data!=null){
                getMenus(data.menuList);
                element.render('nav');
            }else{
                layer.alert("权限不足，请联系管理员",function () {
                    //退出
                    window.location.href="logout";
                });
            }
        });
    });
})
var getMenus=function(data){
    //回显选中
    var ul=$("<ul class='layui-nav layui-nav-tree' lay-filter='test'></ul>");
    for(var i=0;i < data.length;i++){
        var node=data[i];
        if( node.type==0){
            if(node.pid==0){
                var li=$("<li class='layui-nav-item' flag='"+node.id+"'></li>");
                //父级无page
                var a=$("<a class='' href='javascript:;'>"+node.name+"</a>");
                li.append(a);
                //获取子节点
                /*var childArry = getParentArry(node.menuId, data);*/
                var childArry = node.children;
                if(childArry.length>0){
                    a.append("<span class='layui-nav-more'></span>");
                    var dl=$("<dl class='layui-nav-child'></dl>");
                    for (var y in childArry) {
                        var name = childArry[y].name;
                        var url = childArry[y].url;
                        var dd=$("<dd><a href='javascript:;' onclick=\"addTab('" + name + "','" + url + "')\" >"+childArry[y].name+"</a></dd>");

                        //判断选中状态
                        if(pathUri.indexOf(childArry[y].url)>0){
                            li.addClass("layui-nav-itemed");
                            dd.addClass("layui-this")
                        }
                        //TODO 由于layui菜单不是规范统一的，多级菜单需要手动更改样式实现；
                        dl.append(dd);
                    }
                    li.append(dl);
                }
                ul.append(li);
            }
        }
    }
    $(".layui-side-scroll").append(ul);
}
//根据菜单主键id获取下级菜单
//id：菜单主键id
//arry：菜单数组信息
function getParentArry(id, arry) {
    var newArry = new Array();
    for (var x in arry) {
        if (arry[x].pId == id)
            newArry.push(arry[x]);
    }
    return newArry;
}
function updateUsePwd(){
    layer.open({
        type:1,
        title: "修改密码",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['450px'],
        content:$('#useDetail')
    });
}

function addTab(name, url) {
    if(layui.jquery(".layui-tab-title li[lay-id='" + name + "']").length > 0) {
        //选项卡已经存在
        layui.element.tabChange('tabDemo', name);
        layer.msg('切换-' + name)
    } else {
        //动态控制iframe高度
        var tabheight = layui.jquery(window).height();
        contentTxt = '<iframe src="' + url + '" frameborder="0" class="layadmin-iframe" width="100%" height="' + (tabheight) + 'PX"></iframe>';
        //新增一个Tab项
        layui.element.tabAdd('tabDemo', {
            title:name,
            content: contentTxt,
            id: name
        })
        //切换刷新
        layui.element.tabChange('tabDemo', name)
        layer.msg('新增-' + name)
    }
}





