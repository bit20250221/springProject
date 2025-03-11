document.getElementById("saveForm").addEventListener("submit", function(event){
    event.preventDefault();

    const country = document.getElementById("country").value = document.getElementById("country").value.trim();
    const city = document.getElementById("city").value = document.getElementById("city").value.trim();
    const errorMessageCountry = document.getElementById("error-message-country");
    const errorMessageCity = document.getElementById("error-message-city");
    errorMessageCountry.innerText = "";
    errorMessageCity.innerText = "";

    if(country === ""){
        errorMessageCountry.innerText = "국가를 입력하세요.";
    }

    if(city === ""){
        errorMessageCity.innerText = "도시를 입력하세요.";
    }

    if(errorMessageCountry.innerText !== "" || errorMessageCity.innerText !== ""){
        return false;
    }

    if(confirm("국가 : " + country + ", 도시 : " + city + " 등록하시겠습니까?", )){
        alert("요청이 완료되었습니다.!");
        document.getElementById("saveForm").submit();
    }

    return false;
});