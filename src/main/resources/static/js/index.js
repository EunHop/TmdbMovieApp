// footer 로고 클릭하면 맨 위로 올리기
$('.logo').click( function () {
    $('html, body').animate( {scrollTop:0}, 400);
});