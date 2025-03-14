document.getElementById("saveForm").addEventListener("submit", function(event){
    event.preventDefault();

    const userLoginId = document.getElementById("userLoginId").value = document.getElementById("userLoginId").value.trim();
    const pass = document.getElementById("pass").value = document.getElementById("pass").value.trim();
    const confirmPass = document.getElementById("confirmPass").value = document.getElementById("confirmPass").value.trim();

    const errorMessageUserLoginId = document.getElementById("error-message-userLoginId");
    const errorMessagePass = document.getElementById("error-message-pass");
    const errorMessageConfirmPass = document.getElementById("error-message-confirmPass");

    errorMessageUserLoginId.innerText = "";
    errorMessagePass.innerText = "";
    errorMessageConfirmPass.innerText = "";

    if(userLoginId === ""){
        errorMessageUserLoginId.innerText = "아이디를 입력하세요.";
    }else if(userLoginId.length > 20 || userLoginId.length < 5){
        errorMessageUserLoginId.innerText = "아이디는 5~20자리로 입력이 가능합니다.";
    }

    if(pass === ""){
        errorMessagePass.innerText = "비밀번호를 입력하세요.";
    }else if(pass.length < 10 || pass.length > 20){
        errorMessagePass.innerText = "비밀번호는 10~20자리로 입력이 가능합니다.";
    }

    if(confirmPass === ""){
        errorMessageConfirmPass.innerText = "비밀번호 확인을 입력하세요.";
    }

    if(pass !== confirmPass){
        errorMessageConfirmPass.innerText = "비밀번호가 동일하지 않습니다.";
    }

    if(errorMessageUserLoginId.innerText !== "" || errorMessagePass.innerText !== "" || errorMessageConfirmPass.innerText !== ""){
        alert("입력하신 정보를 확인하세요.");
        return false;
    }

    if(confirm("회원가입을 하시겠습니까?", )){
        alert("요청이 완료되었습니다.!");
        document.getElementById("saveForm").submit();
    }

    return false;
});
