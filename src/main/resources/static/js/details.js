function wish() {
    $.ajax({
        url: '/details',
        method: "POST",
        data: $('#wish_form').serialize(),
        success: function () {
            location.replace(location.href);
        }
    })
}

$('#review_btn').click(function () {
    $.ajax({
        url: '/details',
        method: "POST",
        data: $('#review_form').serialize(),
        success: function () {
            location.replace(location.href);
        }
    })
})

$('#update_btn').click(function () {
    $.ajax({
        url: '/details',
        method: "POST",
        data: $('#review_update_form').serialize(),
        success: function () {
            location.replace(location.href);
        }
    })
})

if (document.getElementById('update_review_btn') != null) {
    document.getElementById('update_review_btn').addEventListener("click", function () {
        const update_btn = document.getElementById('update_btn');
        const update_enabled = document.getElementById('update_enabled');
        update_btn.style.display = 'block';
        update_enabled.disabled = false;
        update_enabled.style.borderStyle = 'solid';
        document.getElementById('update_review_btn').style.display = 'none';
    });
}

if (document.getElementById('review_form_text') != null) {
    document.getElementById('review_form_text').addEventListener('keyup', function (event) {
        if (event.key === 'Enter') {
            $.ajax({
                url: '/details',
                method: "POST",
                data: $('#review_form').serialize(),
                success: function () {
                    location.replace(location.href);
                }
            })
        }
    });
}

if (document.getElementById('update_enabled') != null) {
    document.getElementById('update_enabled').addEventListener('keyup', function (event) {
        if (event.key === 'Enter') {
            $.ajax({
                url: '/details',
                method: "POST",
                data: $('#review_update_form').serialize(),
                success: function () {
                    location.replace(location.href);
                }
            })
        }
    });
}