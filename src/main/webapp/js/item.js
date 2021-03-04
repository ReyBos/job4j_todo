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
        $(".js-modal-msg").text("Ошибка при изменении элемента, перезагрузите страницу или повторите запрос позднее.");
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
    });
})

function refreshItemAddForm() {
    $(".js-item-description").val("");
}