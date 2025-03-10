    var fileInput=document.querySelector('.board_image');
    var ImageFileDataList=document.querySelector('.boardImageList');
    var fileTempUploadButton = document.querySelector('.uploadBtn');
    var ImageTempResult = document.querySelector('.board_image_result')
    var imageIndex = 0;

    fileTempUploadButton.addEventListener('click', function(e) {
        e.preventDefault();

        var formData = new FormData();
        var imgFile=fileInput.files[0];

        if(imgFile==null){
            alert('업로드할 파일을 선택하세요.');
            return;
        }

        formData.append('tempImage',imgFile);

        fetch('/image/TempSave',{
            method: 'POST',
            body: formData,
            credentials: "include"
        })
        .then(response => response.json())
        .then(data => {
            // UUIDName 받아서 boardImageList 에 추가하고 정상적으로 추가되면 이미지를 사용자에게 보여준다.
            var ImageDTO=data.ImageDTO;
            console.log(ImageDTO);
            ImageFileDataList.innerHTML  += '<div class="imageDTOValue">' +
            '<input type="hidden" name="boardImageDtoList[' + imageIndex + '].imageSize" value="' + ImageDTO['imageSize'] + '">' +
            '<input type="hidden" name="boardImageDtoList[' + imageIndex + '].isTemporary" value="' + ImageDTO['isTemporary'] + '">' +
            '<input type="hidden" name="boardImageDtoList[' + imageIndex + '].name" value="' + ImageDTO['name'] + '">' +
            '<input type="hidden" name="boardImageDtoList[' + imageIndex + '].uuid" value="' + ImageDTO['uuid'] + '">' +
            '<input type="hidden" name="boardImageDtoList[' + imageIndex + '].uuidname" value="' + ImageDTO['uuidname'] + '">' +
            '</div>';
            imageIndex++;

            const reader = new FileReader();
            reader.onload = function(e) {

                const imageContainer=document.createElement('div');
                imageContainer.style.display='inline-block';
                imageContainer.style.margin='10px';
                imageContainer.style.textAlign='center';

                const imageDeleteButton=document.createElement('button');
                imageDeleteButton.className='tempImageDeleteBtn';
                imageDeleteButton.setAttribute('onclick', 'tempimageDelete(this)');
                imageDeleteButton.textContent='X';


                const imageName=document.createElement('p');
                imageName.textContent=ImageDTO['name'];
                imageName.style.marginBottom='5px';

                const image=document.createElement('img');
                image.src= e.target.result;
                image.style.width='300px';
                image.style.height='300px';

                imageContainer.appendChild(imageDeleteButton);
                imageContainer.appendChild(imageName);
                imageContainer.appendChild(image);

                ImageTempResult.appendChild(imageContainer);
            }
            reader.readAsDataURL(imgFile);

        }).catch(error => {
            console.error('Error:', error);
            alert('업로드 실패!!');
        });

    })

    function tempimageDelete(btn) {
        //나중에 fetch로 db와 서버내 파일 삭제 구현
        var imageContainer=btn.parentNode;
        imageContainer.parentNode.removeChild(imageContainer);
        imageIndex--;
        if(imageIndex===0){
            ImageTempResult.innerHTML='';
        }
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
