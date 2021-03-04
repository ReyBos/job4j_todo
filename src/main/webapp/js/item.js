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
        let doneItems = JSON.parse(data.doneItems);
        let undoneItems = JSON.parse(data.undoneItems);
        console.log(doneItems);
        console.log(undoneItems);
    }).fail(function(err){
        console.log(err);
    });
})