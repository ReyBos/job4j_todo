$(document).ready(function() {
    getListItems();
});

function getListItems() {
    $.ajax({
        type: "GET",
        url: "list"
    }).done(function(data) {
        let doneItems = JSON.parse(data.doneItems);
        let undoneItems = JSON.parse(data.undoneItems);
        let doneItemsStr = drawItems(doneItems);
        $(".js-done-items").html(doneItemsStr);
        let undoneItemsStr = drawItems(undoneItems);
        $(".js-undone-items").html(undoneItemsStr);
    }).fail(function(err){
        console.log(err);
    });
}

function drawItems(items) {
    let rsl = "";
    if (items.length === 0) {
        rsl += "<tr>" +
                    "<td class=\"center-align\">Список дел пуст</td>" +
                "</tr>";
    } else {
        items.forEach(function(item, i, arr) {
            rsl +=  "<tr>" +
                        "<td>" +
                            "<label>" +
                                "<input type=\"checkbox\" data-item-id='" + item.id + "'  data-item-done=" + item.done + ">" +
                                "<span class=\"todo-description\">" + item.description + "</span>" +
                            "</label>" +
                        "</td>" +
                        "<td class=\"right-align\">" +
                            "<a data-item-id='" + item.id + "' class=\"btn-floating btn-small waves-effect waves-light btn-delete\">+</a>" +
                        "</td>" +
                    "</tr>";
        });
    }
    return rsl;
}