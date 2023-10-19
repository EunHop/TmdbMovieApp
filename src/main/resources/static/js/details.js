document.getElementById('update_review_btn').addEventListener("click", function () {
    const update_btn = document.getElementById('update_btn');
    const update_enabled = document.getElementById('update_enabled');
    update_btn.style.display = 'block';
    update_enabled.disabled = false;
    update_enabled.style.borderStyle = 'solid';
    document.getElementById('update_review_btn').style.display = 'none';
});