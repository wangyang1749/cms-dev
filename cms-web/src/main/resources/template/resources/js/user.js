
function Toast(msg, type) {
    if (type) {
        switch (type) {
            case 'error':
                $("#toast-body").css('background', 'red')
                break;
            case 'success':
                $("#toast-body").css('background', 'green')
                break;
        }

    }
    $("#toast-body").html(msg);
    $("#toast").animate({ opacity: '1' ,"z-index":'99999'});
    setTimeout(function () { $("#toast").animate({ opacity: '0',"z-index":'-1' }); }, 2000);
}