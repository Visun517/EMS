$('#log-in-btn').click(function (event) {
    
    event.preventDefault();    
    const eventData = {
        email: $('#email').val(),
        password: $('#password').val(),
    };

    console.log(eventData.email);
    console.log(eventData.password);
    
     $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/api/v1/signin',
        contentType: 'application/json',
        data: JSON.stringify(eventData),
        success: function(response) {
            console.log(response);
            if (response.code === '200') {
                console.log("Login successful");
                localStorage.setItem('email', eventData.email);
                window.location.href = 'dash.html';
                
            } else {
                alert('Error: ' + response.message);
            }
        },
    });

});