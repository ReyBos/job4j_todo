$(document).ready(function() {
    getListItems();
});

function getListItems() {
    $.ajax({
        type: "GET",
        url: "list"
    }).done(function(data) {
        showItems(data)
    }).fail(function(err) {
        $(".js-modal-msg").text("Ошибка загрузки списка, перезагрузите страницу или повторите запрос позднее.");
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
    });
}

function showItems(data) {
    let doneItems = JSON.parse(data.doneItems);
    let undoneItems = JSON.parse(data.undoneItems);
    let doneItemsStr = drawItems(doneItems);
    $(".js-done-items").html(doneItemsStr);
    let undoneItemsStr = drawItems(undoneItems);
    $(".js-undone-items").html(undoneItemsStr);
    let doneItemsAllInfoStr = drawItems(undoneItems, true);
    $(".js-undone-items-all-info").html(doneItemsAllInfoStr);
}

function drawItems(items, showAllInfo = false) {
    let rsl = "";
    if (items.length === 0) {
        rsl += "<tr>" +
                    "<td class=\"center-align\">список пуст</td>" +
                "</tr>";
    } else {
        items.forEach(function(item, i, arr) {
            rsl +=  "<tr>" +
                        "<td>" +
                            "<label>" +
                                "<input class=\"js-change-item-status\" type=\"checkbox\" data-item-id='" + item.id + "'  data-item-done=" + item.done + " " + (item.done ? "checked" : "") + ">" +
                                "<span class=\"todo-description\">" + item.description + "</span>" +
                            "</label>" +
                        "</td>";
            if (showAllInfo) {
                rsl +=  "<td>" + item.user.name + "</td>";
            }
            rsl +=      "<td class=\"right-align\">" +
                            "<a data-item-id='" + item.id + "' class=\"btn-floating btn-small waves-effect waves-light btn-delete js-item-delete\">+</a>" +
                        "</td>" +
                    "</tr>";
        });
    }
    return rsl;
}