var password = document.getElementById("password");
var confirm = document.getElementById("confirm-password");

function checkPassword() {
  document.getElementById("submit").disabled = !(password.value.trim() !== '' && password.value === confirm.value);
}

password.addEventListener('input', checkPassword);
confirm.addEventListener('input', checkPassword);
