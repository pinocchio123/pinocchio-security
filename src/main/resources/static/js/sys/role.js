layui.config({
    base: '/pinocchio-admin/js/',
})
layui.use(['treetable', 'form', 'table'], function () {

    var table = layui.table;
    var o = layui.$, treetable = layui.treetable;
    var form = layui.form, layer = layui.layer;
    var menu;
    var roleMenu;
    table.render({
        elem: '#demo'
        , url: 'listByPage'
        , page: true //是否显示分页
        , cols: [[ //标题栏
            {type: 'checkbox', fixed: 'left'}
            //,{field: 'roleId', title: 'ID', width: 80, sort: true,fixed: true}
            , {field: 'roleName', title: '角色名称', width: 350}
            , {
                field: 'createTime',
                title: '创建时间',
                minWidth: 60,
                templet: '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
            }
            , {field: 'remark', title: '备注', minWidth: 60}
            , {field: 'right', title: '操作', width: 300, toolbar: "#barDemo"}
        ]]
    })


    function treetableInit(roleId) {
        $("#test-tree-table").removeClass("layui-hide");
        //获取菜单资源
        $.ajax({
            url: "/pinocchio-admin/sys/menu/list",
            method: 'POST',
            async: false,
            success: function (results) {
                menu = results;
            }
        });
        $.ajax({
            url: "/pinocchio-admin/sys/menu/roleMenuList",
            method: 'POST',
            data: {"roleId": roleId},
            async: false,
            success: function (results) {
                roleMenu = results;
            }
        });
        treetable.render({
            elem: '#test-tree-table',
            data: menu,
            field: 'name',
            is_checkbox: true,
            cols: [
                {
                    field: 'name',
                    title: '标题',
                    width: '90%',
                },
            ]
        });
        //将已有的权限进行添加
        if (roleMenu.length != 0) {
            $('.layui-table tr[data-id=' + 0 + '] td .layui-form-checkbox').trigger("click");
            for (var j = 0; j < menu.length; j++) {
                var x = 0;
                for (var i = 0; i < roleMenu.length; i++) {
                    if (menu[j].id == roleMenu[i].menuId) {
                        x = 1;
                    }
                }
                if (x == 0) {
                    $('.layui-table tr[data-id=' + menu[j].id + '] td .layui-form-checkbox').trigger("click");
                }
            }
        }
    }


    $('#addBtn').click(function () {
        editRole(0);
    })

    table.on('tool(listTable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var roleId = data.roleId;//获取选中行的对象的id值

        if (layEvent === 'del') { //删除
            deleteRole(roleId);
        } else if (layEvent === 'edit') { //编辑
            editRole(roleId);
        } else if (layEvent === 'perm') { //授权
            permRole(roleId);
            treetableInit(roleId);
        }
    });

    function editRole(roleId) {
        layer.open({
            type: 2//这就是定义窗口类型的属性
            , title: "编辑"
            , shadeClose: true
            , shade: 0.3
            , offset: 'auto'
            , skin: 'layui-layer-molv'
            , area: ['700px', '500px']
            , btn: ['确定', '取消']
            , content: "edit?roleId=" + roleId   //实际开发中传入真实iframe地址
            , yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var paraFormData = body.find("#formData").serialize();
                $.ajax({
                    type: "POST",
                    url: "save",
                    data: paraFormData,
                    dataType: "json",
                    success: function (data) {
                        layer.msg(data.msg);
                        if (data.code == '200') {
                            location.reload();
                            layer.close(index); //关闭弹层
                        }
                    },
                    error: function () {
                        layer.msg("出现错误");
                        return false;
                    }
                });
            }

        });
    }

    function deleteRole(roleId) {
        layer.config({
            anim: 5, //默认动画风格
            skin: 'layui-layer-molv' //默认皮肤
        });
        layer.confirm('确定删除该条记录吗？', function (index) {
            $.ajax({
                type: "POST",
                url: "delete",
                data: {"roleId": roleId},
                dataType: "json",
                success: function (data) {
                    layer.msg(data.msg);
                    if (data.code == '200') {
                        location.reload();
                    }
                },
                error: function () {
                    layer.msg("出现错误");
                    return false;
                }
            });
        });
    }

    function permRole(roleId) {
        layer.open({
            type: 1//这就是定义窗口类型的属性
            , title: "编辑"
            , shadeClose: true
            , shade: 0.3
            , offset: 'auto'
            , skin: 'layui-layer-molv'
            , area: ['700px', '500px']
            , btn: ['确定', '取消']
            // ,content:"perm?roleId="+ roleId   //实际开发中传入真实iframe地址
            , content: $("#test-tree-table")
            , yes: function (index, layero) {
                var ids = treetable.all('checked').ids;
                alert(ids);
                $.ajax({
                    type: "POST",
                    url: "saveRoleMenu",
                    data: {"roleId": roleId, "menuIds": ids.toString()},
                    dataType: "json",
                    success: function (data) {
                        layer.msg(data.msg);
                        $("#test-tree-table").addClass("layui-hide");
                        location.reload();
                    },
                    error: function () {
                        layer.msg("出现错误");
                        $("#test-tree-table").addClass("layui-hide");
                        return false;
                    }
                });
                layer.close(index); //关闭弹层
            }
            , cancel: function (index, layero) {
                $("#test-tree-table").addClass("layui-hide");
                layer.close(index)
            }
            , end: function (index, layero) {
                $("#test-tree-table").addClass("layui-hide");
                layer.close(index)
            }
        });
    }

});

function Format(datetime, fmt) {
    if (parseInt(datetime) == datetime) {
        if (datetime.length == 10) {
            datetime = parseInt(datetime) * 1000;
        } else if (datetime.length == 13) {
            datetime = parseInt(datetime);
        }
    }
    datetime = new Date(datetime);
    var o = {
        "M+": datetime.getMonth() + 1,                 //月份
        "d+": datetime.getDate(),                    //日
        "h+": datetime.getHours(),                   //小时
        "m+": datetime.getMinutes(),                 //分
        "s+": datetime.getSeconds(),                 //秒
        "q+": Math.floor((datetime.getMonth() + 3) / 3), //季度
        "S": datetime.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (datetime.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}