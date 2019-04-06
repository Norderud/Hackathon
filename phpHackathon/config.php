<?php

// Connection innstillinger som inkluderes i alle php filene
$host="localhost";
$user="v19_6120_gr05";
$password="pw.123456";
$db = "v19_6120_db05";

$conn = mysqli_connect($host,$user,$password,$db);

// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }else{  //echo "Connect";


   }

?>
