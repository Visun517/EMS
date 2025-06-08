// const { memo } = require("react");

$('#button').on('click', function(event) {

  let name = $('#name').val();
  let email = $('#email').val();
  let password = $('#password').val();

  if (!name || !email || !password) {
    alert('Please fill in all fields.');
    return;
  }
  console.log("Name: " + name);
  console.log("Email: " + email);
  console.log("Password: " + password);

  
let userData = {
              "email": email,
              "name": name,
              "password": password
            }

  $.ajax({
    url: 'http://localhost:8080/EMS_Web_exploded/api/v1/signup',
    method: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(userData),
    success: function(response) {
      if (response.status === 'success') {
        window.location.href = 'signIn.html';
        return;
      }
    },
    error: function(xhr, status, error) {
      alert("Please Try Again.");
    }
  });
});