var nextPageLesson = 1;
function loadMoreLesson() {
    var urlPage = "/loadMore?page=" + nextPageLesson;
    $.ajax({
        url: urlPage
    }).done(function (data) {
        $("#showMore").append(data);
        nextPageLesson++;
    })
}
var nextPageItems = 1;
function loadMoreItems() {
    var urlPage = "/loadMoreItems?page=" + nextPageItems;
    $.ajax({
        url: urlPage
    }).done(function (data) {
        $("#showMore").append(data);
        nextPageItems++;
    })
}