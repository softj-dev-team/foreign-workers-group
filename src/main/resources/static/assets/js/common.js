$(function(){
    $(document).ajaxError(function myErrorHandler(event, xhr, ajaxOptions, thrownError) {
        console.log(event, xhr, ajaxOptions, thrownError);
        modal.alert("Error");
    });
    $(document).ajaxComplete(function myCompleteHandler(event, xhr, ajaxOptions, thrownError) {
        if(xhr.status == 200 && xhr.responseText.startsWith("<!--")){
            modal.alert("Need Login");
        }
    });
});

window.addEventListener("load", function(){
    setTimeout(loaded, 100);

}, false);

function loaded(){
    window.scrollTo(0, 1);
}

function handleFirstTab(e) {
  if (e.keyCode === 9) {
    document.body.classList.add('user-is-tabbing');

    window.removeEventListener('keydown', handleFirstTab);
    window.addEventListener('mousedown', handleMouseDownOnce);
  }
}

function handleMouseDownOnce() {
  document.body.classList.remove('user-is-tabbing');

  window.removeEventListener('mousedown', handleMouseDownOnce);
  window.addEventListener('keydown', handleFirstTab);
}

window.addEventListener('keydown', handleFirstTab);

//체크박스
$(function(){
  $('#all-chk').click(function(){
     var chk = $(this).is(':checked');//.attr('checked');
      if(chk) $('.checklist input[type="checkbox"]').prop('checked',true);
      else $('.checklist input').prop('checked',false);
  });
});

//파일첨부
var uploadFile = $('.fileBox .uploadBtn');
uploadFile.on('change', function(){
    if(window.FileReader){
        var filename = $(this)[0].files[0].name;
    } else {
        var filename = $(this).val().split('/').pop().split('\\').pop();
    }
    $(this).siblings('.fileName').val(filename);
});
$('.file-up-con .cancel-btn').click(function(){
    $(this).parent().remove();
})

//팝업닫기
$('.cancelbtn, .close-btn').click(function(){
    $(this).parents('.popup').removeClass('on')
});

//네비,카테고리
$('.ham-btn').click(function(){
    $('.category').addClass('on');
    $('body').attr('style', 'overflow:hidden');
  });
$('.cate-close, .cate-bg').click(function(){
    $('.category').removeClass('on');
    $('body').attr('style', '');
});

//팝업 열기 닫기
function popupshow(){
    $("body").addClass("popup-show");
}
function popupclose(){
    $("body").removeClass("popup-show");
}

var modal = {
    alert: function (message) {
        $('#modal-custom-alert p').html(message);
        $('#modal-custom-alert').addClass('on');
    },
    required: function (txt) {
        $('#modal-custom-alert p').html(txt+" required");
        $('#modal-custom-alert').addClass('on');
    },
    confirm: function (message, btnText, callback) {
        $('#modal-custom-confirm p').html(message);
        $('#modal-custom-confirm #modalConfirmBtn').text(btnText);
        $('#modal-custom-confirm #modalConfirmBtn').off('click');
        $('#modal-custom-confirm #modalConfirmBtn').on("click",function(){
            callback();
            modal.close('#modal-custom-confirm');
        })
        $('#modal-custom-confirm').addClass('on');
    },
    open: function(selector){
        $(selector).addClass('on');
    },
    close: function(selector){
        $(selector).removeClass('on');
    }
};

function loading(isShow){
    if(isShow) {
        $("div.loading").addClass("on");
    }else{
        $("div.loading").removeClass("on");
    }
}