/**封装弹出树控件封装*/
/**
 * 页面一开始加载，首先构建树控件
 */
$(document).ready(function () {

});
(function ($) {
    $.extend({
        pop_up_tree: {
            init: function (options) {
                //为了防止页面多个，使用随机数生产ID
                var treeId = "zTree" + Math.random().toString(36).substring(2);
                var treeLayerId = "tree-layer" + Math.random().toString(36).substring(2);
                /**
                 * zTree配置
                 */
                var setting = {
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: options.idKey,
                            pIdKey: options.pIdKey,
                            rootPId: options.rootPId
                        },
                        key: {
                            name: options.name,
                            url: "x"
                        }
                    }
                };
                var ztree;
                //在当前对象后面追加html内容
                $(options.obj).after('<input  id="' + options.value + '" type="text"  name="' + options.value + '" class="hidden"/><div id="' + treeLayerId + '" style="display: none;padding:10px;"><ul id="' + treeId + '" class="ztree"></ul> </div>');
                $('#' + options.value).val(options.obj.val());
                //发送ajax请求
                $.ajax({
                    type: options.type,
                    url: options.url,
                    contentType: "application/json",
                    async: false,
                    dataType: "json",
                    success: function (result) {
                        //如果成功
                        if (result.status === web_status.SUCCESS) {
                            //渲染ztree
                            ztree = $.fn.zTree.init($("#" + treeId), setting, result.result);
                            //根据已经选择的节点ID进行渲染
                            var node = ztree.getNodeByParam(options.idKey, $('#' + options.value).val());
                            //如果node不为空进行查询
                            if (node != null) {
                                ztree.selectNode(node);
                                //当前输入框添加内容
                                $(options.obj).val(node[options.name]);
                            }
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function () {
                        alert("系统错误，请稍后重试！");
                    }
                });
                /**
                 * 弹出layer
                 */
                layer.open({
                    type: 1,
                    offset: '50px',
                    title: "请选择",
                    area: ['300px', '400px'],
                    shade: 0,
                    shadeClose: false,
                    content: jQuery("#" + treeLayerId), //弹框内容
                    btn: ['确定', '取消'],
                    btn1: function (index) {
                        var node = ztree.getSelectedNodes();
                        if (node.length > 0) {
                            //将选中的值放入隐藏value框
                            $('#' + options.value).val(node[0][options.idKey]);
                            //将名称会显示内容框
                            $(options.obj).val(node[0][options.name]);
                        }
                        //选择上级菜单
                        layer.close(index);
                    }
                });
            }
        }
    })
})(jQuery);
$(document).ready(function () {
    //将元素上添加 .pop-up-tree
    $(".pop-up-tree").click(function () {
        $.pop_up_tree.init({
            obj: $(this),
            value: 'parentId', //隐藏value name 属性和id属性
            idKey: 'id',
            pIdKey: 'parentId',
            rootPId: '0',
            name: 'nodeName',
            type: 'POST',
            url: contextPath + 'menu/list'

        });
    });
});