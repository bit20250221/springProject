const alertMessage = (message) => {
    if(message) {
        alert(message);
    }
}
  document.addEventListener("DOMContentLoaded", function() {
      const flashMessageValue = document.getElementById("message").value;
      alertMessage(flashMessageValue);
  });
    var fileInput=document.querySelector('.board_image');
    var ImageFileDataList=document.querySelector('.boardImageList');
    var fileTempUploadButton = document.querySelector('.uploadBtn');
    var ImageTempResult = document.querySelector('.board_image_result')
    var imageIndex = 0;
if (fileTempUploadButton) {
    fileTempUploadButton.addEventListener('click', function(e) {
        e.preventDefault();
        var files= fileInput.files;

        if (!files || files.length === 0) {
                    alert('업로드할 파일을 선택하세요.');
                    return;
        }
    for (let i = 0; i < files.length; i++) {
        let imgFile = files[i];
        let formData = new FormData();
        formData.append('tempImage', imgFile);

        fetch('/image/TempSave',{
            method: 'POST',
            body: formData,
            credentials: "include"
        })
        .then(response => response.json())
        .then(data => {
            var imageBase64 = data.ImageBase64;           // Base64 인코딩된 이미지 문자열
            var imageContentType = data.imageContentType;

            const reader = new FileReader();
            reader.onload = function(e) {

                const imageContainer=document.createElement('div');
                imageContainer.style.display='inline-block';
                imageContainer.style.margin='10px';
                imageContainer.style.textAlign='center';

                var input1=document.createElement('input');
                input1.type='hidden';
                input1.name='ImageName';
                input1.value=data['ImageName'];

                var input2=document.createElement('input');
                input2.type='hidden';
                input2.name='ImageUUIDName';
                input2.value=data['ImageUUIDName'];

                const imageDeleteButton=document.createElement('button');
                imageDeleteButton.className='tempImageDeleteBtn';
                imageDeleteButton.type='button';
                imageDeleteButton.setAttribute('onclick', 'tempimageDelete(this)');
                imageDeleteButton.textContent='X';

                const imageName=document.createElement('p');
                imageName.textContent=data['name'];
                imageName.style.marginBottom='5px';

                const image=document.createElement('img');
                image.src= "data:" + imageContentType + ";base64," + imageBase64;
                image.style.width='300px';
                image.style.height='300px';

                imageContainer.appendChild(input1);
                imageContainer.appendChild(input2);
                imageContainer.appendChild(imageDeleteButton);
                imageContainer.appendChild(imageName);
                imageContainer.appendChild(image);

                ImageTempResult.appendChild(imageContainer);
                imageIndex++;
            }
            reader.readAsDataURL(imgFile);

        }).catch(error => {
            console.error('Error:', error);
            alert('업로드 실패!!');
        });
       }

    });
}
    function tempimageDelete(btn) {
        //나중에 fetch로 db와 서버내 파일 삭제 구현
        var formData= new FormData();
        var imageContainer=btn.parentNode;
        var inputs = imageContainer.querySelectorAll('input');
        var input1 = inputs[0].value;
        var input2 = inputs[1].value;

        console.log('imageName:', input1);
        console.log('imageUUIDName:', input2);

        formData.append('ImageName', input1);
        formData.append('ImageUUIDName', input2);

        fetch('/image/deleteOne',{
            method: 'POST',
            body: formData,
            credentials: "include"
        }).then(response => response.json())
        .then(data => {
            console.log(data);
            alert('삭제 성공!!');
            imageContainer.parentNode.removeChild(imageContainer);
                imageIndex--;
                if(imageIndex===0){
                    ImageTempResult.innerHTML='';
                }
        }).catch(error => {
            console.error('Error:', error);
            alert('삭제 실패!!');
        });


        return;
    }
    var submitButton = document.querySelector('.boardFormSubmit');
    submitButton.addEventListener('click', function(e) {
        e.preventDefault();

        var form = document.querySelector('form');
        form.submit();
    });

    function toggleAction(){
        var action = document.querySelector('.attractionList_element');
        if(action.style.display === 'none'){
            action.style.display = 'block';
        }else {
            action.style.display = 'none';
        }
    }

    //게시글 입력 창에서 벗어나면 임시 업로드된 이미지를 모두 삭제시켜야한다.
    window.addEventListener("beforeunload",function(e){
        var url = "/image/delete";

        /*
        fetch('/image/delete',{
            method: 'POST',
            credentials: "include"
        }).then(response => response.json())
        .then(data => {
            console.log(data);
        }).catch(error => {
            console.error('Error:', error);
        });
        */
        //navigator.sendBeacon(url);

    });