$(".js-show-all-task").change(function () {
    $(".js-todo-list-container-all").toggle();
    $(".js-todo-list-container-main").toggle();
});

$(".js-add-todo-item").click(function () {
    let description = $(".js-item-description").val();
    if (description === "") {
        showModalError("Для добавления задачи необходимо заполнить все поля.");
        return false;
    }
    let data = {
        "description": description
    };
    let categoriesIds = $(".js-item-category").val();
    if (categoriesIds.length !== 0) {
        let categories = [];
        categoriesIds.forEach(function (categoryId) {
            categories.push({id: categoryId})
        })
        data["categories"] = categories;
    }
    $.ajax({
        type: "POST",
        url: "item?action=save",
        contentType: "application/json",
        data: JSON.stringify(data),
    }).done(function(data) {
        showItems(data)
        refreshItemAddForm();
    }).fail(function(err) {
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
        showModalError("Ошибка при добавлении элемента, перезагрузите страницу или повторите запрос позднее.");
    });
});

function refreshItemAddForm() {
    $(".js-item-description").val("");
    $(".js-item-category").val([]);
    $('.js-item-category').formSelect();
}

function showModalError(msg) {
    $(".js-modal-msg").text(msg);
    let instance = M.Modal.getInstance($(".js-modal"));
    instance.open();
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
        showModalError("Ошибка при удалении элемента, перезагрузите страницу или повторите запрос позднее.");
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
        showModalError("Ошибка при обновлении элемента, перезагрузите страницу или повторите запрос позднее.");
    });
});