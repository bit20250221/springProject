document.getElementById("saveForm").addEventListener("submit", function(event){
    event.preventDefault();

    const name = document.getElementById("name").value = document.getElementById("name").value.trim();
    const price = document.getElementById("price").value = document.getElementById("price").value.trim();
    const area = document.getElementById("areaId");
    const selectedAreaText = area.options[area.selectedIndex].text;
    const typeList = document.querySelectorAll('input[name="attractionTypeDtoIdList"]:checked');
    const errorMessageName = document.getElementById("error-message-name");
    const errorMessagePrice = document.getElementById("error-message-price");
    const errorMessageType = document.getElementById("error-message-type");
    errorMessageName.innerText = "";
    errorMessagePrice.innerText = "";
    errorMessageType.innerText = "";

    if(name === ""){
        errorMessageName.innerText = "이름를 입력하세요.";
    }

    if(price === ""){
        errorMessagePrice.innerText = "가격를 입력하세요.";
    }

    if(typeList.length > 3){
        errorMessageType.innerText = "구분은 최대 3개까지 선택이 가능합니다.";
    }

    if(errorMessageName.innerText !== "" || errorMessagePrice.innerText !== "" || errorMessageType.innerText !== ""){
        return false;
    }

    let typeListArr = [];
    typeList.forEach((checkbox) => {
        const id = checkbox.id;
        const label = document.querySelector(`label[for='${id}']`);
        const text = label ? label.innerText : "라벨 없음";

        typeListArr.push(text);
    })

    if(confirm("이름 : " + name + ", 가격 : " + price + ", 지역 : " + selectedAreaText +", 구분 : " + typeListArr.toString() + " 등록하시겠습니까?", )){
        alert("요청이 완료되었습니다.!");
        document.getElementById("saveForm").submit();

    }

    return false;
});