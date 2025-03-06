const reservationSaveLink = (id) => {
    location.href = "/reservation/save/" + id;
}
const reservationListLink = () => {
    location.href = "/reservation/list";
}
const reservationDetailLink = (id) => {
    location.href = "/reservation/detail/" + id;
}
const reservationDeleteLink = (id) => {
    location.href = "/reservation/delete/" + id;
}