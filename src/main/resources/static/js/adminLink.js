const listReq = () => {
    location.href = "/admin/user/";
}
const updateReq = (id) => {
    location.href = "/admin/user/update/" + id;
}
const deleteReq = (id) => {
    location.href = "/admin/user/delete/" + id;
}
const detailReq = (id) => {
    location.href = "/admin/user/" + id;
}
const userSaveLink = () => {
    location.href = "/admin/user/save";
}