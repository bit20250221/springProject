const alertMessage = (message) => {
    if (message) {
        alert(message);
    }
};

document.addEventListener("DOMContentLoaded", function() {
    // Flash 메시지 출력
    const flashMessageValue = document.getElementById("message").value;
    alertMessage(flashMessageValue);

    // 각 업로드 버튼에 대해 이벤트 등록
    document.querySelectorAll('.upload-btn').forEach(uploadButton => {
        uploadButton.addEventListener('click', function(e) {
            e.preventDefault();
            // 같은 폼 내 파일 input 요소 찾기
            const form = this.closest('form');
            const fileInput = form.querySelector('input[type="file"]');
            if (!fileInput || fileInput.files.length === 0) {
                alert('업로드할 파일을 선택하세요.');
                return;
            }
            // 같은 폼 내 결과 표시 영역
            const resultContainer = form.querySelector('.board-image-result');
            // 업로드할 파일들을 순회
            for (let i = 0; i < fileInput.files.length; i++) {
                let imgFile = fileInput.files[i];
                let formData = new FormData();
                formData.append('tempImage', imgFile);

                fetch('/image/TempSave', {
                    method: 'POST',
                    body: formData,
                    credentials: 'include'
                })
                .then(response => response.json())
                .then(data => {
                    const imageBase64 = data.ImageBase64;           // Base64 인코딩된 이미지 문자열
                    const imageContentType = data.imageContentType;

                    const reader = new FileReader();
                    reader.onload = function(e) {
                        // 이미지 컨테이너 생성
                        const imageContainer = document.createElement('div');
                        imageContainer.style.display = 'inline-block';
                        imageContainer.style.margin = '10px';
                        imageContainer.style.textAlign = 'center';

                        // 서버에서 전달받은 이미지 관련 정보(hidden input)
                        const input1 = document.createElement('input');
                        input1.type = 'hidden';
                        input1.name = 'ImageName';
                        input1.value = data['ImageName'];

                        const input2 = document.createElement('input');
                        input2.type = 'hidden';
                        input2.name = 'ImageUUIDName';
                        input2.value = data['ImageUUIDName'];

                        // 삭제 버튼 생성
                        const imageDeleteButton = document.createElement('button');
                        imageDeleteButton.className = 'tempImageDeleteBtn';
                        imageDeleteButton.type = 'button';
                        imageDeleteButton.setAttribute('onclick', 'tempimageDelete(this)');
                        imageDeleteButton.textContent = 'X';

                        // 이미지 이름 표시
                        const imageName = document.createElement('p');
                        imageName.textContent = data['name'];
                        imageName.style.marginBottom = '5px';

                        // 업로드한 이미지 표시
                        const image = document.createElement('img');
                        image.src = "data:" + imageContentType + ";base64," + imageBase64;
                        image.style.width = '300px';
                        image.style.height = '300px';

                        imageContainer.appendChild(input1);
                        imageContainer.appendChild(input2);
                        imageContainer.appendChild(imageDeleteButton);
                        imageContainer.appendChild(imageName);
                        imageContainer.appendChild(image);

                        resultContainer.appendChild(imageContainer);
                    };
                    reader.readAsDataURL(imgFile);
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('업로드 실패!!');
                });
            }
        });
    });

    // 각 "작성" 버튼에 대해 폼 제출 이벤트 등록
    document.querySelectorAll('.board-form-submit').forEach(submitButton => {
        submitButton.addEventListener('click', function(e) {
            e.preventDefault();
            const container = this.closest('.card-body');
            const form = container ? container.querySelector('form') : null;
            if (form) {
                const activeTabElement = document.querySelector('.nav-link.active');
                if(activeTabElement){
                    const activeTabValue = activeTabElement.textContent.trim();
                    let hiddenTabInput = form.querySelector('input[name="activeTab"]');
                    if (!hiddenTabInput) {
                        hiddenTabInput = document.createElement('input');
                        hiddenTabInput.type = 'hidden';
                        hiddenTabInput.name = 'activeTab';
                        form.appendChild(hiddenTabInput);
                    }
                    hiddenTabInput.value = activeTabValue;
                }
                form.submit();

            } else {
                console.error("폼 요소를 찾을 수 없습니다.");
            }
        });
    });
});

// 이미지 삭제 함수 (서버 요청 후 DOM에서 삭제)
function tempimageDelete(btn) {
    const imageContainer = btn.parentNode;
    const inputs = imageContainer.querySelectorAll('input');
    const imageNameValue = inputs[0].value;
    const imageUUIDNameValue = inputs[1].value;

    console.log('imageName:', imageNameValue);
    console.log('imageUUIDName:', imageUUIDNameValue);

    let formData = new FormData();
    formData.append('ImageName', imageNameValue);
    formData.append('ImageUUIDName', imageUUIDNameValue);

    fetch('/image/deleteOne', {
        method: 'POST',
        body: formData,
        credentials: "include"
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        alert('삭제 성공!!');
        imageContainer.parentNode.removeChild(imageContainer);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('삭제 실패!!');
    });
}

// 관광지 선택 영역 토글 함수
function toggleAction(){
    const action = document.querySelector('.attraction-list-element');
    if (action) {
        if (action.style.display === 'none' || action.style.display === '') {
            action.style.display = 'block';
        } else {
            action.style.display = 'none';
        }
    }
}