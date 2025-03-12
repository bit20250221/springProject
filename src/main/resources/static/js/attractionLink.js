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
    const urlParams = new URLSearchParams(window.location.search);
    const type = urlParams.get("type");
    const search = urlParams.get("search");
    location.href = "/attraction/list?page=" + id + "&type=" + type + "&search=" + search;
}
const urlParams = new URLSearchParams(window.location.search);
const type = urlParams.get("type");
const search = urlParams.get("search");
if(type !== null && search !== null){
    document.getElementById("type").value = type;
    document.getElementById("search").value = search;
}
