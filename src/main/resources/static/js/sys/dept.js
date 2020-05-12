layui.config({
    base: '/pinocchio-admin/js/',
})
layui.use(['treetable', 'form'], function () {
    var o = layui.$, treetable = layui.treetable;
    var form = layui.form, layer = layui.layer;
    $.ajax({
        type: "GET",
        url: "gridtreelist",
        dataType: "json",
        success: function (res) {
            treetable.render({
                elem: '#test-tree-table',
                data: res.data,
                field: 'name',
                is_checkbox: true,
                checked: [1, 2, 3],
                /*icon_val: {
                    open: "&#xe619;",
                    close: "&#xe61a;"
                },
                space: 4,*/
                cols: [
                    {
                        field: 'name',
                        title: '名称',
                        width: '70%',
                    },
                    {
                        field: 'actions',
                        title: '操作',
                        width: '30%',
                        template: function (item) {
                            var tem = [];
                            tem.push('<a class="layui-btn layui-btn-xs" lay-filter="edit">编辑</a>\n' +
                                '\t<a class="layui-btn layui-btn-xs layui-btn-danger" lay-filter="del">删除</a>')
                            return tem.join(' <font>|</font> ')
                        },
                    },
                ]
            });
        },
    });


    treetable.on('treetable(del)', function (res) {
        layer.config({
            anim: 5, //默认动画风格
            skin: 'layui-layer-molv' //默认皮肤
        });
        layer.confirm('确定删除该条记录吗？', function (index) {
            $.ajax({
                type: "POST",
                url: "delete",
                data: {"id": res.item.id},
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
    })

    treetable.on('treetable(edit)', function (res) {
        editDept(res.item.id);
    })

    o('.up-all').click(function () {
        treetable.all('up');
    })

    o('.down-all').click(function () {
        treetable.all('down');
    })

    o('.get-checked').click(function () {
        console.dir(treetable.all('checked'));
    })

    $('#addBtn').click(function () {
        editDept(-1);
    })

    form.on('switch(status)', function (data) {
        layer.msg('监听状态操作');
        console.dir(data);
    })

    function editDept(id) {
        layer.open({
            type: 2//这就是定义窗口类型的属性
            , title: "编辑"
            , shadeClose: true
            , shade: 0.3
            , offset: 'auto'
            , skin: 'layui-layer-molv'
            , area: ['700px', '500px']
            , btn: ['确定', '取消']
            , content: "edit?deptId=" + id   //实际开发中传入真实iframe地址
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
})