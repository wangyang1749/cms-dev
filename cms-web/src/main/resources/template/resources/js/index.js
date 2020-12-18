// console.log(document.body.clientWidth )
// console.log($("body").innerWidth())
// console.log($(window).width())
// 吸顶
$(function () {
    if (document.body.clientWidth < 977) {
        // console.log("aa")
        var navBar = $("#mobile-nav1");
        var navBar2 = $("#mobile-nav2");
        var titleTop = navBar.offset().top;

        $(window).scroll(function () {
            var btop = $(window).scrollTop();
            if (btop > titleTop) {
                navBar.addClass('mobile-fix');
                // console.log(navBar.outerHeight()+16)
                let navBarOuterHeight = navBar.outerHeight() + 16
                navBar2.css("margin-top", navBarOuterHeight + "px");
            } else {
                navBar.removeClass('mobile-fix');
                navBar2.css("margin-top", 0);
            }
        })
    }

});

//手机搜索

$(function () {
    $("#mobile-search").click(function () {
        $("#search-panel").slideToggle("fast");
    });
});




function move() {
    var divMove = document.getElementById("content-table");
    if (divMove == null) return;
    divMove.onmousedown = function (e) {
        var ev = e || window.event;  //兼容ie浏览器
        //鼠标点击物体那一刻相对于物体左侧边框的距离=点击时的位置相对于浏览器最左边的距离-物体左边框相对于浏览器最左边的距离  
        var distanceX = ev.clientX - divMove.offsetLeft;
        var distanceY = ev.clientY - divMove.offsetTop;
        document.onmousemove = function (e) {
            var ev = e || window.event;  //兼容ie浏览器  
            divMove.style.left = ev.clientX - distanceX + 'px';
            divMove.style.top = ev.clientY - distanceY + 'px';
        };
        document.onmouseup = function () {
            document.onmousemove = null;
            document.onmouseup = null;
        };
    };
}
// move();

function move(header, panel, closeBtn) {
    var _move = false;//移动标记  
    var _x, _y;//鼠标离控件左上角的相对位置  
    $("#" + header).click(function () {
        //alert("click");//点击（松开后触发）  
    }).mousedown(function (e) {
        _move = true;

        if ($("#" + panel).css("position") != "fixed") {
            $("#" + panel).css("position", "fixed");
            $("#" + panel).css("z-index", "9999");
            $("#" + panel).css("left", e.clientX);
            $("#" + panel).css("top", e.clientY);
            $("#" + closeBtn).css("display", "block");

        }
        _x = e.pageX - parseInt($("#" + panel).css("left"));
        _y = e.pageY - parseInt($("#" + panel).css("top"));

        $("#" + panel).fadeTo(20, 0.5);//点击后开始拖动并透明显示  
    });
    $(document).mousemove(function (e) {
        if (_move) {
            var x = e.pageX - _x;//移动时根据鼠标位置计算控件左上角的绝对位置  
            var y = e.pageY - _y;
            $("#" + panel).css({ top: y, left: x });//控件新位置  
        }
    }).mouseup(function () {
        _move = false;
        $("#" + panel).fadeTo("fast", 1);//松开鼠标后停止移动并恢复成不透明  "fast":规定褪色效果的速度。
    });
    $("#" + closeBtn).click(function () {
        $("#" + panel).css("position", "");
        $("#" + closeBtn).css("display", "none");
    })
}







function siderBar() {
    // console.log("siderbarHeight" + $("#siderbar").outerHeight(true))
    // console.log("siderbarWidth" + $("#siderbar").width())
    // console.log("window" + $(window).height())
    // console.log("document scrollTop" + $(document).scrollTop())
    // console.log("header" + $("#header").outerHeight(true))
    // console.log("footer" + $("#footer").outerHeight(true))
    // console.log("document height" + $(document).height());
    // console.log($("#main-content").outerHeight(true))

    let siderbarHeight = $("#siderbar").outerHeight(true);
    let siderbarWidth = $("#siderbar").width()
    let headerHeight = $("#header").outerHeight(true)
    let footerHeight = $("#footer").outerHeight(true)
    let windowHeight = $(window).height()
    let scrollTop = $(document).scrollTop()
    let documentHeight = $(document).height()
    let mainContentHeight = $("#main-content").outerHeight(true)
    // console.log(documentHeight - windowHeight - scrollTop > footerHeight)
    // console.log(windowHeight - headerHeight - footerHeight > siderbarHeight)
    // 底部导航是否出现并且 浏览器窗口不能同时显示头部侧栏和底部
    if (mainContentHeight > siderbarHeight) {
        if (documentHeight - windowHeight - scrollTop < footerHeight && windowHeight - headerHeight - footerHeight < siderbarHeight) {
            $("#siderbar").css({ "position": "absolute", "bottom": "0", "top": "" })
        } else {
            // 窗口高度是否大于左侧面板高度
            if (windowHeight > siderbarHeight + headerHeight) {
                $("#siderbar").css({ "position": "fixed", "top": headerHeight + "px", "width": siderbarWidth + "px" })
            } else {
                // console.log("2222")
                // 窗口大小+滚动的距离是否=左侧面板的距离
                if (scrollTop + windowHeight > siderbarHeight + headerHeight) {
                    // console.log("ok")
                    $("#siderbar").css({ "position": "fixed", "bottom": "0", "width": siderbarWidth + "px" })
                } else {
                    $("#siderbar").css({ "position": "", "bottom": "" })

                }
            }
        }
    }

}
if (document.body.clientWidth >= 977) {
    siderBar()
    $(document).scroll(function () {
        siderBar()
    })
}

// 增加浏览量
var url = location.hostname;
var protocol = window.location.protocol;
// var token = $.cookie('viewId')
var port = window.location.port

/**文章ajax预览 */
function previewArticle(articleId, viewName) {
    // console.log(articleId + viewName)
    // console.log($("#"+viewName).html().replace(/\s+/g,"")=="")
    let httpUrl = protocol + "//" + url ;
    if(port){
        httpUrl+=":"+port;
    }
    if ($("#" + viewName).html().replace(/\s+/g, "") == "") {
        $.ajax({
            url: httpUrl+"/preview/simpleArticle/" + articleId,
            type: "get",
            success: function (data) {
                // console.log(data)
                $("#" + viewName).html("<div class='p-3'>" + data + "</div>")
            }
        });
    } else {
        $("#" + viewName).html(" ")
    }
    // str=str.replace(/\s+/g,"");  

}

function previewArticleHTML(path, viewName) {
    let httpUrl = protocol + "//" + url ;
    if(port){
        httpUrl+=":"+port;
    }
    // console.log(httpUrl + "/" + path + "/" + viewName + ".html")
    if ($("#" + viewName).html().replace(/\s+/g, "") == "") {
        $.ajax({
            url: httpUrl + "/" + path + "/" + viewName + ".html",
            type: "get",
            success: function (data) {
                // console.log($(data).find(".markdown").html())

                $("#" + viewName).html("<div class='p-3'>" + $(data).find(".markdown").html() + "</div>")
            }
        });
    } else {
        $("#" + viewName).html(" ")
    }

}