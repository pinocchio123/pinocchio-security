layui.use('table', function () {

    var table = layui.table;

    table.render({
        elem: '#demo'
        , url: 'listByPage'
        , page: true //是否显示分页
        , cols: [[ //标题栏
            {type: 'checkbox', fixed: 'left'}
            , {field: 'userId', title: 'ID', width: 80, sort: true, fixed: true}
            , {field: 'username', title: '用户名', width: 350}
            , {field: 'email', title: '邮箱', minWidth: 80}
            , {field: 'mobile', title: '手机', minWidth: 60}
            , {
                field: 'createTime',
                title: '创建时间',
                minWidth: 60,
                templet: '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
            }
            , {field: 'right', title: '操作', width: 300, toolbar: "#barDemo"}
        ]]
    })

    $('#addBtn').click(function () {
        editUser(0);
    })

    table.on('tool(listTable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var userId = data.userId;//获取选中行的对象的id值

        if (layEvent === 'del') { //删除
            deleteUser(userId);
        } else if (layEvent === 'edit') { //编辑
            editUser(userId);
        }
    });

    function editUser(userId) {
        layer.open({
            type: 2//这就是定义窗口类型的属性
            , title: "编辑"
            , shadeClose: true
            , shade: 0.3
            , offset: 'auto'
            , skin: 'layui-layer-molv'
            , area: ['700px', '500px']
            , btn: ['确定', '取消']
            , content: "edit?userId=" + userId   //实际开发中传入真实iframe地址
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
                        layer.msg(data.msg);
                        return false;
                    }
                });

            }
        });
    }

    function deleteUser(userId) {
        layer.config({
            anim: 5, //默认动画风格
            skin: 'layui-layer-molv' //默认皮肤
        });
        layer.confirm('确定删除该条记录吗？', function (index) {
            $.ajax({
                type: "POST",
                url: "delete",
                data: {"userId": userId},
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