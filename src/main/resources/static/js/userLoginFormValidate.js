document.getElementById("loginForm").addEventListener("submit", function(event){
    event.preventDefault();

    const userLoginId = document.getElementById("userLoginId").value = document.getElementById("userLoginId").value.trim();
    const pass = document.getElementById("pass").value = document.getElementById("pass").value.trim();

    const errorMessageUserLoginId = document.getElementById("error-message-userLoginId");
    const errorMessagePass = document.getElementById("error-message-pass");

    errorMessageUserLoginId.innerText = "";
    errorMessagePass.innerText = "";

    if(userLoginId === ""){
        errorMessageUserLoginId.innerText = "아이디를 입력하세요.";
    }else if(userLoginId.length > 10){
        errorMessageUserLoginId.innerText = "아이디는 10자리 이하로 입력이 가능합니다.";
    }

    if(pass === ""){
        errorMessagePass.innerText = "비밀번호를 입력하세요.";
    }else if(pass.length < 10 || pass.length > 20){
        errorMessagePass.innerText = "비밀번호는 10~20자리로 입력이 가능합니다.";
    }

    if(errorMessageUserLoginId.innerText !== "" || errorMessagePass.innerText !== ""){
        alert("입력하신 정보를 확인하세요.");
        return false;
    }

    document.getElementById("loginForm").submit();

});
window.onload = function () {
    if(window.location.search === "?error"){
        alert("로그인 실패!");
    }
}