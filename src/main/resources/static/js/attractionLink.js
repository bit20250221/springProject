const attractionSaveLink = () => {
    location.href = "/attraction/save";
}
const attractionListLink = () => {
    location.href = "/attraction/list";
}
const attractionDetailLink = (id) => {
    location.href = "/attraction/detail/" + id;
}
const attractionUpdateLink = (id) => {
    location.href = "/attraction/update/" + id;
}
const attractionDeleteLink = (id) => {
    location.href = "/attraction/delete/" + id;
}
const attractionMainLink = () => {
    location.href = "/attraction";
}
const attractionReservationLink = (id) => {
    location.href = "/reservation/save/" + id;
}
const attractionPagingLink = (id) => {
    if(id <= 0){
        id = 1;
    }
    location.href = "/attraction/list?page=" + id;
}
