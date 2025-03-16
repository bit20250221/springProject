const alertMessage = (message) => {
    if(message) {
        alert(message);
    }
}
  document.addEventListener("DOMContentLoaded", function() {
      const flashMessageValue = document.getElementById("message").value;
      alertMessage(flashMessageValue);
  });

    var uploadTempBtn = document.querySelector('.updateTempUpload');
    var uploadtemp = document.querySelector('.imageUpload');
    var deleteBtn = document.querySelector('.deleteBtn');
    var uploadImageList=document.querySelector('.updateImageListInner');

    function toggleAction(){
        var action = document.querySelector('.attractionList_element');
        if(action.style.display === 'none'){
            action.style.display = 'block';
        }else {
            action.style.display = 'none';
        }
    }

    //이미지 파일을 업로드 하는 코드
    if(uploadTempBtn){
    uploadTempBtn.addEventListener('click', function(e){
        e.preventDefault();

        var formData = new FormData();
        var boardId = document.querySelector(".board_id").value;
        formData.append('boardId', boardId);

        var files=uploadtemp.files;
        if(!files ||files.length === 0){
            alert('업로드할 파일을 선택하세요.');
            return;
        }

        for(var i=0; i<files.length; i++){
            var imgFile = files[i];
            formData.append('images',imgFile);
        }

        fetch('/image/ImageUpdates', {
            method: 'POST',
            body: formData,
            credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if(data.message ==='Success'){

                console.log('이미지 업로드 성공');
                var i=0;
                var imagevalues=data.UpdateImages;
                Array.from(files).forEach(file => {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                    console.log((i+1)+'번 이미지 업로드')
                    var img = document.createElement('img');
                    var imageouter1=document.createElement('div');
                    var imageouter2=document.createElement('div');
                    var imageContainer = document.createElement('div');
                    var imageName=document.createElement('p');
                    var imagevaluesOne=imagevalues[i];
                    const imageDeleteButton=document.createElement('button');


                    var input1=document.createElement('input');
                    input1.type='hidden';
                    input1.name='ImageName';
                    input1.value=imagevaluesOne.name;

                    var input2=document.createElement('input');
                    input2.type='hidden';
                    input2.name='ImageUUIDName';
                    input2.value=imagevaluesOne.uuidname;

                    imageContainer.className='updateImageElement';
                    imageContainer.style.display='inline-block';
                    imageContainer.style.margin='10px';
                    imageContainer.style.textAlign='center';

                    imageDeleteButton.className='tempImageDeleteBtn';
                    imageDeleteButton.type='button';
                    imageDeleteButton.textContent='X';
                    imageDeleteButton.setAttribute('onclick','deleteTempImage(this)');
                    imageName.textContent=data.UpdateImages[i].name+'(신규)';
                    imageName.style.marginBottom='5px';

                    img.className='boardimgElement';
                    img.src = e.target.result;
                    img.style.width='300px';
                    img.style.height='300px';

                    imageContainer.appendChild(input1);
                    imageContainer.appendChild(input2);
                    imageContainer.appendChild(imageDeleteButton);
                    imageContainer.appendChild(imageName);
                    imageContainer.appendChild(img);

                    //게시글 내 이미지가 처음부터 없어서 이미지 요소들을 저장할 div가 없으면 생성해야한다.
                    if(uploadImageList!=null){
                        imageouter1.appendChild(imageouter2);
                        imageouter2.appendChild(imageContainer);
                        uploadImageList.appendChild(imageouter1);
                    }else{
                        uploadImageList = document.createElement('div');
                        uploadImageList.className='updateImageListInner';
                        document.querySelector('.updateImageList').appendChild(uploadImageList);
                        imageouter1.appendChild(imageouter2);
                        imageouter2.appendChild(imageContainer);
                        uploadImageList.appendChild(imageouter1);
                    }
                    i++;
                    };
                    reader.readAsDataURL(file);
                });


            }else{
                alert('이미지 업로드 실패');
            }
        })
        .catch(error =>{
            console.error('Error:', error);
            alert('이미지 업로드 중 오류 발생');
        });

    });
    }

    var tempdelete=document.querySelector('.tempImageDeleteBtn');
    function deleteTempImage(btn){
        if(confirm('임시로 등록된 이미지를 삭제합니다')){
                var formData = new FormData();
                var parent = btn.parentNode;
                var inputs = parent.querySelectorAll('input');
                var input1 = inputs[0].value;
                var input2 = inputs[1].value;

                formData.append('ImageName',input1)
                formData.append('ImageUUIDName',input2)

                fetch('/image/deleteOne', {
                    method: 'POST',
                    body: formData,
                    credentials: 'include'
                })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    if(data.message ==='Success'){
                        alert('이미지 삭제 완료');
                        parent.remove();
                    }else{
                        alert('이미지 삭제 실패');
                    }
                }).catch(error =>{
                    console.error('Error:', error);
                    alert('임시 이미지 삭제 중 오류 발생 '+ error);
                    return;
                });
        }
    }
    //이미 등록된 기존 이미지만 사용
    function deleteImage(btn){
        if(confirm('정말로 해당 이미지를 삭제하시겠습니까?(복구 불가)')){

            var boardId = document.querySelector(".board_id").value;
            var parent = btn.parentNode;
            var inputs=parent.querySelectorAll('input');
            var input1=inputs[0].value;
            var input2=inputs[1].value;
            var input3=boardId;
            var formData=new FormData();
            formData.append('ImageUUID',input1);
            formData.append('ImageUUIDName',input2);
            formData.append('boardId',input3);

            fetch('/image/deleteImage', {
                method: 'POST',
                body: formData,
                credentials: 'include'
            })
            .then(response => response.json())
            .then(data => {
                    alert('해당 이미지 삭제 완료');
                if(data.message ==='Success'){
                    //이미지가 삭제되었으면 클라이언트 상에서도 안보여야한다.
                    parent.remove();
                }else{
                    alert('이미지 삭제 실패');
                }
            })
            .catch(error=> {
                console.error('Error:', error);
                alert('이미지 삭제 중 오류 발생');
            });
        }
    }

    //여기서는 이미 업로드 된 이미지 파일을 제외하고 임시로 업로드 된 파일들만 업로드 시켜야한다.
    function submitFunction(){
        var submitForm=document.querySelector('.submitForm');

        //관광지 선택지에서 라디오 버튼에 체크된 원소의 값을 가져온다(만약 체크 안되어 있으면 수정도 안됨)
        var attraction_id=document.querySelector('input[name="attraction_id"]:checked');
        if(attraction_id!=null){
            attraction_id=attraction_id.value;
            submitForm.setAttribute('attraction_id', attraction_id);
        }
        var user_id=document.querySelector('.user_id').value;
        submitForm.setAttribute('user_id', user_id);

        submitForm.submit();
    }