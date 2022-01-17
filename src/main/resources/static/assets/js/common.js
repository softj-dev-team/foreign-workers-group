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