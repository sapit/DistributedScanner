

<html>
<body>    
<form action="" method="post">
  <input type="text" name="inputText1"/>
  <input type="submit" name="SubmitButton1"/>
</form>

<form action="" method="post">
  <input type="text" name="inputText2"/>
  <input type="submit" name="SubmitButton2"/>
</form>

<form action="" method="post">
  <input type="text" name="username"/>
  <input type="text" name="password"/>
  <input type="submit" name="SubmitButton3"/>
</form>      

</body>
</html>

<?php 
		
	if(isset($_POST['SubmitButton1'])){ //check if form was submitted
		$input1 = htmlspecialchars($_POST['inputText1']); //get input text
		echo "This is crazy but:" . $input1;
		//echo "DEath";
	} 


	if(isset($_POST['SubmitButton2'])){ //check if form was submitted
		$input2 = $_POST['inputText2']; //get input text

		echo "Success! You entered: ".$input2;
	}       
 

	if(isset($_POST['SubmitButton3'])){ //check if form was submitted
		$input_user = $_POST['username']; //get input text
		$input_pass = $_POST['password']; //get input text

		$servername_db = "localhost";
		$username_db = "root";
		$password_db = "123456";

		// Create connection
		$conn = mysqli_connect($servername_db, $username_db, $password_db ,'mydb') ;
		if (!$conn) {
    		die('Could not connect: ' . mysql_error());
		}
		//SELECT username , location , field FROM Users where username = '' or ''='' and password = '' or ''='' UNION SELECT table_name, column_name, 1 FROM information_schema.columns where ''=''
		$sql = "SELECT username , location , field FROM Users where username = '$input_user' and password = '$input_pass'";
		
		$result = mysqli_query($conn, $sql);
		echo "<table>"; //begin table tag..
		while($rowitem = mysqli_fetch_array($result)) {
			echo "<tr>";
		    echo "<td>" . $rowitem['username'] . "</td>";
		    echo "<td>" . $rowitem['location'] . "</td>";
		    echo "<td>" . $rowitem['field'] . "</td>";
		    echo "</tr>";
		}
		echo "</table>"; //end table tag
	}    
?>


