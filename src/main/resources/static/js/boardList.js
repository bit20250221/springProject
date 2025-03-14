const alertMessage = (message) => {
    if(message) {
        alert(message);
    }
}
  document.addEventListener("DOMContentLoaded", function() {
      const flashMessageValue = document.getElementById("message").value;
      alertMessage(flashMessageValue);
  });
    function writeForm(){
        var tabvalue = document.querySelector('.write_type').value;
        window.location.href="/board/insertBoard?tab="+tabvalue;
    }

    var searchbtn=document.querySelector('.search_button');
    if(searchbtn){
    searchbtn.addEventListener('click', function(){
        var tabValue = document.querySelector('.search_type_1').value;
        var typeValue = document.querySelector('.search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;
        if(tabValue === '전체'){
            tabValue = '';
        }
        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/list?page=0&pageAmount=10&type="+typeValue+"&tab="+tabValue+"&Keyword="+keywordValue;

    });
    }

    var subsearchbtn1=document.querySelector('.subsearch_button_1');
    if(subsearchbtn1){
    subsearchbtn1.addEventListener('click', function(){
        var typeValue = document.querySelector('.search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;
        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/announce?page=0&pageAmount=10&type="+typeValue+"&Keyword="+keywordValue;

    });
    }

    var subsearchbtn2=document.querySelector('.subsearch_button_2');
    if(subsearchbtn2){
    subsearchbtn2.addEventListener('click', function(){
        var typeValue = document.querySelector('.search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;

        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/review?page=0&pageAmount=10&type="+typeValue+"&Keyword="+keywordValue;
    });
    }

    var subsearchbtn3=document.querySelector('.subsearch_button_3');
    if(subsearchbtn3){
    subsearchbtn3.addEventListener('click', function(){
        var typeValue = document.querySelector('.search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;

        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/normal?page=0&pageAmount=10&type="+typeValue+"&Keyword="+keywordValue;
    });
    }

    var subsearchbtn4=document.querySelector('.subsearch_button_4');
    if(subsearchbtn4){
    subsearchbtn4.addEventListener('click', function(){
        var typeValue = document.querySelector('.search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;

        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/inquiry?page=0&pageAmount=10&type="+typeValue+"&Keyword="+keywordValue;
    });
    }

    var subsearchbtn5=document.querySelector('.subsearch_button_5');
    if(subsearchbtn5){
    subsearchbtn5.addEventListener('click', function(){
        var typeValue = document.querySelector('.search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;

        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/report?page=0&pageAmount=10&type="+typeValue+"&Keyword="+keywordValue;
    });
    }

    //관광지 별 검색 기능
    var subsearchbtn6=document.querySelector('.subsearch_button_6');
    if(subsearchbtn6){
        subsearchbtn6.addEventListener('click', function(){
        var typeValue = 'attraction';
        var keywordValue = document.querySelector('.search_keyword_2').value;

        window.location.href="/board/review?page=0&pageAmount=10&type="+typeValue+"&Keyword="+keywordValue;
        });
    }