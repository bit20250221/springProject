document.getElementById("saveForm").addEventListener("submit", function(event){
    event.preventDefault();

    const type = document.getElementById("type").value = document.getElementById("type").value.trim();
    const errorMessageType = document.getElementById("error-message-type");
    errorMessageType.innerText = "";

    if(type === ""){
        errorMessageType.innerText = "구분을 입력하세요.";
    }

    if(errorMessageType.innerText !== ""){
        return false;
    }

    if(confirm("구분 : " + type + " 등록하시겠습니까?", )){
        alert("요청이 완료되었습니다.!");
        document.getElementById("saveForm").submit();
    }

    return false;
});