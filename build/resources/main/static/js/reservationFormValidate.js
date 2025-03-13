document.getElementById("saveForm").addEventListener("submit", function(event){
    event.preventDefault();

    const peplenum = document.getElementById("peplenum").value = document.getElementById("peplenum").value.trim();
    const price = document.getElementById("price").value = document.getElementById("price").value.trim();
    const reservedate = document.getElementById("reservedate").value = document.getElementById("reservedate").value.trim();
    const errorMessagePeplenum = document.getElementById("error-message-peplenum");
    const errorMessageReservedate = document.getElementById("error-message-reservedate");

    errorMessagePeplenum.innerText = "";
    errorMessageReservedate.innerText = "";

    if(peplenum === ""){
        errorMessagePeplenum.innerText = "인원수를 입력하세요.";
    }else if(peplenum < 0){
        errorMessagePeplenum.innerText = "인원수는 음수로 입력하실 수 없습니다.";
    }else if(peplenum > 10 || peplenum < 1){
        errorMessagePeplenum.innerText = "인원수는 1~9명까지 입력이 가능합니다.";
    }

    if(reservedate === ""){
        errorMessageReservedate.innerText = "예약일자를 입력하세요.";
    }else{
        let inputDate = new Date(reservedate);
        inputDate.setHours(0, 0, 0, 0);

        let today = new Date();
        today.setHours(0, 0, 0, 0);
        let sixtyDaysLater = new Date(today);
        sixtyDaysLater.setDate(today.getDate() + 60);

        if(inputDate < today){
            errorMessageReservedate.innerText = "예약일자는 과거의 날짜로 입력하실 수 없습니다.";
        }else if(inputDate > sixtyDaysLater){
            errorMessageReservedate.innerText = "예약일자는 60일 이내로 선택이 가능합니다.";
        }
    }

    if(errorMessagePeplenum.innerText !== "" || errorMessageReservedate.innerText !== ""){
        alert("입력하신 정보를 확인하세요.");
        return false;
    }

    if(confirm("인원수 : " + peplenum + ", 총 비용 : " + peplenum * price + " 예약하시겠습니까?", )){
        alert("요청이 완료되었습니다.!");
        document.getElementById("saveForm").submit();
    }

    return false;
});