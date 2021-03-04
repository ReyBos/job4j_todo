$(".js-show-all-task").change(function () {
    $(".js-todo-list-container-all").toggle();
    $(".js-todo-list-container-main").toggle();
});

$(".js-add-todo-item").click(function () {
    let description = $(".js-item-description").val();
    let data = {"description": description};
    $.ajax({
        type: "POST",
        url: "item?action=save",
        contentType: "application/json",
        data: JSON.stringify(data),
    }).done(function(data) {
        showItems(data)
        refreshItemAddForm();
    }).fail(function(err) {
        $(".js-modal-msg").text("Ошибка при добавлении элемента, перезагрузите страницу или повторите запрос позднее.");
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
    });
});

function refreshItemAddForm() {
    $(".js-item-description").val("");
}

$(".js-main-container").on("click", ".js-item-delete", function() {
    let itemId = $(this).data("item-id");
    let data = {"id": itemId};
    $.ajax({
        type: "POST",
        url: "item?action=delete",
        contentType: "application/json",
        data: JSON.stringify(data),
    }).done(function(data) {
        showItems(data)
    }).fail(function(err) {
        $(".js-modal-msg").text("Ошибка при удалении элемента, перезагрузите страницу или повторите запрос позднее.");
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
    });
});

$(".js-main-container").on("change", ".js-change-item-status", function() {
    let itemId = $(this).data("item-id");
    let itemDone = $(this).data("item-done");
    let data = {"id": itemId, "done": !itemDone};
    $.ajax({
        type: "POST",
        url: "item?action=update",
        contentType: "application/json",
        data: JSON.stringify(data),
    }).done(function(data) {
        showItems(data)
    }).fail(function(err) {
        $(".js-modal-msg").text("Ошибка при обновлении элемента, перезагрузите страницу или повторите запрос позднее.");
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
    });
});