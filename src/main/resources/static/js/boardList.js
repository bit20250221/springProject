    function writeForm(){
        var tabvalue = document.querySelector('.write_type').value;
        window.location.href="/board/insertBoard?tab="+tabvalue;
    }

    var searchbtn=document.getElementById('search_button');
    searchbtn.addEventListener('click', function(){
        var tabValue = document.querySelector('#search_type_1').value;
        var typeValue = document.querySelector('#search_type_2').value;
        var keywordValue = document.querySelector('.search_keyword').value;
        if(tabValue === '전체'){
            tabValue = '';
        }
        if(typeValue === '전체'){
            typeValue = '';
        }
        window.location.href="/board/list?page=0&pageAmount=10&type="+typeValue+"&tab="+tabValue+"&Keyword="+keywordValue;

    });