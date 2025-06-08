var id;

$(document).ready(function () {
  var email = localStorage.getItem('email');
  if (email) {
    alert('Welcome to the dashboard, ' + email);
  } else {
    window.location.href = 'signin.html';
  }

  $('#emImage').on('change', function (e) {
    var file = e.target.files[0];
    if (file) {
      var reader = new FileReader();
      reader.onload = function (e) {
        $('#previewImage').attr('src', e.target.result);
      };
      reader.readAsDataURL(file);
    }
  });
  getEmployeeData();
});

function getEmployeeData() {
  $('#empTableBody').empty();
  $.ajax({
    url: 'http://localhost:8080/EMS_Web_exploded/api/v1/dashboard',
    method: 'GET',
    success: function (response) {
      let data = response.data;
      console.log(data);

      data.forEach(function (item, index) {

        let newData = `<tr>
                                <th scope="row">${item.id}</th>
                                <td>${item.email}</td>
                                <td>${item.name}</td>
                                <td>${item.salary}</td>
                               <td><img src="http://localhost:8080/EMS_Web_exploded/${item.imageUrl}" alt="${item.name}'s image" style="max-width: 100px; max-height: 100px;"></td>
                         </tr>1`;

        $('#empTable').append(newData);

      });

    },
  });

}

$('#log-out-btn').click(function () {
  localStorage.removeItem('email');
  window.location.href = 'signin.html';
});

$('#empTable').on('click', 'tr', function () {
  var employee = $(this);
  id = employee.find('th').text()
  let data = {
    email: employee.find('td:eq(0)').text(),
    name: employee.find('td:eq(1)').text(),
    salary: employee.find('td:eq(2)').text(),
    imageUrl: employee.find('td:eq(3) img').attr('src')
  };
  console.log('Employee data:', data);

  $('#emEmail').val(data.email);
  $('#empName').val(data.name);
  $('#salary').val(data.salary);
  $('#previewImage').attr('src', data.imageUrl);

});

$('#save-btn').click(function () {
  let name = $('#empName').val();
  let email = $('#emEmail').val();
  let salary = $('#salary').val();
  let image = $('#emImage')[0].files[0];

  if (!name || !email || !salary || !image) {
    alert('Please fill all fields');
    return;
  }

  const formData = new FormData();
  formData.append('name', name);
  formData.append('email', email);
  formData.append('salary', salary);
  formData.append('image', image);

  $.ajax({
    url: 'http://localhost:8080/EMS_Web_exploded/api/v1/dashboard',
    method: 'POST',
    contentType: false,
    processData: false,
    data: formData,
    success: function (response) {
      if (response.status === '200') {
        alert('Message: ' + response.message);
        getEmployeeData();
        resetForm();
        return;
      }
    },
    error: function (xhr, status, error) {
      alert('Message: ' + error);
    },

  });

});


$('#update-btn').click(function () {
  let name = $('#empName').val();
  let email = $('#emEmail').val();
  let salary = $('#salary').val();
  let image = $('#emImage')[0].files[0];

  if (!name || !email || !salary) {
    alert('Please fill all fields');
    return;
  }

  const formData = new FormData();
  formData.append('name', name);
  formData.append('email', email);
  formData.append('salary', salary);
  formData.append('image', image);
  formData.append('id', id);

  $.ajax({
    url: 'http://localhost:8080/EMS_Web_exploded/api/v1/dashboard',
    method: 'POST',
    contentType: false,
    processData: false,
    data: formData,
    success: function (response) {
      if (response.status !== '200') {
        getEmployeeData();
        resetForm();
        alert('Message: ' + response.message);     
        return;
      }
    },
    error: function (xhr, status, error) {
      alert('Message: ' + error);
    },

  });

});

$('#delete-btn').click(function () {

  if (!id) {
    alert('Please fill all fields');
    return;
  }

  console.log(id)

  $.ajax({
    url: 'http://localhost:8080/EMS_Web_exploded/api/v1/dashboard?id=' + id,
    method: 'DELETE',
    success: function (response) {
      if (response.code === '200') {
        getEmployeeData();
        resetForm();
        alert('Message: ' + response.message);
        return;
      }
    },
    error: function (xhr, status, error) {
      alert('Message: ' + error);
    },

  });

});

function resetForm() {
  $('#empName').val('');
  $('#emEmail').val('');
  $('#salary').val('');
  $('#emImage').val('');
  $('#previewImage').attr('src', '');
  id = null;
}

