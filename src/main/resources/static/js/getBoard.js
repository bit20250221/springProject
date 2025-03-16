const alertMessage = (message) => {
    if(message) {
        alert(message);
    }
};

document.addEventListener("DOMContentLoaded", function() {
    const flashMessageValue = document.getElementById("message").value;
    alertMessage(flashMessageValue);
});

function back() {
    location.href = "/board/list";
}

function deleteboard(){
    if(confirm("삭제하시겠습니까?")){
        const board_id = document.querySelector(".boardNum").value;
        const form = document.createElement("form");
        form.method = "post";
        form.action = "/board/deleteBoard/" + board_id;

        const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
        const csrfParamMeta = document.querySelector('meta[name="_csrf_parameterName"]');
        if(csrfTokenMeta && csrfParamMeta){
            const csrfInput = document.createElement("input");
            csrfInput.type = "hidden";
            csrfInput.name = csrfParamMeta.getAttribute("content");
            csrfInput.value = csrfTokenMeta.getAttribute("content");
            form.appendChild(csrfInput);
        }

        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "board_id";
        input.value = board_id;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    }
}

function updateboard(){
    const board_id = document.querySelector(".boardNum").value;
    location.href = "/board/updateBoard/" + board_id;
}

// 댓글 목록 새로고침: 서버에서 해당 게시글의 댓글 목록을 가져와 동적으로 표시함
function commentRefresh(){
    const boardIdElement = document.querySelector(".boardNum");
    if(!boardIdElement){
        console.error("boardNum element not found");
        return;
    }
    const boardId = boardIdElement.value;
    fetch(`/comments/board/${boardId}`)
    .then(response => response.json())
    .then(data => {
        const currentUserElement = document.getElementById("currentUser");
        const currentUser = currentUserElement ? currentUserElement.value : "";
        const listGroup = document.querySelector("#commentList .list-group");
        if(listGroup) {
            listGroup.innerHTML = "";
        }

        if(data && data.length > 0){
            data.forEach(comment => {
                const commentDiv = document.createElement("div");
                // 동적으로 생성된 댓글 컨테이너에 commentItem 클래스를 추가합니다.
                commentDiv.className = "list-group-item commentItem";
                commentDiv.innerHTML = `
                    <input type="hidden" class="commentId" value="${comment.id}">
                    <div class="d-flex w-100 justify-content-between">
                        <h6 class="mb-1 fw-bold">${comment.userLoginId}</h6>
                        <small class="text-muted">${comment.createDate}</small>
                    </div>
                    <p class="mb-1 commentContent">${comment.content}</p>
                    <small class="text-muted">${comment.updateDate ? comment.updateDate : ""}</small>
                    <div class="mt-2">
                        <button class="comment_delete btn btn-outline-danger btn-sm" onclick="commentDelete(this)">삭제하기</button>
                        ${comment.userLoginId === currentUser ?
                            `<div class="d-inline-block ms-2"><button class="comment_update btn btn-outline-primary btn-sm" onclick="commentUpdate(this)">수정하기</button></div>`
                            : ""}
                    </div>
                `;
                if(listGroup) {
                    listGroup.appendChild(commentDiv);
                }
            });
            document.getElementById("commentList").style.display = "block";
            document.getElementById("emptyCommentList").style.display = "none";
            document.getElementById("commentCount").textContent = data.length;
        } else {
            document.getElementById("commentList").style.display = "none";
            document.getElementById("emptyCommentList").style.display = "block";
            document.getElementById("commentCount").textContent = "0";
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("서버와의 통신 실패");
    });
}

// 댓글 등록: 입력된 댓글 내용을 서버로 전송한 후 새로고침
function commentInsert(){
    const commentInput = document.querySelector(".commentInput");
    const content = commentInput.value.trim();
    if(content === ""){
        alert("댓글 내용을 입력해주세요.");
        return;
    }
    const boardId = document.querySelector(".boardNum").value;
    const formData = new FormData();
    formData.append("content", content);
    formData.append("boardId", boardId);
    fetch('/comments/write', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if(data.comment){
            commentRefresh();
            commentInput.value = "";
        } else {
            alert("댓글 등록에 실패했습니다.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("서버와의 통신 실패");
        commentInput.value = "";
        commentRefresh();
    });
}

// 댓글 삭제: 클릭한 댓글의 숨겨진 commentId 값을 이용하여 삭제 처리 후 새로고침
function commentDelete(buttonElement){
    const commentDiv = buttonElement.closest(".list-group-item");
    if(!commentDiv) {
        console.error("댓글 컨테이너를 찾을 수 없습니다.");
        return;
    }
    const commentIdInput = commentDiv.querySelector(".commentId");
    if(!commentIdInput) {
        console.error("댓글 ID 요소를 찾을 수 없습니다.");
        return;
    }
    const commentId = commentIdInput.value;
    if(confirm("정말로 삭제하시겠습니까?")){
        fetch(`/comments/delete/${commentId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if(!response.ok){
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

// 댓글 수정: 해당 댓글의 내용을 prompt로 받아 수정하고, 서버에 전송 후 새로고침
function commentUpdate(buttonElement){
    const commentDiv = buttonElement.closest(".list-group-item");
    if(!commentDiv){
        console.error("댓글 컨테이너를 찾을 수 없습니다.");
        return;
    }
    const commentIdInput = commentDiv.querySelector(".commentId");
    if(!commentIdInput){
        console.error("댓글 ID 요소를 찾을 수 없습니다.");
        return;
    }
    const commentId = commentIdInput.value;
    const commentContentElement = commentDiv.querySelector(".commentContent");
    if(!commentContentElement){
        console.error("댓글 내용을 찾을 수 없습니다.");
        return;
    }
    const currentContent = commentContentElement.textContent;
    const newContent = prompt("수정할 내용을 입력하세요:", currentContent);
    if(newContent === null || newContent.trim() === ""){
        alert("수정 내용이 비어있습니다.");
        return;
    }
    const formData = new FormData();
    formData.append("newContent", newContent);
    fetch(`/comments/update/${commentId}`, {
        method: 'PUT',
        body: formData
    })
    .then(response => {
        if(!response.ok){
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
