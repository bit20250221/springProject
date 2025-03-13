const alertMessage = (message) => {
    if(message) {
        alert(message);
    }
}
  document.addEventListener("DOMContentLoaded", function() {
      const flashMessageValue = document.getElementById("message").value;
      alertMessage(flashMessageValue);
  });

    function back() {
        location.href="/board/list";
    }
    function deleteboard(){
        var result = confirm("삭제하시겠습니까?");
        if (result) {

            var board_id = document.querySelector(".boardID").textContent;
            var form = document.createElement("form");
            console.log(board_id);
            form.id = "deleteForm";
            form.method = "post";
            form.action = "/board/deleteBoard/" + board_id;

            var input = document.createElement("input");
            input.type = "hidden";
            input.name = "board_id";
            input.value = board_id;
            form.appendChild(input);

            document.body.appendChild(form);
            form.submit();
        }
    }

    function updateboard(){
        var board_id = document.querySelector(".boardID").textContent;
        location.href = "/board/updateBoard/" + board_id;
    }