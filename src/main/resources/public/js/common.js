function submit(formName, containerDiv) {
    var serializedData = $('#' + formName).serialize();
    console.log(serializedData)
    $.ajax({
        url: formName,
        method: 'post',
        data: serializedData,
        success: function(response, statusText, xhr) {
            $('#' + containerDiv).html(response);
        }
    })
}

function getSubmit(formName, containerDiv) {
    var serializedData = $('#' + formName).serialize();
    console.log(serializedData)
    $.ajax({
        url: formName,
        method: 'get',
        data: serializedData,
        success: function(response, statusText, xhr) {
            $('#' + containerDiv).html(response);
        }
    })
}