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

            var board_id = document.querySelector(".boardNum").value;
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
        var board_id = document.querySelector(".boardNum").value;
        location.href = "/board/updateBoard/" + board_id;
    }

    //댓글 관련 api
    function commentRefresh(){
        const boardIdElement = document.querySelector(".boardNum");
        if(boardIdElement==null){
            console.error("boardId element not found");
            return;
        }
        const boardId=boardIdElement.value;

        var refreshUrl="/comments/board/"+boardId;
        fetch(refreshUrl)
        .then(response => response.json())
        .then(data => {
             const currentUserElement=document.querySelector("#currentUser");
             let currentUser="";
             if(currentUserElement!=null){
                 currentUser=currentUserElement.value;
             }
             const commentElementsDiv = document.querySelector(".comment.elements");
             if(commentElementsDiv)
             commentElementsDiv.innerHTML = "";
             if(data && data.length > 0){
                 data.forEach(comment => {
                     const commentDiv = document.createElement("div");
                     commentDiv.className = "comment element";
                     commentDiv.innerHTML = `
                            <div class="comment element content">
                            <input class="comment element content num" type="hidden" value="${comment.id}">
                            <p class="comment element content user">${comment.userLoginId}</p>
                            <p class="comment element content content">${comment.content}</p>
                            <p class="comment element  content credate">${comment.createDate}</p>
                            ${comment.updateDate !==null ? `<p class="comment element content update">${comment.updateDate}</p>` : ''}
                            </div>
                            <div class="comment button">
                            ${currentUser !== "" ? `<button class="comment_delete" onclick="commentDelete(this)">삭제하기</button>` : ''}
                            ${comment.userLoginId !== currentUser ? '':`<button class="comment_update" onclick="commentUpdate(this)">수정하기</button>`}
                            </div>
                            `;
                     commentElementsDiv.appendChild(commentDiv);
                 });
                    document.getElementById("commentList").style.display = "block";
                    document.getElementById("emptyCommentList").style.display = "none";
                    document.getElementById("commentCount").textContent = data.length;
                }else{
                    document.getElementById("commentList").style.display = "none";
                    document.getElementById("emptyCommentList").style.display = "block";
                    document.getElementById("commentCount").textContent = "0";
                }
            }).catch(error => {
                 console.error('Error:', error);
                 alert("서버와의 통신 실패");
            });
    }

    function commentInsert(){
            const commentInput = document.querySelector(".commentInput");
            const content = commentInput.value.trim();
            if (content === "") {
                alert("댓글 내용을 입력해주세요.");
                return;
            }
            const boardId = document.querySelector(".boardNum").value;
            var formData=new FormData();
            formData.append("content", content);
            formData.append("boardId", boardId);
            fetch('/comments/write', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.comment) {
                    commentRefresh();
                    commentInput.value = "";
                } else {
                    alert("댓글 등록에 실패했습니다.");
                }
            }).catch(error => {
                console.error('Error:', error);
                alert("서버와의 통신 실패");
                commentInput.value = "";
                commentRefresh();
            });
    }

    function commentDelete(buttonElement){
        const commentDiv = buttonElement.closest(".comment.element");
        const commentId = commentDiv.querySelector(".content.num").value;
        if (confirm("정말로 삭제하시겠습니까?")) {
        fetch(`/comments/delete/${commentId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("댓글 삭제에 실패했습니다.");
            }
            commentRefresh();
        })
        .catch(error => {
               console.error(error);
               alert("댓글 삭제 중 오류가 발생했습니다.");
           });
        }
    }

    function commentUpdate(buttonElement){
        const commentDiv = buttonElement.closest(".comment.element");
        if (!commentDiv) {
            console.error("댓글 컨테이너를 찾을 수 없습니다.");
            return;
        }

        const commentIdElement = commentDiv.querySelector("input.comment.element.content.num");
        if (!commentIdElement) {
            console.error("댓글 ID 요소를 찾을 수 없습니다.");
            return;
        }
        const commentId = commentIdElement.value;

        const pElements = commentDiv.querySelectorAll("p.comment.element.content");
        if (pElements.length < 2) {
            console.error("댓글 내용을 담은 요소를 찾을 수 없습니다.");
            return;
        }
        const contentText = pElements[1];
        const currentContent = contentText.textContent;

        const newContent = prompt("수정할 내용을 입력하세요:", currentContent);
        var formData=new FormData();
        if (newContent === null || newContent.trim() === "") {
            alert("수정 내용이 비어있습니다.");
            return;
        }
        formData.append("newContent", newContent);
        fetch('/comments/update/'+commentId,{
            method: 'PUT',
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("댓글 수정에 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            commentRefresh();
        })
        .catch(error => {
            console.error(error);
            alert("댓글 수정 중 오류가 발생했습니다.");
        });
    }